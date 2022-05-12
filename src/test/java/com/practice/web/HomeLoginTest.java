package com.practice.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
public class HomeLoginTest {

    public static void main(String args[]) {
        try {
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
//            options.setCapability(CapabilityType.BROWSER_NAME, "chrome");
//            WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
            WebDriver driver = new RemoteWebDriver(new URL("http://1.15.154.192:8080/wd/hub"), options);
//            WebDriver driver = new RemoteWebDriver(new URL("http://1.15.154.192:8080"), options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://home-uat.gymbomate.com/login");
            // 设置cookie刷新页面
            driver.manage().deleteAllCookies();
            driver.manage().addCookie(new Cookie("flash", "true"));
            driver.manage().addCookie(new Cookie("homeWebVersionControl", "22032412"));
            driver.manage().addCookie(new Cookie("userName", "dean.xu"));
            driver.navigate().refresh();
            driver.findElement(By.cssSelector("#username")).sendKeys("dean.xu");
            driver.findElement(By.cssSelector("#password")).sendKeys("Wsxb13579./");
            driver.findElement(By.cssSelector(".gym-login-form-item-button")).click();
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 退出
//            driver.quit();
        }

    }
}
