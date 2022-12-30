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

import java.io.Reader;
import java.io.Writer;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.csv4j.write.CsvWriter;

public final class Csv {
  private Csv() {}

  /**
   * Read rows from the given CSV-formatted character stream.
   */
  public static CsvReader read(Reader r) {
    return new CsvReader(r);
  }

  /**
   * Read rows from the given CSV-formatted character stream.
   */
  public static CsvWriter write(Writer w) {
    return new CsvWriter(w);
  }
}
