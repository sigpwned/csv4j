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
import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;

/**
 * Parses well-formatted records from a character stream in CSV format
 */
public class CsvParser {
  private final CsvFormat format;

  public CsvParser(CsvFormat format) {
    this.format = requireNonNull(format);
  }

  public CsvRecord parseRecord(PushbackReader in) throws IOException {
    List<CsvField> result = new ArrayList<>();

    result.add(parseField(in));
    while (attempt(in, getFormat().getColumnSeparatorChar())) {
      result.add(parseField(in));
    }

    while (peek1(in) == '\r' || peek1(in) == '\n')
      in.read();

    return CsvRecord.of(result);
  }

  private CsvField parseField(PushbackReader in) throws IOException {
    if (attempt(in, -1))
      throw new EOFException();

    boolean quoted;
    StringBuilder result = new StringBuilder();
    if (attempt(in, getFormat().getQuoteChar())) {
      quoted = true;
      for (int ch = in.read(); ch != -1; ch = in.read()) {
        if (ch == getFormat().getEscapeChar() && attempt(in, getFormat().getQuoteChar())) {
          result.append(getFormat().getQuoteChar());
        } else if (ch == getFormat().getQuoteChar()) {
          break;
        } else {
          result.append((char) (ch & 0xFFFF));
        }
      }
    } else {
      quoted = false;

      int ch = peek1(in);
      while (ch != -1 && ch != getFormat().getColumnSeparatorChar() && ch != '\r' && ch != '\n') {
        result.append((char) (in.read() & 0xFFFF));
        ch = peek1(in);
      }
    }

    return CsvField.of(quoted, result.toString());
  }

  private int peek1(PushbackReader r) throws IOException {
    int buf = r.read();
    if (buf != -1)
      r.unread(buf);
    return buf;
  }

  private boolean attempt(PushbackReader r, int ch) throws IOException {
    boolean result;

    int buf = r.read();
    if (buf == ch) {
      result = true;
    } else {
      if (buf != -1)
        r.unread(buf);
      result = false;
    }

    return result;
  }

  /**
   * @return the format
   */
  public CsvFormat getFormat() {
    return format;
  }
}
