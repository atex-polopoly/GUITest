package com.polopoly.guitest.agent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This agent interacts with the tree select fields in the Polopoly Admin GUI.  
 */
public class TreeSelectAgent {

    private final WebDriver webDriver;
    private final WaitAgent waitAgent;
    private final GuiBaseAgent guiBaseAgent;


    public TreeSelectAgent(WebDriver webDriver, WaitAgent waitAgent, GuiBaseAgent guiBaseAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
        this.guiBaseAgent = guiBaseAgent;
    }
    
    /**
     * Selects a node in a specific tree select field 
     * @param treeSelectLabel the label of the tree select field 
     * @param path the node path. For example "Subject/Education" will select "Eduction". 
     * @return this agent
     * @throws Exception
     */
    public GuiBaseAgent selectNode(String treeSelectLabel, String path) throws Exception {
        internalSelectNode(treeSelectLabel, path, true);
        return guiBaseAgent;
    }

    /**
     * Unselects a node in a specific tree select field 
     * @param treeSelectLabel the label of the tree select field 
     * @param path the node path. For example "Subject/Education" will select "Eduction". 
     * @return this agent
     * @throws Exception
     */
    public GuiBaseAgent unselectNode(String treeSelectLabel, String path) throws Exception {
        internalSelectNode(treeSelectLabel, path, false);
        return guiBaseAgent;
    }
    
    /**
     * Selects the iframe if any iframe exists
     * @param treeSelectLabel the tree select label
     * @return the previous frame name if iframe exists. Otherwise null
     * @throws Exception
     */
    private String selectTreeIframeIfExist(String treeSelectLabel) throws Exception {
        
        String locator = "//h2[text()='" + treeSelectLabel + "']/../iframe[1]";
        
        String prevoiusFrameName = null;
        
        if (!webDriver.findElements(By.xpath(locator)).isEmpty()) {
            prevoiusFrameName = 
                (String) ((JavascriptExecutor) webDriver).executeScript("return window.name");
            
            WebElement iframeElement = webDriver.findElement(By.xpath(locator));
            webDriver.switchTo().frame(iframeElement);
            waitAgent.waitForElement(By.xpath("//body"));
        }
        
        return prevoiusFrameName;
    }
    
    /**
     * Internal method to select/unselect a node
     * @param treeSelectLabel the tree select label
     * @param path the path
     * @param select true if too select a node
     * @return the node label
     * @throws Exception
     */
    private void internalSelectNode(String treeSelectLabel, String path, boolean select)
        throws Exception {
        
        int lastIndexOf = path.lastIndexOf("/");
        String expandPath = null;
        if (lastIndexOf != -1) {
             expandPath = path.substring(0, lastIndexOf);    
        } else {
            expandPath = path;
        }

        String previousFrameName = selectTreeIframeIfExist(treeSelectLabel);

        expand(treeSelectLabel, expandPath);
        
        String node = path.substring(lastIndexOf + 1, path.length());
        String xpathNodeLink = "//a[text()='"+node+"']";
        if (!select) {
            xpathNodeLink = "//a/span[text()='"+node+"']";
        }
        waitAgent.waitForElement(By.xpath(xpathNodeLink));
        webDriver.findElement(By.xpath(xpathNodeLink)).click();
        
        if (select) {
            waitAgent.waitForElement(By.xpath("//span[@class='selected' and text()='" + node + "']"));
        }
        
        if (previousFrameName != null) {
            webDriver.switchTo().window(webDriver.getWindowHandle());
            webDriver.switchTo().frame(previousFrameName);
        }
    }
    
    /**
     * Expand a node in a tree select 
     * @param treeSelectLabel the tree select label
     * @param path the path to expand
     * @return this agent
     * @throws Exception
     */
    public GuiBaseAgent expand(String treeSelectLabel, String path) throws Exception {
        expandOrCollapse(treeSelectLabel, path, true);
        return guiBaseAgent;
    }
    
    /**
     * Collapse a node in a tree select 
     * @param treeSelectLabel the tree select label
     * @param path the path to collapse
     * @return this agent
     * @throws Exception
     */
    public GuiBaseAgent collapse(String treeSelectLabel, String path) throws Exception {
        expandOrCollapse(treeSelectLabel, path, false);
        return guiBaseAgent;
    }
    
    /**
     * Internal method to expand/collapse a node
     * @param treeSelectLabel the tree select label 
     * @param path the node path
     * @param expand true if too expand
     * @throws Exception
     */
    private void expandOrCollapse(String treeSelectLabel, String path, boolean expand) throws Exception {
        
        String previousFrameName = selectTreeIframeIfExist(treeSelectLabel);

        if (path != null && !"".equals(path)) {
            String[] segments = null;
            if (expand) {
                segments = path.split("/");
            } else {
                List<String> list = Arrays.asList(path.split("/"));
                Collections.reverse(list);
                String[] temp = path.split("/");
                segments = new String[temp.length];
                
                for (int i = 0; i < list.size(); i++) {
                    segments[i] = list.get(i);
                }
            }
            
            for (String segment : segments) {            
                if (!"".equals(segment)) {
                    if (expand)  {
                        if (webDriver.findElements(By.xpath("//img[@alt='Contract " + segment +"']")).isEmpty()) {
                            waitAgent.waitForElement(By.xpath("//img[@alt='Expand " + segment + "']"));
                            webDriver.findElement(By.xpath("//img[@alt='Expand " + segment +"']")).click();
                            waitAgent.waitForElement(By.xpath("//img[@alt='Contract " + segment + "']"));
                        }
                    } else {
                        if (webDriver.findElements(By.xpath("//img[@alt='Expand " + segment +"']")).isEmpty()) {
                            waitAgent.waitForElement(By.xpath("//img[@alt='Contract " + segment + "']"));
                            webDriver.findElement(By.xpath("//img[@alt='Contract " + segment +"']")).click();
                            waitAgent.waitForElement(By.xpath("//img[@alt='Expand " + segment + "']"));
                        }
                    }
                }
            }
        }
        
        if (previousFrameName != null) {
            webDriver.switchTo().window(webDriver.getWindowHandle());
            webDriver.switchTo().frame(previousFrameName);
        }
    }

}
