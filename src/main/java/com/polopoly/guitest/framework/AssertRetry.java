package com.polopoly.guitest.framework;


/**
 * This class is an assert with a retry mechanism.   
 */
public class AssertRetry {
    
    public interface Function {
        public void doAssert() throws Exception;
    }
    
    /**
     * Do asserts in a certain interval until it timeouts.   
     * @param timeoutInMilliseconds
     * @param intervalInMilliseconds
     * @param function the function to run
     * @throws Exception
     */
    public static void assertRetry(long timeoutInMilliseconds, long intervalInMilliseconds, 
                                    AssertRetry.Function function) 
        throws Exception {
        
        long endTime = System.currentTimeMillis() + timeoutInMilliseconds;
        while (true) {
            try {
                function.doAssert();
                return;
            } catch (Error e) {
                if (!(e instanceof junit.framework.AssertionFailedError)) {
                    throw e;
                }
                long now = System.currentTimeMillis();
                if (now > endTime) {
                    throw e;
                }
            }
            Thread.sleep(intervalInMilliseconds);
        }
        
    }
}
