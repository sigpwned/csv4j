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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.util.CsvFormats;

/**
 * Reads well-formatted records from character stream in CSV format. This object is not thread-safe.
 * The user may interleave calls to {@link #readNext()}, {@link #iterator()} and its return values,
 * {@link #spliterator()} and its return values, and {@link #stream()} and its return values, and
 * the results will remain consistent.
 */
public class CsvReader implements AutoCloseable, Iterable<CsvRecord> {
  public static final int MIN_PUSHBACK = 1;

  private final CsvParser parser;
  private final LineCountingCharStream in;

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
    this.in = new LineCountingCharStream(requireNonNull(in));
    this.parser = requireNonNull(parser);
  }

  /**
   * @return The next record in this reader's CSV data if it exists, or {@code null} otherwise.
   */
  public CsvRecord readNext() throws IOException {
    return next();
  }

  /**
   * @return An {@link Iterator} for each remaining row this reader's CSV data.
   * @throws UncheckedIOException in case of {@link IOException}
   */
  public Iterator<CsvRecord> iterator() {
    return new Iterator<CsvRecord>() {
      @Override
      public boolean hasNext() {
        CsvRecord peeked;
        try {
          peeked = CsvReader.this.peek();
        } catch (IOException e) {
          throw new UncheckedIOException("Failed to peek next row", e);
        }
        return peeked != null;
      }

      @Override
      public CsvRecord next() {
        CsvRecord result;
        try {
          result = CsvReader.this.next();
        } catch (IOException e) {
          throw new UncheckedIOException("Failed to read next row", e);
        }
        if (result == null)
          throw new NoSuchElementException();
        return result;
      }
    };
  }

  @Override
  public Spliterator<CsvRecord> spliterator() {
    return Spliterators.spliteratorUnknownSize(iterator(),
        Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
  }

  /**
   * @return A {@link Stream} of the remaining rows in this reader's CSV data.
   * @throws UncheckedIOException in case of {@link IOException}
   */
  public Stream<CsvRecord> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  public CsvFormat getFormat() {
    return getParser().getFormat();
  }

  @Override
  public void close() throws IOException {
    getIn().close();
  }

  /**
   * Basically an {@link Optional}. Used to avoid warnings source analysis warnings.
   */
  private static class OptionalRecord {
    private static final OptionalRecord EMPTY = new OptionalRecord(null);

    public static OptionalRecord empty() {
      return EMPTY;
    }

    public static OptionalRecord ofNullable(CsvRecord value) {
      return value != null ? new OptionalRecord(value) : EMPTY;
    }

    public final CsvRecord value;


    public CsvRecord orElseNull() {
      return value != null ? value : null;
    }

    private OptionalRecord(CsvRecord value) {
      this.value = value;
    }
  }

  /**
   * If this value is {@code null}, then the next record is unknown. Otherwise, the next record (or
   * its absence) is contained in this value.
   */
  private OptionalRecord next;

  private CsvRecord peek() throws IOException {
    if (next == null) {
      if (getIn().peek() == -1) {
        next = OptionalRecord.empty();
      } else {
        next = OptionalRecord.ofNullable(getParser().parseRecord(in));
      }
    }
    return next.orElseNull();
  }

  private CsvRecord next() throws IOException {
    CsvRecord result = peek();
    next = null;
    return result;
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
  private LineCountingCharStream getIn() {
    return in;
  }
}
