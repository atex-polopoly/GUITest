package com.polopoly.guitest.agent;

import com.google.inject.name.Named;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This agent handles log in of users into the Polopoly Admin GUI.  
 */
public class LoginAgent {
    
    private static Logger LOG = Logger.getLogger(LoginAgent.class.getName());
    private final String baseUrl;
    private final WebDriver webDriver;
    private final WaitAgent waitAgent;
    private final FrameAgent frameAgent;
    private final LogoutAgent logoutAgent;

    @Inject
    public LoginAgent(WebDriver webDriver,
                      WaitAgent waitAgent,
                      FrameAgent frameAgent,
                      LogoutAgent logoutAgent,
                      @Named("baseUrl") String baseUrl) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
        this.frameAgent = frameAgent;
        this.logoutAgent = logoutAgent;
        this.baseUrl = baseUrl;
    }
    
    /**
     * Log in with an Admin GUI user into the Polopoly Admin GUI. Always log out 
     * the current user.
     * @param username the user login name
     * @param password the user login password
     * @return this agent
     */
    public LoginAgent login(String username, String password) {
        
        try {
            logoutAgent.logout();
        } catch (Throwable e) {
            LOG.log(Level.WARNING, "Exception occur while logging out", e);
        }

        webDriver.get(baseUrl + "/polopoly/Login.jsp");
        webDriver.findElement(By.name("j_username")).sendKeys(username);
        webDriver.findElement(By.name("j_password")).sendKeys(password);
        
        webDriver.findElement(By.name("mainform")).submit();
        webDriver.switchTo().window(webDriver.getWindowHandle());


        waitAgent.waitForElement(By.id("mainframeset"));
        
        frameAgent.navFrame();
        waitAgent.waitForPageToLoad();
        frameAgent.userSessionFrame();
        waitAgent.waitForPageToLoad();
        frameAgent.workFrame();
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
    public LoginAgent loginIfNecessary(String username, String password)
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
    public LoginAgent loginAsSysadmin() {
        return login("sysadmin", "sysadmin");
    }
    
    /**
     * Log in if necessary as user 'sysadmin' into the Polopoly Admin GUI
     * @return this agent
     */
    public LoginAgent loginIfNecessaryAsSysadmin() {
        return loginIfNecessary("sysadmin", "sysadmin");
    }
}
