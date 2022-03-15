package utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dto.*;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;
import repositories.TwilioRepository;


import org.w3c.dom.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CallUtils
{
    private TwilioRepository repository;
    private ReadConfigHelper config;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    public CallUtils(TwilioRepository repository
                    , ReadConfigHelper config)
    {
        this.repository = repository;
        this.config = config;
    }

    public boolean isCallComplete()
    {
        Call call = getLastCall();
        boolean callNotComplete= !call.getStatus().equals("completed");
        int attempt = 0;
        while (callNotComplete && attempt <= 20)
        {
            waitFor(30);
            call = getLastCall();
            callNotComplete = !call.getStatus().equals("completed");
            attempt++;
        }
        return !callNotComplete;
    }

    public List<EventTable> readMessage()
    {
        List<EventTable> messages = new ArrayList<>();
        Call call = getLastCall();
        CallEventResponse eventResponse = repository.getCallEvents(call.getSid());
        List<Events> events = eventResponse.getEvents();
        for (Events event: events) {
            XMLContentResponse responseContext = convertXML(event.getResponse().getResponse_body());
            if(!StringUtils.isEmpty(responseContext.getSay())){
                messages.add(new EventTable(){{
                    setSay(responseContext.getSay().replace("  "," "));
                }});
            }
        }

        return messages;
    }

    private Call getLastCall()
    {
        CallResponse calls = repository.getCalls();
        Call call = calls.getCalls().stream().filter(c -> c.getFrom().equals(config.readTwilioFromPhoneNumber())
                && c.getTo().equals(config.readTwilioToPhoneNumber())
                && c.getDirection().equals("inbound")).findFirst().orElse(null);
        if(call == null){
            throw new RuntimeException("No inbound call found");
        }
        return call;
    }

    private XMLContentResponse convertXml(String xmlContent)
    {
        XMLContentResponse convertString = new XMLContentResponse();
        if(xmlContent == null || xmlContent.isEmpty()){
            return convertString;
        }
        try
        {
            XmlMapper xmlMapper = (XmlMapper) new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            convertString
                    = xmlMapper.readValue(xmlContent, XMLContentResponse.class);

        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return convertString;
    }

    private XMLContentResponse convertXML(String xmlResponse){
        XMLContentResponse convertString = new XMLContentResponse();
        try
        {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlResponse));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodeList=doc.getElementsByTagName("*");
            convertString.setSay(listSay(nodeList));

        }catch (Exception ex){}

        return convertString;
    }

    private List<String> listSay(NodeList nodeList)
    {
        List<String> listSay = new ArrayList();
        String oldInnerTag = "";
        for (int i=0; i<nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if(element.hasChildNodes() && oldInnerTag.equals(element.getTagName())){
                NodeList innerNodeList =  element.getElementsByTagName("*");
                listSay.addAll(listInnerSay(innerNodeList));

            }
            String elementText = getElementText((Element) nodeList.item(i));
            if(elementText != null)
            {
                listSay.add(elementText);
            }
            oldInnerTag = element.getTagName();

        }
        return listSay;
    }

    private List<String> listInnerSay(NodeList nodeList)
    {
        List<String> listSay = new ArrayList();
        for (int i=0; i<nodeList.getLength(); i++) {
            String elementText = getElementText((Element) nodeList.item(i));
            if(elementText != null)
            {
                listSay.add(elementText);
            }
        }
        return listSay;
    }

    private String getElementText(Element element)
    {
        if(element.getTagName().equals("Say"))
        {
            return element.getTextContent();
        }
        return null;
    }



    private void waitFor(int seconds)
    {
        try
        {
            TimeUnit.SECONDS.sleep(seconds);
        }
        catch (Exception ex)
        {

        }
    }
}
