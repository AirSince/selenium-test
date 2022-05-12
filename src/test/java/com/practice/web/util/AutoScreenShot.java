package com.practice.web.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自动截图
 */
public class AutoScreenShot {

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }

    public static void screenShot(WebDriver driver) {
        String fileName = "" + getDateTime();
        work(driver, fileName);
    }

    public static void screenShot(WebDriver driver, String path, String fileName) {
        if (path == null && fileName == null) {
            screenShot(driver);
            return;
        }
        if (path == null) {
            work(driver, fileName);
            return;
        }
        if (fileName == null) {
            work(driver, path + getDateTime());
            return;
        }
        work(driver, path + fileName);
    }

    private static void work(WebDriver driver, String fileName) {
        File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(screenShot, new File(fileName + ".jpg"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
