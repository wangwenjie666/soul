/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.plugin.request.handler;

import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.plugin.base.utils.CacheKeyUtils;
import org.apache.shenyu.plugin.request.cache.RequestRuleHandleCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * request plugin handler test.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestPluginHandlerTest {

    private RequestPluginHandler requestPluginHandler;

    private RuleData ruleData;

    @Before
    public void setUp() {
        this.requestPluginHandler = new RequestPluginHandler();
        this.ruleData = new RuleData();
        this.ruleData.setSelectorId("test-selectorId");
        this.ruleData.setName("test-request-plugin");
        this.ruleData.setHandle("{\"header\":{\"addHeaders\":{\"addKey\":\"addValue\"},\"replaceHeaderKeys\":{\"oldKey\":\"newKey\"},"
                + "\"setHeaders\":{\"oldKey\":\"newValue\"},\"removeHeaderKeys\":[\"removeKey\"],\"notEmptyConfig\":true},"
                + "\"parameter\":{\"addParameters\":{\"addKey\":\"addValue\"},\"replaceParameterKeys\":{\"oldKey\":\"newKey\"},"
                + "\"setParameters\":{\"oldKey\":\"newValue\"},\"removeParameterKeys\":[\"removeKey\"],\"notEmptyConfig\":true},"
                + "\"cookie\":{\"addCookies\":{\"addKey\":\"addValue\"},\"replaceCookieKeys\":{\"oldKey\":\"newKey\"},"
                + "\"setCookies\":{\"oldKey\":\"newValue\"},\"removeCookieKeys\":[\"removeKey\"],\"notEmptyConfig\":true},\"emptyConfig\":false}");
    }

    @Test
    public void testHandlerRule() {
        this.requestPluginHandler.handlerRule(this.ruleData);
        RequestRuleHandleCache cache = RequestRuleHandleCache.getInstance();
        assertNotNull(cache.obtainHandle(CacheKeyUtils.INST.getKey(this.ruleData)));
    }

    @Test
    public void testRemoveRule() {
        this.requestPluginHandler.handlerRule(this.ruleData);
        RuleData ruleData = new RuleData();
        ruleData.setSelectorId("test");
        ruleData.setName("test");
        RequestRuleHandleCache cache = RequestRuleHandleCache.getInstance();
        this.requestPluginHandler.removeRule(this.ruleData);
        assertNull(cache.obtainHandle(CacheKeyUtils.INST.getKey(this.ruleData)));
        this.requestPluginHandler.removeRule(ruleData);
        assertNull(cache.obtainHandle(CacheKeyUtils.INST.getKey(ruleData)));
        this.requestPluginHandler.removeRule(null);
    }

    @Test
    public void testPluginNamed() {
        assertEquals(this.requestPluginHandler.pluginNamed(), "request");
    }
}
