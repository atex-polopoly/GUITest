package com.polopoly.guitest.agent;

import java.util.List;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * This agent interacts with Polopoly quick content creator fields in the Polopoly Admin GUI.
 */
public class QuickContentCreatorAgent {

    private final WebDriver webDriver;
    private final FrameAgent frameAgent;

    @Inject
    public QuickContentCreatorAgent(WebDriver webDriver, FrameAgent frameAgent) {
        this.webDriver = webDriver;
        this.frameAgent = frameAgent;
    }
    
    /**
     * Creates a content using the quick content creator.  
     * @param label The label in the quick creator of the content to create.
     * @return this
     */
    public QuickContentCreatorAgent createContent(String label) {
    
        return createContent(label, 0);
    }
    
    /**
     * Creates a content using the quick content creator with a specific section title.
     * @param label The label in the quick creator of the content to create.
     * @param index The index of the item to create. Useful if the provided label results in several entries.
     * @return this
     */
    public QuickContentCreatorAgent createContent(String label, int index) {
        
        if(index < 0){
            throw new IllegalArgumentException("You need to specify a positive index rather than " + index);
        }

        frameAgent.navFrame();

        List<WebElement> toggleIconElement = 
        		webDriver.findElements(By.xpath("//div[contains(@class, 'quickCreator folded')]//div[contains(@class, 'toggleIcon')]"));
        
        if (!toggleIconElement.isEmpty()) {
        	toggleIconElement.get(0).click();
        }
        
        
        String selectLocator = "//div[@class='quickCreator contentCreator container']//a//span[text()='" + label + "']/../../a";
        webDriver.findElement(By.xpath(selectLocator));
        List<WebElement> webElements = webDriver.findElements(By.xpath(selectLocator));
        WebElement createLink = webElements.get(index);
        createLink.click();
        
        return this;
    }
}
