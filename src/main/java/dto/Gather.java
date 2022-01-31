package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gather {
    @JsonProperty("Say")
    private String say;

    public String getSay() {
        return say;
    }

    public void setSay(String say) {
        this.say = say;
    }
}
