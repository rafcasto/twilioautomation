import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import dto.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import repositories.TwilioRepository;
import repositories.TwilioRepositoryImpl;
import utils.ReadConfigHelper;


import java.util.List;

public class TestApi {
    @Test
    public void TestApi()
    {
        TwilioRepository repository = new TwilioRepositoryImpl(new ReadConfigHelper());
        CallResponse calls = repository.getCalls();
        Call call = calls.getCalls().stream().filter(c -> c.getFrom().equals("+642108765733")
                && c.getTo().equals("+6468806272")
                && c.getDirection().equals("inbound")).findFirst().orElse(null);
        CallEventResponse eventResponse = repository.getCallEvents(call.getSid());
        List<Events> events = eventResponse.getEvents();
        for (Events event: events) {
            System.out.println(event.getRequest().getUrl());
            XMLContentResponse responseContext = convertXml(event.getResponse().getResponse_body());
            System.out.println(responseContext.getSay());

        }
        System.out.println(call.getSid());
    }

    private String getSay(String xml)
    {
        XMLContentResponse responseContext = convertXml(xml);
        if(!StringUtils.isEmpty(responseContext.getSay()))
        {
            return responseContext.getSay();
        }
        Gather gather = responseContext.getGather();
        if(gather != null && !StringUtils.isEmpty(gather.getSay()))
        {
            return gather.getSay();
        }
        return "";

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
