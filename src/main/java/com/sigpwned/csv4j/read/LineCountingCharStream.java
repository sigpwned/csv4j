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

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.PushbackReader;

public class LineCountingCharStream implements AutoCloseable {
  private final PushbackReader delegate;
  private int newlineLookback;
  private int linenum;

  public LineCountingCharStream(PushbackReader delegate) {
    this.delegate = requireNonNull(delegate);
  }

  /**
   * We handle four styles of newlines: '\r', '\n', '\r\n', and '\n\r'. The newline is considered
   * part of the line it starts on. The character after the newline starts the next line. All
   * newline styles are preserved in all cases. No standardization of newlines is performed.
   * 
   * @return
   * @throws IOException
   */
  /* default */ int read() throws IOException {
    int currentRead = getDelegate().read();
    if (currentRead == -1)
      return currentRead;

    // Should we update our line number?
    if (currentRead == '\r' || currentRead == '\n') {
      if (newlineLookback == '\r' || newlineLookback == '\n') {
        if (currentRead == newlineLookback) {
          // This should never happen. We should not have provided lookbehind.
          throw new AssertionError("failed to recognized two identical newlines in a row");
        } else {
          // This is a \r\n or \n\r. We should increment and clear lookback.
          linenum = linenum + 1;
          newlineLookback = 0;
        }
      } else {
        int newlineLookahead = peek();
        if (newlineLookahead == '\r' || newlineLookahead == '\n') {
          if (newlineLookahead == currentRead) {
            // This is two consecutive newlines, i.e., \r\r or \n\n. Increment with no lookbehind.
            linenum = linenum + 1;
            newlineLookback = 0;
          } else {
            // This is either \r\n or \n\r. We should not increment and give lookbehind.
            newlineLookback = currentRead;
          }
        } else {
          // This is a standalone newline. We have no lookback to consult.
          linenum = linenum + 1;
          newlineLookback = 0;
        }
      }
    } else {
      // This has nothing to do with newlines. Clear our lookback, just in case.
      newlineLookback = 0;
    }

    return currentRead;
  }

  public int peek() throws IOException {
    int first = getDelegate().read();
    if (first == -1)
      return -1;

    getDelegate().unread(first);

    return first;
  }

  /**
   * The zero-indexed line number of the next character to return.
   */
  public int linenum() {
    return linenum;
  }

  // public int peek2() throws IOException {
  // int first = getDelegate().read();
  // if (first == -1)
  // return -1;
  //
  // int second = getDelegate().read();
  // if (second == -1) {
  // getDelegate().unread(first);
  // return -1;
  // }
  //
  // getDelegate().unread(second);
  // getDelegate().unread(first);
  //
  // return second;
  // }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  private PushbackReader getDelegate() {
    return delegate;
  }
}
