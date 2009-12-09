IMPORTANT DEPLOYMENT INFORMATION
================================

If you are building and deploying this sample from the command line, it
is absolutely vital that you run the command 

./asadmin start-database

from the glassfish bin directory *before* deploying this application.

If you are building and deploying the sample from NetBeans, and are
using the NetBeans Glassfish V3 integration module to start the
container fromthe "Servers" tab, then the database is automatically
started when you start the container.

Note to sample developers
=========================

The actual source code for this sample is maintained in subversion 
under the repository
 <https://mojarra.dev.java.net/svn/mojarra/branches/scrumtoys-glassfishv3-20090506>.

The source code checked in here under CVS is arrived at by checking
out the above repo and running 

ant -f blueprints-deploy-sample.xml -Dblueprint.sample=<sample_dir>

where <sample_dir> is the local workarea to which the sample should be copied,
blueprints fashion.

The above SVN url also resolves to a Maven project, which you can use if
you so desire.
