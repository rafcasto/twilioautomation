package repositories;

import dto.TWLRequest;
import utils.ReadConfigHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TwilioRepositoryImpl extends BaseRepository implements TwilioRepository{
    private String callUrl = "https://api.twilio.com/2010-04-01/Accounts/%s/Calls.json";
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

    private String buildFormCall(TWLRequest request)
    {

        return  "Twiml="+encodeString(request.getTwiml())+
                "&To="+encodeString(config.readTwilioToPhoneNumber())+
                "&From="+encodeString(config.readTwilioToPhoneNumber());


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
