package steps;

import controllers.WebDriverController;
import io.cucumber.java.Before;
import utils.ReadConfigHelper;

public class BaseStep
{
    WebDriverController driver;
    ReadConfigHelper config;
    public BaseStep(WebDriverController driver, ReadConfigHelper config)
    {
        this.driver = driver;
        this.config = config;
    }

    @Before
    public void setUp()
    {
        this.driver.getDriver().get(config.readTwilioClientUrl());
    }
}
