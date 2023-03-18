package com.dailydatahub.dailydatacrawler.module;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SeleniumComponent {
    private static String SELENIUM_ID = "webdriver.chrome.driver";
    private static String SELENIUM_PATH_JENKINS = "external/selenium/chromedriver_linux64/chromedriver";
    private static String SELENIUM_PATH_MAC = "external/selenium/chromedriver_mac64/chromedriver";
    private static String SELENIUM_PATH_MAC_M1 = "external/selenium/chromedriver_mac64_arm64/chromedriver";
    private static String SELENIUM_PATH_LINUX = "external/selenium/chromedriver_linux64/chromedriver";
    private static String SELENIUM_PATH_WINDOWS = "external/selenium/chromedriver_win32/chromedriver.exe";
    private WebDriver driver;

    public SeleniumComponent() {
        try {
            InputStream inputStream = new ClassPathResource(SELENIUM_PATH_JENKINS).getInputStream();
            File file = File.createTempFile("chromedriver", "");
            try {
                FileUtils.copyInputStreamToFile(inputStream,file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            file.setReadable(true, false);
            file.setExecutable(true, false);
            file.setWritable(true, false);
            System.setProperty(SELENIUM_ID, file.getPath());
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--lang=ko");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.setCapability("ignoreProtectedModeSettings", true);
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
        } catch (Exception eA) {
            // eA.printStackTrace();
           System.out.println("jenkins failed");
            try {
                InputStream inputStream = new ClassPathResource(SELENIUM_PATH_LINUX).getInputStream();
                File file = File.createTempFile("chromedriver", "");
                try {
                    FileUtils.copyInputStreamToFile(inputStream,file);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
                file.setReadable(true, false);
                file.setExecutable(true, false);
                file.setWritable(true, false);
                System.setProperty(SELENIUM_ID, file.getPath());
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--lang=ko");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.setCapability("ignoreProtectedModeSettings", true);
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
                driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
            } catch (Exception eB) {
                // eB.printStackTrace();
                System.out.println("linux failed");
                try {
                    InputStream inputStream = new ClassPathResource(SELENIUM_PATH_WINDOWS).getInputStream();
                    File file = File.createTempFile("chromedriver", "");
                    try {
                        FileUtils.copyInputStreamToFile(inputStream,file);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }
                    file.setReadable(true, false);
                    file.setExecutable(true, false);
                    file.setWritable(true, false);
                    System.setProperty(SELENIUM_ID, file.getPath());
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--lang=ko");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.setCapability("ignoreProtectedModeSettings", true);
                    options.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(options);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
                    driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
                } catch (Exception eC) {
                    // eC.printStackTrace();
                    System.out.println("window failed");
                    try {
                        InputStream inputStream = new ClassPathResource(SELENIUM_PATH_MAC).getInputStream();
                        File file = File.createTempFile("chromedriver", "");
                        try {
                            FileUtils.copyInputStreamToFile(inputStream,file);
                        } finally {
                            IOUtils.closeQuietly(inputStream);
                        }
                        file.setReadable(true, false);
                        file.setExecutable(true, false);
                        file.setWritable(true, false);
                        System.setProperty(SELENIUM_ID, file.getPath());
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--lang=ko");
                        options.addArguments("--no-sandbox");
                        options.addArguments("--disable-dev-shm-usage");
                        options.addArguments("--disable-gpu");
                        options.setCapability("ignoreProtectedModeSettings", true);
                        options.addArguments("--remote-allow-origins=*");
                        driver = new ChromeDriver(options);
                        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
                        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
                    } catch (Exception eD) {
                        // eD.printStackTrace();
                        System.out.println("mac failed");
                        try {
                            InputStream inputStream = new ClassPathResource(SELENIUM_PATH_MAC_M1).getInputStream();
                            File file = File.createTempFile("chromedriver", "");
                            try {
                                FileUtils.copyInputStreamToFile(inputStream,file);
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                            file.setReadable(true, false);
                            file.setExecutable(true, false);
                            file.setWritable(true, false);
                            System.setProperty(SELENIUM_ID, file.getPath());
                            ChromeOptions options = new ChromeOptions();
                            options.addArguments("--lang=ko");
                            options.addArguments("--no-sandbox");
                            options.addArguments("--disable-dev-shm-usage");
                            options.addArguments("--disable-gpu");
                            options.setCapability("ignoreProtectedModeSettings", true);
                            options.addArguments("--remote-allow-origins=*");
                            driver = new ChromeDriver(options);
                            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
                            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
                        }catch(Exception eF){
                            // eF.printStackTrace();
                            System.out.println("mac 64 failed");
                        }
                        
                    }
                }
            }
        }
    }

    public String requestUrlByTag(String url, String elementName) {
        driver.get(url);
        WebElement webElement = driver.findElement(By.tagName(elementName));
        String outerHTML = webElement.getAttribute("outerHTML");
        return outerHTML;
    }

    public String requestUrlById(String url, String idName) {
        driver.get(url);
        WebElement webElement = driver.findElement(By.tagName("body"));
        WebElement targetElement = webElement.findElement(By.id(idName));
        String outerHTML = targetElement.getAttribute("outerHTML");
        return outerHTML;
    }

    public String requestUrlbyClass(String url, String className) {
        driver.get(url);
        WebElement webElement = driver.findElement(By.tagName("body"));
        WebElement targetElement = webElement.findElement(By.className(className));
        System.out.println(targetElement);
        String outerHTML = targetElement.getAttribute("outerHTML");
        return outerHTML;
    }

    public void quitDriver() {
        driver.quit();
    }

}