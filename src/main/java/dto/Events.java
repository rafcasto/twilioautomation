package dto;

public class Events
{
    public ERequest getRequest() {
        return request;
    }

    public void setRequest(ERequest request) {
        this.request = request;
    }

    public EResponse getResponse() {
        return response;
    }

    public void setResponse(EResponse response) {
        this.response = response;
    }

    private ERequest request;
    private EResponse response;
}
