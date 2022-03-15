import dto.XMLContentResponse;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import repositories.TwilioRepository;
import repositories.TwilioRepositoryImpl;
import utils.CallUtils;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TestXmlConverter
{
    private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Response>\n" +
            "    <Gather input=\"dtmf\" timeout=\"5\" numDigits=\"1\" actionOnEmptyResult=\"false\" speechTimeout=\"5\" speechModel=\"phone_call\" enhanced=\"true\" hints=\"\">\n" +
            "        <Say voice=\"Polly.Joanna\">Thank you for calling.</Say>\n" +
            "        <Pause length=\"2\"/>\n" +
            "        <Say voice=\"Polly.Joanna\">To continue in english, press 1. For spanish, press 2.</Say>\n" +
            "    </Gather>\n" +
            "    <Say voice=\"Polly.Joanna\">Please wait while we access your account.</Say>\n" +
            "    <Pause length=\"2\"/>\n" +
            "    <Redirect method=\"POST\">https://webhooks.twilio.com/v1/Accounts/ACfc710591c3f748ad48a8d1e3c0dcbee2/Flows/FWb626912306bc5d8d07e407d71fe32191?FlowEvent=return&amp;language=en-US&amp;voice=Polly.Joanna</Redirect>\n" +
            "</Response>";
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    @Test
    public void convertXMLTest()
    {
        XMLContentResponse response = convertXML(xml);
        System.out.println(response.getSay().replace("  "," "));
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
}
