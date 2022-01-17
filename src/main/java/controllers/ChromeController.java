package controllers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeController implements WebDriverController{
    public ChromeController()
    {
        setUpController();
    }
    private WebDriver driver;
    public WebDriver getDriver() {
        return driver;
    }
    private void setUpController()
    {
        String resource = getClass().getClassLoader().getResource("chromedriver.exe").getPath();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--use-fake-ui-for-media-stream");
        System.setProperty("webdriver.chrome.driver", resource);
        driver = new ChromeDriver(options);
    }
}
