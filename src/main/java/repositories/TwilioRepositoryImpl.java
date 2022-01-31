package repositories;

import com.mashape.unirest.http.HttpResponse;
import dto.CallEventResponse;
import dto.CallResponse;
import dto.TWLRequest;
import utils.ReadConfigHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TwilioRepositoryImpl extends BaseRepository implements TwilioRepository{
    private String callUrl = "https://api.twilio.com/2010-04-01/Accounts/%s/Calls.json";
    private String callEvents = "https://api.twilio.com/2010-04-01/Accounts/%s/Calls/%s/Events.json";
    private ReadConfigHelper config;
    public TwilioRepositoryImpl(ReadConfigHelper config) {
        super(config);
        this.config = config;
    }

    @Override
    public void makeCall(TWLRequest request) {
           String url = String.format(callUrl,config.readTwilioSid());
           post(url,buildFormCall(request));
    }

    @Override
    public CallResponse getCalls() {
        String url = String.format(callUrl,config.readTwilioSid());
        HttpResponse<String> stringResponse = get(url);
        CallResponse response = convertToObject(stringResponse.getBody(),CallResponse.class);
        return response;
    }

    @Override
    public CallEventResponse getCallEvents(String sidCallId) {
        String url = String.format(callEvents,config.readTwilioSid(),sidCallId);
        HttpResponse<String> stringResponse = get(url);
        CallEventResponse response = convertToObject(stringResponse.getBody(),CallEventResponse.class);
        return response;
    }

    private String buildFormCall(TWLRequest request)
    {

        return  "Twiml="+encodeString(request.getTwiml().replace("$toNumber",config.readTwilioToPhoneNumber()))+
                "&To="+encodeString(config.readTwilioFromPhoneNumber())+
                "&From="+encodeString(config.readTwilioFromPhoneNumber());


    }

    private String encodeString(String val)
    {
        String encodeMessage = null;
        try {

            encodeMessage = URLEncoder.encode(val, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeMessage;
    }
}
