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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public final class Boms {
  private Boms() {}

  /**
   * The UTF-8 byte order mark (BOM)
   */
  public static final byte[] UTF_8 = new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

  /**
   * The UTF-16 little-endian byte order mark (BOM)
   */
  public static final byte[] UTF_16LE = new byte[] {(byte) 0xFF, (byte) 0xFE};

  /**
   * The UTF-16 big-endian byte order mark (BOM)
   */
  public static final byte[] UTF_16BE = new byte[] {(byte) 0xFE, (byte) 0xFF};

  /**
   * The maximum length of a byte order mark (BOM) supported by this library. Note that other BOMs
   * exist that are longer than this (e.g., UTF-32LE), but they are not supported by this library.
   * 
   */
  public static final int MAX_BOM_LENGTH = 3;

  /**
   * <p>
   * Reads the byte order mark (BOM) from the given input stream, detects the character set of the
   * stream using the BOM, and returns a reader that decodes the stream using the detected character
   * set. If no BOM is present, then the default character set is used.
   * 
   * @param in The input stream
   * @param defaultCharset The default character set to use if no BOM is present
   * @return a reader that decodes the stream using the detected character set
   * @throws IOException if an I/O error
   * 
   * @see #detectCharsetFromBom(PushbackInputStream)
   * @see #decodeFromBom(PushbackInputStream, Charset)
   */
  public static Reader decodeFromBom(InputStream in, Charset defaultCharset) throws IOException {
    return decodeFromBom(new PushbackInputStream(in, MAX_BOM_LENGTH), defaultCharset);
  }

  /**
   * <p>
   * Reads the byte order mark (BOM) from the given input stream, detects the character set of the
   * stream, and returns a reader that decodes the stream using the detected character set. If no
   * BOM is present, then the default character set is used. The given stream must support pushback
   * of at least {@link #MAX_BOM_LENGTH} bytes.
   * </p>
   * 
   * @param in The input stream
   * @param defaultCharset The default character set to use if no BOM is present
   * @return a reader that decodes the stream using the detected character
   * @throws NullPointerException if {@code in} is {@code null}
   * @throws NullPointerException if {@code defaultCharset} is {@code null}
   * @throws IOException if an I/O error
   * 
   * @see #detectCharsetFromBom(PushbackInputStream)
   */
  public static Reader decodeFromBom(PushbackInputStream in, Charset defaultCharset)
      throws IOException {
    if (in == null)
      throw new NullPointerException();
    if (defaultCharset == null)
      throw new NullPointerException();
    Charset charset = detectCharsetFromBom(in).orElse(defaultCharset);
    return new InputStreamReader(in, charset);
  }

  /**
   * <p>
   * Detects the character set of a stream by reading the byte order mark (BOM) at the beginning of
   * the stream. If a BOM is present, then the BOM is discarded from the stream and the detected
   * character set is returned. If no BOM is present, then an empty optional is returned and the
   * stream is not modified. The given stream must support pushback of at least
   * {@link #MAX_BOM_LENGTH} bytes.
   * </p>
   * 
   * <p>
   * This approach to detecting the character set will work well for spreadsheets exported from
   * Excel. However, it may not work well for CSV files from other sources. If users are not
   * confident of the provenance of input files, then they should consider using a more
   * sophisticated approach, like <a href="https://github.com/sigpwned/chardet4j">chardet4j</a>, to
   * detect the character set of the input stream.
   * </p>
   * 
   * @param in the input stream
   * @return the character set detected from the BOM, or an empty optional if no BOM is present
   * @throws IOException if an I/O error
   * 
   * @see #detectBom(PushbackInputStream)
   */
  public static Optional<Charset> detectCharsetFromBom(PushbackInputStream in) throws IOException {
    byte[] bom = detectBom(in);
    if (bom == null)
      return Optional.empty();

    if (Arrays.equals(bom, UTF_8)) {
      return Optional.of(StandardCharsets.UTF_8);
    } else if (Arrays.equals(bom, UTF_16LE)) {
      return Optional.of(StandardCharsets.UTF_16LE);
    } else if (Arrays.equals(bom, UTF_16BE)) {
      return Optional.of(StandardCharsets.UTF_16BE);
    }

    // Uh... this is very strange.
    in.unread(bom);
    return Optional.empty();
  }

  /**
   * Detects the byte order mark (BOM) of a stream. If a BOM is present, then the BOM is discarded
   * from the stream and the detected BOM is returned. If no BOM is present, then {@code null} is
   * returned and the stream is not modified. The given stream must support pushback of at least
   * {@link #MAX_BOM_LENGTH} bytes. Supports BOMs for {@link StandardCharsets.UTF_8 UTF-8},
   * {@link StandardCharsets#UTF_16LE UTF-16 LE}, and {@link StandardCharsets#UTF_16BE UTF-16 BE}
   * encodings.
   * 
   * @param in the input stream
   * @return the BOM detected from the stream, or {@code null} if no BOM is present
   * @throws IOException if an I/O error
   */
  public static byte[] detectBom(PushbackInputStream in) throws IOException {
    byte[] prefix = readUpToNBytes(in, MAX_BOM_LENGTH);

    byte[] bom;
    if (rangeEquals(UTF_8, 0, prefix, 0, UTF_8.length)) {
      bom = UTF_8;
    } else if (rangeEquals(UTF_16LE, 0, prefix, 0, UTF_16LE.length)) {
      bom = UTF_16LE;
    } else if (rangeEquals(UTF_16BE, 0, prefix, 0, UTF_16BE.length)) {
      bom = UTF_16BE;
    } else {
      bom = null;
    }

    int bomlen = bom != null ? bom.length : 0;
    if (bomlen < prefix.length)
      in.unread(prefix, bomlen, prefix.length - bomlen);

    return bom;
  }

  /**
   * Reads up to {@code n} bytes from the input stream {@code in}. If the stream ends before reading
   * {@code n} bytes, the returned array will be shorter than {@code n}.
   * 
   * @param in the input stream
   * @param n the maximum number of bytes to read
   * @return an array of bytes read from the stream
   * @throws IOException if an I/O error
   */
  private static byte[] readUpToNBytes(InputStream in, int n) throws IOException {
    byte[] buffer = new byte[n];

    int total = 0;
    while (total < buffer.length) {
      int nread = in.read(buffer, total, buffer.length - total);
      if (nread == -1)
        return Arrays.copyOfRange(buffer, 0, total);
      total = total + nread;
    }

    return buffer;
  }

  /**
   * Checks if the range of bytes in {@code xs} starting at {@code xstart} is equal to the range of
   * bytes in {@code ys} starting at {@code ystart} and of length {@code len}.
   * 
   * @param xs the first array of bytes
   * @param xstart the starting index in {@code xs}
   * @param ys the second array of bytes
   * @param ystart the starting index in {@code ys}
   * @param len the length of the range
   * @return {@code true} if the ranges are equal, {@code false} otherwise
   */
  private static boolean rangeEquals(byte[] xs, int xstart, byte[] ys, int ystart, int len) {
    if (xstart + len > xs.length)
      return false;

    if (ystart + len > ys.length)
      return false;

    for (int i = 0; i < len; i++)
      if (xs[xstart + i] != ys[ystart + i])
        return false;

    return true;
  }
}
