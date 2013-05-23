package com.polopoly.guitest.agent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This agent interacts with toolbars in the Polopoly Admin GUI. 
 */
public class ToolbarAgent {

    private final WebDriver webDriver;
    private final WaitAgent waitAgent;
    private final GuiBaseAgent guiBaseAgent;

    public ToolbarAgent(WebDriver webDriver, WaitAgent waitAgent, GuiBaseAgent guiBaseAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
        this.guiBaseAgent = guiBaseAgent;
    }
    
    /**
     * Clicks on a button with a specific action name in the current frame pane
     * @param actionName the button's action name
     * @return this agent
     */
    public GuiBaseAgent clickOnButton(String actionName)
    {   
        webDriver.findElement(By.xpath("//div[@class='toolbar']//a[@*='" + actionName + "']")).click();
        waitAgent.waitForPageToLoad();
        
        return guiBaseAgent;
    }
    
    /**
     * Clicks on the button with action name 'saveAndView'
     * @return this agent
     */
    public GuiBaseAgent clickOnSaveAndView() {
        return clickOnButton("saveAndView");
    }
    
    /**
     * Clicks on the button with action name 'saveAndClose'
     * @return this agent
     */
    public GuiBaseAgent clickOnSaveAndClose() {
        return clickOnButton("saveAndClose");
    }
    
    /**
     * Clicks on the button with action name 'saveAndEdit'
     * @return this agent
     */
    public GuiBaseAgent clickOnSaveAndEdit() {
        return clickOnButton("saveAndEdit");
    }
    
    /**
     * Clicks on the button with action name 'abortAndView'
     * @return this agent
     */
    public GuiBaseAgent clickOnCancelAndView() {
        return clickOnButton("abortAndView");
    }
    
    /**
     * Clicks on the button with action name 'abortAndClose'
     * @return this agent
     */
    public GuiBaseAgent clickOnCancelAndClose() {
        return clickOnButton("abortAndClose");
    }
    
    /**
     * Clicks on the button with action name 'insert'
     * @return this agent
     */
    public GuiBaseAgent clickOnInsert() {
        return clickOnButton("insert");
    }
    
    /**
     * Clicks on the button with action name 'unlock'
     * @return this agent
     */
    public GuiBaseAgent clickOnUnlock() {
        return clickOnButton("unlock");
    }
    
    /**
     * Clicks on the button with action name 'saveAndFastInsert'
     * @return this agent
     */
    public GuiBaseAgent clickOnSaveAndFastInsert() {
        return clickOnButton("saveAndFastInsert");
    }
    
    /**
     * Clicks on the button with action name 'saveAndInsert'
     * @return this agent
     */
    public GuiBaseAgent clickOnSaveAndInsert() {
        return clickOnButton("saveAndInsert");
    }
    
    /**
     * Clicks on the button with action name 'editUsersAndGroups'
     * @return this agent
     */
    public GuiBaseAgent clickOnUsersAndGroups() {
        return clickOnButton("editUsersAndGroups");
    }
    
    /**
     * Clicks on the button with action name 'saveTemporary'
     * @return this agent
     */
    public GuiBaseAgent clickOnSaveDraft() {
        return clickOnButton("saveTemporary");
    }

    /**
     * Clicks on the button with action name 'close'
     * @return this agent
     */
    public GuiBaseAgent clickOnClose() {
        return clickOnButton("close");
    }
    
    /**
     * Clicks on the button with action name 'edit'
     * @return this agent
     */
    public GuiBaseAgent clickOnEdit() {
        return clickOnButton("edit");
    }
    
    /**
     * Clicks on the button with action name 'preview'
     * @return this agent
     */
    public GuiBaseAgent clickOnPreview() {
        return clickOnButton("preview");
    }
    
    /**
     * Clicks on the button with action name 'refresh'
     * @return this agent
     */
    public GuiBaseAgent clickOnRefresh() {
        return clickOnButton("refresh");
    }
    
    /**
     * Clicks on the button with action name 'copy'
     * @return this agent
     */
    public GuiBaseAgent clickOnCopy() {
        return clickOnButton("copy");
    }
    
    /**
     * Clicks on the button with action name 'properties'
     * @return this agent
     */
    public GuiBaseAgent clickOnProperties() {
        return clickOnButton("properties");
    }
    
    /**
     * Removes a content from a target with a specific title or content id
     * @param targetTitle the title of the target 
     * @param titleOrContentId the title or the content id
     * @return this agent
     */
    public GuiBaseAgent removeContentFrom(String targetTitle, String titleOrContentId) {
        
        
        String selectorPrefix = "//h2[contains(text(),'"+targetTitle+"')]/..//";
        String selectorTitle = "a[contains(text(), '"+titleOrContentId+"')]";
        String selectorContentId = "img[contains(@title, '"+titleOrContentId+"')]";
        String selectorSuffix = "//button[contains(@title, 'Remove')]";
        
        try {
            Float.parseFloat(titleOrContentId);
            selectorPrefix += selectorContentId;
        } catch (Exception e) {
            selectorPrefix += selectorTitle;
        }
        
        // Slots
        String selector = selectorPrefix + "/../.." + selectorSuffix;
        

        if (webDriver.findElements(By.xpath(selector)).isEmpty()) {
            // Content lists
            selector = selectorPrefix + selectorSuffix;
        }
        
        if (webDriver.findElements(By.xpath(selector)).isEmpty()) {
           selector = "//fieldset[contains(@class, '"+targetTitle+"']/" + selectorSuffix;
        }
        
        webDriver.findElement(By.xpath(selector)).click();
        waitAgent.waitForPageToLoad();
        
        return guiBaseAgent;
        
    }
}
