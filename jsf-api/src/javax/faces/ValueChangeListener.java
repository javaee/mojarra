package javax.faces;

import java.util.EventListener;

/**
 * The listener interface for handling value-change events.
 * An object should implement this interface if it needs
 * to respond when a value is changed on a component.
 *
 * @see WSelectBoolean#addValueChangeListener
 * @see WSelectOne#addValueChangeListener
 * @see WTextEntry#addValueChangeListener
 */
public interface ValueChangeListener extends EventListener {
    /**
     * Invoked when a value-change event occurs.
     * @param event the ValueChangeEvent object describing the
     *          value-change event
     */
    public void handleValueChange(ValueChangeEvent event);
}
