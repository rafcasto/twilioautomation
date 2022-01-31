package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CallResponse
{
    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    private List<Call> calls;
}
