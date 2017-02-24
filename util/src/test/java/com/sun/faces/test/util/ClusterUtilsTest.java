package com.sun.faces.test.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClusterUtilsTest {
    
    /**
     * Test getBaseUrls method.
     */
    @Test
    public void testGetBaseUrls() {
        System.setProperty("integration.url", "dummy");
        assertEquals(1, ClusterUtils.getBaseUrls().length);
        System.setProperty("integration.url2", "dummy2");
        assertEquals(2, ClusterUtils.getBaseUrls().length);
        System.setProperty("integration.url2", "");
        assertEquals(1, ClusterUtils.getBaseUrls().length);
    }
}
