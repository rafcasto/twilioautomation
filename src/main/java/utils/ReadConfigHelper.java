package utils;

import java.io.InputStream;
import java.util.Properties;

public class ReadConfigHelper
{
    private String propFileName = "config.properties";
    private Properties prop = new Properties();
    public ReadConfigHelper()
    {
        initProps();
    }

    public String readTwilioClientUrl(){return prop.getProperty("twilio.client");}
    public String readTwilioSid(){return prop.getProperty("twilio.sid");}
    public String readTwilioAuth(){return prop.getProperty("twilio.auth");}
    public String readTwilioFromPhoneNumber(){return prop.getProperty("twilio.from.phone");}
    public String readTwilioToPhoneNumber(){return prop.getProperty("twilio.to.phone");}
    private void initProps()
    {
        try
        {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if(inputStream == null)
            {
                throw new RuntimeException("property file: '"+ propFileName+"' not found in the classpath");
            }
            prop.load(inputStream);
        }catch (Exception ex)
        {
            throw new RuntimeException(ex.getStackTrace().toString());
        }
    }
}
