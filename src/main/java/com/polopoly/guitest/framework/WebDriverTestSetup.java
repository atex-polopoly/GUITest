package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.openqa.selenium.WebDriver;

/**
 * This class holds test setup for Web Driver Tests. 
 */
public class WebDriverTestSetup {

    public final WebDriver webDriver;
    public final String baseURL;
    public final boolean logOutAfterTest;

    /**
     * Constructor
     * @param webDriver the Web Driver use for test process
     */
    @Inject
    public WebDriverTestSetup(WebDriver webDriver,
                              @Named("baseUrl") String baseUrl,
                              @Named("logOutAfterTest") boolean logOutAfterTest)
    {
        this.webDriver = webDriver;
        this.baseURL = baseUrl;
        this.logOutAfterTest = logOutAfterTest;
    }
    


}
