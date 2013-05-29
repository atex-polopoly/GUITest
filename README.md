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

@RunWith(PolopolyWebTestRunner.class)
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
```    

Provided Agents
=============

Write you own Agents
==================


WebDriver lifecycle & configuration
===================

The abstract class WebDriverProvider is responsible of both initiating and tearing down WebDriver.
Unless you provide a custom implementation through java.util.ServiceLoader a FirefoxDriver will be instantiated by default.

If you want to override this behaviour implement in your project the WebDriverProvider class and create a pointer to it in META-INF/services

For instance:

```java
package example;

public class MyWebDriverProvider extends WebDriverProvider {

    private static final Logger LOG = Logger.getLogger(FirefoxWebDriverProvider.class.getCanonicalName());

    @Override
    public WebDriver initDriver() {
        return new MyCustomWebDriver();
    }

}
```


```bash
echo "example.MyWebDriverProvider" > META-INF/services
```



The default behaviour is to create a new instance of the driver for each test run. You can change this by overriding the method WebDriverProvider






Integration with Polopoly TestBase
============================


Note about installing
=====================

When you run "mvn install" you must have access to a Polopoly runtime for the integration tests phase to complete without errors.
Use 'polopoly.guitest.properties' if you don't have Polopoy running on localhost to point to another Polopoly runtime. For instance:

    mvn -Dpolopoly.guitest.properties=src/test/resources/webtests.integration.properties 


