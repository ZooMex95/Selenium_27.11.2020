import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "https://www.rgs.ru/";
        driver.get(baseUrl);
    }

    @Test
    public void seleniumTest()  {

        // Выбрать меню
        String menuButtonXPath = "//a[normalize-space()='Меню' and @data-toggle='dropdown']"; //a[@data-target='#main-navbar-collapse']  //div[@id='main-navbar-collapse']/ol[@class='nav navbar-nav navbar-nav-rgs-menu pull-left open'] //a[@data-toggle='dropdown'][@class='hidden-xs']
        List<WebElement> insuranceButtonList = driver.findElements(By.xpath(menuButtonXPath));
        if (!insuranceButtonList.isEmpty()){
            insuranceButtonList.get(0).click();
        }

        // Выбрать ДМС
        String dmsButtonXPath = "//a[normalize-space()='ДМС']";
        WebElement dmsButton = driver.findElement(By.xpath(dmsButtonXPath));
        dmsButton.click();

/*        //Проверить заголовок
        String checkDmsHeaderXPath = "//h1[contains(@class, 'content-document-header')]";
        waitUntilElementToBeVisible(By.xpath(checkDmsHeaderXPath));
        WebElement checkDmsHeader = driver.findElement(By.xpath(checkDmsHeaderXPath));  НЕРАЗРЫВНОЕ ТИРЕ ИЛИ ГЕТ ТАЙТЛ
        Assert.assertEquals("Заголовок не соответствует / отсутствует", "ДМС — добровольное медицинское страхование", checkDmsHeader.getText());*/

        //Нажать "Отправить заявку"
        String buttonSendRequestXPath = "//a[normalize-space()='Отправить заявку']";
        WebElement buttonSendRequest = driver.findElement(By.xpath(buttonSendRequestXPath));
        buttonSendRequest.click();

        //Проверить "Заявка на добровольное медецинское страхование
        String checkOpenRequestXPath = "//b[text()='Заявка на добровольное медицинское страхование']";
        waitUntilElementToBeVisible(By.xpath(checkOpenRequestXPath));
        WebElement checkOpenRequest = driver.findElement(By.xpath(checkOpenRequestXPath));
        Assert.assertEquals("Заявка не открылась", "Заявка на добровольное медицинское страхование", checkOpenRequest.getText());

        //











    }

    @After
    public void after() {
       // driver.quit();
    }

    private void waitUntilElementToBeVisible (By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUntilElementToBeVisible (WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));

    }


}
