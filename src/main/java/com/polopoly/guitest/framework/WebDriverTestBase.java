package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import org.junit.After;

/**
 * This is an abstract base class that should be extended by a junit test class 
 * with purpose of running selenium browser tests.  
 */
public abstract class WebDriverTestBase {

    @Inject
    protected WebDriverTestSetup webDriverTestSetup;

    public WebDriverTestBase() {
        super();
    }


   @After
    public void afterTest() throws Exception {
        try {
    } finally {
        if (webDriverTestSetup.webDriver != null) {
            
            if(webDriverTestSetup.logOutAfterTest){
                webDriverTestSetup.webDriver.get(webDriverTestSetup.baseURL + "/polopoly/logout");
            }
            
            webDriverTestSetup.webDriver.quit();
            webDriverTestSetup = null;
        }
      }
    }


}
