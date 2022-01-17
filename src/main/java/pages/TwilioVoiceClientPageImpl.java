package pages;

import controllers.WebDriverController;
import org.openqa.selenium.By;

public class TwilioVoiceClientPageImpl extends BasePage implements TwilioVoiceClientPage{
    private By startUpDeviceBtn = By.id("startup-button");
    private By logs = By.id("log");

    public TwilioVoiceClientPageImpl(WebDriverController driver) {
        super(driver);
    }

    public void startUpDevice() {
        findElement(startUpDeviceBtn).click();
        waitForTextToBePresent(findElement(logs),"Twilio.Device Ready to make and receive calls!");
    }

    public void acceptCall() {

    }

    public void rejectCall() {

    }

    public void hangupCall() {

    }


}
