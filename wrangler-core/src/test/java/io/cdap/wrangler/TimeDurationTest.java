/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */


package io.cdap.wrangler;

import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

public class TimeDurationTest {

    @Test
    public void testParsingMilliseconds() {
        TimeDuration td = new TimeDuration("250ms");
        Assert.assertEquals(250, td.getMilliseconds());
    }

    @Test
    public void testParsingSeconds() {
        TimeDuration td = new TimeDuration("2.5s");
        Assert.assertEquals(2500, td.getMilliseconds());
    }

    @Test
    public void testParsingMinutes() {
        TimeDuration td = new TimeDuration("2.5m");
        System.out.println("Milliseconds: " + td.getMilliseconds());
        Assert.assertEquals(150000, td.getMilliseconds());
    }

    @Test
    public void testParsingHours() {
        TimeDuration td = new TimeDuration("2.5h");

        Assert.assertEquals(9000000, td.getMilliseconds());
    }

    @Test
    public void testParsingDays() {
        TimeDuration td = new TimeDuration("2.5d");
        Assert.assertEquals(216000000, td.getMilliseconds());
    }

    @Test
    public void testParsingInvalidInput() {
        try {
            new TimeDuration("abc");
            Assert.fail("Expected TimeDuration to throw an exception for invalid input");
        } catch (Exception e) {
            // Expected
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testParsingDifferentUnits() {
        TimeDuration tdMs = new TimeDuration("250ms");
        Assert.assertEquals(250, tdMs.getMilliseconds());

        TimeDuration tdS = new TimeDuration("2.5s");
        Assert.assertEquals(2500, tdS.getMilliseconds());

        TimeDuration tdM = new TimeDuration("2.5m");
        Assert.assertEquals(150000, tdM.getMilliseconds());

        TimeDuration tdH = new TimeDuration("2.5h");
        Assert.assertEquals(9000000, tdH.getMilliseconds());

        TimeDuration tdD = new TimeDuration("2.5d");
        Assert.assertEquals(216000000, tdD.getMilliseconds());
    }
}
