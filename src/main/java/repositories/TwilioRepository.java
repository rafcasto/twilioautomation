package repositories;

import dto.CallEventResponse;
import dto.CallResponse;
import dto.TWLRequest;

import java.util.List;

public interface TwilioRepository
{
    public void makeCall(TWLRequest request);
    public CallResponse getCalls();
    public CallEventResponse getCallEvents(String sidCallId);
}
