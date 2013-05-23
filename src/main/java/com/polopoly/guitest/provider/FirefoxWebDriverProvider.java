package com.polopoly.guitest.provider;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FirefoxWebDriverProvider extends WebDriverProvider {

    private static final Logger LOG = Logger.getLogger(FirefoxWebDriverProvider.class.getCanonicalName());

    @Override
    public WebDriver getDriver() {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        try {
            JavaScriptError.addExtension(firefoxProfile);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not add JS error extension to Firefox profile.");
        }
        WebDriver driver = new FirefoxDriver(firefoxProfile);
        System.err.println("created webdriver: " + driver);
        return driver;
    }



}
