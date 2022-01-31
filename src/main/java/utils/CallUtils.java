package utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dto.*;
import org.apache.commons.lang3.StringUtils;
import repositories.TwilioRepository;

import java.util.ArrayList;
import java.util.List;

public class CallUtils
{
    private TwilioRepository repository;
    private ReadConfigHelper config;
    public CallUtils(TwilioRepository repository
                    , ReadConfigHelper config)
    {
        this.repository = repository;
        this.config = config;
    }
    public List<EventTable> readMessage()
    {
        List<EventTable> messages = new ArrayList<>();
        CallResponse calls = repository.getCalls();
        Call call = calls.getCalls().stream().filter(c -> c.getFrom().equals(config.readTwilioFromPhoneNumber())
                && c.getTo().equals(config.readTwilioToPhoneNumber())
                && c.getDirection().equals("inbound")).findFirst().orElse(null);
        CallEventResponse eventResponse = repository.getCallEvents(call.getSid());
        List<Events> events = eventResponse.getEvents();
        for (Events event: events) {

            XMLContentResponse responseContext = convertXml(event.getResponse().getResponse_body());
            if(!StringUtils.isEmpty(responseContext.getSay())){
                messages.add(new EventTable(){{
                    setSay(responseContext.getSay());
                }});
            }


        }

        return messages;
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
}
