package dto;

public class ERequest
{
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public EParameters getParameters() {
        return parameters;
    }

    public void setParameters(EParameters parameters) {
        this.parameters = parameters;
    }

    private String method;
    private EParameters parameters;
}
