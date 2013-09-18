package com.polopoly.guitest.integration;

import com.atex.testinject.TestInjectRunner;
import com.google.inject.Inject;
import com.polopoly.guitest.agent.GuiBaseAgent;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestInjectRunner.class)
public class YetAnotherTestIT {

    @Inject
    private GuiBaseAgent gui;

    @Test
    public void loginAgain() {
        gui.login("emma", "emma");
    }

}
