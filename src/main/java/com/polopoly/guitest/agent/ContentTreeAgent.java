package com.polopoly.guitest.agent;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ContentTreeAgent {

    private final WebDriver webDriver;

    @Inject
    public ContentTreeAgent(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public void open(String label) {
        List<WebElement> elements = webDriver.findElements(By.xpath("//div[contains(@class,'contentTree')]" +
                "//div[contains(@class,'tree')]" +
                "//a[contains(@class, 'defaultAction')]"));

        for (WebElement element : elements) {
            if (element.getText().trim().equals(label)) {
                element.click();
                return;
            }
        }

        throw new NoSuchElementException("could not find Content Tree element identified by label: " + label);
    }




    private ContentTreeAgent toggle(String label, Boolean expand) {
        List<WebElement> navEntries = webDriver.findElements(By.xpath("//div[contains(@class,'contentTree')]" +
                "//div[contains(@class,'tree')]" +
                "//li[a[contains(@class, 'expander')]]"));
        for (WebElement navEntry : navEntries) {
            WebElement defaultAction = navEntry.findElement(By.xpath("./a[contains(@class, 'defaultAction')]"));
            if (defaultAction.getText().trim().equals(label)) {
                WebElement expander = navEntry.findElement(By.xpath("./a[contains(@class, 'expander')]"));
                if (expander.getAttribute("onclick").contains("shouldBeExpanded\":" + expand)) {
                    expander.click();
                    // after the click we need to allow
                    // for a delay for the javascript action to take place
                    // otherwise drilling down the nav tree will fail
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return this;
                }
            }
        }
        throw new NoSuchElementException("could not find Content Tree element identified by label: " + label);
    }

    public ContentTreeAgent collapse(String label) {
        return toggle(label, false);
    }

    public ContentTreeAgent expand(String label) {
        return toggle(label, true);
    }




}
