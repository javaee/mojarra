This demo requires the Java EE APIs.

* See if it's possible remove all <navigation-rule> instances

* How does the error page work?

* Define how this should run on non EE6 containers.  What about BV?

* Log4j and JSTL ended up in WEB-INF/lib

* Non-existent documentation

* JavaDocs, remove @author javadocs.

* Remove README in WEB-INF

* Document how it runs on Tomcat with OpenEJB

* eclipseProject directory?

* scrumtoys-uml what is it?  Increases confusion.


also, can't safely use @PersistenceContext in a threaded app, need to use @PersistenceUnit
From IRC:
You are now known as jimdriscoll.
[10:01am] jimdriscoll: edburns:  I think I see a different problem in the scrumtoys app.
[10:01am] jimdriscoll: http://weblogs.java.net/blog/ss141213/archive/2005/12/dont_use_persis_1.html
[10:01am] jimdriscoll: ^^ Says that persistencecontexts are not threadsafe.
[10:01am] jimdriscoll: But we use @PersistenceContext in a @SessionScoped bean.
[10:02am] jimdriscoll: While not a clear problem (it will *almost* always work), I think that we can only safely use @PersistenceContext in a RequestScoped or NoneScoped bean.
[10:03am] ioss[qoob]: ViewScope should work too
[10:03am] jimdriscoll: Yes, that one too - I forgot it was there.
[10:04am] jimdriscoll: But App scope is a clear no-no, and Session scoped is probably bad, since a single user can (and many times will) run multiple windows.
[10:04am] ioss[qoob]: even so, if SessionScoped does not work, ViewScope will have problems too, because to make SessionScope not work, you will probably need 2 open Windows
[10:04am] ioss[qoob]: right, and viewscope will not yet work with multiple open windows
[10:04am] jimdriscoll: Nor should it, right?
[10:05am] ioss[qoob]: viewscope should i would say
[10:05am] ioss[qoob]: but viewscope gets at the moment invalidated, if you leave the view
[10:06am] ioss[qoob]: and as there is not yet a concept of a "window", you will most of the time leaving a page, invalidating the viewscope in the other view
[10:07am] ioss[qoob]: but that is only partially related to your "problem"
[10:08am] ioss[qoob]: but why not switch to the PersistenceUnit?
[10:09am] jimdriscoll: Not my app - just letting Ed know.  edburns?  Ack?
[10:10am] ioss[qoob]: i think we can easily change that, as spock has abstracted the jpa stuff quite well, with the anonymous classes

