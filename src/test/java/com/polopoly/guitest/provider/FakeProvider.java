package com.polopoly.guitest.provider;

import org.openqa.selenium.WebDriver;

public class FakeProvider extends WebDriverProvider {

    @Override
    protected WebDriver initDriver() {
        throw new FakeProvider.FakeProviderException();
    }

    private class FakeProviderException extends RuntimeException {
    }
}
