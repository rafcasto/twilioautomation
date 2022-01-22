package repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import utils.HeaderConstants;
import utils.ReadConfigHelper;

import java.util.HashMap;

public class BaseRepository
{
    private ObjectMapper mapper = new ObjectMapper();
    private ReadConfigHelper config;
    public BaseRepository(ReadConfigHelper config)
    {
        this.config = config;
    }
    public HttpResponse<String> post(String url,  String requestBody)
    {
        HttpResponse<String> response = null;
        try
        {
            response =  Unirest.post(url)
                    .basicAuth(config.readTwilioSid(),config.readTwilioAuth())
                    .headers(getHeaders())
                    .body(requestBody)
                    .asString();
            if(response.getStatus() != 201){
                throw new Exception(response.getBody());
            }

        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
        return response;
    }

    public <T> T convertToObject(String json, Class<T> t1){
        try
        {
            return (T) mapper.readValue( json,t1);
        }catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }
        return null;
    }

    public String convertObjectToString(Object userRequest)
    {
        String jsonString = null;
        try
        {
            jsonString = mapper.writeValueAsString(userRequest);

        }catch (Exception ex)
        {
            jsonString = "";
        }
        return jsonString;
    }

    public HashMap<String,String> getHeaders()
    {
        HashMap<String,String> headers = new HashMap<String,String>();
        headers.put(HeaderConstants.ACCEPT, "*/*");
        headers.put(HeaderConstants.CONTENT_TYPE, "application/x-www-form-urlencoded");
        headers.put(HeaderConstants.CACHE_CONTROL, "no-cache");
        return headers;
    }

}
