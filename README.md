<p align="right">
English description | <a href="README_RU.md">Описание на русском</a>
</p>

# Swing-back
[![Build Status](https://travis-ci.org/sbtqa/swing-back.svg?branch=master)](https://travis-ci.org/sbtqa/swing-back) [![GitHub release](https://img.shields.io/github/release/sbtqa/swing-back.svg?style=flat-square)](https://github.com/sbtqa/swing-back/releases) [![Maven Central](https://img.shields.io/maven-central/v/ru.sbtqa.tag/swing-back.svg)](https://mvnrepository.com/artifact/ru.sbtqa.tag/swing-back)

Swing-back it's a framework for automated testing swing-application using a PageObject pattern.

## Documentation
### Test example

There is example of using the swing-back with comments [here](https://github.com/sbtqa/swingback-test)

### Loading and starting test application

There are several ways to load required application for testing via the swing-back.

Swing-back supports a loading from file system. This requires add a few properties into *application.properties* file witch are stored
in *classpath/config*.

* swingback.app.startclass = the full name of the main class your application **(mandatory)**
* swingback.forms.package = full package path to package with forms
* swingback.jvm.prop.systemProperty1 = value1 **(optional)**

  swingback.jvm.prop.systemProperty2 = value2

  …

  swingback.jvm.prop.systemPropertyN = valueN


In this instance systemProperty1, systemProperty2, … systemPropertyN will be set in the jvm context in which the application will be ran.


With these parameters the required jars your application must be stored in a folder with path *classpath/app/jars*.
All required resources for the test application must be stored in a *classpath/app/resources* folder.

If you want to specify a custom location with jars you should to add more parameters:

* swingback.app.jars.path.abs = the absolute path to jars folder
* swingback.app.resources.path.abs = the absolute path to resources folder

In this case swing-back will search the jars and resources in the folder specified folders.

### Searching element on form

For finding form element use choosers.
There are some choosers in priority order for easy to use.

* ComponentChooserByName (determine element by name swing component.)

* ComponentChooserByToolTip (determine element by tool tip swing component.)

* ComponentChooserByText (determine element by text swing component.)

* ComponentChooserByIndex (determine element by index among other components with the same type).

* ComponentChooserByCoordinates (determine element by coordinates swing components.)

You can write yourself chooser by analogy.

## Contacts
If you found error or you have a question? [Check](https://github.com/sbtqa/swing-back/issues), maybe someone asked before, not found? Just [create a new issue](https://github.com/sbtqa/swing-back/issues/new)!

## License 
Page-Factory is released under the Apache 2.0. [Details here](https://github.com/sbtqa/swing-back/blob/master/LICENSE).