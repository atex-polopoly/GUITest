package com.polopoly.guitest.framework;


import com.polopoly.guitest.agent.GuiBaseAgent;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(PolopolyWebTestRunner.class)
public class AnotherLoginIT {

    @Inject
    private GuiBaseAgent gui;


    @Test
    public void loginAsSusperUser() {
        gui.loginAsSysadmin();
    }

}
