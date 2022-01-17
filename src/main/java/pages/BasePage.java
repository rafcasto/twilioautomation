package pages;

import controllers.WebDriverController;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.*;


public class BasePage
{
    private WebDriverController driver;
    public BasePage(WebDriverController driver)
    {
        this.driver = driver;
    }

    public  WebElement findElement(By locator)
    {
        WebDriverWait wait =  new WebDriverWait(driver.getDriver(),Duration.ofSeconds(10));
        return  wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForTextToBePresent(WebElement element,String text)
    {
        WebDriverWait wait =  new WebDriverWait(driver.getDriver(),Duration.ofSeconds(30));
        wait.until(ExpectedConditions.textToBePresentInElement(element,text));
    }
}
