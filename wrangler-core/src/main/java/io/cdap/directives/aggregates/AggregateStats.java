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

package io.cdap.directives.aggregates;

import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.cdap.api.annotation.Name;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.Collections;
import java.util.List;

/**
 * A directive for computing aggregate statistics like total size or duration.
 */
@Name("aggregate-stats")
public class AggregateStats implements Directive {

  private String sizeInputCol;
  private String timeInputCol;
  private String sizeOutputCol;
  private String timeOutputCol;

  private long totalBytes = 0;
  private long totalMilliseconds = 0;

  @Override
  public UsageDefinition define() {
    String[] columnNames = {"sizeColumn", "timeColumn", "sizeResultColumn", "timeResultColumn"};
    TokenType[] columnTypes = {TokenType.COLUMN_NAME, TokenType.COLUMN_NAME, 
                              TokenType.COLUMN_NAME, TokenType.COLUMN_NAME};

    UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
    for (int i = 0; i < columnNames.length; i++) {
      builder.define(columnNames[i], columnTypes[i]);
    }

    return builder.build();
  }

  @Override
  public void initialize(Arguments args) {
    sizeInputCol = ((ColumnName) args.value("sizeColumn")).value();
    timeInputCol = ((ColumnName) args.value("timeColumn")).value();
    sizeOutputCol = ((ColumnName) args.value("sizeResultColumn")).value();
    timeOutputCol = ((ColumnName) args.value("timeResultColumn")).value();
  }

  @Override
  public List<Row> execute(List<Row> rows, ExecutorContext context) {
    
    for (Row row : rows) {
      Object sizeValue = row.getValue(sizeInputCol);
      Object timeValue = row.getValue(timeInputCol);

      System.out.println("Row: " + row);
      System.out.println("sizeValue = " + sizeValue + ", timeValue = " + timeValue);

      if (sizeValue != null) {
        try {
          System.out.println("Parsing size: " + sizeValue.toString()); // ✅ Add this
          ByteSize bs = new ByteSize(sizeValue.toString());
          System.out.println("Bytes parsed: " + bs.getBytes());        // ✅ And this
          this.totalBytes += bs.getBytes();
        } catch (Exception e) {
          e.printStackTrace(); // show exact error
        }
      }

      if (timeValue != null) {
        try {
          System.out.println("Parsing time: " + timeValue.toString());
          TimeDuration td = new TimeDuration(timeValue.toString());
          System.out.println("Milliseconds parsed: " + td.getMilliseconds());
          this.totalMilliseconds += td.getMilliseconds();
        } catch (Exception e) {
          // Silent exception handling or customize based on your logging needs
          // System.err.println("Error processing time value: " + e.getMessage());
        }
      }
    }
    System.out.println("Total bytes: " + this.totalBytes);
    System.out.println("Total milliseconds: " + this.totalMilliseconds);


    // Create output row with calculated values
    Row output = new Row();
    double totalSizeMB = this.totalBytes / (1024.0 * 1024.0); // convert bytes to MB
    double totalTimeSec = this.totalMilliseconds / 1000.0;    // convert ms to sec

    output = output.add(sizeOutputCol, totalSizeMB);
    output = output.add(timeOutputCol, totalTimeSec);

    System.out.println("totalBytes = " + totalBytes);
  System.out.println("totalSizeMB = " + totalSizeMB);


    return Collections.singletonList(output);
  }

  @Override
  public void destroy() {
    // No resources to release
  }
}