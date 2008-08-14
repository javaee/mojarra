package javax.faces.event;

import javax.faces.application.Application;

/**
 * RELEASE_PENDING (edburns,rogerk) review docs
 * <p>This event will be published by the runtime <em>before</em>
 * the factories associated with this Application are released.</p>
 *
 * <p>This event is useful for listeners that need to perform custom
 * shutdown processing without having to rely on <code>ServletContextListener</code>s
 * which will be invoked after all of the application artifacts have been removed.</p>
 *
 * @since 2.0
 */
public class ApplicationPreDestroyEvent extends SystemEvent {

    private static final long serialVersionUID = 8105212785161493162L;

    /**
     * Constructs a new <code>ApplicationPreDestroyEvent</code> for this application.
     * @param application the application that has been configured
     */
    public ApplicationPreDestroyEvent(Application application) {
        super(application); 
    }

}
