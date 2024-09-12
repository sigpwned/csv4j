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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import org.junit.Test;

public class LineCountingCharStreamTest {
  @Test
  public void test() throws IOException {
    // We should recognize all four types of newlines

    // Newline style \n
    try (LineCountingCharStream n =
        new LineCountingCharStream(new PushbackReader(new StringReader("a\nb\nc\n\n"), 1))) {
      assertThat(n.linenum(), is(0));
      assertThat(n.read(), is((int) 'a'));
      assertThat(n.linenum(), is(0));
      assertThat(n.read(), is((int) '\n'));
      assertThat(n.linenum(), is(1));
      assertThat(n.read(), is((int) 'b'));
      assertThat(n.linenum(), is(1));
      assertThat(n.read(), is((int) '\n'));
      assertThat(n.linenum(), is(2));
      assertThat(n.read(), is((int) 'c'));
      assertThat(n.linenum(), is(2));
      assertThat(n.read(), is((int) '\n'));
      assertThat(n.linenum(), is(3));
      assertThat(n.read(), is((int) '\n'));
      assertThat(n.linenum(), is(4));
      assertThat(n.read(), is(-1));
    }

    // Newline style \r
    try (LineCountingCharStream r =
        new LineCountingCharStream(new PushbackReader(new StringReader("a\rb\rc\r\r"), 1))) {
      assertThat(r.linenum(), is(0));
      assertThat(r.read(), is((int) 'a'));
      assertThat(r.linenum(), is(0));
      assertThat(r.read(), is((int) '\r'));
      assertThat(r.linenum(), is(1));
      assertThat(r.read(), is((int) 'b'));
      assertThat(r.linenum(), is(1));
      assertThat(r.read(), is((int) '\r'));
      assertThat(r.linenum(), is(2));
      assertThat(r.read(), is((int) 'c'));
      assertThat(r.linenum(), is(2));
      assertThat(r.read(), is((int) '\r'));
      assertThat(r.linenum(), is(3));
      assertThat(r.read(), is((int) '\r'));
      assertThat(r.linenum(), is(4));
      assertThat(r.read(), is(-1));
    }

    // Newline style \r\n
    try (LineCountingCharStream rn = new LineCountingCharStream(
        new PushbackReader(new StringReader("a\r\nb\r\nc\r\n\r\n"), 1))) {
      assertThat(rn.linenum(), is(0));
      assertThat(rn.read(), is((int) 'a'));
      assertThat(rn.linenum(), is(0));
      assertThat(rn.read(), is((int) '\r'));
      assertThat(rn.linenum(), is(0));
      assertThat(rn.read(), is((int) '\n'));
      assertThat(rn.linenum(), is(1));
      assertThat(rn.read(), is((int) 'b'));
      assertThat(rn.linenum(), is(1));
      assertThat(rn.read(), is((int) '\r'));
      assertThat(rn.linenum(), is(1));
      assertThat(rn.read(), is((int) '\n'));
      assertThat(rn.linenum(), is(2));
      assertThat(rn.read(), is((int) 'c'));
      assertThat(rn.linenum(), is(2));
      assertThat(rn.read(), is((int) '\r'));
      assertThat(rn.linenum(), is(2));
      assertThat(rn.read(), is((int) '\n'));
      assertThat(rn.linenum(), is(3));
      assertThat(rn.read(), is((int) '\r'));
      assertThat(rn.linenum(), is(3));
      assertThat(rn.read(), is((int) '\n'));
      assertThat(rn.linenum(), is(4));
      assertThat(rn.read(), is(-1));
    }

    // Newline style \n\r
    try (LineCountingCharStream nr = new LineCountingCharStream(
        new PushbackReader(new StringReader("a\n\rb\n\rc\n\r\n\r"), 1))) {
      assertThat(nr.linenum(), is(0));
      assertThat(nr.read(), is((int) 'a'));
      assertThat(nr.linenum(), is(0));
      assertThat(nr.read(), is((int) '\n'));
      assertThat(nr.linenum(), is(0));
      assertThat(nr.read(), is((int) '\r'));
      assertThat(nr.linenum(), is(1));
      assertThat(nr.read(), is((int) 'b'));
      assertThat(nr.linenum(), is(1));
      assertThat(nr.read(), is((int) '\n'));
      assertThat(nr.linenum(), is(1));
      assertThat(nr.read(), is((int) '\r'));
      assertThat(nr.linenum(), is(2));
      assertThat(nr.read(), is((int) 'c'));
      assertThat(nr.linenum(), is(2));
      assertThat(nr.read(), is((int) '\n'));
      assertThat(nr.linenum(), is(2));
      assertThat(nr.read(), is((int) '\r'));
      assertThat(nr.linenum(), is(3));
      assertThat(nr.read(), is((int) '\n'));
      assertThat(nr.linenum(), is(3));
      assertThat(nr.read(), is((int) '\r'));
      assertThat(nr.linenum(), is(4));
      assertThat(nr.read(), is(-1));
    }

  }
}
