package javax.faces.event;

import javax.faces.application.Application;

/**
 * <p class="changed_added_2_0">This event must be published by the
 * runtime <em>before</em> the factories associated with this {@link
 * Application} are released.</p>
 *
 * <p class="changed_added_2_0">This event is useful for listeners that
 * need to perform custom shutdown processing without having to rely on
 * <code>ServletContextListener</code>s which will be invoked after all
 * of the application artifacts have been removed.</p>
 *
 * @since 2.0
 */
public class PreDestroyApplicationEvent extends SystemEvent {

    private static final long serialVersionUID = 8105212785161493162L;

    /**
     * <p class="changed_added_2_0">Constructs a new
     * <code>PreDestroyApplicationEvent</code> for this application.</p>
     *
     * @param application the application that has been configured
     *
     * @since 2.0
     */
    public PreDestroyApplicationEvent(Application application) {
        super(application); 
    }
    
    /**
     * <p class="changed_added_2_0">The source {@link Application} that sent this event.</p>
     * 
     * @since 2.0
     */

    public Application getApplication() {
        return (Application) getSource();
    }
    
}
