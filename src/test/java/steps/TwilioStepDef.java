package steps;

import dto.EventTable;
import dto.TWLRequest;
import dto.TWLTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.TwilioVoiceClientPage;
import repositories.TwilioRepository;
import utils.CallUtils;

import java.util.Iterator;
import java.util.List;

public class TwilioStepDef {
    private TwilioVoiceClientPage page;
    private TwilioRepository repository;
    private CallUtils callUtils;
    public TwilioStepDef(TwilioVoiceClientPage page
            , TwilioRepository repository
            , CallUtils callUtils)
    {
        this.page = page;
        this.repository = repository;
        this.callUtils = callUtils;
    }
    @Given("Twilio client is started")
    public void twilioClientIsStarted() {
        page.startUpDevice();
    }


    @Given("customer makes a call")
    public void customerMakesACall(List<TWLTable> table) {
       TWLTable twlDetails = table.stream().findFirst().orElse(null);
       TWLRequest request = new TWLRequest(){{
            setTwiml(twlDetails.getTwiml());
        }};
       repository.makeCall(request);
    }

    @When("Twilio client picks up the call")
    public void twilioClientPicksUpTheCall() {
        page.acceptCall();
    }

    @Then("A new service log is created")
    public void aNewServiceLogIsCreated(List<EventTable> expectedMessages)
    {
        List<EventTable> actualMessages = this.callUtils.readMessage();
        Assert.assertEquals("Different number of messages",expectedMessages.size(),actualMessages.size());
        Iterator<EventTable> expectedIterator =  expectedMessages.iterator();
        Iterator<EventTable> actualIterator = actualMessages.iterator();
        while(expectedIterator.hasNext() && actualIterator.hasNext())
        {
            EventTable expectedEvents = expectedIterator.next();
            EventTable actualEvents = actualIterator.next();
            Assert.assertEquals("Messages do not match",expectedEvents.getSay(),actualEvents.getSay().trim());

        }

    }

    @And("Wait for call to finish")
    public void waitForCallToFinish()
    {
        page.waitForCallToFinish();
    }
}
