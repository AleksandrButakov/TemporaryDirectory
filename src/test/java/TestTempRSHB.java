import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public class TestTempRSHB {
    WebDriver driver;
    WebElement element;
    WebElement slider;
    WebDriverWait driverWait;

    @Test
    public void testCase01() {
        System.out.println("testCase01");
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
        System.out.println("Cookie agreement window");
        //createPause();
        element = driver.findElement(By.xpath("//*[@id=\"alert\"]/div/div/div[2]/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        System.out.println("Step_01");
        System.out.println("Individuals");
        //createPause();
        // Частным лицам
        // $$("div.b-main-menu-items [href='/natural/']")
        element = driver.findElement(By.cssSelector("div.b-main-menu-items [href='/natural/']"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();
        // driver.findElement(By.xpath("/html/body/div[6]/div[1]/header/nav/div[1]/a[1]")).click();

        System.out.println("Credits");
        // createPause();
        // $$("div.b-page-head [href='/natural/loans/']")
        // "div.b-page-head [href='/natural/loans/']"
        element = driver.findElement(By.cssSelector("div.b-page-head [href='/natural/loans/"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // скрыт alert-ом
        System.out.println("Loan without collateral");
        //createPause();
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
        // указываем какой скрипт необходимо выполнить. Необходимо почистить все сессионные данные,
        // которые находятся в определенном окне
        javascriptExecutor.executeScript("window.sessionStorage.clear()");
        createPause();

        // ссылка на кнопку понятно
        //button[@class='cookie-consent__submit-button button button__white']
        element = driver.findElement(By.xpath("//button[@class='cookie-consent__submit-button button button__white']"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // двигаем ползунок в сторону 2 500 000 пока не достигнем результата
        String s;
        slider = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[3]/div/div[2]/div[4]"));
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[3]/div/input"));

        s = element.getAttribute("value");
        String s1;
        s1 = determinePositionSlider(s);

        System.out.println("Calculation of monthly payment");
        System.out.println(s);
        System.out.println(s1);
        int sm;
        sm = Integer.parseInt(s1);
        while (sm != 2500000) {
            if (sm < 2500000) {
                slider.sendKeys(Keys.ARROW_RIGHT);
                s = element.getAttribute(("value"));
                s1 = determinePositionSlider(s);
                sm = Integer.parseInt(s1);
            } else {
                slider.sendKeys(Keys.ARROW_LEFT);
                s = element.getAttribute(("value"));
                s1 = determinePositionSlider(s);
                sm = Integer.parseInt(s1);
            }
        }

        // введем количество месяцев для рассчета кредита
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[4]/div/input"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        System.out.println("Enter period");
        element.clear();
        // createPause();
        element.sendKeys("60");

        // Установич checkbox согласно заданию, певый isSelected, второй и третий is Diselected
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[1]/label/span/input"));
        if (!element.isSelected()) {
            element.click();
        }
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[2]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[3]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }

        // проверим что ежемесячный платеж составляет 53061 Р
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[1]/div[1]"));
        s = element.getText();
        System.out.println(s);
        Assert.assertEquals(s, "56 118 ₽");

        // проверим что процентная ставка составляет 12.4%
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[2]/div[1]"));
        s = element.getText();
        System.out.println(s);
        Assert.assertEquals(s, "12.4%");

        System.out.println("Test is closed");
        driver.close();
    }

    @Test
    public void testCase02() {
        System.out.println("testCase02");
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        WebDriverWait driverWait = new WebDriverWait(driver,30);
        //WebDriverWait driverWait = new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOf(element));
        driver.get("https://www.rshb.ru");

        // кликнуть "Частным лицам"
        System.out.println("Individuals");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/header/nav/div[1]/a[1]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // кликнуть "Кредиты"
        System.out.println("Credits");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[1]/nav/a[3]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // закроем окно принятия cookies
        System.out.println("Cookies window");
        element = driver.findElement(By.xpath("//*[@id=\"alert\"]/div/div/div[2]/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // кликнуть "Потребительский кредит без обеспечения"
        System.out.println("consumer credit without collateral");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[5]/div[1]/div[1]/a"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // закроем окно cookies
        System.out.println("Close cookies window");
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // тип платежа выбрать "Аннуитентный"
        System.out.println("Annuity");
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[2]/div[2]/div/button[1]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // двигаем ползунок в сторону 2 500 000 пока не достигнем результата
        String s;
        slider = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[3]/div/div[2]/div[4]"));

        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[3]/div/input"));

        s = element.getAttribute("value");
        String s1;
        s1 = determinePositionSlider(s);

        System.out.println("Calculation of monthly payment");
        System.out.println(s);
        System.out.println(s1);
        int sm;
        sm = Integer.parseInt(s1);
        while (sm != 2500000) {
            if (sm < 2500000) {
                slider.sendKeys(Keys.ARROW_RIGHT);
                s = element.getAttribute(("value"));
                s1 = determinePositionSlider(s);
                sm = Integer.parseInt(s1);
            } else {
                slider.sendKeys(Keys.ARROW_LEFT);
                s = element.getAttribute(("value"));
                s1 = determinePositionSlider(s);
                sm = Integer.parseInt(s1);
            }
        }

        // срок кредита 60 месяцев
        System.out.println("Number of mounts");
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[4]/div/input"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys("60");

        // Активны только checkBox Получаю ЗП в Россельхоз и Комплексная защита
        System.out.println("Select checkbox");

        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[1]/label/span/input"));
        if (!element.isSelected()) {
            element.click();
        }
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[2]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[3]/label/span/input"));
        if (!element.isSelected()) {
            element.click();
        }

        // проверим что ставка = 8,4%
        System.out.println("Let's check the size of the interest rate");
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[2]/div[1]"));
        s = element.getText();
        Assert.assertEquals(s, "8.4%");

        // проверим что размер ежемесячного платежа составляет 51171
        System.out.println("Let's check the monthly payment amount");
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[1]/div[1]"));
        s = element.getText();
        Assert.assertEquals(s, "51 171 ₽");

        driver.close();
    }

    @Test
    public void testCase03() {

        System.out.println("testCase03");
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebDriverWait driverWait = new WebDriverWait(driver, 30);

        // Зайти на сайт rshb.ru
        System.out.println("Go to the website");
        driver.get("https://www.rshb.ru");

        // закроем окно с cookies
        element = driver.findElement(By.xpath("//*[@id=\"alert\"]/div/div/div[2]/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // Кликнуть «Частным лицам»
        System.out.println("Click on 'Private person'");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/header/nav/div[1]/a[1]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // Кликнуть «Вклады и сбережения»
        System.out.println("Click on 'Deposits and Savings'");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[1]/nav/a[5]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // Кликнуть «Накопительный счёт»
        System.out.println("Click on 'Savings Account'");
        element = driver.findElement(By.xpath("//*[@id=\"lazycross b-sections-menu-item-cover-img334563\"]/img"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // Кликнуть «расчет доходности по сбережениям»
        System.out.println("Click on 'Calculation of profitability on savings'");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div/div/a"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // Проверить что таблица «Вклады и сбережения» соответствует:
        String s;
        // Накопительный счет «Моя выгода» = 5.5%
        System.out.println("Savings account 'My benefit' = 5.5%");
        element = driver.findElement(By.xpath("//*[@id=\"deposit_451879\"]/td[5]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        s = element.getText();
        Assert.assertEquals(s, "5.5%");

        // Накопительный счет «Моя копилка» = 5%
        System.out.println("Savings account 'My piggy bank' = 5%");
        element = driver.findElement(By.xpath("//*[@id=\"deposit_409523\"]/td[5]"));
        s = element.getText();
        Assert.assertEquals(s, "5%");

        // Накопительный счет «Мой счет» = 4%
        System.out.println("Savings account 'My account' = 4%");
        element = driver.findElement(By.xpath("//*[@id=\"deposit_330854\"]/td[5]"));
        s = element.getText();
        Assert.assertEquals(s, "4%");

        // Накопительный счет «Ультра» = 0.01%
        System.out.println("Savings account 'Ultra' = 0.01%");
        element = driver.findElement(By.xpath("//*[@id=\"deposit_451946\"]/td[5]"));
        s = element.getText();
        Assert.assertEquals(s, "0.01%");

        // Накопительный счет «Премиум» = 0.01%
        System.out.println("Premium savings account = 0.01%");
        element = driver.findElement(By.xpath("//*[@id=\"deposit_451924\"]/td[5]"));
        s = element.getText();
        Assert.assertEquals(s, "0.01%");

        // Накопительный счет «До востребования» = 0.01%
        System.out.println("Accumulative account 'On demand' = 0.01%");
        element = driver.findElement(By.xpath("//*[@id=\"deposit_11790\"]/td[5]"));
        s = element.getText();
        Assert.assertEquals(s, "0.01%");

        driver.close();
    }

    // иетод задания паузы
    public void createPause() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // выберем только цифры из поля убрав символы форматирования
    public String determinePositionSlider(String s) {
        String s1 = "";
        char sT;
        int j =  0, a;
        a = s.length();
        while (j < a) {
            sT = s.charAt(j);
            if (sT == '0' || sT == '1' || sT == '2' || sT == '3' || sT == '4' || sT == '5' || sT == '6' || sT == '7' ||
                    sT == '8' || sT == '9') {
                s1 = s1 + sT;
            }
            j++;
        }
        return s1;
    }

    // метод ожидания видимости элемента
    public void waitElementAppear (WebElement element) {
        driverWait.until(ExpectedConditions.visibilityOf(element));
    }


}