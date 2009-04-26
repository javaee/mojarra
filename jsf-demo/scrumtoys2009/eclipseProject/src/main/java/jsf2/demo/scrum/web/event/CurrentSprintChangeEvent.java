package jsf2.demo.scrum.web.event;

import javax.faces.event.SystemEvent;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public class CurrentSprintChangeEvent extends SystemEvent {

    public CurrentSprintChangeEvent(Object source) {
        super(source);
    }
}
