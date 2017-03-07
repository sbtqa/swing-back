# Library for testing swing-application

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

### Test example

There is example of using the swing-back with comments.

Look at
