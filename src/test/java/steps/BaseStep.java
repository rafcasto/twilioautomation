package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.WebDriverController;
import io.cucumber.java.*;
import utils.ReadConfigHelper;

import java.lang.reflect.Type;

public class BaseStep
{
    private WebDriverController driver;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ReadConfigHelper config;
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
    @After
    public void wrapUp(){
        this.driver.getDriver().close();
        if(driver.getDriver() != null){
            driver.getDriver().quit();
        }
    }

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object transformer(Object fromValue, Type toValueType) {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
    }
}
