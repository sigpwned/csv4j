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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;

/**
 * One row of a CSV file
 */
public class CsvRecord {
  public static CsvRecord of(CsvField... fields) {
    return new CsvRecord(asList(fields));
  }

  public static CsvRecord of(List<CsvField> fields) {
    return new CsvRecord(fields);
  }

  private final List<CsvField> fields;

  public CsvRecord(List<CsvField> fields) {
    this.fields = unmodifiableList(fields);
  }

  /**
   * @return the fields
   */
  public List<CsvField> getFields() {
    return fields;
  }

  public int size() {
    return getFields().size();
  }

  public boolean isEmpty() {
    return getFields().isEmpty();
  }

  @Override
  public int hashCode() {
    return Objects.hash(fields);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CsvRecord other = (CsvRecord) obj;
    return Objects.equals(fields, other.fields);
  }

  @Override
  public String toString() {
    return "CsvRecord [fields=" + fields + "]";
  }
}
