package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.GuiBaseAgent;
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
