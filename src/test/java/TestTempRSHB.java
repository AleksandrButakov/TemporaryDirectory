import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.json.JsonOutput;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TestTempRSHB {
    WebDriver driver;
    WebElement element;
    WebDriverWait driverWait;

    @Test
    public void webTest() {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebDriverWait driverWait = new WebDriverWait(driver,30);
        //WebDriverWait driverWait = new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOf(element));
        driver.get("https://www.rshb.ru/");

        /*
        работа со всплывающими окнами
        // Alert alert = driver.switchTo().alert();
        Alert alert = (new WebDriverWait(driver, 10)).until(ExpectedConditions.alertIsPresent());
        alert.dismiss();
        alert accept();
         */

        // ожидаем окно принятие cookies
        System.out.println("Окно cookies");
        createPause();
        element = driver.findElement(By.xpath("//*[@id=\"alert\"]/div/div/div[2]/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        System.out.println("Step_01");
        System.out.println("Частным лицам");
        createPause();
        // Частным лицам
        // $$("div.b-main-menu-items [href='/natural/']")
        element = driver.findElement(By.cssSelector("div.b-main-menu-items [href='/natural/']"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();
        // driver.findElement(By.xpath("/html/body/div[6]/div[1]/header/nav/div[1]/a[1]")).click();

        System.out.println("Step_02");
        System.out.println("Кредиты");
        createPause();
        // $$("div.b-page-head [href='/natural/loans/']")
        // "div.b-page-head [href='/natural/loans/']"
        element = driver.findElement(By.cssSelector("div.b-page-head [href='/natural/loans/"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // скрыт alert-ом
        System.out.println("Step_03");
        System.out.println("Кредит без обеспечения");
        createPause();
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[5]/div[1]/div[1]/a"));
        driverWait.until(ExpectedConditions.visibilityOf(element));

        // проверим что элемент видимый
        if(element.isDisplayed()){
            System.out.println("Element is Visible");
        } else {
            System.out.println("Element is InVisible");
        }

        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            e.printStackTrace();
            System.out.println("!!! Элемент не кликабельный");
        }

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        driver.manage().deleteAllCookies();
        // указываем какой скрипт необходимо выполнить. Необходимо почистить все сессионные данные, которые
        // которые находятся в определенном окне
        javascriptExecutor.executeScript("window.sessionStorage.clear()");

        createPause();
        driver.close();
    }

    public void createPause() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}