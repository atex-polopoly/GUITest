Polopoly GUITest
==============

web test framework for the Polopoly GUI.


few examples
============

just logging in ...


    package com.polopoly.guitest.framework;

    import com.google.inject.Inject;
    import com.polopoly.guitest.agent.GuiBaseAgent;
    import org.junit.Test;
    import org.junit.runner.RunWith;

    @RunWith(PolopolyWebTestRunner.class)
    public class GuiLoginIT {

      @Inject
      private GuiBaseAgent gui;


      @Test
      public void loginAgain() {
          gui.login("emma", "emma");
      }

    }



create some content ....

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
        contentTree.open("Greenfield Times");
        gui.workFrame().selectTab("Articles & Resources");
        webDriver.findElement(By.xpath("//button[contains(text(), 'Create')]")).click();
        input.typeInTextfield("Title *", "my foolish article.");
        gui.toolbar.clickOnSaveAndInsert();
      }

    }
    

Provided Agents
=============

Write you own Agents
==================


WebDriver lifecycle
================


Configure
========

Integration with Polopoly TestBase
============================


Note about installing
=====================

When you run "mvn install" you must have access to a Polopoly runtime for the integration tests phase to complete without errors.
Use 'polopoly.guitest.properties' if you don't have Polopoy running on localhost to point to another Polopoly runtime. For instance:

    mvn -Dpolopoly.guitest.properties=src/test/resources/webtests.integration.properties 


