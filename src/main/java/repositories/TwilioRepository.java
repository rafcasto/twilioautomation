package repositories;

import dto.TWLRequest;

public interface TwilioRepository
{
    public void makeCall(TWLRequest request);
}
