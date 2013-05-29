package com.polopoly.guitest.framework;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.polopoly.guitest.Module;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gmola
 */
public class PolopolyWebTestRunner extends BlockJUnit4ClassRunner {

    private static final Logger LOG = Logger.getLogger(PolopolyWebTestRunner.class.toString());
    private static final Level LOG_LEVEL = Level.INFO;

    private static Injector injector;
    private static Object initLock = new Object();

    public PolopolyWebTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        initInjectorIfNecessary();
    }

    private void initInjectorIfNecessary() {
        synchronized (initLock) {
            if (injector == null) {
                injector = Guice.createInjector(new Module());
            }
        }
    }

    @Override
    protected Object createTest() throws Exception {
        Object obj = super.createTest();
        LOG.log(LOG_LEVEL, "initiating environment for test injectors");
        injector.injectMembers(obj);
        return obj;
    }


    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        try {
            super.runChild(method, notifier);
        } finally {
            LOG.log(LOG_LEVEL, "resetting WebDriver");
            injector.getInstance(DriverShutdownHook.class).shutDownDriver();
        }
    }

}
