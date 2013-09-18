package com.polopoly.guitest.webdriver;

import org.openqa.selenium.WebDriver;

public class FakeInitializer implements WebDriverInitializer {

    @Override
    public WebDriver initialize() {
        throw new FakeInitializer.FakeProviderException();
    }

    private class FakeProviderException extends RuntimeException {
    }
}
