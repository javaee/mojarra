RUNNING THE DEMO
================

Prerequisites:
-------------
   GlassFish 4
   Java 7
   JSF 2.2.x

   This demo also utilizes external JavaScript libraries: 
       jscolor - displays a color control an devices where
                 <input type="color"> is not supported.
       modernizr - checks if <input type="color"> is supported.
- 
0. This demo utilizes a small JSF Web Socket library consisting of:
   - a JSF Web Socket composite component (webapp/resources/h5/ws.xhtml)
   - a Web Socket JavaScript library (webapp/resources/js/jsf-ws.js)
   - There are two application areas that need to be modified:
     - You will need to modify HOSTNAME to the name of the host the Web Socket endpoint
       will be running on:  (ex: edburns-mac.local)
       in the following application files:
          - webapp/index.xhtml
          - webapp/resources/js/Matrix.js
   The Web Socket JSF composite component has been designed to open mutiple 
   channels and assicated handlers (although this feature has not been tested).

1. Start GlassFish

2. Build the demo: mvn clean install

3. Deploy the demo.

4. Start the web socket server endpoint:
   Make sure you have the following env variables set:
     JSF_JAR:  the JSF jar (ex: javax.faces.jar)
     DEMO_HOME: Top level location of this demo:
          (ex: MOJARRA_2_2X_ROLLING/jsf-demo/sandbox/html5/matrix)
   In this directory:  
     ./run-matrix.sh 
   You should see messages like:
      Listening for transport dt_socket at address: 9009
Oct 10, 2013 10:08:16 AM org.glassfish.tyrus.server.ServerContainerFactory create
INFO: Provider class loaded: org.glassfish.tyrus.container.grizzly.GrizzlyEngine
Oct 10, 2013 10:08:16 AM org.glassfish.grizzly.http.server.NetworkListener start
INFO: Started listener bound to [0.0.0.0:8021]
Oct 10, 2013 10:08:16 AM org.glassfish.grizzly.http.server.HttpServer start
INFO: [HttpServer] Started.
Oct 10, 2013 10:08:16 AM org.glassfish.tyrus.server.Server start
INFO: WebSocket Registered apps: URLs all start with ws://localhost:8021
Oct 10, 2013 10:08:16 AM org.glassfish.tyrus.server.Server start
INFO: WebSocket server started.
Press any key to stop the server...

5. Visit localhost:8080/matrix/index.jsf
   You should see messages like:
Oct 10, 2013 10:09:40 AM matrix.Matrix handleMessage
INFO: WS ENDPOINT BROADCASTING MESSAGE:register
Oct 10, 2013 10:09:40 AM matrix.Matrix handleMessage
INFO: BROADCASTING MESSAGE NEXT SESSION:SessionImpl{uri=/matrix/matrix, id='77a4c57d-f99d-4c9f-aaba-537ca7574715', endpoint=EndpointWrapper{endpointClass=null, endpoint=org.glassfish.tyrus.core.AnnotatedEndpoint@1fc202e5, uri='/matrix/matrix', contextPath='/matrix'}}

6. Play with the demo.


NOTE: Because this is primarily a Web Socket demo, it is recommended that the demo be run with at least two 
      UIs.  For the optimal WOW factor, we recommend a wireless connection.  For example, in addition 
      to the main computer/laptop, you could also connect with an IPAD.
