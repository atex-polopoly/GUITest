package com.polopoly.guitest.integration;


import com.polopoly.guitest.agent.GuiBaseAgent;
import com.polopoly.testnj.TestNJRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(TestNJRunner.class)
public class AnotherLoginIT {

    @Inject
    private GuiBaseAgent gui;


    @Test
    public void loginAsSusperUser() {
        gui.loginAsSysadmin();
    }

}
