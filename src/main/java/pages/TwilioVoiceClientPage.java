package pages;

public interface TwilioVoiceClientPage
{
    public void startUpDevice();
    public void acceptCall();
    public void rejectCall();
    public void hangupCall();
    public void waitForCallToFinish();
}
