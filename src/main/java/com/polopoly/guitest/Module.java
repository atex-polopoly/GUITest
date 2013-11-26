package com.polopoly.guitest;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.polopoly.guitest.webdriver.FirefoxWebDriverInitializer;
import com.polopoly.guitest.webdriver.WebDriverInitializer;
import com.polopoly.guitest.webdriver.WebDriverProvider;
import com.polopoly.testnj.TestNJContext;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class Module extends AbstractModule {

    private static final Logger LOG = Logger.getLogger(Module.class.getCanonicalName());
    private static final String PROPERTIES_FILE_PTR = "polopoly.guitest.properties";
    private static final String DEFAULT_PROPERTIES_FILE = "/webtests.properties";


    @Override
    protected void configure() {
        try {
            Names.bindProperties(binder(), loadProperties());
        } catch (IOException e) {
            LOG.severe("cannot read properties file!");
            throw new RuntimeException(e);
        }
        WebDriverProvider.setDriverInitializer(bindInitializer());
        WebDriverProvider webDriverProvider = new WebDriverProvider();

        TestNJContext.addCallback(webDriverProvider);

        bind(WebDriver.class).toProvider(webDriverProvider);
    }


    private WebDriverInitializer bindInitializer() {
        ServiceLoader<WebDriverInitializer> serviceLoader = ServiceLoader.load(WebDriverInitializer.class);
        List<WebDriverInitializer> initializers = new ArrayList<WebDriverInitializer>();
        for (WebDriverInitializer initializer : serviceLoader) {
            initializers.add(initializer);
        }
        if (initializers.isEmpty()) {
            return defaultWebDriverInitializer();
        } else {
            if (initializers.size() > 1) {
                LOG.info("multiple bindings for WebDriver found, picking up first in list");
            }
            LOG.info("binding WebDriver webdriver to " + initializers.get(0).getClass().getCanonicalName());
            return initializers.get(0);
        }
    }


    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        if (System.getProperty(PROPERTIES_FILE_PTR) != null) {
            String fileName = System.getProperty(PROPERTIES_FILE_PTR);
            if (new File(fileName).isFile()) {
                LOG.info("Reading properties from file: " + PROPERTIES_FILE_PTR);
                properties.load(new FileInputStream(fileName));
            } else {
                LOG.info("Reading properties from classpath: " + PROPERTIES_FILE_PTR);
                properties.load(getClass().getResourceAsStream(fileName));
            }
        } else {
            LOG.info("Reading default property file.");
            properties.load(getClass().getResourceAsStream(DEFAULT_PROPERTIES_FILE));
        }
        return properties;
    }


    private WebDriverInitializer defaultWebDriverInitializer() {
        return new FirefoxWebDriverInitializer();
    }

}
