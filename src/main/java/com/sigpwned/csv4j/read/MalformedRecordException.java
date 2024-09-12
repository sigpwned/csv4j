/*-
 * =================================LICENSE_START==================================
 * csv4j
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2024 Andy Boothe
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

import com.sigpwned.csv4j.CsvException;

@SuppressWarnings("serial")
public class MalformedRecordException extends CsvException {
  /**
   * The zero-indexed line number on which the malformed record starts.
   */
  private final int linenum;

  public MalformedRecordException(int linenum) {
    super("CSV record starting online " + linenum + " is malformed");
    this.linenum = linenum;
    if (linenum < 0)
      throw new IllegalArgumentException("linenum must be non-negative");
  }

  public int getLinenum() {
    return linenum;
  }
}
