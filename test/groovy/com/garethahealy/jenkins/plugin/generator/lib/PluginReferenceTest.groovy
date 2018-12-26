/*-
 * #%L
 * GarethHealy :: Jenkins Plugin Generator Lib
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.jenkins.plugin.generator.lib

import org.junit.Assert
import org.junit.Test

class PluginReferenceTest {

    @Test
    void canCreate() {
        PluginReference pluginReference = new PluginReference([name: "kubernetes", version: "1.12.0"], "kubernetes", false)

        Assert.assertNotNull(pluginReference)
        Assert.assertNotNull(pluginReference.reference)
        Assert.assertNotNull(pluginReference.comparableVersion)
        Assert.assertEquals("1.12.0", pluginReference.comparableVersion.toString())
    }

    @Test
    void canCreatePinned() {
        PluginReference pluginReference = new PluginReference([name: "kubernetes"], "kubernetes:1.12.0", true)

        Assert.assertNotNull(pluginReference)
        Assert.assertNotNull(pluginReference.reference)
        Assert.assertNotNull(pluginReference.comparableVersion)
        Assert.assertEquals("1.12.0", pluginReference.comparableVersion.toString())
    }

    @Test
    void canParse() {
        Map<String, String> parsed = PluginReference.parse("kubernetes:1.12.0", false)

        Assert.assertNotNull(parsed)
        Assert.assertNotNull(parsed.name)
        Assert.assertNotNull(parsed.version)

        Assert.assertEquals("kubernetes", parsed.name)
        Assert.assertEquals("1.12.0", parsed.version)
    }
}
