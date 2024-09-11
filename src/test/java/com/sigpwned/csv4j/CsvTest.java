/*-
 * =================================LICENSE_START==================================
 * csv4j
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
 * ====================================SECTION=====================================
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
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.csv4j;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.csv4j.util.CsvFormats;
import com.sigpwned.csv4j.write.CsvWriter;

public class CsvTest {
  @Test
  public void smokeTest() throws IOException {
    final StringWriter wbuf = new StringWriter();

    final List<CsvRecord> expecteds = Arrays.asList(
        CsvRecord.of(Arrays.asList(new CsvField(true, "alpha"), new CsvField(true, "bravo"),
            new CsvField(true, "charlie"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "hello"), new CsvField(true, "10"),
            new CsvField(true, "5"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "world"), new CsvField(true, ""),
            new CsvField(false, ""))));

    try (final CsvWriter w = new CsvWriter(CsvFormats.CSV, wbuf)) {
      for (CsvRecord e : expecteds)
        w.writeNext(e);
    }

    final StringReader rbuf = new StringReader(wbuf.toString());

    final List<CsvRecord> observeds = new ArrayList<>();

    try (final CsvReader r = new CsvReader(CsvFormats.CSV, rbuf)) {
      for (CsvRecord o = r.readNext(); o != null; o = r.readNext())
        observeds.add(o);
    }

    assertThat(observeds, is(expecteds));
  }

  @Test
  public void readTest() throws IOException {
    final List<CsvRecord> expecteds = Arrays.asList(
        CsvRecord.of(Arrays.asList(new CsvField(true, "alpha"), new CsvField(true, "bravo"),
            new CsvField(true, "charlie"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "hello"), new CsvField(true, "10"),
            new CsvField(true, "5"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "world"), new CsvField(true, ""),
            new CsvField(false, ""))));

    // TODO Use text blocks when update to Java 17
    final StringReader rbuf = new StringReader(
        "\"alpha\",\"bravo\",\"charlie\"\n" + "\"hello\",\"10\",\"5\"\n" + "\"world\",\"\",\n");

    final List<CsvRecord> observeds = new ArrayList<>();

    try (final CsvReader r = new CsvReader(CsvFormats.CSV, rbuf)) {
      for (CsvRecord o = r.readNext(); o != null; o = r.readNext())
        observeds.add(o);
    }

    assertThat(observeds, is(expecteds));
  }

  @Test
  public void writeTest() throws IOException {
    final List<CsvRecord> records = Arrays.asList(
        CsvRecord.of(Arrays.asList(new CsvField(true, "alpha"), new CsvField(true, "bravo"),
            new CsvField(true, "charlie"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "hello"), new CsvField(true, "10"),
            new CsvField(true, "5"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "world"), new CsvField(true, ""),
            new CsvField(false, ""))));

    final StringWriter wbuf = new StringWriter();
    try (final CsvWriter w = new CsvWriter(CsvFormats.CSV, wbuf)) {
      for (CsvRecord r : records)
        w.writeNext(r);
    }

    // TODO Use text blocks when update to Java 17
    final String expected =
        "\"alpha\",\"bravo\",\"charlie\"\n" + "\"hello\",\"10\",\"5\"\n" + "\"world\",\"\",\n";

    assertThat(wbuf.toString(), is(expected));
  }

  @Test
  public void iteratorTest() throws IOException {
    final List<CsvRecord> expecteds = Arrays.asList(
        CsvRecord.of(Arrays.asList(new CsvField(true, "alpha"), new CsvField(true, "bravo"),
            new CsvField(true, "charlie"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "hello"), new CsvField(true, "10"),
            new CsvField(true, "5"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "world"), new CsvField(true, ""),
            new CsvField(false, ""))));

    // TODO Use text blocks when update to Java 17
    final StringReader rbuf = new StringReader(
        "\"alpha\",\"bravo\",\"charlie\"\n" + "\"hello\",\"10\",\"5\"\n" + "\"world\",\"\",\n");

    final List<CsvRecord> observeds = new ArrayList<>();

    try (final CsvReader r = new CsvReader(CsvFormats.CSV, rbuf)) {
      for (CsvRecord o : r)
        observeds.add(o);
    }

    assertThat(observeds, is(expecteds));
  }

  @Test
  public void streamTest() throws IOException {
    final List<CsvRecord> expecteds = Arrays.asList(
        CsvRecord.of(Arrays.asList(new CsvField(true, "alpha"), new CsvField(true, "bravo"),
            new CsvField(true, "charlie"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "hello"), new CsvField(true, "10"),
            new CsvField(true, "5"))),
        CsvRecord.of(Arrays.asList(new CsvField(true, "world"), new CsvField(true, ""),
            new CsvField(false, ""))));

    // TODO Use text blocks when update to Java 17
    final StringReader rbuf = new StringReader(
        "\"alpha\",\"bravo\",\"charlie\"\n" + "\"hello\",\"10\",\"5\"\n" + "\"world\",\"\",\n");

    final List<CsvRecord> observeds;
    try (final CsvReader r = new CsvReader(CsvFormats.CSV, rbuf)) {
      observeds = r.stream().collect(toList());
    }

    assertThat(observeds, is(expecteds));
  }
}
