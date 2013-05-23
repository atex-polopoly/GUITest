package com.polopoly.guitest.agent;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

public class SearchAgent {

    private final WebDriver webDriver;
    private final FrameAgent frameAgent;
    private final WaitAgent waitAgent;

    @Inject
    public SearchAgent(WebDriver webDriver, FrameAgent frameAgent, WaitAgent waitAgent) {
        this.webDriver = webDriver;
        this.frameAgent = frameAgent;
        this.waitAgent = waitAgent;
    }
    
    /**
     * Performs a free text search in the Polopoly Admin GUI  
     * @param searchText the text to search for
     * @return this agent
     */
    public SearchAgent search(String searchText) {
        
        frameAgent.searchFrame();

        WebElement element = webDriver.findElement(new ByChained(By.id("search"), 
        		                                                 By.className("searchInput")));
        element.clear();
        element.sendKeys(searchText);
        
        // element.sendKeys(Keys.RETURN); // Doesn't work in Firefox
        // Workaround by getting the hidden button's name to be able to execute  
        // an javascript 
        
        String searchBtnName = 
        		webDriver.findElement(new ByChained(By.id("search"), 
        				                            By.className("searchbox"), 
        				                            By.tagName("button"))).getAttribute("name");
        ((JavascriptExecutor) webDriver).executeScript("document.mainform." + searchBtnName + ".click()");
        
        waitAgent.waitForAjaxPageToLoad();
        
        return this;
    }
    
    /**
     * This will check if a certain text string exists in the search result pane.  
     * @param text the text string to check
     * @return true if exists
     */
    public boolean isPresentInSearchResult(String text) {
        frameAgent.searchFrame();
        String locator = getSearchItemLocator(text);
        return webDriver.findElements(By.xpath(locator)).size() > 0;
    }

    private String getSearchItemLocator(String text) {
        return "//div[contains(@class, 'searchAreaResult')]//div[contains(@class, 'searchAreaResultItem')]//*[contains(text(), '"+text+"')]";
    }
    
    /**
     * Clicks the "copy" button of the first search hit given a search term
     * @return this agent
     */
    public SearchAgent copySearchItem(String text){
        frameAgent.searchFrame();
        String itemLocator = getSearchItemLocator(text);
        itemLocator += "/../../a[@class='copy_button']";
        WebElement copyButton = webDriver.findElement(By.xpath(itemLocator));

        ((JavascriptExecutor)webDriver).executeScript("$(arguments[0]).show();", copyButton);
        copyButton.click();
        return this;
    }

    
    /**
     * Gets the number of the search hits
     * @return number of search hits
     */
    public int getNumberOfSearchHits() {
        frameAgent.searchFrame();
        WebElement element = webDriver.findElement(
                By.xpath("//div[@id='search']//span[@class='numberOfResults']"));
        return Integer.valueOf((String)((JavascriptExecutor)webDriver).executeScript("return arguments[0].innerHTML",element));
    }
    
    
    
    /**
     * Filter the search result by facet navigation for a specific category 
     * @param categoryType the title of category type 
     * @param category the label of the category
     * @return this agent
     */
    public SearchAgent filterSearchOn(String categoryType, String category) {
        frameAgent.searchFrame();
        
        showFilterPane();
       
        String locator = "//div[contains(@class,'search_facets')]";
        
        if (categoryType != null) {
            locator += "/ul[contains(@class,'facets_fields')]/li[contains(text(), '" + categoryType + "')]";
        }
        
        locator += "//a[contains(text(),'"+category+"')]";

        webDriver.findElement(By.xpath(locator)).click();
        waitAgent.waitForAjaxPageToLoad();
        
        return this;
    }
    
    /**
     * Filter the search result by facet navigation for a specific category 
     * @param category the label of the category
     * @return this agent
     */
    public SearchAgent filterSearchOn(String category) {
        filterSearchOn(null, category);
        return this;
    }
    
    /**
     * Display the search filter pane.
     * @return this agent
     */
    public SearchAgent showFilterPane() {
        
        frameAgent.searchFrame();
        
        String locator = "//div[@id='search']//li[contains(@class,'facets_toggle')]";
        if (webDriver.findElements(By.xpath(locator + "/a[contains(@class, 'active')]")).isEmpty()) {
            webDriver.findElement(By.xpath(locator + "/a")).click();
        }
        
        return this;
    }
    
    /**
     * Hide the search filter pane
     * @return this agent
     */
    public SearchAgent hideFilterPane() {
        frameAgent.searchFrame();
        
        String locator = "//div[@id='search']//li[contains(@class,'facets_toggle')]/a[contains(@class, 'active')]";
        if (webDriver.findElements(By.xpath(locator)).size() == 1) {
            webDriver.findElement(By.xpath(locator)).click();
        }
        return this;
    }
}
