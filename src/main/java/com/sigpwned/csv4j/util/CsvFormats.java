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
package com.sigpwned.csv4j.util;

import com.sigpwned.csv4j.CsvFormat;

/**
 * Constants for common CSV formats
 * 
 * @see CsvFormat
 */
public final class CsvFormats {
  private CsvFormats() {}

  /**
   * Standard comma-separated value (CSV) format.
   */
  public static final CsvFormat CSV = CsvFormat.of('"', '"', ',');

  /**
   * A flavor of tab-separated value (TSV) format that quotes fields and uses the tab character to
   * separate columns. This is a popul
   */
  public static final CsvFormat TSV = CsvFormat.of('"', '"', '\t');
}
