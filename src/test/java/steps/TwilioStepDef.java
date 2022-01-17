package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.TwilioVoiceClientPage;

public class TwilioStepDef {
    private TwilioVoiceClientPage page;
    public TwilioStepDef(TwilioVoiceClientPage page)
    {
        this.page = page;
    }
    @Given("Twilio client is started")
    public void twilioClientIsStarted() {
        page.startUpDevice();
    }


    @Given("customer makes a call {string}{int}{string}")
    public void customerMakesACall(String arg0, int arg1, String arg2) {
        String arg = arg0;
    }

    @When("Twilio client picks up the call")
    public void twilioClientPicksUpTheCall() {
    }

    @Then("A new service log is created")
    public void aNewServiceLogIsCreated() {
    }
}
