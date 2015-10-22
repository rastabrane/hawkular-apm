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
package org.hawkular.btm.server.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.btm.api.model.btxn.BusinessTransaction;
import org.hawkular.btm.api.services.BusinessTransactionCriteria;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author gbrown
 */
public class BusinessTransactionServiceElasticsearchTest {

    private BusinessTransactionServiceElasticsearch bts;

    @BeforeClass
    public static void initClass() {
        System.setProperty("hawkular-btm.data.dir", "target");
    }

    @Before
    public void beforeTest() {
        bts = new BusinessTransactionServiceElasticsearch();
        bts.init();
    }

    @After
    public void afterTest() {
        bts.clear(null);
        bts.close();
    }

    @Test
    public void testQueryBTxnName() {
        List<BusinessTransaction> btxns = new ArrayList<BusinessTransaction>();

        BusinessTransaction btxn1 = new BusinessTransaction();
        btxn1.setId("id1");
        btxn1.setName("btxn1");
        btxn1.setStartTime(1000);
        btxns.add(btxn1);

        BusinessTransaction btxn2 = new BusinessTransaction();
        btxn2.setId("id2");
        btxn2.setName("btxn2");
        btxn2.setStartTime(2000);
        btxns.add(btxn2);

        BusinessTransaction btxn3 = new BusinessTransaction();
        btxn3.setId("id3");
        btxn3.setStartTime(3000);
        btxns.add(btxn3);

        bts.store(null, btxns);

        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to wait");
        }

        BusinessTransactionCriteria criteria = new BusinessTransactionCriteria();
        criteria.setStartTime(100);
        criteria.setName("btxn1");

        List<BusinessTransaction> result1 = bts.query(null, criteria);

        assertNotNull(result1);
        assertEquals(1, result1.size());
        assertEquals("id1", result1.get(0).getId());
        assertEquals("btxn1", result1.get(0).getName());
    }

    @Test
    public void testQueryNoBTxnName() {
        List<BusinessTransaction> btxns = new ArrayList<BusinessTransaction>();

        BusinessTransaction btxn1 = new BusinessTransaction();
        btxn1.setId("id1");
        btxn1.setName("btxn1");
        btxn1.setStartTime(1000);
        btxns.add(btxn1);

        BusinessTransaction btxn2 = new BusinessTransaction();
        btxn2.setId("id2");
        btxn2.setName("btxn2");
        btxn2.setStartTime(2000);
        btxns.add(btxn2);

        BusinessTransaction btxn3 = new BusinessTransaction();
        btxn3.setId("id3");
        btxn3.setStartTime(3000);
        btxns.add(btxn3);

        bts.store(null, btxns);

        try {
            synchronized (this) {
                wait(1000);
            }
        } catch (Exception e) {
            fail("Failed to wait");
        }

        BusinessTransactionCriteria criteria = new BusinessTransactionCriteria();
        criteria.setStartTime(100);
        criteria.setName("");

        List<BusinessTransaction> result1 = bts.query(null, criteria);

        assertNotNull(result1);
        assertEquals(1, result1.size());
        assertEquals("id3", result1.get(0).getId());
        assertNull(result1.get(0).getName());
    }

}
