package com.polopoly.guitest.integration;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.ContentCreatorAgent;
import com.polopoly.guitest.agent.ContentTreeAgent;
import com.polopoly.guitest.agent.GuiBaseAgent;
import com.polopoly.guitest.agent.InputAgent;
import com.polopoly.guitest.agent.WaitAgent;
import com.polopoly.testnj.TestNJRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(TestNJRunner.class)
public class AMoreContrievedExampleIT {

    @Inject
    GuiBaseAgent gui;

    @Inject
    WebDriver webDriver;

    @Inject
    ContentTreeAgent contentTree;

    @Inject
    InputAgent input;
    
    @Inject
    ContentCreatorAgent creator;
    
    @Inject
    WaitAgent wait;
    
    @Test
    public void createArticle() throws Exception {
        gui.loginAsSysadmin().navFrame();
        contentTree.expand("SiteEngine").expand("Sites").open("Greenfield Times");
        gui.workFrame();
        wait.waitForPageToLoad();
        gui.selectTab("Articles & Resources");
        creator.createContent("List of local content", "Standard article");
        input.typeInTextfield("Title *", "my foolish article.");
        gui.toolbar.clickOnSaveAndInsert();
    }

}
