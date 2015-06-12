# This has been moved to https://github.com/markuskhouri/tinywebdb4j since Google is shutting down the 'Google Code Project' site. #

# Summary #
This is a Java implementation of the server-side service that interacts with the [App Inventor for Android](http://appinventor.googlelabs.com/about/)'s [TinyWebDB](http://appinventor.googlelabs.com/learn/reference/components/notready.html#TinyWebDB) component.  Using this [servlet](http://code.google.com/p/tinywebdb4j/source/browse/trunk/TinyWebDbServer/src/com/example/android/TinyWebDbServerServlet.java), a Java developer can create their own TinyWebDB Service, similar to http://appinvtinywebdb.appspot.com/ .

The project is configured to be used with [Google App's Engine](http://code.google.com/appengine/) directly.  That said, the sevlet code can be used on any Java Server, e.g. Tomcat, as there is nothing specific to GAE in the code.

## Details ##
Notes:
  1. The methods `getValueAssociatedWithTag` and `storeValueAssociatedWithTag` is where you put your application's logic.
  1. The code is generic, meaning that it can simply be copied into any Java application server.
    1. The code is currently a straight Java Servlet.
    1. SpringMVC and other implementation may be added in the future.
  1. The [Gson library](http://code.google.com/p/google-gson/) is used to convert Java Objects into its JSON representation.
  1. The code is developed in [Eclipse](http://eclipse.org/), though should be portable to any IDE, with the appropriate changes.  The following Eclipse plugins are used:
    1. [Google Plugin for Eclipse](http://code.google.com/eclipse/)
    1. [Subclipse](http://subclipse.tigris.org/) to connect Eclipse with this Subversion repository
  1. To ease introduction, the project is configured to work with the latest version of [Google Apps Engine](http://code.google.com/appengine/).
    1. When configuring the Google Apps Engine locally, add the following parameter in the Run/Debug Configuration within Eclipse for the Arguments tab, Program Arguments text block to allow reference to the server by a device other than localhost (for details, see the 'address' switch at [Java version](http://code.google.com/appengine/docs/java/tools/devserver.html#Command_Line_Arguments) [Phyton version](http://code.google.com/appengine/docs/python/tools/devserver.html#Command_Line_Arguments)) : ` -a 0.0.0.0 `. This should be placed just after the default entry of ` --port=8888 `.

## Sidebar - 'Voting' component ##

Information about using this code in conjunction with the 'Voting' AI component is detailed [here](https://code.google.com/p/tinywebdb4j/wiki/AppsInventorVotingComponent).

## Example Usage ##

Below are screenshots of a very simple App Inventor app that uses tinywebdb4j.  The first image shows the screen.  The second image shows the Blocks Editor for the retrieval.  The third image shows the Blocks Editor for the storage.  The forth image shows one way to work with a returned list of values that uses the pipe character ("|") as the delimiter; this shows obtaining the first value.

**Change Service URL**  Please change the Service URL, i.e. what is shown as http://192.168.2.5:8888 in the screenshots, to your own Service URL.  (The Service URL is nothing more than where you have the [TinyWebDbServerServlet](http://code.google.com/p/tinywebdb4j/source/browse/trunk/TinyWebDbServer/src/com/example/android/TinyWebDbServerServlet.java) running.)

  1. ![http://tinywebdb4j.googlecode.com/svn/wiki/images/Screen.png](http://tinywebdb4j.googlecode.com/svn/wiki/images/Screen.png)
  1. ![http://tinywebdb4j.googlecode.com/svn/wiki/images/RetrieveTag.png](http://tinywebdb4j.googlecode.com/svn/wiki/images/RetrieveTag.png)
  1. ![http://tinywebdb4j.googlecode.com/svn/wiki/images/StoreValue.png](http://tinywebdb4j.googlecode.com/svn/wiki/images/StoreValue.png)
  1. ![http://tinywebdb4j.googlecode.com/svn/wiki/images/SplittingADelimitedList.png](http://tinywebdb4j.googlecode.com/svn/wiki/images/SplittingADelimitedList.png)

Feedback is appreciated.