package com.polopoly.guitest.agent;

import junit.framework.AssertionFailedError;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

/**
 * This agent navigates (opens a content in a view or edit mode) in the Polopoly Admin Gui. 
 */
public class ContentNavigatorAgent {

    private final WebDriver webDriver;
    private final WaitAgent waitAgent;
    private final FrameAgent frameAgent;
    private final ActionEventAgent actionEventAgent;
    private final ToolbarAgent toolbarAgent;


    @Inject
    public ContentNavigatorAgent(WebDriver webDriver, WaitAgent waitAgent, FrameAgent frameAgent, ActionEventAgent actionEventAgent, ToolbarAgent toolbarAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
        this.frameAgent = frameAgent;
        this.actionEventAgent = actionEventAgent;
        this.toolbarAgent = toolbarAgent;
    }

    /**
     * Opens a content in view mode in the work frame 
     * @param contentIdString the content to open. Either a real content id or 
     * an external content id
     * @return this agent
     * @throws Exception
     */
    public ContentNavigatorAgent openContent(final String contentIdString) throws Exception {
        return internalOpenOrEditContent("view", contentIdString);
    }
    
    /**
     * Open a content in view or edit mode. 
     * @param actionEventName the action event name: 'view' or 'edit'
     * @param contentIdString the content to open
     * @return this agent
     * @throws AssertionFailedError if not able to open the content
     * @throws Exception
     */
    private ContentNavigatorAgent internalOpenOrEditContent(
            final String actionEventName,
            final String contentIdString) throws AssertionFailedError,
            Exception {
        frameAgent.workFrame();
        actionEventAgent.triggerActionEvent(actionEventName,
                contentIdString,
                "work");
        waitAgent.waitForPageToLoad();
        
//        final ContentId contentId;
//        try {
//            contentId = getContentId(contentIdString);
//        } catch (Exception e) {
//            throw new AssertionFailedError("Failed to verify that the requested " +
//            		"content was opened. ContentId: " + contentIdString 
//            		+ ", Exception: " + e.getMessage());
//        }
//        
//        final WebDriver webDriver = guiAgent.getWebDriver();
//        final String checkedContentIdString = contentId.getContentIdString();
//        
//        
//        
//        // Fallback
//        if (!isContentOpen(webDriver, checkedContentIdString)) {
//            
//            AssertRetry.assertRetry(OPEN_CONTENT_WAIT_MAX_TIME, 1000, new AssertRetry.Function() {
//                public void doAssert() throws Exception {
//                    guiAgent.agentFrame().workFrame();
//                    guiAgent.agentActionEvent().triggerActionEvent(actionEventName, 
//                            checkedContentIdString, "work");
//                    guiAgent.agentWait().waitForPageToLoad();
//                    
//                    if (!isContentOpen(webDriver, checkedContentIdString)) {
//                        throw new AssertionFailedError("The content with id: " 
//                                + checkedContentIdString + " was never opened");
//                    }
//                }
//            });
//        }
        
        return this;
    }
    
    
//    private boolean isContentOpen(final WebDriver webDriver, String contentIdString) {
//        try {
//            WebElement webElement = webDriver.findElement(
//                    By.xpath("//div[@class='content-info' and contains(@id,'content-info-" + contentIdString + "')]"));
//            
//            if (webElement != null) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (NoSuchElementException e) {
//            return false;
//        }
//    }
    
    
//    private ContentId getContentId(String contentIdString) throws Exception {
//        
//         CMServer cmServer = TestApplication.getCmClient().getCMServer();
//        
//        ContentId contentId = null;
//
//        try {
//            contentId = ContentIdFactory.createContentId(contentIdString);
//        } catch (IllegalArgumentException iae) {
//            ContentRead content = cmServer.getContent(new ExternalContentId(contentIdString));
//            contentId = content.getContentId();
//        }
//
//        return contentId;
//    }
    
    /**
     * Returns the current opened/active content's id
     * @return a content id
     * @throws Exception
     */
    public String getOpenedContentContentId() throws Exception {
        String attributeId = webDriver.findElement(By.className("content-info")).getAttribute("id");
        return attributeId.substring("content-info-".length());
    }
    
    /**
     * Checks if the current opened/active content is locked
     * @return true if locked
     */
    public boolean isOpenedContentLocked() throws Exception {
        if (webDriver.findElements(By.xpath("//div[@class = 'content-info']//img[contains(@src, 'lock.png')]")).size() == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Opens a content in edit mode in the work frame
     * @param contentIdString the content to open. Either a real content id or 
     * an external content id
     * @return this agent
     * @throws Exception 
     */
    public ContentNavigatorAgent editContent(String contentIdString) throws Exception {
        openContent(contentIdString);
        toolbarAgent.clickOnEdit();
        return this;
    }
}
