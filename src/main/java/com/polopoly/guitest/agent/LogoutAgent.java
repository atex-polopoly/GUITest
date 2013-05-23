package com.polopoly.guitest.agent;

import com.google.inject.name.Named;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.inject.Inject;

/**
 * This agent handles log out of users from the Polopoly Admin GUI.  
 */
public class LogoutAgent {

    private final WebDriver webDriver;
    private final String baseUrl;
    private final WaitAgent waitAgent;

    @Inject
    public LogoutAgent(WebDriver webDriver, WaitAgent waitAgent, @Named("baseUrl") String baseUrl) {
        this.webDriver = webDriver;
        this.baseUrl = baseUrl;
        this.waitAgent = waitAgent;
    }
    
    /**
     * Log out the current Admin user from the Polopoly Admin GUI.
     * @return this agent
     */
    public LogoutAgent logout() {
        

        if (webDriver.findElements(By.id("mainframeset")).size() == 1) {
            webDriver.get(baseUrl + "/polopoly/logout");
            waitAgent.waitForElement(By.className("loginMessage"));
        }
        return this;
    }
}