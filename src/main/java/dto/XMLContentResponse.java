package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class XMLContentResponse {




    public Gather getGather() {
        return gather;
    }

    public void setGather(Gather gather) {
        this.gather = gather;
    }

    @JsonProperty("Gather")
    private Gather gather;

    public String getSay() {
        return readSay();
    }

    public void setSay(List<String> say) {
        this.say = say;
    }

    @JsonProperty("Say")
    private List<String> say;

    private String readSay() {
        String completeMessage = null;
        if ((gather != null && gather.getSay() != null) && !gather.getSay().isEmpty()) {
            completeMessage = contactSay(gather.getSay());
        }
        if (say != null && !say.isEmpty()) {
            if(completeMessage == null){
                completeMessage = contactSay(say);
            }
            else{
                completeMessage += contactSay(say);
            }
        }
        return completeMessage;
    }

    private String contactSay(List<String> say)
    {
        String completedString = null;
        for (String s:say) {
            if(completedString == null){
                completedString = s + " ";
            }else {
                completedString = completedString + " " + s;
            }

        }
        return completedString;
    }


}
