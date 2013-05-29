package com.polopoly.guitest.agent;


import junit.framework.Assert;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

public class Agent007 {

    @Inject
    private WebDriver webdriver;

    public String whisper() {
        Assert.assertNotNull(webdriver);
        return "my name is Bond, James Bond";
    }

}
