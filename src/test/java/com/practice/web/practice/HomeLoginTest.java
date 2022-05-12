package com.practice.web.practice;

import com.practice.web.StaticSeleniumTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import java.util.concurrent.TimeUnit;

public class HomeLoginTest extends StaticSeleniumTest {

    public static void main(String args[]) {
        try {
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
            driver.quit();
        }

    }
}