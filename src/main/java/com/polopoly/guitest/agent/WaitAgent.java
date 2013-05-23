package com.polopoly.guitest.agent;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This agent verifies and waits until pages are complete loaded in the Polopoly Admin GUI.
 */
public class WaitAgent {

    private static Logger LOG = Logger.getLogger(WaitAgent.class.getName());

    private static final String SELENIUM_PAGE_LOAD_COUNT = "SeleniumPageLoadCount";
    private final static long DEFAULT_TIMEOUT_SECONDS = 60L;
    private final WebDriver webDriver;

    @Inject
    public WaitAgent(WebDriver webDriver) {
        this.webDriver  = webDriver;
    }
    
    /**
     * Waits for the page in the current selected frame is loaded. 
     * @return this agent
     */
    public WaitAgent waitForPageToLoad() {

        try {

            final String windowName = (String) ((JavascriptExecutor) webDriver).executeScript("return window.name");
            final String pageLoadVariableName = SELENIUM_PAGE_LOAD_COUNT + windowName;
            final String oldPageLoadVariableName = SELENIUM_PAGE_LOAD_COUNT
                    + windowName + "Old";

            webDriver.switchTo().window(webDriver.getWindowHandle());
            
            List<JavaScriptError> jsErrors = JavaScriptError.readErrors(webDriver);
            if (jsErrors.size() != 0) {
            	LOG.log(Level.WARNING, "JS errors found that could block the wait for page to load.");
            	for (JavaScriptError javaScriptError : jsErrors) {
            		LOG.log(Level.WARNING, "JS error: " + javaScriptError.getErrorMessage());
				}     	
            }

            Long seleniumPolopoly = safeGetEval(webDriver, "return window."
                    + pageLoadVariableName);

            if (seleniumPolopoly != null) {

                safeGetEval(webDriver, "if(window." + oldPageLoadVariableName
                        + "==null) {window." + oldPageLoadVariableName + "=0}");
                try {
                    
                    WebDriverWait webDriverWait = new WebDriverWait(webDriver, DEFAULT_TIMEOUT_SECONDS);
                    webDriverWait.until(new ExpectedCondition<Boolean>() {
                        public Boolean apply(WebDriver webDriver) {
                            JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                            return (Boolean) executor.executeScript("return (window." + pageLoadVariableName + "!=" + "window." + oldPageLoadVariableName + ")");
                        }
                    });
                } finally {
                    updateOldPageLoadCounter(webDriver, pageLoadVariableName,
                            oldPageLoadVariableName);
                    if (!("null".equals(windowName) || "main".equals(windowName)
                            || "selenium_main_app_window".equals(windowName))) {
                        
                        try {
                            webDriver.switchTo().frame(windowName);
                        } catch (NoSuchFrameException e) {
                            try {
                                webDriver.switchTo().window(windowName);
                            } catch (NoSuchWindowException e1) {
                                webDriver.switchTo().window(webDriver.getWindowHandle());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (!e.getMessage().contains("Window does not exist")) {
                e.printStackTrace();
            }
        }
        
        return this;
    }

    private void updateOldPageLoadCounter(WebDriver webDriver,
            String pageLoadVariableName, String oldPageLoadVariableName) {
        
        int[] oldNew = getOldAndNewPageLoadCount(webDriver, pageLoadVariableName,
                oldPageLoadVariableName);

        int diff = oldNew[1] - (oldNew[0] + 1);

        if (diff > 0) {
            try {
                throw new Exception(
                        "Load page counter mismatch, missed "
                                + diff
                                + " loads. Test is probably missing gui.waitForPageLoad() statements.");
            } catch (Exception e) {
                JavascriptExecutor executer = (JavascriptExecutor) webDriver;
                String windowName = (String) executer.executeScript("return window.name");
                LOG.log(Level.WARNING,
                        "Error waiting for page to load (current frame="
                                + windowName + "). ", e);
            }
        }

        safeGetEval(webDriver, "return window." + oldPageLoadVariableName + "=" + oldNew[1]);

    }
    
    private int[] getOldAndNewPageLoadCount(WebDriver webDriver,
            String pageLoadVariableName, String oldPageLoadVariableName)
    {
        Long seleniumPolopoly1 = safeGetEval(webDriver,"return window." + pageLoadVariableName);
        int seleniumIntValue = seleniumPolopoly1.intValue();

        Long seleniumPolopolyOld = safeGetEval(webDriver, "return window." + oldPageLoadVariableName);
        int seleniumPolopolyOldIntValue = seleniumPolopolyOld.intValue();
        
        int[] oldNew = new int[]{ seleniumPolopolyOldIntValue, seleniumIntValue};

        return oldNew;
    }

    private Long safeGetEval(WebDriver webDriver, String text) {
        return recursiveSafeGetEval(webDriver, text, 1, 5);
    }

    private Long recursiveSafeGetEval(WebDriver webDriver, String text,
            int depth, int maxLevel) {
        
        Long sd = null;

        try {
            sd = (Long) ((JavascriptExecutor) webDriver).executeScript(text);
        } catch (RuntimeException e) {
            if (depth > maxLevel) {
                throw e;
            }

            LOG.log(Level.WARNING,
                    "Unable to execute javascript due to exception:"
                            + e.getMessage() + ". Try " + depth + " out of "
                            + maxLevel + " max tries.");

            sd = recursiveSafeGetEval(webDriver, text, depth + 1, maxLevel);
        }

        return sd;
    }
    
    /**
     * Waits for the ajax page in the current selected frame is loaded. 
     * @return this agent
     */
    public WaitAgent waitForAjaxPageToLoad() {
        
        WebDriverWait webDriverWait = new WebDriverWait(webDriver,
                DEFAULT_TIMEOUT_SECONDS);
        webDriverWait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                return (Boolean) executor.executeScript("return (window.AjaxEventController.numEventsInProgress == 0)");
            }
        }); 
        
        return this;
    }

    /**
     * Waits for an element in the current selected frame is loaded.
     * @param locator the locator to the element
     * @return this agent
     * @throws Exception
     */
    public WaitAgent waitForElement(final By locator) {
        
        WebDriverWait webDriverWait = new WebDriverWait(webDriver,
                DEFAULT_TIMEOUT_SECONDS);
        webDriverWait.until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver webDriver) {
                return webDriver.findElement(locator);
            }
        });

        return this;
    }
    
    /**
     * Waits until an element with specific text content is found in the current selected frame.
     * @param text the element's text content 
     * @return this agent
     * @throws Exception
     */
    public WaitAgent waitForText(final String text) throws Exception {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver,
                DEFAULT_TIMEOUT_SECONDS);
            webDriverWait.until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver webDriver) {
                    return webDriver.findElement(By.xpath("//*[text() = '" + text + "']"));
                }
            });
        return this;
    }
    
    /**
     * Waits until the condition returns true. Executed in the current frame context.
     * Note that the script snippet need to return a boolean value. 
     * @param condition the java script condition
     * @return this agent
     * @throws Exception
     */
    public WaitAgent waitForCondition(final String condition) throws Exception {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver,
                DEFAULT_TIMEOUT_SECONDS);
            webDriverWait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver webDriver) {
                    JavascriptExecutor executor = (JavascriptExecutor) webDriver;
                    Boolean executeScript = (Boolean) executor.executeScript(condition);
                    return executeScript;
                }
            });
        return this;
    }
}
