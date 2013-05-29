package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.ContentTreeAgent;
import com.polopoly.guitest.agent.GuiBaseAgent;
import com.polopoly.guitest.agent.InputAgent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@RunWith(PolopolyWebTestRunner.class)
public class AMoreContrievedExampleIT {

    @Inject
    GuiBaseAgent gui;

    @Inject
    WebDriver webDriver;

    @Inject
    ContentTreeAgent contentTree;

    @Inject
    InputAgent input;

    @Test
    public void createArticle() throws Exception {
        gui.loginAsSysadmin().navFrame();
        contentTree.expand("SiteEngine").expand("Sites").open("Greenfield Times");
        gui.workFrame().selectTab("Articles & Resources");
        webDriver.findElement(By.xpath("//button[contains(text(), 'Create')]")).click();
        input.typeInTextfield("Title *", "my foolish article.");
        gui.toolbar.clickOnSaveAndInsert();
    }

}
