package com.polopoly.guitest.provider;

import com.google.inject.Provider;
import com.polopoly.guitest.framework.DriverShutdownHook;
import org.openqa.selenium.WebDriver;

/**
 * Guice Provider with a thread local scope, it takes
 * care both of initialization and shutdown for the driver.
 *
 */
public abstract class WebDriverProvider implements Provider<WebDriver>, DriverShutdownHook {

    private ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();

    @Override
    public synchronized WebDriver get() {
        WebDriver driver = threadLocal.get();
            if (driver == null || !active(driver)) {
            driver = initDriver();
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

    protected abstract WebDriver initDriver();

    public final void shutDownDriver() {
        threadLocal.get().quit(); // quit webdriver
        threadLocal.set(null);
    }


}
