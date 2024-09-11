# CSV4J [![tests](https://github.com/sigpwned/csv4j/actions/workflows/tests.yml/badge.svg)](https://github.com/sigpwned/csv4j/actions/workflows/tests.yml) [![Maven Central Version](https://img.shields.io/maven-central/v/com.sigpwned/csv4j)](https://search.maven.org/artifact/com.sigpwned/csv4j)

CSV4J is a simple CSV reader and writer for Java 8 or later.

## Goals

* Provide CSV reading and writing functionality compatible with both de-facto and official standards
* Expose low-level encoding data
* Simple, compact API

## Non-Goals

* Object mapping
* Header handling

## Why Yet Another CSV Library?

There are already many good libraries with CSV support available: [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/), [Super CSV](http://super-csv.github.io/super-csv/), [Opencsv](https://opencsv.sourceforge.net/), [Jackson](https://github.com/FasterXML/jackson-dataformats-text/tree/master/csv), etc. So why publish another?

In my experience, most of these libraries either:

1. Have long-standing bugs
2. Are complex to use
3. Do too much
4. Do not give visibility into low-level encoding data, particularly whether individual fields are quoted
5. Do not handle [byte order marks](https://en.wikipedia.org/wiki/Byte_order_mark) (BOMs)

In response, this library is designed to be:

1. Correct
2. Simple
3. Focused
4. Transparent
5. BOM-aware

## Code Examples

To read data in the standard CSV format, use:

    try (CsvReader rows=new CsvReader(openReader())) {
        for(CsvRecord row=rows.readNext();row!=null;row=rows.readNext()) {
            // Do something here
        }
    }

To read data in the standard CSV format while respecting BOMs -- for example, to read CSV files exported from Excel -- use:

    try (CsvReader rows=new CsvReader(Boms.decodeFromBom(openInputStream(), StandardCharsets.UTF_8))) {
        for(CsvRecord row=rows.readNext();row!=null;row=rows.readNext()) {
            // Do something here
        }
    }

To write data in the standard CSV format, use:

    try (CsvWriter rows=new CsvWriter(openWriter())) {
        rows.writeNext(CsvRecord.of(
            CsvField.of(true, "Hello"),
            CsvField.of(true, "World")));
        rows.writeNext(CsvRecord.of(
            CsvField.of(false, "Foo"),
            CsvField.of(false, "Bar")));
    }

The `CsvReader` also has an `iterator` capability:

    try (CsvReader rows=new CsvReader(openReader())) {
        for(CsvRecord row : rows) {
            // Do something here
        }
    }

The `CsvReader` also has a `stream` capability:

    try (CsvReader rows=new CsvReader(openReader())) {
        rows.stream.forEach(r -> {
            // Do something here
        });
    }

## Related projects

The csv4j library has no dependencies. However, these libraries may be useful when processing CSV data.

### chardet4j

Users may find [chardet4j](https://github.com/sigpwned/chardet4j) useful for decoding byte streams into character streams when character encodings are not known ahead of time, for example with user input:

    try (CsvReader rows=new CsvReader(Chardet.decode(openInputStream(), StandardCharsets.UTF_8))) {
        // Process rows here like normal...
    }
    
This code considers BOMs and performs a much smarter, more thorough byte frequency analysis to detect charsets.