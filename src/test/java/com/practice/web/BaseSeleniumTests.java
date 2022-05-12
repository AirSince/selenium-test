package com.practice.web;

import com.practice.web.util.AutoScreenShot;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseSeleniumTests{

    protected WebDriver driver;

    @Rule
    public TestName testName = new TestName();

    /**
     * 点击元素(如果存在并显示)并等待N秒
     *
     * @param cssSelector css选择器
     */
    protected void elementClickIfExist(String cssSelector, int second) throws InterruptedException {
        if (webElementDisplayed(By.cssSelector(cssSelector))) {
            elementClick(cssSelector, second);
        }
    }

    /**
     * 点击元素并等待N秒
     *
     * @param cssSelector css选择器
     */
    protected void elementClick(String cssSelector, int second) throws InterruptedException {
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
    protected void elementSendKeys(String cssSelector, String inputString) {
        findElement(cssSelector).sendKeys(inputString);
    }

    /**
     * 根据cssSelector选择器获取元素
     *
     * @param cssSelector css选择器
     * @return WebElement
     */
    protected WebElement findElement(String cssSelector) {
        return findElement(By.cssSelector(cssSelector));
    }

    /**
     * 根据By获取元素
     *
     * @param by
     * @return WebElement
     */
    protected WebElement findElement(By by) {
        return this.driver.findElement(by);
    }

    /**
     * 判断元素是否存在
     *
     * @param selector
     * @return boolean
     */
    protected boolean webElementExist(By selector) {
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
    protected boolean webElementDisplayed(By selector) {
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
    protected void open(String url) {
        this.driver.get(url);
    }

    /**
     * 设置隐式等待
     *
     * @param time
     */
    protected void implicitlyWait(int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    /**
     * 页面最大化
     */
    protected void windowMaximize() {
        driver.manage().window().maximize();
    }

    /**
     * 通过css选择元素
     *
     * @param cssSelector
     * @return
     */
    protected WebElement FindElementByCss(String cssSelector) {
        return driver.findElement(By.cssSelector(cssSelector));
    }

    /**
     * css显式等待获元素
     *
     * @param cssSelector css
     * @param time        显示等待时间（秒）
     * @return
     */
//    protected WebElement waitAndFindElementByCss(String cssSelector, int time) {
//        WebDriverWait wait = new WebDriverWait(driver, time);
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
//    }

    /**
     * 通过xPath选择元素
     *
     * @param xPathSelector
     * @return
     */
    protected WebElement FindElementByXpath(String xPathSelector) {
        return driver.findElement(By.xpath(xPathSelector));
    }

    /**
     * xpath显式等待获元素
     *
     * @param xPathSelector xpath
     * @param time          时间（秒）
     * @return
     */
//    protected WebElement waitAndFindElementByXpath(String xPathSelector, int time) {
//        WebDriverWait wait = new WebDriverWait(driver, time);
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPathSelector)));
//    }

    /**
     * 设置cookie刷新页面
     *
     * @param cookieKey
     * @param cookieValue
     */
    protected void addCookie(String cookieKey, String cookieValue) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(cookie);
        driver.navigate().refresh();
    }

    /**
     * 截图保存
     *
     * @param path 路径
     * @param name 图片名字
     */
    public void screenShot(String path, String name) {
        AutoScreenShot.screenShot(driver, null, name);
    }
}
