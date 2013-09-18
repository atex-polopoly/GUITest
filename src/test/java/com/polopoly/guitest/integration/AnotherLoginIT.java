package com.polopoly.guitest.integration;


import com.atex.testinject.TestInjectRunner;
import com.polopoly.guitest.agent.GuiBaseAgent;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(TestInjectRunner.class)
public class AnotherLoginIT {

    @Inject
    private GuiBaseAgent gui;


    @Test
    public void loginAsSusperUser() {
        gui.loginAsSysadmin();
    }

}
