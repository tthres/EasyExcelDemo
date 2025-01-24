# OpenXLSX

OpenXLSX is a C++ library for reading, writing, creating and modifying
Microsoft Excel® files, with the .xlsx format.

## (aral-matrix) 25 October 2024 - Added default values to a newly created XLCellFormat (from XLCellFormats::create, when no template is provided)

These missing defaults could lead to followup errors when any style index of this cell was later assumed valid to access said style by index (Exception if the index was not in a valid range). All style indexes available in a cell format are now zero-initialized (with no assumptions what the style with index 0 may be configured as, normally it's defaults - if you want to be sure, provide a cell with a known format as copyFrom template to XLCellFormats::create).

Change history is found in the [detailed change log](#detailed-change-log).

## (aral-matrix) 29 September 2024 - Support for styles, merging cells (and more)

Today the features from the development branch finally made it into the main branch :) For details, please refer to the [detailed change log](#detailed-change-log) below.

In summary:
* ```OpenXLSX/headers/XLStyles.hpp```: XLStyles class (and lots of subclasses) has been added, providing nearly complete access to all Excel formatting capabilities.
* ```OpenXLSX/headers/XLMergeCells.hpp``` and ```XLSheet.hpp```: XLMergeCells class is made accessible through XLWorksheet in order to create / delete cell merges
* ```Examples/Demo10.cpp``` demonstrates how styles and merges are used **Note:** The section that is disabled with ```testBasics = false``` *will* break the resulting Excel Spreadsheet if enabled, the only purpose is to demonstrate access to *all* new classes and methods. If you want to use them, make sure to use them correctly

*Note on XLNumberFormat(s)*: Contrary to all other XLStyles elements, these do not use an index within the XML as the referrable ID (XLCellFormat::setNumberFormatId), but instead a user-defined ID that can be set via XLNumberFormat::setNumberFormatId - and for an XLCellFormat, can be set to either a self-defined number format ID, or to a [format predefined by MS](https://learn.microsoft.com/en-us/dotnet/api/documentformat.openxml.spreadsheet.numberingformat?view=openxml-3.0.1). Generally, for custom formats, it is recommended to use IDs > 100.


*On the to-do list:*
* support predefined number formats with constants that have meaningful names
* support for XML comments
* (TBD) support for Rich Text elements in cell strings

## (aral-matrix) 04 September 2024 - new development branch

A couple of days ago I finally had the time to learn enough git functionality to be able to work with branches. So I created a development branch with the newest features that I have mentioned in some pull requests / issues. Feel free to [have a look](https://github.com/troldal/OpenXLSX/tree/development-aral). This way you don't have to wait until the main repository is updated.

## (aral-matrix) 19/20 August 2024 updated pull requests

Closed a multitude of pull requests that had been implemented in the May 2024 Update, implemented two more editorials from PR #246 and #253.

## May 2024 Update

After a long period of inactivity, I have decided to resume development of OpenXLSX. The code has been cleaned up and a significant number of bugs have been fixed. The library has been tested on Windows, macOS and Linux, and should work on all platforms. 

I would like to extend my sincere thanks to all the people who have contributed to the project, either by reporting bugs, suggesting features or by submitting pull requests. I would also like to thank all the people who have starred the project, and who have shown interest in the project.

In particular, I would like to thank Lars Uffmann (https://codeberg.org/lars_uffmann/) for his contributions to the project. Lars has spent significant time and effort in cleaning up the code, fixing bugs and implementing new features. Without his help, the project would not have been in the state it is today.

### 20/05/2024
* Google Benchmark is no longer included in the repository. To run the benchmarks, Google Benchmark have to be installed on the system. If you use vcpkg, it will be downloaded automatically.
* Minor performance improvements to XLCellReference.

### 06/08/2024
* Housekeeping updates to the code.
* Added Makefile.GNU for building on Linux. The project will continue to be based on CMake, and the makefile may not always be kept up-to-date.

## Table of Contents

- [Motivation](#motivation)
- [Ambition](#ambition)
- [Compatibility](#compatibility)
- [Build Instructions](#build-instructions)
- [Current Status](#current-status)
- [Performance](#performance)
- [Caveats](#caveats)
  - [File Size](#file-size)
  - [Memory Usage](#memory-usage)
  - [Unicode](#unicode)
- [Reference Guide](#reference-guide)
- [Example Programs](#example-programs)
- [Changes](#changes)

## Motivation

Many programming languages have the ability to modify Excel files,
either natively or in the form of open source libraries. This includes
Python, Java and C#. For C++, however, things are more scattered. While
there are some libraries, they are generally less mature and have a
smaller feature set than for other languages.

Because there are no open source library that fully fitted my needs, I
decided to develop the OpenXLSX library.

## Ambition

The ambition is that OpenXLSX should be able to read, write, create and
modify Excel files (data as well as formatting), and do so with as few
dependencies as possible. Currently, OpenXLSX depends on the following
3rd party libraries:

- PugiXML
- Zippy (C++ wrapper around miniz)
- Boost.Nowide (for opening files with non-ASCII names on Windows)

These libraries are all header-only and included in the repository, i.e. it's not necessary to download and build 
separately.

Also, focus has been put on **speed**, not memory usage (although there are options for reducing the memory usage, 
at the cost of speed; more on that later).

## Compatibility

OpenXLSX has been tested on the following platforms/compilers. Note that
a '-' doesn't mean that OpenXLSX doesn't work; it just means that it
hasn't been tested:

|         | GCC   | Clang | MSVC |
|:--------|:------|:------|:-----|
| Windows | MinGW | MinGW | +    |
| Cygwin  | -     | -     | N/A  |
| MacOS   | +     | +     | N/A  |
| Linux   | +     | +     | N/A  |

The following compiler versions should be able to compile OpenXLSX
without errors:

- GCC: Version 7
- Clang: Version 8
- MSVC: Visual Studio 2019

Clang 7 should be able to compile OpenXLSX, but apparently there is a
bug in the implementation of std::variant, which causes compiler errors.

Visual Studio 2017 should also work, but hasn't been tested.

## Build Instructions
OpenXLSX uses CMake as the build system (or build system generator, to be exact). Therefore, you must install CMake first, in order to build OpenXLSX. You can find installation instructions on www.cmake.org.

The OpenXLSX library is located in the OpenXLSX subdirectory to this repo. The OpenXLSX subdirectory is a 
self-contained CMake project; if you use CMake for your own project, you can add the OpenXLSX folder as a subdirectory 
to your own project. Alternatively, you can use CMake to generate make files or project files for a toolchain of your choice. Both methods are described in the following.

### Integrating into a CMake project structure

By far the easiest way to use OpenXLSX in your own project, is to use CMake yourself, and then add the OpenXLSX 
folder as a subdirectory to the source tree of your own project. Several IDE's support CMake projects, most notably 
Visual Studio 2019, JetBrains CLion, and Qt Creator. If using Visual Studio, you have to specifically select 'CMake project' when creating a new project.

The main benefit of including the OpenXLSX library as a source subfolder, is that there is no need to locate the 
library and header files specifically; CMake will take care of that for you. Also, the library will be build using 
the same configuration (Debug, Release etc.) as your project. In particular, this a benefit on Windows, where is it 
not possible to use Release libraries in a Debug project (and vice versa) when STL objects are being passed through 
the library interface, as they are in OpenXLSX. When including the OpenXLSX source, this will not be a problem.

By using the `add_subdirectory()` command in the CMakeLists.txt file for your project, you can get access to the 
headers and library files of OpenXLSX. OpenXLSX can generate either a shared library or a static library. By default 
it will produce a shared library, but you can change that in the OpenXLSX CMakeLists.txt file. The library is 
located in a namespace called OpenXLSX; hence the full name of the library is `OpenXLSX::OpenXLSX`.

The following snippet is a minimum CMakeLists.txt file for your own project, that includes OpenXLSX as a subdirectory. Note that the output location of the binaries are set to a common directory. On Linux and MacOS, this is not really required, but on Windows, this will make your life easier, as you would otherwise have to copy the OpenXLSX shared library file to the location of your executable in order to run.

```cmake
cmake_minimum_required(VERSION 3.15)
project(MyProject)

set(CMAKE_CXX_STANDARD 17)

# Set the build output location to a common directory
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/output)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/output)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/output)

add_subdirectory(OpenXLSX)

add_executable(MyProject main.cpp)
target_link_libraries(MyProject OpenXLSX::OpenXLSX)
```

Using the above, you should be able to compile and run the following code, which will generate a new Excel file 
named 'Spreadsheet.xlsx':

```cpp
#include <OpenXLSX.hpp>

using namespace OpenXLSX;

int main() {

    XLDocument doc;
    doc.create("Spreadsheet.xlsx");
    auto wks = doc.workbook().worksheet("Sheet1");

    wks.cell("A1").value() = "Hello, OpenXLSX!";

    doc.save();

    return 0;
}
```

### Building as a separate library

If you wish to produce the OpenXLSX binaries and include them in your project yourself, it can be done using CMake and a compiler toolchain of your choice.

From the command line, navigate the OpenXLSX subdirectory of the project root, and execute the following 
commands:

```
mkdir build
cd build
cmake ..
```

The last command will configure the project. This will configure the project using the default toolchain. If you 
want to specify the toolchain, type `cmake -G "<toolchain>" ..` with `<toolchain>` being the toolchain you wish to 
use, for example "Unix Makefiles", "Ninja", "Xcode", or "Visual Studio 16 2019". See the CMake documentation for 
details.

Finally, you can build the library using the command:

```
cmake --build . --target OpenXLSX --config Release
```

You can change the `--target` and `--config` arguments to whatever you wish to use.

When built, you can install it using the following command:

```
cmake --install .
```

This command will install the library and header files to the default location on your platform (usually /usr/local/ 
on Linux and MacOS, and C:\Program Files on Windows). You can set a different location using the --prefix argument. 

Note that depending on the platform, it may not be possible to install both debug and release libraries. On Linux 
and MacOS, this is not a big issue, as release libraries can be used for both debug and release executables. Not so 
for Windows, where the configuration of the library must be the same as for the executable linking to it. For that 
reason, on Windows, it is much easier to just include the OpenXLSX source folder as a subdirectory to your CMake 
project; it will save you a lot of headaches.

## Current Status

OpenXLSX is still work in progress. The following is a list of features
which have been implemented and should be working properly:

- Create/open/save files
- Read/write/modify cell contents
- Copy cells and cell ranges
- Copy worksheets
- Cell ranges and iterators
- Row ranges and iterators

Features related to formatting, plots and figures have not been
implemented, and are not planned to be in the near future.

It should be noted, that creating const XLDocument objects, is currently
not working!

## Performance

The table below is the output from a benchmark (using the Google
Benchmark library), which shows that read/write access can be done at a
rate of around 4,000,000 cells per second. Floating point numbers are
somewhat lower, due to conversion to/from strings in the .xml file.

```
Run on (16 X 2300 MHz CPU s)
CPU Caches:
  L1 Data 32 KiB (x8)
  L1 Instruction 32 KiB (x8)
  L2 Unified 256 KiB (x8)
  L3 Unified 16384 KiB (x1)
Load Average: 2.46, 2.25, 2.19
---------------------------------------------------------------------------
Benchmark                 Time             CPU   Iterations UserCounters...
---------------------------------------------------------------------------
BM_WriteStrings        2484 ms         2482 ms            1 items=8.38861M items_per_second=3.37956M/s
BM_WriteIntegers       1949 ms         1949 ms            1 items=8.38861M items_per_second=4.30485M/s
BM_WriteFloats         4720 ms         4719 ms            1 items=8.38861M items_per_second=1.77767M/s
BM_WriteBools          2167 ms         2166 ms            1 items=8.38861M items_per_second=3.87247M/s
BM_ReadStrings         1883 ms         1882 ms            1 items=8.38861M items_per_second=4.45776M/s
BM_ReadIntegers        1641 ms         1641 ms            1 items=8.38861M items_per_second=5.11252M/s
BM_ReadFloats          4173 ms         4172 ms            1 items=8.38861M items_per_second=2.01078M/s
BM_ReadBools           1898 ms         1898 ms            1 items=8.38861M items_per_second=4.4205M/s
```

## Caveats

### File Size

An .xlsx file is essentially a bunch of .xml files in a .zip archive.
Internally, OpenXLSX uses the miniz library to compress/decompress the
.zip archive, and it turns out that miniz has an upper limit regarding
the file sizes it can handle.

The maximum allowable file size for a file in an archive (i.e. en entry
in a .zip archive, not the archive itself) is 4 GB (uncompressed).
Usually, the largest file in an .xlsx file/archive, will be the .xml
files holding the worksheet data. I.e., the worksheet data may not
exceed 4 GB. What that translates to in terms of rows and columns depend
a lot on the type of data, but 1,048,576 rows x 128 columns filled with
4-digit integers will take up approx. 4 GB. The size of the compressed
archive also depends on the data held in the worksheet, as well as the
compression algorithm used, but a workbook with a single worksheet of 4
GB will usually have a compressed size of 300-350 MB.

The 4 GB limitation is only related to a single entry in an archive, not
the total archive size. That means that if an archive holds multiple
entries with a size of 4GB, miniz can still handle it. For OpenXLSX,
this means that a workbook with several large worksheets can still be
opened.

### Memory Usage

OpenXLSX uses the PugiXML library for parsing and manipulating .xml
files in .xlsx archive. PugiXML is a DOM parser, which reads the entire
.xml document into memory. That makes parsing and manipulation
incredibly fast.

However, all choices have consequences, and using a DOM parser can also
demand a lot of memory. For small spreadsheets, it shouldn't be a
problem, but if you need to manipulate large spreadsheets, you may need
a lot of memory.

The table below gives an indication of how many columns of data can be
handled by OpenXLSX (assuming 1,048,576 rows):

|           | Columns |
|:----------|:--------|
| 8 GB RAM  | 8-16    |
| 16 GB RAM | 32-64   |
| 32 GB RAM | 128-256 |

Your milage may vary. The performance of OpenXLSX will depend on the
type of data in the spreadsheet.

Note also that it is recommended to use OpenXLSX in 64-bit mode. While
it can easily be used in 32-bit mode, it can only access 4 GB of RAM,
which will severely limit the usefulness when handling large
spreadsheets.

If memory consumption is an issue for you, you can build the OpenXLSX
library in compact mode (look for the ENABLE_COMPACT_MODE in the
CMakeLists.txt file), which will enable PugiXML's compact mode. OpenXLSX
will then use less memory, but also run slower. See further details in
the PugiXML documentation
[here](https://pugixml.org/docs/manual.html#dom.memory.compact). A test
case run on Linux VM with 8 GB RAM revealed that OpenXLSX could handle a
worksheet with 1,048,576 rows x 32 columns in compact mode, versus
1,048,576 rows x 16 columns in default mode.

### Unicode

By far the most questions I get about OpenXLSX on Github, is related to Unicode.
It is apparently (and understandably) a source of great confusion for many people.

Early on, I decided that OpenXLSX should focus on the Excel part, and not be a 
text encoding/conversion utility also. Therefore, **all text input/output to 
OpenXLSX MUST be in UTF-8 encoding**... Otherwise it won't work as expected. 
It may also be necessary that the source code files are saved in UTF-8 format. 
If, for example, a source file is saved in UTF-16 format, then any string literals
will also be in UTF-16. So if you have any hard-coded string literals in your source
code, then the source file **must** also be saved in UTF-8 format.

All string manipulations and usage in OpenXLSX uses the C++ std::string,
which is encoding agnostic, but can easily be used for UTF-8 encoding.
Also, Excel uses UTF-8 encoding internally (actually, it might be
possible to use other encodings, but I'm not sure about that).

For the above reason, **if you work with other text encodings, you have
to convert to/from UTF-8 yourself**. There are a number of options (e.g.
Boost.Nowide or Boost.Text). Internally, OpenXLSX uses Boost.Nowide; it has a number
of handy features for opening files and converting between std::string and std::wstring
etc. I will also suggest that you watch James McNellis' presentation at [CppCon 2014](https://youtu.be/n0GK-9f4dl8),
and read [Joel Spolsky's blog](https://www.joelonsoftware.com/2003/10/08/the-absolute-minimum-every-software-developer-absolutely-positively-must-know-about-unicode-and-character-sets-no-excuses/).

Unicode on Windows is particularly challenging. While UTF-8 is well 
supported on Linux and MacOS, support on Windows is more limited. For example, 
output of non-ASCII characters (e.g. Chinese or Japanese characters) to the 
terminal window will look like gibberish. As mentioned, sometimes you also have to be mindful 
of the text encoding of the source files themselves. Some users have had problems
with OpenXLSX crashing when opening/creating .xlsx files with non-ASCII 
filenames, where it turned out that the ***source code*** for the test program
was in a non-UTF-8 encoding, and hence the input string to OpenXLSX was also
non-UTF-8. To stay sane, I recommend that source code files are always 
in UTF-8 files; all IDE's I know of can handle source code files in UTF-8 
encoding. Welcome to the wonderful world of unicode on Windows 🤮

### Zip Library
An Excel-file is essentially just a bunch of .xml files wrapped in a .zip archive. OpenXLSX uses a 3rd party library to extract the .xml files from the .zip archive. The default library used by OpenXLSX is Zippy, which is an object-oriented wrapper around miniz. The miniz library is fast, and is header-only, which is ideal for OpenXLSX. 

However, it is possible to use a different zip-library, if you want to. In rare cases, you may experience stability issues with miniz. In those cases, it may be useful to try a different zip-library.

Using the Zippy/miniz library requires no special efforts; it will work straight out of the box. Using a different zip-library, however, will require some work. 

In order to use a different zip-library, you must create a wrapper class that conforms to the interface specified by the IZipArchive class. Note that this is implemented using *type erasure*, meaning that no inheritance is required; the class just needs to have a conforming interface, thats all. After that, provide an object of the class and supply it to the OpenXLSX constructor.

To see an example of how this is done, take a look at Demo1A in the Examples folder. This example uses a class called CustomZip (using libzip as the zip library) which can be found under Examples/external/CustomZip. In order to build the example program, make sure that libzip (and it's dependencies) is installed on your computer, and enable the OPENXLSX_ENABLE_LIBZIP option in the CMakeLists.txt file in the OpenXLSX root.

As mentioned, the Demo1A example program uses libzip. libzip is a very stable library and widely used. However, my experience is that it is quite slow for large zip files, such as large spreadsheets. For that reason, libzip may not be the ideal solution, but it is useful for showing how a different zip library can be used.

## Example Programs

In the 'Examples' folder, you will find several example programs, that illustrates how to use OpenXLSX. Studying 
those example programs is the best way to learn how to use OpenXLSX. The example programs are annotated, so it 
should be relatively easy to understand what's going on.

## Changes

### New in version 0.4.x
OpenXLSX can now use other zip libraries than the default Zippy/miniz library. See Demo1A as an example of how it's done

### New in version 0.3.x
This version includes row ranges and iterators. It also support assignment of containers of cell values to XLRow 
objects. This is significantly faster (up to x2) than using cell ranges or accessing cells by cell references.

### New in version 0.2.x

The internal architecture of OpenXLSX has been significantly re-designed
since the previous version. The reason is that the library was turning
into a big ball of mud, and it was increasingly difficult to add
features and fix bugs. With the new architecture, it will (hopefully) be
easier to manage and to add new features.

Due to the re-design of the architecture, there are a few changes to the
public interface. These changes, however, are not significant, and it
should be easy to update:

* All internal objects are now handled as values rather than pointers,
  similar to the approach taken in the underlying PugiXML library. This
  means that when requesting a certain worksheet from a workbook, the
  resulting worksheet is not returned as a pointer, but as an object
  that supports both copying and moving.
* The distinction between interface objects and implementation objects
  are now gone, as it made it very difficult to manage changes. It was
  an attempt to implement the pimpl idiom, but it wasn't very effective.
  In the future, I may try to implement pimpl again, but only if it can
  be done in a simpler way.
* All member functions have been renamed to begin with a small letter
  (camelCase), i.e. the member function WorksheetCount() is renamed to
  worksheetCount(). This was done mostly for cosmetic reasons.

I realise that these changes may cause problems for some users. Because
of that, the previous version of OpenXLSX can be found in the "legacy"
branch of this repository. However, I strongly recommend that you
transition to the new version instead.

<h2 id="detailed-change-log">Detailed change log</h2>

### (aral-matrix) 14 October 2024 - added basic support to suppress harmless warnings (e.g. about XLSX unsupported features)
* ```XLDocument.hpp```: added ```showWarnings()``` (default setting) and ```suppressWarnings()```
* ```XLStyles.hpp```: added ```suppressWarnings``` parameter to constructor (default: ```false```)
* ```XLDocument::open```: if ```suppressWarnings()``` was called, suppress warnings about ignored comment xml files and unhandled workbook items
* ```XLDocument::open```: ```m_suppressWarnings``` setting is forwarded to XLStyles constructor

### (aral-matrix) 02 October 2024 - Issue #278 - missing include statement in XLException
* ```XLException.hpp```: added missing ```#include <string>```

### (aral-matrix) 30 September 2024 - Pull request #185 - create workbook relationship in _rels/.rels if missing
* ```XLDocument::open``` will create a missing workbook relationship in ```_rels/.rels``` if, and only if, a workbook with the default path xl/workbook.xml exists in the archive

### (aral-matrix) 29 September 2024 - prepare merge into main repository
* modified Demo9 to match style / behavior of other Demo programs
* re-created legacy ```XLDocument::create``` and ```XLDocument::saveAs``` function interfaces, but marked them as ```[[deprecated]]```. The new interfaces require explicit specification of ```XLForceOverwrite``` or ```XLDoNotOverwrite```. Once the deprecated function definitions can be removed, ```XLDoNotOverwrite``` could become the new default behavior
* moved nowide back into the correct location ```OpenXLSX/external/nowide/nowide```
* removed ```Demo0``` / kept it in the Makefile as a test vehicle
* updated ```Notes``` folder
* created ```Scripts``` folder with ```xml-format.sh``` (XML linting, useful to analyze XLSX zip contents in a text editor)
* moved ```make-gnu.sh``` to ```Scripts/make-gnu.sh```
* added ```Scripts/cmake-cleanup.sh``` to prepare (not execute!) commands that remove all temporary files generated by cmake
* added ```Scripts/demos-cleanup.sh``` to remove all xlsx files created by the Demos
* support for ```void setSavingDeclaration(XLXmlSavingDeclaration const& savingDeclaration)``` using ```class XLXmlSavingDeclaration``` (defined in ```XLXmlData.hpp```) that can be used to pass a custom XML version, encoding, and standalone property to pugixml

### (aral-matrix) September 2024 - to-do list:
- for more to-do items, refer to ```Notes/todo-list.txt```
- TBD: could XLRowData also benefit from passing through to setDefaultCellAttributes a column styles vector?
- completion of style support as much as is reasonable (not color themes, most likely) per known documentation of xl/styles.xml
- XLAlignmentStyle: check / throw if vertical alignments are used as horizontal and vice versa
- XLStyles ::create functions: implement good default style properties for all styles
- TBD: permit setting a format reference for shared strings

### (aral-matrix) 29 September 2024 (development-aral branch: 06 September) 2024 - enhance XLMergeCells and related XLWorksheet functionality

* ```XLWorksheet``` now allows to access an object managing the worksheet's merged cell ranges
  * ```XLMergeCells XLWorksheet::merges()``` - access the XLMergeCells class for the worksheet directly
  * ```void mergeCells(XLCellRange const& rangeToMerge, bool emptyHiddenCells = false)``` - merge cells defined by rangeToMerge
  * ```void mergeCells(const std::string& rangeReference, bool emptyHiddenCells = false)``` - merge cells defined by rangeReference
  * ```void unmergeCells(XLCellRange const& rangeToUnmerge)``` - unmerge cells defined by rangeToUnmerge
  * ```void unmergeCells(const std::string& rangeReference)``` - unmerge cells defined by rangeReference

* ```XLMergeCells```: added methods
  * ```int32_t XLMergeCells::findMerge(const std::string& reference)``` - find a merge matching reference
  * ```bool mergeExists(const std::string& reference)``` - test if a merge with reference exists
  * ```int32_t XLMergeCells::findMergeByCell(const std::string& cellRef)``` - find a merge containing std::string cellRef
  * ```int32_t XLMergeCells::findMergeByCell(XLCellReference cellRef)``` - find a merge containing XLCellReference cellRef
  * ```size_t count()``` - get count of merges defined in the worksheet
  * ```const char* merge(int32_t index)``` - get merge reference string at index
  * ```const char* operator[](int32_t index)``` - overload of operator[] as a shortcut to ::merge
  * ```int32_t appendMerge(const std::string& reference)``` - define a new merge, invoked by XLWorksheet::mergeCells
  * ```void deleteMerge(int32_t index)``` - delete merge with the given index from the worksheet (= unmerge cells), invoked by XLWorksheet::unmergeCells

* added example usage of this functionality to ```Demo10.cpp```

### (aral-matrix) 29 September 2024 (development-aral branch: 03 September 2024) - ignore worksheet internal subfolders that are not known (e.g. customXml)
* ```XLDocument::open``` will now ignore unknown subfolders (they remain unmodified and unaccessible in the zip in memory and stay in the archive upon saving). This prevents throwing an exception for any XLSX file written by a "creative" application that added items unknown to this library
* made a ```constexpr``` from ```const unsigned int pugi_parse_settings``` and moved it to ```XLDocument.hpp``` so that the const becomes available to XLStyles and XLSharedStrings
* XLStyles and XLSharedStrings now take care of creating a valid default document element, if an archive is missing the corresponding XML file or the document element is not present
* XLStyles constructor now ensures that cellFormats() has at least the default entry with index 0, so that a call to cellFormats().create() will not return this reserved index

### (aral-matrix) 29 September 2024 (development-aral branch: 02 September 2024) - major update with (nearly) complete styles support & cell merging
* implemented support for worksheet styles in ```XLStyles``` class - interface can be found in ```OpenXLSX/headers/XLStyles.hpp```
* support for merged cells
* ```Examples/Demo10.cpp``` demonstrates use of the new XLStyles & cell merging functionality
* plenty of bugfixes
* support for non-creating ```XLCellIterator```: can now iterate over a range and test ```XLCellIterator::cellExists``` before accessing it. This allows to skip non-existing cells without creating them.
* support for ```workbook##.xml```, XML namespaces and random 64bit (relationship) identifiers
  * a non-standard (== not ```xl/workbook.xml```) workbook XML name is now accepted if it is correctly referenced in ```_rels/.rels```
  * XLSX documents that use namespaces throughout their XML files are now supported in the following way:
    * ```bool OpenXLSX::enable_xml_namespaces()``` must be used to enable namespace support *before* opening a document using e.g. a namespace ```x``` (like so: ```<x:row r="1">```) everywhere
    * the document is then opened normally
    * any and all node creations will expose to the user normal node names (as if the namespaces were not in use) and will save/create elements with the correct namespace (of their parent node)
    * if a node is accessed with a namespace, the leading namespace will be stripped and then the node name will be checked based on the parent node's name
      * Example: ```insert_child_before("x:row", curRow )``` in the ```<y:sheetData>``` node will strip the ```x:``` and insert a row element named ```<y:row>```, using the parent node's namespace
    * all XMLNode functions where the namespace could be relevant (see ```OpenXLSX/headers/XLXmlParser.hpp```) take as an optional last argument a ```bool force_ns``` that - if true - will respect a namespace passed into the function for node creation / access. A helper ```const bool OpenXLSX::XLForceNamespace = true``` is available for code readability
      * Example: ```XMLNode::insert_child_before("x:row", curRow, XLForceNamespace)``` in the ```<y:sheetData>``` node like above, will insert a row element named ```x:row```
  * OpenXLSX by default assumes that worksheet relationship IDs are sequential and stored in the form ```id="rId##"``` where ```##``` is the sequence number, and when a new component is added to the workbook, a new relationship ID is determined by taking the highest existing value within the workbook, and adding +1 to it. During investigation of [https://github.com/troldal/OpenXLSX/issues/254] a workbook was found to use (seemingly) random 64 bit integers, stored in the form ```r:id="Rxx"```, with ```xx``` being a hexadecimal representation of a 64 bit integer.
    * ```OpenXLSX::UseRandomIDs()``` must be invoked to switch ID functionality *before* opening such a document
    * the document is then opened normally and new document components will be added with a 64 bit high quality randomizer, which should be reasonably safe to never cause a collision
    * (optional) ```OpenXLSX::UseSequentialIDs()``` can be used to restore default (sequential) relationship ID functionality

**NOTE** on ```enable_xml_namespaces```: Namespace support is transparent and can be used with documents that do not use such namespaces. However, it can have a small (<5%) performance impact on large workbooks and is therefore optional to enable.

**CAUTION** with ```UseRandomIDs```: Please be aware that a mixed mode, where one document is opened with random ID support and another without sequential relationship IDs, is not supported for the time being. If you have to work with documents of different types, you should always configure the desired relationship ID behavior for the next document, work with that, close it, and change the relationship ID configuration before opening the next document.

Please disregard Examples/Demo0.cpp and Examples/Demo9.cpp for now (Demo0 is trivial and was used to test non-standard workbook support, and Demo9 requires a review with better comments). Demo9 showcases how to use the new XLCellAssignable to copy-assign cell contents (like Excel copy & paste).

### (aral-matrix) 04 September 2024 - new features on the development branch:
* ```XLCellIterator``` no longer creates empty cells just for iterating over them, and provides ```::cellExists()``` to test if the currently pointed-to cell is already in the worksheet XML, before accessing it
* ```XLStyles``` support
* ```XLWorksheet``` now provides ```XLMergeCells``` support for merging / unmerging cell ranges
* ```Demo10.cpp``` has a lot of examples on how to use the new XLStyles functionality
* ignore ```customXml``` etc in the xlsx archive
* accept non-standard workbook names (if correctly identified in the relationships)
* support for documents with XML namespaces throughout the worksheets
* support for documents using non-sequential, random relationship IDs

### (aral-matrix) 02 September 2024 - ensure that all worksheets are contained in app.xml <TitlesOfParts> and reflected in <HeadingPairs> value for Worksheets

* Code refactored in ```XLProperties.cpp XLAppProperties``` to create ```<TitlesOfParts>``` and ```<HeadingPairs>``` nodes (and subnodes) if missing, added method ```alignWorksheets```, called by ```XLDocument::open``` to ensure that all worksheets listed in the workbook xml are also accounted for in ```docProps/app.xml```.
* Started to encapsulate whitespace "pretty format" support in a separate function to remove clutter from the functional code -> ```XLProperties.cpp prettyInsertXmlNodeBefore```, maybe this can eventually become a global function, paired with a TBW function ```prettyInsertXmlNodeAfter```, and could be used by all classes, cleaning up the code for whitespace support substantially.
* changed some confusing variable names in ```XLProperties.cpp``` for ```<HeadingPairs>``` subnodes: ```pairCount``` -> ```pairValueParent```, ```pairCountValue``` -> ```pairValue```

### (aral-matrix) 01 September 2024 - ignore phonetic tags consistently in shared strings table

Code refactored in XLDocument::open to read the shared strings table while consistently ignoring phonetic tags, which were previously only ignored if the very first child of an ```<si>``` tag was a phonetic tag. Will now be ignored anywhere before, after or in between text ```<t>``` and rich text ```<r>``` tags.

### (aral-matrix) 31 August 2024 - added fallback solution to get document relationships

Included a "dumb" fallback solution in ```XLRelationships.cpp GetTypeFromString``` to support previously unknown relationship domains, e.g. ```type="http://purl.oclc.org/ooxml/officeDocument/relationships/worksheet"```. Altered behavior will initially test against the hardcoded relationship domains, and if that test fails to identify a relationship type string, the type string will be searched for an occurrence of ```/relationships/``` and if that substring is found, the type detection fallback will try to evaluate the relationship type based on the substring starting with ```/relationships/```, ignoring the domain. For the above example, that would result in a test of ```typeString.substr( comparePos ) == "/relationships/worksheet"```, where comparePos == 41 (the position at which substring ```/relationships/``` begins).

In anticipation of a potential future need for a similar "dumb" fallback solution, repeating hardcoded strings in ```XLContentTypes.cpp GetTypeFromString``` were also replaced with string constants.

Updated .gitignore to a more generic version that excludes everything and explicitly re-includes all desired files.

### (aral-matrix) 18 August 2024 - minor bugfixes and elimination of warnings
* BUGFIX XLRelationships.cpp `BinaryAsHexString`: replaced char array with std::string, as ISO C++ standard does not permit variable size arrays
* BUGFIX XLRelationships.cpp `Rand64`: added explicit type cast, as bitwise shift-left does *not* do integer promotion to long on the left operand
* Makefile.GNU: added warning flags `-Wpedantic -Wextra` and **removed all previously disabled flags after below patches**
* reformatted whitespaces in XLRelationships.cpp to align with project style (`f( param )` -> `f(param)`, `a[ index ]` -> `a[index]` and `if( condition )` -> `if (condition)`)
* Eliminate some warnings:
  * zippy.hpp: assign explicit uint64_t type to a nameless enum to address `warning: enumerated and non-enumerated type in conditional expression [-Wextra]`
  * zippy.hpp: added typecasts to void on function parameters for functions that are not yet implemented, also added explicit exceptions to ensure those functions are never invoked
    * affects:
      * `void ZipArchive::Save(std::ostream& stream)`
      * `void ZipArchive::ExtractDir(const std::string& dir, const std::string& dest)`
      * `void ZipArchive::ExtractAll(const std::string& dest)`
  * XLDocument.cpp: disabled `-Wunused-function` by pragma for functions `fileExists` and `isDirectory`
  * XLRow.hpp & .cpp: changed return type of rowNumber to `uint32_t` (was `uint64_t`). **CAUTION**: there is no validity check on the underlying XML (nor was there ever one in case a value was inconsistent with OpenXLSX::MAX_ROWS)
  * changed some loop variable types, modified range checks and added static_cast instructions to eliminate `-Wsign-compare`
    * affects: XLCellReference.cpp, XLMergeCells.cpp, XLSharedStrings.cpp, Demo9.cpp
  * XLCellIterator.hpp: removed const-ness of return value of `bool endReached()` to eliminate `-Wignored-qualifiers` (ignored because const on trivial return types accomplishes nothing)
  * added utilities/XLUtilities.cpp `OpenXLSX::ignore` template, can be used to suppress `-Wunused-parameter` and `-Wunused-variable` like so: `OpenXLSX::ignore(unusedValue)`
    * now used in: XLComments.cpp, XLStyles.cpp
  * XLXmlParser.cpp XMLNode::namespaced_name_shared_ptr: made no-op lambda parameter unnamed to eliminate `-Wunused-parameter`
  * Demo10.cpp: cast command line parameters to void to suppress -Wunused-parameter
  * wrapped all `#pragma warning` lines in another pragma to disable `-Wunknown-pragmas` (on gcc/g++)
    * wrapper pragma:
      * `#pragma GCC diagnostic push`
      * `#pragma GCC diagnostic ignored "-Wunknown-pragmas" // disable warning about below #pragma warning being unknown`
      * `#   pragma /* offending lines go here */`
      * `#pragma GCC diagnostic pop`
    * affects: 
      * `zippy.hpp`
      * `IZipArchive.hpp`
      * `XLCell.hpp`
      * `XLCellIterator.hpp`
      * `XLCellRange.hpp`
      * `XLCellReference.hpp`
      * `XLCellValue.hpp`
      * `XLColor.hpp`
      * `XLColumn.hpp`
      * `XLCommandQuery.hpp`
      * `XLContentTypes.hpp`
      * `XLDateTime.hpp`
      * `XLDocument.hpp`
      * `XLException.hpp`
      * `XLFormula.hpp`
      * `XLMergeCells.hpp`
      * `XLProperties.hpp`
      * `XLRelationships.hpp`
      * `XLRowData.hpp`
      * `XLRow.hpp`
      * `XLSharedStrings.hpp`
      * `XLSheet.hpp`
      * `XLStyles.hpp`
      * `XLWorkbook.hpp`
      * `XLXmlData.hpp`
      * `XLXmlFile.hpp`
      * `XLZipArchive.hpp`
      * `Examples/Demo5.cpp`

### (aral-matrix) 17 August 2024 - bugfix in XLAppProperties::createFromTemplate
* BUGFIX: TitlesOfParts is now correctly inserted into the `<Properties>` (document) element, was previously wrongly appended to headingPairs
* entries in /xl/ are now explitly checked for known & handled file types (`/xl/worksheets/sheet*`, `/xl/sharedStrings.xml`, `/xl/styles.xml`, `/xl/theme/theme*`) and otherwise ignored. This fixes an issue when the workbook contains `/xl/pivotCache/` and `/xl/pivotTables/` entries until support for those is implemented (if ever ;P)

### (aral-matrix) 14 August 2024 - use row style, or if not set, column style for default cell style
* Newly created cells will now use the style that is set for the row or the column (if set and != XLDefaultCellFormat), with row style taking precedence
  * added XLUtilities.hpp getColumnStyle which retrieves the column style for a given column index from the `<cols>` element obtained via a rowNode parent's parent
  * added XLUtilities.hpp setDefaultCellAttributes, which sets the cell reference and the cell style, taking an optional vector of column styles that will improve performance using a previously extracted set of column styles
  * XLRowData now uses setDefaultCellAttributes after creating a new cell
  * XLUtilities.hpp getCellNode now takes the same optional vector of column styles and passes it on to setDefaultCellAttributes, which is now used after creating a new cell
  * XLCellIterator constructor now requires a pointer to a column styles vector (size can be 0) which is used when new cells need to be created
  * XLCellRange now has a new member `std::vector< XLStyleIndex > m_columnStyles`, and a method fetchColumnStyles that will populate the vector so that it can be passed to the XLCellIterator constructor
    * if used, the optimization function has to be called *after* all column styles for the range are configured - columns configured afterwards will result in cells not getting the format setting
    * if the function is not used, each cell creation will result in a call to getColumnStyle, which has a small performance impact (<5%) for few columns, but is likely to be more impactful with the amount of formatted columns increasing
* the remaining XLSheet cell() functions (taking an XLCellReference or a rowNumber / columnNumber) have been modified to return an XLCellAssignable
  * unchanged: XLCellAssignable XLWorksheet::cell(const std::string& ref) const
  * return type changed: XLCellAssignable cell(const XLCellReference& ref) const
  * return type changed: XLCellAssignable cell(uint32_t rowNumber, uint16_t columnNumber) const;
  * the XLCellAssignable is now constructed in the latter method, and the other two pass through the return value of this latter method
  
### (aral-matrix) 11 August 2024 - support for non-creating XLCellIterators, iterator performance patch, bugfix
* XLCellIterators can now be used to iterate over a range and *test* `XLCellIterator::cellExists()` without creating the XML for the cell.
* cell (and row) XML will now only be created when an XLCellIterator is dereferenced
* Performance improvement: Execution time on Demo5 is down from (on my test system) 86 seconds to 75 seconds (-12.8%)
* XLCellIterator bugfix since last commit: m_hintCell (now m_hintNode) was being initialized to other.m_currentCell (should have been other.m_hintCell) in copy constructor and copy assignment operator

### (aral-matrix) 08 August 2024 - support for XLWorksheet::mergeCells and ::unmergeCells
* support for XLWorksheet::mergeCells and unmergeCells (with XLCellRange or std::string range reference parameter, option to clear cell contents)
  * added XLWorksheet new method ::mergeCells with parameter emptyHiddenCells (and convenience const bool XLEmptyHiddenCells = true)
  * added XLWorksheet new method ::unmergeCells
  * added XLCell::clear method with bitwise-OR combineable flags: XLKeepCellStyle, XLKeepCellType, XLKeepCellValue, XLKeepCellFormula 
  * added new module XLMergeCells.hpp / XLMergeCells.cpp
* XLCellRange constructor: added XLInputError exception when range is constructed with a topLeft cell that is to the right or lower than bottomRight cell
* Demo10: included a demonstration of mergeCells

### (aral-matrix) 01 August 2024 - xl/styles.xml - support for XLFill patternFill all pattern types, XLFonts scheme & vertAlign, all XLAlignmentStyle values
* patternFill now supports all patternType values per standard
* XLFonts now support scheme major/minor/none (`<font><scheme val="major"/></font>`)
* XLFonts now support vertical align run style (`<font><vertAlign val="subscript"/></font>`)
* XLAlignmentStyle can now be used with all defined horizontal and vertical alignment styles
* Examples/Demo10.cpp has been updated to test some(!) of the new formatting elements

### (aral-matrix) 31 July 2024 - xl/styles.xml - support for fill::gradientFill and XLDataBarColor
* gradientFill elements within `<fills><fill><gradientFill>...</gradientFill></fill>...</fills>` are now supported
* along with that come a few new classes: XLGradientStops, XLGradientStop, XLDataBarColor
* XLLine color properties are now controlled via the XLDataBarColor as well

### (aral-matrix) 29 July 2024 - support for workbook##.xml and XML namespaces
* it appears that a workbook does not always have to be at xl/workbook.xml
  * --> the workbook path is now read from the document relationships, if it has an entry for "officeDocument"
* also, XML namespaces can be used for node names throughout (almost) all XML files as long as they are defined in the document root(?) node with xmlns:<namespace> - e.g. xmlns:x
  * added functions enable_xml_namespaces and disable_xml_namespaces
  * when enable_xml_namespaces() was invoked, XLXmlParser XMLNode now wraps all pugi::xml_node methods so that a node referred to by a name will be found regardless of namespace, and children will be created automatically inheriting the node's own namespace

### (aral-matrix) 21 July 2024 - xl/styles.xml - support for cell formatting in work
Please refer to Demo10 and XLStyles.hpp on how to set cell formatting.
In short:
* size_t XLCell::cellFormat() and bool XLCell::setCellFormat(size_t cellFormatIndex)
* these functions get/set an index to a format defined in xl/styles.xml <styleSheet>:<cellXfs>
* <cellXfs> refers to 
  * numFmtId -> XLStyles::numberFormats().numberFormatById( uint32_t numberFormatId ) // where numberFormatId is the numFmtId attribute of the numFmt entry
  * fontId -> XLStyles::fonts().fontByIndex( fontId )
  * fillId -> XLStyles::fills().fillByIndex( fillId )
  * borderId -> XLStyles::borders().borderByIndex( borderId )
  * xfId -> XLStyles::cellStyleFormats( xfId ) // xfId appears to refer to entries in the <cellStyleXfs> array - untested currently
* it is not yet clear what the purpose of the <cellStyles> array is
* all array getter functions (numberFormats, fonts, fills, borders, cellStyleFormats, cellFormats, cellStyles) support the operator[] to access an object by index
* all objects provide getter and setter functions for all supported attributes. Especially in the color domain, there's probably quite a bit of support missing at this stage
* color support: only rgb via XLColor (XLColor::hex()) is supported at this stage. In particular, no color themes are supported

## Credits

- Thanks to [@zeux](https://github.com/zeux) for providing the awesome
  PugiXML library.
- Thanks to [@richgel999](https://github.com/richgel999) for providing
  the miniz library.
- Thanks to [@tectu](https://github.com/Tectu) for cleaning up my
  CMakeLists.txt files.
