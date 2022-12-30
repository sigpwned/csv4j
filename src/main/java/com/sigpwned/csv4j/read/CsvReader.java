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
package com.sigpwned.csv4j.read;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.util.CsvFormats;

/**
 * Reads well-formatted records from character stream in CSV format
 */
public class CsvReader implements AutoCloseable {
  public static final int MIN_PUSHBACK = 1;

  private final CsvParser parser;
  private final PushbackReader in;

  public CsvReader(Reader in) {
    this(CsvFormats.CSV, in);
  }

  public CsvReader(CsvFormat format, Reader in) {
    this(format, new PushbackReader(in, MIN_PUSHBACK));
  }

  public CsvReader(CsvFormat format, PushbackReader in) {
    this(new CsvParser(format), in);
  }

  public CsvReader(CsvParser parser, PushbackReader in) {
    this.in = requireNonNull(in);
    this.parser = requireNonNull(parser);
  }

  /**
   * @return The next record in this reader's CSV data if it exists, or {@code null} otherwise.
   */
  public CsvRecord readNext() throws IOException {
    CsvRecord result;
    if (peek1(getIn()) == -1) {
      result = null;
    } else {
      result = getParser().parseRecord(in);
    }
    return result;
  }

  /**
   * @return An {@link Iterator} for each remaining row this reader's CSV data.
   * @throws UncheckedIOException in case of {@link IOException}
   */
  public Iterator<CsvRecord> iterator() {
    final CsvRecord first;
    try {
      first = readNext();
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read initial row", e);
    }
    return new Iterator<CsvRecord>() {
      private CsvRecord next = first;

      @Override
      public boolean hasNext() {
        return next != null;
      }

      @Override
      public CsvRecord next() {
        CsvRecord result = next;
        try {
          next = readNext();
        } catch (IOException e) {
          throw new UncheckedIOException("Failed to read next row", e);
        }
        return result;
      }
    };
  }

  /**
   * @return A {@link Stream} of the remaining rows in this reader's CSV data.
   * @throws UncheckedIOException in case of {@link IOException}
   */
  public Stream<CsvRecord> stream() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(),
        Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED), false);
  }

  public CsvFormat getFormat() {
    return getParser().getFormat();
  }

  @Override
  public void close() throws IOException {
    getIn().close();
  }

  /**
   * @return the parser
   */
  private CsvParser getParser() {
    return parser;
  }

  /**
   * @return the in
   */
  private PushbackReader getIn() {
    return in;
  }

  private int peek1(PushbackReader r) throws IOException {
    int buf = r.read();
    if (buf != -1)
      r.unread(buf);
    return buf;
  }
}
