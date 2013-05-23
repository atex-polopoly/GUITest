package com.polopoly.guitest.agent;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * This agent interacts on input control elements in the Polopoly Admin GUI.
 */
public class InputAgent {

    private final WebDriver webDriver;

    public final CKEditorAgent ckEditor;

    @Inject
    public InputAgent(WebDriver webDriver, CKEditorAgent agentCKEditor) {
        this.webDriver = webDriver;
        this.ckEditor = agentCKEditor;
    }
    
    /**
     * Types text into a textfield with a specific label
     * @param fieldLabel the label of the text field
     * @param value the text value to set
     * @return this agent
     */
    public InputAgent typeInTextfield(String fieldLabel, String value) {
        return typeInInternal("input", fieldLabel, value);
    }
    
    /**
     * Types text into a textarea with a specific label
     * @param fieldLabel the label of the text area
     * @param value the text value to set
     * @return this agent
     */
    public InputAgent typeInTextarea(String fieldLabel, String value) {
        return typeInInternal("textarea", fieldLabel, value);
    }
    
    /**
     * Internal method to type text into a input control element. 
     * @param type type of input control. "input", "textarea"
     * @param fieldLabel the label of the input field
     * @param value the value to set
     * @return this agent
     */
    private InputAgent typeInInternal(String type, String fieldLabel, String value) {
        String locator = getFieldLocator(fieldLabel, type);
        
        WebElement webElement = webDriver.findElement(By.xpath(locator));
        webElement.sendKeys(value);
        
        return this;
    }
    
    /**
     * Returns the xpath location of the field type
     * @param fieldLabel the label of the input field
     * @param fieldType the input control type: "input", "textarea" 
     * @return a xpath location
     */
    private String getFieldLocator(String fieldLabel, String fieldType) {
        return "//h2[contains(text(), '"+ fieldLabel +"')]/..//"+ fieldType;
    }
    
    /**
     * Types a text in all WYSIWYG editor that are present
     * @param value the text to set
     * @return this agent
     * @throws Exception
     */
    public InputAgent typeInCKEditor(String value) throws Exception {
        ckEditor.setText(value);
        return this;
    }
    
    /**
     * Types a text in a WYSIWYG editor with a specific label
     * @param fieldLabel the label of the editor
     * @param value the text to set
     * @return this agent
     * @throws Exception
     */
    public InputAgent typeInCKEditor(String fieldLabel, String value) throws Exception {
        ckEditor.setText(fieldLabel, value);
        return this;
    }

    /**
     * Selects an option in a select box with a specific label
     * @param fieldLabel the label of the selection field 
     * @param optionLabel the label of the option to select
     * @return this agent
     */
    public InputAgent select(String fieldLabel, String optionLabel) {
        Select selectBox = new Select(webDriver.findElement(
                By.xpath("//h2[text()='" + fieldLabel + "']/..//select")));
        selectBox.selectByVisibleText(optionLabel);
        return this;
    }
    
    /**
     * Checks (clicks on) a checkbox alternative with a specific label 
     * @param fieldLabel the label of the radio alternative
     * @return this agent
     */
    public InputAgent checkCheckbox(String fieldLabel) {
        webDriver.findElement(
                By.xpath("//*[contains(text(),'" + fieldLabel + "')]/..//input[1]")).click();
        return this;
    }

    /**
     * Unchecks (clicks if selected) a checkbox/radio alternative with a specific label 
     * @param fieldLabel the label of the radio alternative
     * @return this agent
     */
    public InputAgent uncheckCheckBox(String fieldLabel) {
        WebElement webElement = webDriver.findElement(By.xpath("//*[contains(text(),'" + fieldLabel + "')]/..//input[1]"));
        if (webElement.isSelected())  {
            webElement.click();    
        }
        return this;
    }

    /**
     * Checks (clicks on) a radio alternative with a specific label 
     * @param fieldLabel the label of the radio alternative
     * @return this agent
     */
    public InputAgent checkRadio(String fieldLabel) {
        webDriver.findElement(
                By.xpath("//*[contains(text(),'" + fieldLabel + "')]/../..//input[1]")).click();
        return this;
    }

    
    /**
     * Unchecks (clicks if selected) a checkbox/radio alternative with a specific label 
     * @param fieldLabel the label of the radio alternative
     * @return this agent
     */
    public InputAgent uncheckRadio(String fieldLabel) {
        WebElement webElement = webDriver.findElement(By.xpath("//*[contains(text(),'" + fieldLabel + "')]/../..//input[1]"));
        if (webElement.isSelected())  {
            webElement.click();    
        }
        return this;
    }

    
    /**
     * Returns the value of textfield with a specific field label
     * @param fieldLabel the label of the textfield 
     */
    public String getTextfieldValue(String fieldLabel) {
        return webDriver.findElement(
                By.xpath(getFieldLocator(fieldLabel, "input"))).getAttribute("value");
    }
    
    /**
     * Returns the value of textarea with a specific field label
     * @param fieldLabel the label of the textarea 
     */
    public String getTextareaValue(String fieldLabel) {
        return webDriver.findElement(
                By.xpath(getFieldLocator(fieldLabel,
                        "textarea"))).getText();
    }
}
