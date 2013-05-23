package com.polopoly.guitest.agent;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.inject.Inject;

/**
 * This agent selects a HTML frame to set the current Selenium frame context.  
 */
public class FrameAgent {

    private final WebDriver webDriver;

    @Inject
    public FrameAgent(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    
    /**
     * Selects a HTML frame by name or id
     * @param frame the HTML name of the frame
     * @return this agent
     */
    public FrameAgent selectFrame(String frame) {
        
        webDriver.switchTo().window(webDriver.getWindowHandle());
        webDriver.switchTo().frame(frame);   
        return this;
    }

    
    /**
     * Selects returns the name of the current frame
     */
    public String getCurrentFrame(){
        String frame = null;
        try{
            WebElement el = (WebElement) ((JavascriptExecutor) webDriver).executeScript(
                    "return window.frameElement");
            
            webDriver.switchTo().defaultContent();
            frame = el.getAttribute("name");
            return frame;
        } finally{
            selectFrame(frame);
        }
    }
    
    /**
     * Selects the 'work' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public FrameAgent workFrame() {
        return selectFrame("work");
    }
    
    /**
     * Selects the 'search' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public FrameAgent searchFrame() {
        return selectFrame("search");
    }
    
    /**
     * Selects the 'nav' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public FrameAgent navFrame() {
        return selectFrame("nav");
    }
    
    /**
     * Selects the 'userSession' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public FrameAgent userSessionFrame() {
        return selectFrame("userSession");
    }
    
    /**
     * Selects the 'preview' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public FrameAgent previewFrame() {
        return selectFrame("preview");
    }
}
