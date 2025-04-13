/*
 * Copyright © 2024 Cask Data, Inc.
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

import io.cdap.wrangler.api.Row;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Unit test for the aggregate-stats directive with byte size and time duration.
 */
public class AggregateStatsTest {

    @Test
    public void testAggregateStats() throws Exception {
        List<Row> rows = Arrays.asList(
            createRow("size", "10KB", "time", "200ms"),
            createRow("size", "1MB", "time", "3s"),
            createRow("size", "512KB", "time", "500ms")
        );

        String[] recipe = new String[]{
            "aggregate-stats :size :time :total_size_mb :total_time_sec"
        };

        // Run the directive pipeline
        List<Row> result = TestingRig.execute(recipe, rows);

        // calculations
        double expectedMB = (10240 + 1048576 + 524288) / (1024.0 * 1024.0);
        double expectedSec = (200 + 3000 + 500) / 1000.0;

        Row output = result.get(result.size() - 1);
        double actualTotalSizeMb = Double.parseDouble(output.getValue("total_size_mb").toString());

        Assert.assertEquals(expectedMB, actualTotalSizeMb, 0.001);
        Assert.assertEquals(expectedSec,
            Double.parseDouble(output.getValue("total_time_sec").toString()), 0.001);
    }

    private Row createRow(String key1, String value1, String key2, String value2) {
        Row row = new Row();
        row.add(key1, value1);
        row.add(key2, value2);
        return row;
    }
}
