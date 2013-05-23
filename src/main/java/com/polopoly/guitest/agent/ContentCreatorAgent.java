package com.polopoly.guitest.agent;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * This agent interacts with Polopoly content creator fields in the Polopoly Admin GUI.
 */
public class ContentCreatorAgent {

    private final WebDriver webDriver;
    private final WaitAgent waitAgent;

    @Inject
    public ContentCreatorAgent(WebDriver webDriver, WaitAgent waitAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
    }
    
    /**
     * Creates a content using the content creator with a specific section title.
     * @param sectionTitle the title of the content creator container
     */
    public ContentCreatorAgent createContent(String sectionTitle) {
        
        String selectLocator = "//h2[contains(text(),'"+ sectionTitle+"')]";
        
        WebElement webElement = webDriver.findElement(By.xpath(selectLocator + "/..//button[1]"));
        webElement.click();
        waitAgent.waitForPageToLoad();
        
        return this;
    }
    
    /**
     * Creates a content using the content creator with a specific section title and content type label.
     * @param sectionTitle the title of the content creator container
     * @param contentTypeOptionLabel the label of the content type    
     */
    public ContentCreatorAgent createContent(String sectionTitle, String contentTypeOptionLabel) {
        
        String selectLocator = selectOnPosition(sectionTitle,
                contentTypeOptionLabel, 1);
        
        WebElement webElement = webDriver.findElement(By.xpath(selectLocator + "/../button[1]"));
        webElement.click();
        waitAgent.waitForPageToLoad();
        
        return this;
    }
    /**
     * Creates a content using the content creator with a specific section title; category type label
     * and content type label.
     * @param sectionTitle the title of the content creator container
     * @param categoryTypeOptionLabel the label of the content creator category type
     * @param contentTypeOptionLabel the label of the content type    
     */
    public ContentCreatorAgent createContent(String sectionTitle, 
                                             String categoryTypeOptionLabel, 
                                             String contentTypeOptionLabel) {
        selectOnPosition(sectionTitle, categoryTypeOptionLabel, 1);
        String selectLocator = selectOnPosition(sectionTitle, 
                                                contentTypeOptionLabel, 2);
        WebElement webElement = webDriver.findElement(By.xpath(selectLocator + "/../button[1]"));
        webElement.click();
        waitAgent.waitForPageToLoad();
        
        return this;
    }
    
    /**
     * Selects an option with a specific label in a select input on specific position.
     * @param sectionTitle the content creator title
     * @param optionLabel the option label
     * @param position the position of the select input
     */
    private String selectOnPosition(String sectionTitle, String optionLabel, int position) {

        String selectLocatorPrefix = "//h2[contains(text(),'"+ sectionTitle+"')]";
        String selectLocatorSuffix = "select[position()="+position+"]/option[text()='"+optionLabel + "']/..";
        
        String selectLocator = selectLocatorPrefix + "/../" + selectLocatorSuffix;
        if (webDriver.findElements(By.xpath(selectLocator)).isEmpty()) {
            selectLocator = selectLocatorPrefix + "/following-sibling::*/" + selectLocatorSuffix; 
        }
        Select selectBox = new Select(webDriver.findElement(By.xpath(selectLocator)));
        selectBox.selectByVisibleText(optionLabel);
                
        return selectLocator;
    }
}
