package com.polopoly.guitest.agent;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
/**
 * This agent interacts with the clipboard in the Polopoly Admin GUI.
 */
public class ClipboardAgent {

    private final WebDriver webDriver;
    private final WaitAgent waitAgent;


    @Inject
    public ClipboardAgent(WebDriver webDriver, WaitAgent waitAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
    }

    /**
     * Copy the current opened content into the clipboard
     * @return this agent
     * @throws Exception
     */
    public ClipboardAgent copyOpenedContent() throws Exception {

        webDriver.findElement(
                By.xpath("//div[contains(@class,'content-info')]/button[contains(@class, 'clipboard')]")).click();

        return this;
    }

    /**
     * Copy a specific content into the clipboard present in the current frame pane
     * @param contentIdString the content's id to copy
     * @return this agent
     * @throws Exception
     */
    public ClipboardAgent copyContent(String contentIdString) throws Exception {
        webDriver.findElement(By.xpath("//button[contains(@class, 'clipboard') and contains(@onclick, '"
                       + contentIdString + "')]")).click();
        return this;
    }

    /**
     * Paste the current content in the clipboard into a specific target,
     * will wait on a page reload
     * @param target the target's label to paste in
     * @return this agent
     * @throws Exception
     */
    public ClipboardAgent pasteContent(String target)
        throws Exception {
        return pasteContent(target, false);
    }

    /**
     * Paste the current content in the clipboard into a specific target,
     * will wait for ajax refresh
     * @param target the target's label to paste in
     * @return this agent
     * @throws Exception
     */
    public ClipboardAgent pasteContent(String target, boolean isAjaxList)
        throws Exception {

        String pasteSelectorPrefix = "//h2[contains(text(),'"+target+"')]";
        String pasteSelectorSuffix = "/button[contains(@class, 'clipboard') and contains(@title, 'Paste')]";

        // Slots
        String pasteSelector = pasteSelectorPrefix + "/../" + pasteSelectorSuffix;

        if (webDriver.findElements(By.xpath(pasteSelector)).isEmpty()) {
            // Content lists
            pasteSelector = pasteSelectorPrefix + pasteSelectorSuffix;
        }

        if (webDriver.findElements(By.xpath(pasteSelector)).isEmpty()) {
           pasteSelector = "//fieldset[contains(@class, '"+target+"']/" + pasteSelectorSuffix;
        }

        webDriver.findElement(By.xpath(pasteSelector)).click();

        if(isAjaxList) {
            waitAgent.waitForAjaxPageToLoad();
        } else {
            waitAgent.waitForPageToLoad();
        }

        return this;
    }

    /**
     * Returns the size of clipboard
     * @return the size of the clipboard
     */
    public int getClipboardSize()
    {
        JavascriptExecutor executer = (JavascriptExecutor) webDriver;
        Long count = (Long) executer.executeScript("return window.JSClipboard.getInstance().getLatestClip().isMultiClip == null ? "
                                              + "1 : window.JSClipboard.getInstance().getLatestClip().getClipCount();");
        return count.intValue();
    }
}
