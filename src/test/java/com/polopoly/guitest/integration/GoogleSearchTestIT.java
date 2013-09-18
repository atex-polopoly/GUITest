package com.polopoly.guitest.integration;

import com.atex.testinject.TestInjectRunner;
import com.google.inject.Inject;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(TestInjectRunner.class)
public class GoogleSearchTestIT {

    @Inject
    WebDriver webDriver;

    @Test
    public void search() {
        webDriver.get("http://www.google.com");
        WebElement element = webDriver.findElement(By.name("q"));
        element.sendKeys("atex polopoly");
        element.submit();
        Assert.assertTrue(webDriver.getTitle().contains("Google"));
    }

    @Test
    public void dummy() {
        webDriver.get("http://www.atex.com");
        Assert.assertTrue(webDriver.getTitle().contains("Atex"));
    }


}
