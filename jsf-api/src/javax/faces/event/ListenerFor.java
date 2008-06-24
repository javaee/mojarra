/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="changed_added_2_0">Classes tagged with this listener are
 * installed in the system as listeners using the method {@link
 * javax.faces.application.Application#subscribeToEvent} or {@link
 * javax.faces.component.UIComponent#subscribeToEvent}, passing the
 * arguments as given in the annotation declaration.  Generally, the
 * factory responsible for creating instances of the artifact, or the
 * entity responsible for taking action on an instance shortly after its
 * creation, will take responsibility for interrogating instances for
 * possession of this annotation and take the appropriate action.</p>
 *
 * <p class="changed_added_2_0">The default implementation must support
 * attaching this annotation to {@link
 * javax.faces.component.UIComponent} or {@link
 * javax.faces.render.Renderer} classes.  See the javadocs for those
 * classes for specific information on how to process the annotation in
 * those cases.</p>
 *
 * @since 2.0
 */

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface ListenerFor {

    /* RELEASE_PENDING (edburns,rogerk) should this state that the default
     *  implementation doesn't support subclass relationships?  
     * edburns: yes.  I went a step further and said the impl must not honor
     * subclass relationships.
     */ 


    /**
     * <p class="changed_added_2_0">The kind of system event for which
     * this class will be installed as a listener.  The implementation
     * only supports exact matches on the <code>Class</code> and must
     * not honor subclass relationships.</p>
     */

    public Class<? extends SystemEvent> systemEventClass();


    /**
     * <p class="changed_added_2_0">The kind of object that emits events
     * of the type given by the value of the {@link #systemEventClass}
     * attribute.</p>
     */ 
    public Class sourceClass();

}
