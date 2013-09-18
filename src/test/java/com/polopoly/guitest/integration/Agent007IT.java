package com.polopoly.guitest.integration;


import com.atex.testinject.TestInjectRunner;
import com.google.inject.Inject;
import com.polopoly.guitest.agent.Agent007;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestInjectRunner.class)
public class Agent007IT {


    @Inject
    Agent007 agent007;


    @Test
    public void not_that_secret_mission() {
        Assert.assertEquals("my name is Bond, James Bond", agent007.whisper());
    }


}
