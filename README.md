Polopoly GUITest
==============

web test framework for the Polopoly GUI.


few examples
============

just logging in ...


```java
package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.GuiBaseAgent;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestInjectRunner.class)
public class GuiLoginIT {

    @Inject
    private GuiBaseAgent gui;


    @Test
    public void loginAgain() {
        gui.login("emma", "emma");
    }

}
```


now create some content:

```java
package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.ContentTreeAgent;
import com.polopoly.guitest.agent.GuiBaseAgent;
import com.polopoly.guitest.agent.InputAgent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@RunWith(TestInjectRunner.class)
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
```    

Provided Agents
=============

For an up-to-date list refer to the package __com.polopoly.guitest.agent__

Write your own Agents
==================

Project defined agents can be injected in exactly the same way provided agents are.

Check the Agent007 and Agent007IT in the test source folder for an example of how to write/inject own agents.


WebDriver configuration and lifecycle
===================

**Configuration**

The abstract class WebDriverProvider is responsible of both initiating and tearing down WebDriver.
Unless you provide a custom implementation through java.util.ServiceLoader a FirefoxDriver will be instantiated by default.

If you want to override this behaviour implement in your project the WebDriverProvider class and create a pointer to it in META-INF/services

For instance:

```java
package example;

public class MyWebDriverProvider extends WebDriverProvider {

    @Override
    public WebDriver initDriver() {
        return new MyCustomWebDriver();
    }

}
```


```bash
echo "example.MyWebDriverProvider" > META-INF/services/com.polopoly.guitest.webdriver.WebDriverProvider
```


**Lifecycle**

The default behaviour is to create a new instance of the driver for each test run.

You can change this by overriding the method WebDriverProvider.shutDownDriver in you project provider class.




Integration with Polopoly TestBase
============================

TBD


Note about installing
=====================

When you run "mvn install" you must have access to a Polopoly runtime for the integration tests phase to complete without errors.

Use 'polopoly.guitest.properties' if you don't have Polopoy running on localhost. For instance:

    mvn -Dpolopoly.guitest.properties=src/test/resources/webtests.integration.properties 


