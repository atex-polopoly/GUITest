package com.polopoly.guitest.agent;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.openqa.selenium.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base agent for commons GUI operation, such as logging in/out,
 * frame selection, tab selection.
 *
 *
 */
public class GuiBaseAgent {

    private static Logger LOG = Logger.getLogger(GuiBaseAgent.class.getName());

    private final String baseUrl;
    private final WebDriver webDriver;
    private final WaitAgent waitAgent;
    private final FrameAgent frameAgent;

    public final ToolbarAgent toolbar;
    public final TreeSelectAgent treeselect;


    @Inject
    public GuiBaseAgent(@Named("baseUrl") String baseUrl, WebDriver webDriver, WaitAgent waitAgent, FrameAgent frameAgent, FrameAgent frameAgent1) {
        this.baseUrl = baseUrl;
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
        this.frameAgent = frameAgent1;
        this.toolbar = new ToolbarAgent(webDriver, waitAgent, this);
        this.treeselect = new TreeSelectAgent(webDriver, waitAgent, this);
    }

    /**
     * Log in with an Admin GUI user into the Polopoly Admin GUI. Always log out
     * the current user.
     * @param username the user login name
     * @param password the user login password
     * @return this agent
     */
    public GuiBaseAgent login(String username, String password) {

        try {
            logout();
        } catch (Throwable e) {
            LOG.log(Level.WARNING, "Exception occur while logging out", e);
        }

        webDriver.get(baseUrl + "/polopoly/Login.jsp");
        webDriver.findElement(By.name("j_username")).sendKeys(username);
        webDriver.findElement(By.name("j_password")).sendKeys(password);

        webDriver.findElement(By.name("mainform")).submit();
        webDriver.switchTo().window(webDriver.getWindowHandle());


        waitAgent.waitForElement(By.id("mainframeset"));

        navFrame();
        waitAgent.waitForPageToLoad();
        userSessionFrame();
        waitAgent.waitForPageToLoad();
        workFrame();
        waitAgent.waitForPageToLoad();

        webDriver.switchTo().defaultContent();

        return this;
    }

    /**
     * Log in if necessary as an Admin GUI user into the Polopoly Admin GUI.
     * @param username the user login name
     * @param password the user login password
     * @return this agent
     */
    public GuiBaseAgent loginIfNecessary(String username, String password)
    {
        webDriver.switchTo().window(webDriver.getWindowHandle());

        if (webDriver.findElements(By.id("mainframeset")).isEmpty()) {
            return login(username, password);
        }

        return this;
    }

    /**
     * Log in as user 'sysadmin' into the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent loginAsSysadmin() {
        return login("sysadmin", "sysadmin");
    }

    /**
     * Log in if necessary as user 'sysadmin' into the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent loginIfNecessaryAsSysadmin() {
        return loginIfNecessary("sysadmin", "sysadmin");
    }

    /**
     * Log out the current Admin user from the Polopoly Admin GUI.
     * @return this agent
     */
    public GuiBaseAgent logout() {


        if (webDriver.findElements(By.id("mainframeset")).size() == 1) {
            webDriver.get(baseUrl + "/polopoly/logout");
            waitAgent.waitForElement(By.className("loginMessage"));
        }
        return this;
    }

    /**
     * Selects a HTML frame by name or id
     * @param frame the HTML name of the frame
     * @return this agent
     */
    private GuiBaseAgent selectFrame(String frame) {
        frameAgent.selectFrame(frame);
        return this;
    }

    /**
     * Selects the 'work' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent workFrame() {
        return selectFrame("work");
    }

    /**
     * Selects the 'search' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent searchFrame() {
        return selectFrame("search");
    }

    /**
     * Selects the 'nav' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent navFrame() {
        return selectFrame("nav");
    }

    /**
     * Selects the 'userSession' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent userSessionFrame() {
        return selectFrame("userSession");
    }

    /**
     * Selects the 'preview' frame in the Polopoly Admin GUI
     * @return this agent
     */
    public GuiBaseAgent previewFrame() {
        return selectFrame("preview");
    }

    public GuiBaseAgent selectTab(String tabTitle) {

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
