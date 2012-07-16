/*
 *    Copyright 2012 Juan Alberto López Cavallotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdto.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import junit.framework.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class designed for concurrency tests. Inspired on a blog post by
 * Matieu Carbou: http://blog.mycila.com/2009/11/writing-your-own-junit-extensions-using.html
 * 
 * @author Juan Alberto López Cavallotti
 */
public class ConcurrencyRule implements TestRule {
    
    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyRule.class);
    
    private final int threadsSpawn;

    /**
     * Builds a concurrency test with the given number of worker threads.
     * @param threadsSpawn 
     */
    public ConcurrencyRule(int threadsSpawn) {
        this.threadsSpawn = threadsSpawn;
    }
    
    @Override
    public Statement apply(final Statement base, Description description) {
        
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(threadsSpawn + 1);
        //build a new executor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadsSpawn, threadsSpawn, 1, TimeUnit.HOURS, queue);
        
        //build the count down latch to synchronize the start.
        final CountDownLatch startLatch = new CountDownLatch(1);
        
        //build the count down latch to synchronize the end.
        final CountDownLatch endLatch = new CountDownLatch(threadsSpawn);
        
        //atomic variable to write the results of the tests.
        final AtomicBoolean result = new AtomicBoolean(false);
        
        logger.info("Scheduling "+threadsSpawn+" threads for testing...");
        
        //iterate until all the threads are created
        for (int i = 0; i < threadsSpawn; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        startLatch.await();
                        base.evaluate();
                    } catch (Throwable ex) {
                        logger.error("Error while executing test",ex);
                        result.set(true);
                    } finally {
                        endLatch.countDown();
                    }
                }
            });
        }
        
        //rise the flag and allow everyone to start.
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        logger.info("Finished scheduling threads, now waiting for the results...");
        try {
            endLatch.await();
            long endTime = System.currentTimeMillis();
            
            //compute the running time
            logger.info("Running Time: "+(endTime - startTime)+" millis");
            logger.info("All threads have finished.");
            
            //if some of the tests have failed, then fail all!
            if (result.get()) {
                Assert.fail("Some of the child threads have failed the test!");
            }
            
        } catch (InterruptedException ex) {
            logger.error("Error while executing concurrency test");
            throw new RuntimeException(ex);
        }
        
        
        return base;
    }
}
