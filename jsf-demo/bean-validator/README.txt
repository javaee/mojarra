This demo, the bean-validator demo, demonstrates the use of Bean Validation (JSR 303)
with JSF.  This demo assumes it will be run against an EE6 complaint container, such
as Glassfish V3.

This demo contains several JSR 303 constraints which are applied to the model
(in this case, JSF managed beans).  

When JSR 303 is present on the classpath, JSF will automatically add a validator
to each EditableValueHolder that will interact with any defined constraints on the
model bound to said EditableValueHolder.  

One other item to note here is that this demo dynamically enables/disables
these bean validators based on the current view being displayed.  See the Facelet
templates for details on how this is accomplished.

