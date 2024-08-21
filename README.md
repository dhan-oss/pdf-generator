## PDF Generator


[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](https://github.com/itext/itext7/blob/master/LICENSE.md)

This library uses [itext-java](https://github.com/itext/itextpdf) which is a high-performance, battle-tested library that allows you to create, adapt, inspect and maintain PDF documents, allowing you to add PDF functionality to your software projects with ease.

### Create customised PDF documents
* With this PDF generator, using acrofields in any template, you can generate PDF documents in bulk with custom input in each.

### How to Use
This is a springboot application. Once running as an application, http call needs to be made for the same.

* Download the zip or clone the Git repository.
* Unzip the zip file (if you downloaded one)
* Open Command Prompt and Change directory (cd) to folder containing pom.xml
* Open Eclipse
  * File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
  * Select the right project
* Choose the Spring Boot Application file (search for @SpringBootApplication)
* Right Click on the file and Run as Java Application

#### HTTP Call
```api
curl --location 'http://localhost:8080/generatePdf' \
--header 'Content-Type: application/json' \
--data '{}'
```

```JSON
{
    "request_type": "PDF_LOCAL",
    "request_body": {
        "SourcePath": "<base path>/src/main/templates/source/test.pdf",
        "DestinationPath": "<base path>/src/main/templates/destination/test.pdf",
        "<acrofield_checkbox_1>": {
            "AcroFieldType": "check",
            "AcroFieldValue": "Yes",
            "AcroFieldImagePath": ""
        },
        "<acrofield_textbox_1>": {
            "AcroFieldType": "text",
            "AcroFieldValue": "ABCDE12345",
            "AcroFieldImagePath": ""
        },
        "<acrofield_image_1>": {
            "AcroFieldType": "image",
            "AcroFieldValue": "",
            "AcroFieldImagePath": "<base path>/src/main/templates/source/<image_file_name>.jpeg"
        }
    }
}
```
Over here, `request_type` can be `PDF_LOCAL` or `PDF_REMOTE` depending on path of source and destination files.
Accordingly, values of `SourcePath` and `DestinationPath` would change.

### The key features of iText Core/Community are:

* Core library:
    * PDF creation with the use of our layout engine
    * PDF manipulation, e.g. merging multiple PDFs into one, adding new content, ...
    * PDF digital signing
    * PDF form creation and manipulation
    * Working with PDF/A documents
    * Working with PDF/UA documents
    * FIPS-compliant cryptography
    * Barcode generation
    * SVG support

This is Open-Sourced under AGPL License.