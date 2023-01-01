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
 * One cell of a row in a CSV file
 */
public class CsvField {
  private boolean quoted;
  private String text;

  public CsvField() {
    this(false, "");
  }

  public CsvField(boolean quoted, String text) {
    setQuoted(quoted);
    setText(text);
  }

  /**
   * @return the quoted
   */
  public boolean isQuoted() {
    return quoted;
  }

  /**
   * @param quoted the quoted to set
   */
  public void setQuoted(boolean quoted) {
    this.quoted = quoted;
  }

  public CsvField withQuoted(boolean quoted) {
    setQuoted(quoted);
    return this;
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    if (text == null)
      throw new NullPointerException();
    this.text = text;
  }

  public CsvField withText(String text) {
    setText(text);
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(quoted, text);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CsvField other = (CsvField) obj;
    return quoted == other.quoted && Objects.equals(text, other.text);
  }

  @Override
  public String toString() {
    return "CsvField [quoted=" + quoted + ", text=" + text + "]";
  }
}
