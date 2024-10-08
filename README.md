# APIDiff

APIDiff is a utility to compare two or more versions of an API, each as
defined by a series of options similar to those supported by `javac`.

## Building `apidiff`

`apidiff` uses the following dependencies:

* _[Daisy Diff]_: an HTML comparison library, required when building `apidiff`
* _[Java Diff Utils]_: a plain-text comparison library, required when building `apidiff`
* _[JUnit]_: the testing framework, used to run some of the tests for `apidiff`

Suitable versions of these dependencies can be downloaded by running
`make/build.sh`.

### Building with GNU Make

The default configuration uses values provided in `make/dependencies.gmk`,
unless these values have been overridden on the command line used to run `make`.

````
    make -C make Makefile <target>
````

### Building with an IDE

An IDE such as IntelliJ IDEA needs the following configuration:

* Sources Root: `src`
* JUnit Test Root: `test/junit`
* Libraries:
  * _Daisy Diff_, _HTMLCleaner_, _Java Diff Utils_ available for compilation
  * _JUnit_ available for testing

In addition, some JUnit tests require access to internal classes in
the `jdk.compiler` and `jdk.jdeps` modules:

````
--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
--add-exports jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED
--add-modules jdk.jdeps
--add-exports jdk.jdeps/com.sun.tools.classfile=ALL-UNNAMED
````

The following compiler options may also be desirable:

````
-Xdoclint:missing/protected
-Xlint:unchecked
````

_Note:_ When working on the test files, be careful not to mark module source directories
as `Test Sources Root` at the same time as any source directories that are not 
for a module.

## Documentation

A "man" page is generated as part of the build, and is the primary source
of information for how to run `apidiff`.

## ShowDocs

`showDocs` is a small utility program that is a simple wrapper around the 
`APIReader` and `SerializedFormReader` classes in `apidiff`, that can provide
filtered views of the files generated by `javadoc`, in order to view the
parts of those files that will be compared by `apidiff`.


[Daisy Diff]: https://github.com/DaisyDiff/DaisyDiff
[Java Diff Utils]: https://github.com/java-diff-utils/java-diff-utils
[JUnit]: https://junit.org/
