package com.polopoly.guitest;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.polopoly.guitest.provider.FirefoxWebDriverProvider;
import com.polopoly.guitest.provider.WebDriverProvider;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
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
        bind(WebDriver.class).toProvider(loadWebDriverProvider());
    }


    private WebDriverProvider loadWebDriverProvider() {
        ServiceLoader<WebDriverProvider> serviceLoader = ServiceLoader.load(WebDriverProvider.class);
        List<WebDriverProvider> providers = new ArrayList<WebDriverProvider>();
        for (WebDriverProvider provider : serviceLoader) {
            providers.add(provider);
        }
        if (providers.isEmpty()) {
            return defaultWebDriverProvider();
        } else {
            if (providers.size() > 1) {
                LOG.info("multiple bindings for WebDriver found, picking up first in list");
            }
            LOG.info("binding WebDriver provider to " + providers.get(0).getClass().getCanonicalName());
            return providers.get(0);
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


    private WebDriverProvider defaultWebDriverProvider() {
        return new FirefoxWebDriverProvider();
    }

}
