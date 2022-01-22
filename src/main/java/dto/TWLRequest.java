package dto;

public class TWLRequest
{
    public String getTwiml() {
        return twiml;
    }

    public void setTwiml(String twiml) {
        this.twiml = twiml;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String twiml;
    private String to;
    private String from;
}
