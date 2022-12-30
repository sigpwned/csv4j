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

import java.util.List;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;

/**
 * Converts CSV records into well-formatted character strings in CSV format
 */
public class CsvFormatter {
  private final CsvFormat format;

  public CsvFormatter(CsvFormat format) {
    this.format = format;
  }

  public String formatRecord(CsvRecord record) {
    List<CsvField> fields = record.getFields();

    StringBuilder result = new StringBuilder();

    result.append(formatField(fields.get(0)));
    for (int i = 1; i < fields.size(); i++) {
      result.append(getFormat().getColumnSeparatorChar());
      result.append(formatField(fields.get(i)));
    }

    return result.toString();
  }

  public String formatField(CsvField field) {
    StringBuilder result = new StringBuilder();
    if (field.isQuoted()) {
      result.append(getFormat().getQuoteChar());
      for (int i = 0; i < field.getText().length(); i++) {
        char ch = field.getText().charAt(i);
        if (ch == getFormat().getQuoteChar())
          result.append(getFormat().getEscapeChar());
        result.append(ch);
      }
      result.append(getFormat().getQuoteChar());
    } else if (field.getText() != null) {
      result.append(field.getText());
    } else {
      // We indicate null unquoted
    }
    return result.toString();
  }

  /**
   * @return the format
   */
  public CsvFormat getFormat() {
    return format;
  }
}
