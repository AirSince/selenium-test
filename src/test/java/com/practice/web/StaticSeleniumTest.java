package com.practice.web;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StaticSeleniumTest {

    public static ChromeDriver driver;

    private static String driverName = "chromedriver101";

    static {
        ClassLoader classLoader = StaticSeleniumTest.class.getClassLoader();
        String driverFile = classLoader.getResource(driverName).getFile();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driverFile))
                .build();

        // 浏览器设置
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true); // 无页面启动
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.merge(capabilities);
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        //  TODO 设置默认下载路径
        String downloadPath = "/Users/dean.xu/Downloads";
        prefs.put("download.default_directory", downloadPath);
        // 不设置删除上面两行
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(service, options);
    }

    /**
     * 点击元素(如果存在并显示)并等待N秒
     *
     * @param cssSelector css选择器
     */
    protected static void elementClickIfExist(String cssSelector, int second) throws InterruptedException {
        if (webElementDisplayed(By.cssSelector(cssSelector))) {
            elementClick(cssSelector, second);
        }
    }

    /**
     * 点击元素并等待N秒
     *
     * @param cssSelector css选择器
     */
    protected static void elementClick(String cssSelector, int second) throws InterruptedException {
        findElement(cssSelector).click();
        if (second > 0) {
            Thread.sleep(second * 1000);
        }
    }

    /**
     * 向某个框中输入内容
     *
     * @param cssSelector css选择器
     * @param inputString 输入内容
     */
    protected static void elementSendKeys(String cssSelector, String inputString) {
        findElement(cssSelector).sendKeys(inputString);
    }

    /**
     * 根据cssSelector选择器获取元素
     *
     * @param cssSelector css选择器
     * @return WebElement
     */
    protected static WebElement findElement(String cssSelector) {
        return findElement(By.cssSelector(cssSelector));
    }

    /**
     * 根据By获取元素
     *
     * @param by
     * @return WebElement
     */
    protected static WebElement findElement(By by) {
        return driver.findElement(by);
    }

    /**
     * 判断元素是否存在
     *
     * @param selector
     * @return boolean
     */
    protected static boolean webElementExist(By selector) {
        try {
            driver.findElement(selector);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * 判断元素是否显示
     *
     * @param selector
     * @return boolean
     */
    protected static boolean webElementDisplayed(By selector) {
        if (webElementExist(selector)) {
            return findElement(selector).isDisplayed();
        }
        return false;
    }

    /**
     * 打开URL
     *
     * @param url
     */
    protected static void open(String url) {
        driver.get(url);
    }

    public static void setUp() {

    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static String getMobile() {
        String number = "";
        number = "139";
        Random random = new Random();
        for (int j = 0; j < 8; j++)
            number += random.nextInt(9);
        return number;
    }
}
