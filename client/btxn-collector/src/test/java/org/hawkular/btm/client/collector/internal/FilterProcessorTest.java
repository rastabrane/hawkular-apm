/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.btm.client.collector.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hawkular.btm.api.model.admin.Filter;
import org.junit.Test;

/**
 * @author gbrown
 */
public class FilterProcessorTest {

    @Test
    public void testGlobalExclusionFilter() {
        Filter f1 = new Filter();
        f1.getExclusions().add("exclude");

        FilterProcessor fp = new FilterProcessor("btc1", f1);

        assertTrue(fp.isIncludeAll());

        assertTrue(fp.isExcluded("exclude"));
    }

    @Test
    public void testIncludeFilter() {
        Filter f1 = new Filter();
        f1.getInclusions().add("include");

        FilterProcessor fp = new FilterProcessor("btc1", f1);

        assertFalse(fp.isIncludeAll());

        assertTrue(fp.isIncluded("include"));

        assertFalse(fp.isExcluded("include"));
    }

    @Test
    public void testIncludeAndExcludeFilter() {
        Filter f1 = new Filter();
        f1.getInclusions().add("include");
        f1.getExclusions().add("exclude");

        FilterProcessor fp = new FilterProcessor("btc1", f1);

        assertFalse(fp.isIncludeAll());

        assertTrue(fp.isIncluded("include and exclude"));

        assertTrue(fp.isExcluded("include and exclude"));
    }

    @Test
    public void testExcludeBTMFilter() {
        Filter f1 = new Filter();
        f1.getExclusions().add("https?://.*/hawkular/btm");

        FilterProcessor fp = new FilterProcessor("btc1", f1);

        assertTrue(fp.isIncludeAll());

        assertTrue(fp.isExcluded("http://localhost:8080/hawkular/btm/transactions"));
    }

}
