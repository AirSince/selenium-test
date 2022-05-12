package com.practice.web;

import com.practice.web.listener.MateJunitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@RunWith(MateJunitRunner.class)
@SpringBootTest
public class MateLoginTest extends SeleniumClassTests {

    @Test
    public void actions() {

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://home-uat.gymbomate.com/login");
        driver.manage().window().maximize(); // 页面最大化

        // 设置cookie刷新页面
        driver.manage().deleteAllCookies();
        driver.manage().addCookie(new Cookie("flash", "true"));
        driver.manage().addCookie(new Cookie("homeWebVersionControl", "22032412"));
        driver.manage().addCookie(new Cookie("userName", "dean.xu"));
        driver.navigate().refresh();
        driver.findElement(By.cssSelector("#username")).sendKeys("dean.xu");
        driver.findElement(By.cssSelector("#password")).sendKeys("Wsxb13579./");
        driver.findElement(By.cssSelector(".gym-login-form-item-button")).click();
        try {
            //页面等待
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
