package com.polopoly.guitest.framework;


import com.polopoly.guitest.agent.LoginAgent;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(PolopolyWebTestRunner.class)
public class AnotherLoginIT {

    @Inject
    private LoginAgent login;


    @Test
    public void loginAsSusperUser() {
        login.loginAsSysadmin();
    }

}
