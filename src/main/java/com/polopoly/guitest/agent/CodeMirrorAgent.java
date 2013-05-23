package com.polopoly.guitest.agent;

import static junit.framework.Assert.*;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CodeMirrorAgent {

    private WebDriver webDriver;
    private JavascriptExecutor jsExecutor;

    @Inject
    public CodeMirrorAgent(WebDriver webDriver) {
        this.webDriver = webDriver;
        jsExecutor = (JavascriptExecutor) webDriver;
    }

    /**
     *
     * @param editorName
     * @param editorContent Must not contain \n characters or \" characters
     */
    public void setText(String editorName, String editorContent)
    {
        String componentid = getComponentId(editorName);
        assertFalse("CodeMirrorAgent does not support setting text that contains \"", editorContent.contains("\""));
        assertFalse("CodeMirrorAgent ECMAScript® string literals can not contain newlines characters, did you mean \\n?,", editorContent.contains("\n"));
        jsExecutor.executeScript("window.codemirror_" + componentid + ".setValue(\"" + editorContent + "\")");
    }

    private String getComponentId(String editorname)
    {
        String locator = "//h2[text()='" + editorname + "']/following-sibling::textarea";
        WebElement codeMirrorEditor = webDriver.findElement(By.xpath(locator));
        assertNotNull("CodeMirror editor with name '" + editorname + "' not found", codeMirrorEditor);

        return codeMirrorEditor.getAttribute("id");
    }
}
