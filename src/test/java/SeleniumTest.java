import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "https://www.rgs.ru/";
        driver.get(baseUrl);
    }

    @Test
    public void seleniumTest()  {

        // Выбрать меню
        String menuButtonXPath = "//a[normalize-space()='Меню' and @data-toggle='dropdown']";
        List<WebElement> insuranceButtonList = driver.findElements(By.xpath(menuButtonXPath));
        if (!insuranceButtonList.isEmpty()){
            insuranceButtonList.get(0).click();
        }

        // Выбрать ДМС
        String dmsButtonXPath = "//a[normalize-space()='ДМС']";
        WebElement dmsButton = driver.findElement(By.xpath(dmsButtonXPath));
        dmsButton.click();

       //Проверить заголовок
        String checkDmsHeaderXPath = "//h1[contains(@class, 'content-document-header')]";
        waitUntilElementToBeVisible(By.xpath(checkDmsHeaderXPath));
        WebElement checkDmsHeader = driver.findElement(By.xpath(checkDmsHeaderXPath));
        Assert.assertEquals("Заголовок не соответствует / отсутствует", "ДМС — добровольное медицинское страхование", checkDmsHeader.getText());

        //Нажать "Отправить заявку"
        String buttonSendRequestXPath = "//a[normalize-space()='Отправить заявку']";
        WebElement buttonSendRequest = driver.findElement(By.xpath(buttonSendRequestXPath));
        buttonSendRequest.click();

        //Проверить "Заявка на добровольное медецинское страхование
        String checkOpenRequestXPath = "//h4[@class='modal-title']/b[text()='Заявка на добровольное медицинское страхование']";
        waitUntilElementToBeVisible(By.xpath(checkOpenRequestXPath));
        WebElement checkOpenRequest = driver.findElement(By.xpath(checkOpenRequestXPath));
        Assert.assertEquals("Заявка не открылась", "Заявка на добровольное медицинское страхование", checkOpenRequest.getText());

        //Заполнение полей ФИО
        String fieldXPath = "//input[@name='%s']";
        fillInputField(fieldXPath, "LastName", "Фамилия");
        fillInputField(fieldXPath, "FirstName", "Имя");
        fillInputField(fieldXPath, "MiddleName", "Отчество");

        //Заполнение региона
        WebElement region = driver.findElement(By.xpath("//select[@name='Region']"));
        region.click();
        WebElement exactRegion = driver.findElement(By.xpath("//select[@name='Region']/option[@value='28']"));
        exactRegion.click();
        Assert.assertEquals("Поле заполнено не корректно", "28", exactRegion.getAttribute("value"));

        //Телефон
        String telephoneXPath = "//label[text()='Телефон']/following-sibling::input";
        WebElement telephone = driver.findElement(By.xpath(telephoneXPath));
        telephone.click();
        telephone.sendKeys("9851234567");
        Assert.assertEquals("Поле заполнено не корректно", "+7 (985) 123-45-67", telephone.getAttribute("value"));

        //Почта
        String emailXPath = "//input[@name='Email']";
        WebElement email = driver.findElement(By.xpath(emailXPath));
        email.click();
        email.sendKeys("qwertyqwerty");
        Assert.assertEquals("Поле заполнено не корректно", "qwertyqwerty", email.getAttribute("value"));

        //Дата
        String dateXPath = "//input[@name='ContactDate']";
        LocalDate date = LocalDate.now().plusDays(7);
        WebElement dateOfContact = driver.findElement(By.xpath(dateXPath));
        dateOfContact.click();
        dateOfContact.sendKeys(date.format(DateTimeFormatter.ofPattern("dd MM yyyy")));  //Дата контакта через 7 дней
        Assert.assertEquals("Поле заполнено не корректно", date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), dateOfContact.getAttribute("value"));

        //Комментарий
        String commentXPath = "//textarea[@name='Comment']";
        WebElement comment = driver.findElement(By.xpath(commentXPath));
        comment.click();
        comment.sendKeys("Комментарий");
        Assert.assertEquals("Поле заполнено не корректно", "Комментарий", comment.getAttribute("value"));

        //Согласие
        String checkboxXPath = "//input[@class='checkbox']";
        WebElement checkbox = driver.findElement(By.xpath(checkboxXPath));
        checkbox.click();
        Assert.assertEquals("Поле заполнено не корректно", "on", checkbox.getAttribute("value"));

        //Нажатие "Отправить"
        String buttonSendXPath = "//button[normalize-space()='Отправить']";
        WebElement buttonSend = driver.findElement(By.xpath(buttonSendXPath));
        buttonSend.click();

        //Проверка ошибки почты
        String emailErrorXPath = "//label[@class='validation-error']";
        WebElement emailError = driver.findElement(By.xpath(emailErrorXPath));
        waitUntilElementToBeVisible(emailError);
        Assert.assertEquals("Ошибка не появилась", " Введите адрес электронной почты", emailError.getText());

    }

    @After
    public void after() {
        driver.quit();
    }

    private void fillInputField(String fieldXPath, String name, String value) {
        WebElement element = driver.findElement(By.xpath(String.format(fieldXPath, name)));
        waitUntilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);

        Assert.assertEquals("Поле заполнено не корректно", value, element.getAttribute("value"));

    }


    private void waitUntilElementToBeVisible (By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUntilElementToBeVisible (WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));

    }

    private void waitUntilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }


}
