package com.practice.web;

import com.practice.web.util.AutoScreenShot;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 同一个测试类只打开一次浏览器，完成全部@test测试，（需要拆分测试case时使用）
 */
public class SeleniumClassTests extends BaseSeleniumTests {
    static WebDriver driver;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] nameArray = getClass().getName().split("\\.");
        String className = nameArray[nameArray.length - 1];
        AutoScreenShot.screenShot(driver, className + "/", testName.getMethodName());
    }

    @BeforeClass
    public static void beforeClass() {
//        ClassLoader classLoader = SeleniumClassTests.class.getClassLoader();
//        String driverFile = classLoader.getResource("chromedriver101").getFile();
//        String driverFile = "/Users/dean.xu/Downloads/chromedriver101";
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        ChromeDriverService service = new ChromeDriverService.Builder()
//                .usingDriverExecutable(new File(driverFile))
//                .build();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true); // 无页面启动
        options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
        //options.addArguments("--headless");
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.merge(capabilities);

        // 设置 “chrome正受到自动测试软件的控制” 信息栏不显示
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        // 设置 "是否记住密码" 弹窗不显示
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
//        driver = new ChromeDriver(service, options);
        try {
            driver = new RemoteWebDriver(new URL("http://1.15.154.192:8080/wd/hub"), options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}
