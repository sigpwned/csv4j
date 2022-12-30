# CSV4J [![integration](https://github.com/sigpwned/csv4j/actions/workflows/integration.yml/badge.svg)](https://github.com/sigpwned/csv4j/actions/workflows/integration.yml) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_csv4j&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_csv4j) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_csv4j&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_csv4j) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=sigpwned_csv4j&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=sigpwned_csv4j)

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

In my experience, most of these libraries either (a) have long-standing bugs, (b) are complex to use, (c) do too much, or (d) do not give visibility into low-level encoding data, particularly whether individual fields are quoted. This library is intended to be (a) correct, (b) simple, (c) focused, and (d) provide perfect visibility into low-level encoding.

## Code Examples

To read data in the standard CSV format, use:

    try (CsvReader rows=new CsvReader(openReader())) {
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

Users may find [chardet4j](https://github.com/sigpwned/chardet4j) useful for decoding byte streams into character streams when character encodings are not known ahead of time, for example with user input:

    try (CsvReader rows=new CsvReader(Chardet.decode(openInputStream(), StandardCharsets.UTF_8))) {
        // Process rows here like normal...
    }