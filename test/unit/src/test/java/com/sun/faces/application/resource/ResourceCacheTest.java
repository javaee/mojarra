package com.sun.faces.application.resource;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResourceCacheTest {

    @Test
    public void noMemoryLeakWithContracts() {
        ResourceCache cache = new ResourceCache(-1L);
        ResourceInfo resourceInfo = new ResourceInfo(new ContractInfo("foo"), "bar.gif", null, null);
        List<String> contracts = new ArrayList<String>(Arrays.asList("foo", "baz"));
        cache.add(resourceInfo, contracts);
        // now we clear the contracts list, which was used to create the cache entry.
        contracts.clear();
        ResourceInfo cachedResourceInfo = cache.get("bar.gif", null, null, Arrays.asList("foo", "baz"));
        assertThat(cachedResourceInfo, is(resourceInfo));
    }

}
