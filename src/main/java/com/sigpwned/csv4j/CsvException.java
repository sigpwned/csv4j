package com.sigpwned.csv4j;

import java.io.IOException;

@SuppressWarnings("serial")
public abstract class CsvException extends IOException {
  public CsvException(String message) {
    super(message);
  }

  public CsvException(String message, Throwable cause) {
    super(message, cause);
  }

  public CsvException(Throwable cause) {
    super(cause);
  }
}
