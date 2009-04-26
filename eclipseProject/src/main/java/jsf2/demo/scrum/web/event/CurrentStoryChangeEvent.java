package jsf2.demo.scrum.web.event;

import javax.faces.event.SystemEvent;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public class CurrentStoryChangeEvent extends SystemEvent {

    public CurrentStoryChangeEvent(Object source) {
        super(source);
    }
}
