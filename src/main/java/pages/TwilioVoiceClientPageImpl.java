package pages;

import controllers.WebDriverController;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TwilioVoiceClientPageImpl extends BasePage implements TwilioVoiceClientPage{
    private By startUpDeviceBtn = By.id("startup-button");
    private By acceptCallBtn = By.id("button-accept-incoming");
    private By logs = By.id("log");
    private By hangup = By.id("button-hangup-incoming");
    public TwilioVoiceClientPageImpl(WebDriverController driver) {
        super(driver);
    }

    public void startUpDevice() {
        findElement(startUpDeviceBtn).click();
        waitForTextToBePresent(findElement(logs),"Twilio.Device Ready to make and receive calls!");
    }

    public void acceptCall() {
        findElement(acceptCallBtn).click();
        System.out.println("Call responed");
    }

    public void rejectCall() {

    }

    public void hangupCall() {

    }

    @Override
    public void waitForCallToFinish() {
        WebElement hangUp = findElement(hangup);
        waitForElementNotToBePresent(hangUp);
    }


}
