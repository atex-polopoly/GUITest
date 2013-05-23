package com.polopoly.guitest.agent;


import com.google.inject.Inject;
import org.json.simple.JSONValue;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * This agent interacts on a CKEditor field in Polopoly Admin GUI.  
 */
public class CKEditorAgent {

    private final WebDriver webDriver;
    private final WaitAgent waitAgent;

    @Inject
    public CKEditorAgent(WebDriver webDriver, WaitAgent waitAgent) {
        this.webDriver = webDriver;
        this.waitAgent = waitAgent;
    }

    /**
     * Sets a text content into the all present CKEditor fields
     * @param text the text content to set
     */
    public void setText(final String text) throws Exception {
        
        waitForCKEditor();
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("var fck; for (var f in window.CKEDITOR.instances)"
                        + "{ fck = window.CKEDITOR.instances[f]; };"
                        + "fck.setData('"
                        + JSONValue.escape(text)
                        + "');");
        
        waitforDirtyStateOnCKEditor();
    }
    
    /**
     * Sets a text content into the CKEditor field with a specific field label
     * @param fieldLabel the field label   
     * @param textValue the text content to set  
     */
    public void setText(String fieldLabel, String textValue)
            throws Exception {
        
        String editorId = getEditorId(fieldLabel);
        waitForCKEditor(editorId);
        
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript(getCKEditorJSString(editorId) + "fck.setData('" + textValue + "');");
    }
    
    /**
     * Returns the text content for a CKEditor present in the current frame pane
     * @return the text content
     */
    public String getText() throws Exception {
        waitForCKEditor();
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        String result = (String) executor.executeScript("var fck; for (var f in window.CKEDITOR.instances)"
                        + "{ fck = window.CKEDITOR.instances[f]; };"
                        + " return fck.getData();");
        return result;
    }
    
    /**
     * Returns the text content for a CKEditor with a specific field label
     * @return the text content
     */
    public String getText(String fieldLabel) throws Exception {
        
        String editorId = getEditorId(fieldLabel);
        waitForCKEditor(editorId);
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        String result = (String) executor.executeScript(getCKEditorJSString(editorId)
                + "return fck.getData();");
        return result;
    }

    /**
     * Executes a command on a CKEditor that is present in the current frame pane
     * @param commandName the command name to execute
     * @throws Exception
     */
    public void executeCommand(String commandName) throws Exception {
        waitForCKEditor();
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("var fck; for (var f in window.CKEDITOR.instances)"
                        + "{ fck = window.CKEDITOR.instances[f]; };"
                        + "fck.execCommand('" + commandName + "');");
    }

    /**
     * Executes a command on a CKEditor with a field label
     * @param fieldLabel the field label 
     * @param command the command to execute
     *
     */
    public void executeCommand(String fieldLabel, String command) throws Exception {
        String editorId = getEditorId(fieldLabel);
        waitForCKEditor(editorId);
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript(getCKEditorJSString(editorId) + "fck.execCommand('"
                + command + "');");
    }

    private String getCKEditorJSString(String id) {
        return "var fck = window.CKEDITOR.instances['"
                + id + "'];";
    }

    public void waitForCKEditor(final String editorId) throws Exception {
        
        waitAgent.waitForCondition("return window.CKEDITOR != null");
        waitAgent.waitForCondition(getCKEditorJSString(editorId)
                + "try{fck.EditorWindow.document}catch(e){} return fck != null;");
    }
    
    public void waitForCKEditor() throws Exception {

        waitAgent.waitForCondition("return window.CKEDITOR != null");
        
        String secondCondition = "var fck = null; var fckInstId = null; for (var f in window.CKEDITOR.instances)"
            + "{ fck = window.CKEDITOR.instances[f]; fckInstId = f; } "
            + " try{fck.EditorWindow.document}catch(e){} return fckInstId != null";
        waitAgent.waitForCondition(secondCondition);
    }

    public void waitforDirtyStateOnCKEditor()
            throws Exception {
        waitAgent.waitForCondition("var fck; for (var f in window.CKEDITOR.instances)"
                + "{ fck = window.CKEDITOR.instances[f]; };"
                + "return fck.checkDirty();");
    }


    private String getEditorId(String label)
            throws Exception {
        
        WebElement webElement = 
            webDriver.findElement(By.xpath("//fieldset/h2[text() = '"
                    + label
                    + "']/../textarea"));
        return webElement.getAttribute("id");
    }
}
