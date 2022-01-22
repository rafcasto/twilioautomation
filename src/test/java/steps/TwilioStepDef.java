package steps;

import dto.TWLRequest;
import dto.TWLTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.TwilioVoiceClientPage;
import repositories.TwilioRepository;

import java.util.List;

public class TwilioStepDef {
    private TwilioVoiceClientPage page;
    private TwilioRepository repository;
    public TwilioStepDef(TwilioVoiceClientPage page, TwilioRepository repository)
    {
        this.page = page;
        this.repository = repository;
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
    public void aNewServiceLogIsCreated() {
    }
}
