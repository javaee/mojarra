package javax.faces.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.io.Serializable;


/**A default implementation of the
 * ComponentPartialStateHolder.
 *
 * Provides a constructor to which the
 * Component itself can be passed, and provides
 * a default-behaviour for also evaluating
 * value-expressions when the value returned
 * from the partial-state map is null.
 *
 */
class DefaultComponentPartialStateMap extends AbstractPartialStateMap {
    private transient final UIComponent component;
    private transient final FacesContext facesContext;

    public DefaultComponentPartialStateMap(FacesContext facesContext, final UIComponent component) {
      this.component = component;
      this.facesContext = facesContext;
    }

    public Object get(final Serializable key) {
       
        final Object value = super.get(key);
        if (value == null) {
            ValueExpression ve = component.getValueExpression(key.toString());
            return ((ve != null) ? ve.getValue(facesContext.getELContext()) : null);
        }

        return value;
    }

    public Object get(final Serializable key, final Object defaultValue) {
        final Object value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
