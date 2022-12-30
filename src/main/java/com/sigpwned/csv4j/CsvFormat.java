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

import java.util.Objects;

/**
 * A description of how data is encoded in a CSV file
 */
public class CsvFormat {
  public static CsvFormat of(char quoteChar, char escapeChar, char columnSeparatorChar) {
    return new CsvFormat(quoteChar, escapeChar, columnSeparatorChar);
  }

  private final char quoteChar;
  private final char escapeChar;
  private final char columnSeparatorChar;

  public CsvFormat(char quoteChar, char escapeChar, char columnSeparatorChar) {
    if (columnSeparatorChar == quoteChar)
      throw new IllegalArgumentException("columnSeparatorChar and quoteChar must be different");
    this.quoteChar = quoteChar;
    this.escapeChar = escapeChar;
    this.columnSeparatorChar = columnSeparatorChar;
  }

  /**
   * @return the quoteChar
   */
  public char getQuoteChar() {
    return quoteChar;
  }

  /**
   * @return the escapeChar
   */
  public char getEscapeChar() {
    return escapeChar;
  }

  /**
   * @return the columnSeparatorChar
   */
  public char getColumnSeparatorChar() {
    return columnSeparatorChar;
  }

  @Override
  public int hashCode() {
    return Objects.hash(columnSeparatorChar, escapeChar, quoteChar);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CsvFormat other = (CsvFormat) obj;
    return columnSeparatorChar == other.columnSeparatorChar && escapeChar == other.escapeChar
        && quoteChar == other.quoteChar;
  }

  @Override
  public String toString() {
    return "CsvFormat [quoteChar=" + quoteChar + ", escapeChar=" + escapeChar
        + ", columnSeparatorChar=" + columnSeparatorChar + "]";
  }
}
