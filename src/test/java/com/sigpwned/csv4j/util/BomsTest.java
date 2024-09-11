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
package com.sigpwned.csv4j.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.Test;

public class BomsTest {
  @Test
  public void detectUtf8BomTest() throws IOException {
    assertThat(
        Boms.detectCharsetFromBom(
            new PushbackInputStream(new ByteArrayInputStream(Boms.UTF_8), Boms.MAX_BOM_LENGTH)),
        is(Optional.of(StandardCharsets.UTF_8)));
  }

  @Test
  public void detectUtf16LEBomTest() throws IOException {
    assertThat(
        Boms.detectCharsetFromBom(
            new PushbackInputStream(new ByteArrayInputStream(Boms.UTF_16LE), Boms.MAX_BOM_LENGTH)),
        is(Optional.of(StandardCharsets.UTF_16LE)));
  }

  @Test
  public void detectUtf16BEBomTest() throws IOException {
    assertThat(
        Boms.detectCharsetFromBom(
            new PushbackInputStream(new ByteArrayInputStream(Boms.UTF_16BE), Boms.MAX_BOM_LENGTH)),
        is(Optional.of(StandardCharsets.UTF_16BE)));
  }

  @Test
  public void detectNoBomTest() throws IOException {
    assertThat(
        Boms.detectCharsetFromBom(new PushbackInputStream(
            new ByteArrayInputStream(new byte[] {0, 0, 0}), Boms.MAX_BOM_LENGTH)),
        is(Optional.empty()));
  }

  @Test
  public void detectNoBomEofTest() throws IOException {
    assertThat(
        Boms.detectCharsetFromBom(
            new PushbackInputStream(new ByteArrayInputStream(new byte[] {}), Boms.MAX_BOM_LENGTH)),
        is(Optional.empty()));
  }
}
