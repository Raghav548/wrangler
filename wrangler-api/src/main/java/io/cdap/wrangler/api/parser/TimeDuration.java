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

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Token implementation for parsing and storing time durations like ms, s, seconds.
 */
public class TimeDuration implements Token {
    private final String original;
    private final long milliseconds;

    public TimeDuration(String value) {
        this.original = value;
        this.milliseconds = parse(value);
    }

    private long parse(String value) {
        value = value.trim().toLowerCase();

         // Handle negative numbers
        if (value.startsWith("-")) {
            // Remove negative sign and parse as positive number
            value = value.substring(1);
            // ... (rest of the method remains the same)
        }

        if (value.endsWith("ms")) {
            return (long) Double.parseDouble(value.replace("ms", ""));
        } else if (value.endsWith("s")) {
            return (long) (Double.parseDouble(value.replace("s", "")) * 1000);
        } else if (value.endsWith("sec")) {
            return (long) (Double.parseDouble(value.replace("sec", "")) * 1000);
        } else if (value.endsWith("seconds")) {
            return (long) (Double.parseDouble(value.replace("seconds", "")) * 1000);
        } else if (value.endsWith("m")) {
            System.out.println(value);
            return (long) (Double.parseDouble(value.replace("m", "")) * 60 * 1000);
        } else if (value.endsWith("min")) {
            return (long) (Double.parseDouble(value.replace("min", "")) * 60 * 1000);
        } else if (value.endsWith("minute")) {
            return (long) (Double.parseDouble(value.replace("minute", "")) * 60 * 1000);
        } else if (value.endsWith("minutes")) {
            return (long) (Double.parseDouble(value.replace("minutes", "")) * 60 * 1000);
        } else if (value.endsWith("h")) {
            return (long) (Double.parseDouble(value.replace("h", "")) * 60 * 60 * 1000);
        } else if (value.endsWith("hour")) {
            return (long) (Double.parseDouble(value.replace("hour", "")) * 60 * 60 * 1000);
        } else if (value.endsWith("hours")) {
            return (long) (Double.parseDouble(value.replace("hours", "")) * 60 * 60 * 1000);
        } else if (value.endsWith("d")) {
            return (long) (Double.parseDouble(value.replace("d", "")) * 24 * 60 * 60 * 1000);
        } else if (value.endsWith("day")) {
            return (long) (Double.parseDouble(value.replace("day", "")) * 24 * 60 * 60 * 1000);
        } else if (value.endsWith("days")) {
            return (long) (Double.parseDouble(value.replace("days", "")) * 24 * 60 * 60 * 1000);
        } else { 
            return Long.parseLong(value);    
     
        }
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public Object value() {
        return milliseconds;
    }

    @Override
    public TokenType type() {
        return TokenType.NUMERIC;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(milliseconds);
    }
}
