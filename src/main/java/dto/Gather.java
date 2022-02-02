package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Gather {
    @JsonProperty("Say")
    private List<String> say;

    public List<String> getSay() {
        return say;
    }

    public void setSay(List<String> say) {
        this.say = say;
    }
}
