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
package com.sigpwned.csv4j.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.Writer;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.util.CsvFormats;

/**
 * Writes well-formatted records to a character stream in CSV format
 */
public class CsvWriter implements AutoCloseable {
  private final CsvFormatter formatter;
  private final Writer out;

  public CsvWriter(Writer out) {
    this(CsvFormats.CSV, out);
  }

  public CsvWriter(CsvFormat format, Writer out) {
    this(new CsvFormatter(format), out);
  }

  public CsvWriter(CsvFormatter formatter, Writer out) {
    this.formatter = requireNonNull(formatter);
    this.out = requireNonNull(out);
  }

  /**
   * Write the given record to this writer's CSV data
   */
  public void writeNext(CsvRecord next) throws IOException {
    getOut().write(getFormatter().formatRecord(next));
    getOut().write('\n');
  }

  public CsvFormat getFormat() {
    return getFormatter().getFormat();
  }

  @Override
  public void close() throws IOException {
    getOut().close();
  }

  /**
   * @return the formatter
   */
  private CsvFormatter getFormatter() {
    return formatter;
  }

  /**
   * @return the out
   */
  private Writer getOut() {
    return out;
  }
}
