package com.polopoly.guitest.provider;

import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

/**
 * Guice Provider with a thread local scope.
 *
 */
public abstract class WebDriverProvider implements Provider<WebDriver> {

    private ThreadLocal<WebDriver> threadLocal = new ThreadLocal<WebDriver>();

    @Override
    public synchronized WebDriver get() {
        WebDriver driver = threadLocal.get();
            if (driver == null || !active(driver)) {
            driver = getDriver();
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

    protected abstract WebDriver getDriver();

}
