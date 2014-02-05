This is a very basic JSF program, designed to show you how to set one up.

To compile it, run maven 2.  Install the resulting war to your application server.

There are two pages: hello and response, and a single resource, wave.med.gif.
There is also a single configuration file, web.xml, and a single Java file, HelloBean.java.

HelloBean holds a single property, name.

The hello.xhtml page requests that the user enter a name, which is stored in the bean.
When the button is pressed, the response page is displayed, displaying the name entered.