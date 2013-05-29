package com.polopoly.guitest.integration;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.GuiBaseAgent;
import com.polopoly.guitest.framework.PolopolyWebTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(PolopolyWebTestRunner.class)
public class YetAnotherTestIT {

    @Inject
    private GuiBaseAgent gui;


    @Test
    public void loginAgain() {
        gui.login("emma", "emma");
    }

}
