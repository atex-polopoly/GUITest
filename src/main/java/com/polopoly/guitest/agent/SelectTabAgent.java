package com.polopoly.guitest.agent;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

/**
 * This agent interacts with tabs panes in the Polopoly Admin GUI. 
 */
public class SelectTabAgent {


    private final WebDriver webDriver;
    private final WaitAgent waitAgent;

    @Inject
    public SelectTabAgent(WebDriver webDriver, WaitAgent waitAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
    }
    
    /**
     * Selects and opens a tab with a specific title
     * @param tabTitle the title of tab
     */
    public SelectTabAgent selectTab(String tabTitle) {
        
        String classString = null;

        try {
            WebElement webElement = webDriver.findElement(By.xpath("//div[@class='tabbedMenu']/ul/li/a[contains(text(),'"+ tabTitle+"')]/.."));
            classString = webElement.getAttribute("class");
        } catch (NoSuchElementException e) {
        }

        if (!"selected".equals(classString)) {

            WebElement webElement = webDriver.findElement(By.xpath("//div[@class='tabbedMenu']/ul/li/a[contains(text(),'"+tabTitle+"')]"));
            String hrefAttribute = webElement.getAttribute("href");
            webElement.click();
            
            if (hrefAttribute.contains("idleCursor()")) { // Ajax tab
                waitAgent.waitForAjaxPageToLoad();
            } else {
                waitAgent.waitForPageToLoad();
            }
        }
        return this;
    }
}
