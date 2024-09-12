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
