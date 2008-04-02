#
# $Id: README.txt,v 1.3 2006/03/30 23:23:16 rlubke Exp $
#

What is Selenium?  Here is a direct quote from the Selenium web site [1]:

Selenium is a test tool for web applications. Selenium tests run directly 
in a browser, just as real users do. And they run in Internet Explorer, 
Mozilla and Firefox on Windows, Linux, and Macintosh. No other test tool 
covers such a wide array of platforms.

    * Browser compatability testing. Test your application to see 
      if it works correctly on different browsers and operating systems. 
      The same script can run on any Selenium platform.
    * System functional testing. Create regression tests to verify 
      application functionality and user acceptance.
      
      
The next question, is why use this to validate JSF?  One of the big 
issues in JSF 1.2, was compatibility with 1.1 based applications (Shale,
ADF Faces, and SEAM to name a few).  While we could potentially write
HtmlUnit test cases to validate the sample applications of the aforementioned
frameworks, doing so is very time consuming.  Using Selenium, a JSF developer
can cut that time down dramatically and provide useful test cases for 
acceptence testing.  

How do I run the tests?
    
    - shale-mailreader
       * download the shale-mailreader application from 
         http://struts.apache.org and install it on GlassFish         
       * From the browser, load the Selenium IDE
       * Load and run the tests in this order make sure the base URL
         is set to localhost:8080 or whatever is appropriate
          + shale-mailreader-userlogon-not-registered.html
          + shale-mailreader-user-profile.html
       * The shale-mailreader application *must* be redeployed
         before each subsequent test run
     - shale-sql-browser
       * download the shale-sql-browser application from
         http://struts.apache.org and install it on GlassFish
       * From the browser, load the Selenium IDE
       * Load and run the tests in this order make sure the base URL
         is set to localhost:8080 or whatever is appropriate
           + shale-sql-browser.html
      - shale-usecases
       * download the shale-usecases application from
         http://struts.apache.org and install it on GlassFish
       * From the browser, load the Selenium IDE
       * Load and run the tests in this order make sure the base URL
         is set to localhost:8080 or whatever is appropriate
           + shale-usecases.html  
       * NOTE:
           + run the tests using 'walk' mode.  It seems the locale switch
             causes the test to trip up when in 'run' mode
           + this doesn't validate the remoting examples as they return XML
             and it seems Selenium can't validate the text.
TODO:
  - Add Selenium test cases for:
     * shale-sql-browser
     * shale-use-cases
     * SEAM
     * ADF Faces demo application
     
  - Create a testsuite for continuous 
    integration testing with our Luntbuild
    server
       
       
            
      
      
[1] http://www.openqa.org/selenium/