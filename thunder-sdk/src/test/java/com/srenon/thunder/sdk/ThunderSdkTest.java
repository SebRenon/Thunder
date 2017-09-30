package com.srenon.thunder.sdk;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ThunderSdkTest {

    @Test
    public void initialize() {
        // Not initialized yet
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("");
        assertNotNull(ThunderSdk.getInstance());
    }
}