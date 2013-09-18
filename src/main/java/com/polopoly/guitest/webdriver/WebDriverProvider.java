package com.polopoly.guitest.webdriver;

import com.atex.testinject.TestHooks;
import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

import java.util.logging.Logger;

/**
 * Guice Provider with a thread local scope, it takes
 * care both of initialization and shutdown for the driver.
 *
 */
public class WebDriverProvider implements Provider<WebDriver>, TestHooks {

    private ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();
    private static final Logger LOG = Logger.getLogger(WebDriverProvider.class.getCanonicalName());

    private static WebDriverInitializer webDriverInitializer;

    public WebDriverProvider() {
        LOG.fine("initializing provider");
    }

    public static void setDriverInitializer(WebDriverInitializer webDriverInitializer) {
        WebDriverProvider.webDriverInitializer = webDriverInitializer;
    }

    @Override
    public synchronized WebDriver get() {
        WebDriver driver = threadLocal.get();
        if (driver == null || !active(driver)) {
            driver = webDriverInitializer.initialize();
            LOG.info("inizializing webDriver.");
            threadLocal.set(driver);
        }
        return driver;
    }

    private boolean active(WebDriver driver) {
        try {
            driver.getTitle();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void before() {
        LOG.info("webDriverProvider test hook: before");
    }

    @Override
    public void after() {
        LOG.info("webDriverProvider test hook: after");
        threadLocal.get().quit();
        threadLocal.set(null);
    }

}
