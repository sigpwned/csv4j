package com.sigpwned.csv4j.read;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.util.CsvFormats;

public class CsvParserTest {
  /**
   * regression test for #35
   */
  @Test(expected = MalformedRecordException.class)
  public void givenTsvFile_whenParseAsCsvFile_thenMalformedRecordException() throws IOException {
    // Note the file is TSV
    URL resource = getClass().getResource("example.tsv");

    // Note the format is CSV
    CsvParser parser = new CsvParser(CsvFormats.CSV);

    try (LineCountingCharStream in = new LineCountingCharStream(new PushbackReader(
        new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8), 1))) {
      parser.parseRecord(in);
    }
  }

  @Test
  public void givenMalformedLine2File_whenParse_thenMalformedRecordExceptionForLine2()
      throws IOException {
    // Note the file is TSV
    URL resource = getClass().getResource("malformed-line-2.csv");

    // Note the format is CSV
    CsvParser parser = new CsvParser(CsvFormats.CSV);

    MalformedRecordException problem = null;
    try (LineCountingCharStream in = new LineCountingCharStream(new PushbackReader(
        new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8), 1))) {

      int count = 0;
      for (CsvRecord r = parser.parseRecord(in); r != null; r = parser.parseRecord(in)) {
        count = count + 1;
      }
    } catch (MalformedRecordException e) {
      problem = e;
    }

    assertThat(problem.getLinenum(), is(1));
  }
}
