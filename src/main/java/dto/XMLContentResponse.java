package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;


public class XMLContentResponse {


    public String getSay() {
        return readSay();
    }

    public void setSay(String say) {
        this.say = say;
    }

    public Gather getGather() {
        return gather;
    }

    public void setGather(Gather gather) {
        this.gather = gather;
    }

    @JsonProperty("Gather")
    private Gather gather;
    @JsonProperty("Say")
    private String say;

    private String readSay()
    {
        if(!StringUtils.isEmpty(say))
        {
            return say;
        }

        if(gather != null && !StringUtils.isEmpty(gather.getSay())){
            return gather.getSay();
        }
        return "";
    }

}
