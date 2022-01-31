package dto;

import java.util.List;

public class CallEventResponse
{
    public List<Events> getEvents() {
        return events;
    }

    public void setEvents(List<Events> events) {
        this.events = events;
    }

    private List<Events> events;
}
