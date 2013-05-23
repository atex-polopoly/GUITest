package com.polopoly.guitest.framework;

import com.google.inject.Inject;
import com.polopoly.guitest.agent.LoginAgent;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(PolopolyWebTestRunner.class)
public class YetAnotherTestIT {

    @Inject
    private LoginAgent loginAgent;


    @Test
    public void loginAgain() {
        loginAgent.login("emma", "emma");
    }

}
