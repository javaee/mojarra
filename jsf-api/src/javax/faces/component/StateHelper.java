package javax.faces.component;

import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Docs
 *
 * @since 2.0
 */
public interface StateHelper extends StateHolder {


    /**
     *
     * @param key
     * @param value
     * @return
     */
    Object put(Serializable key, Object value);


    /**
     *
     * @param key
     * @return
     */
    Object remove(Serializable key);


    /**
     *
     * @param key
     * @param mapKey
     * @param value
     * @return
     */
    Object put(Serializable key, String mapKey, Object value);


    /**
     *
     * @param key
     * @return
     */
    Object get(Serializable key);


    /**
     *
     * @param key
     * @return
     */
    Object eval(Serializable key);


    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Object eval(Serializable key, Object defaultValue);


    /**
     *
     * @param key
     * @param value
     */
    void add(Serializable key, Object value);


    /**
     *
     * @param key
     * @param valueOrKey
     * @return
     */
    Object remove(Serializable key, Object valueOrKey);


    /**
     *
     * @param context
     * @return
     */
    Object saveState(FacesContext context);


    /**
     *
     * @param context
     * @param state
     */
    void restoreState(FacesContext context, Object state);


    /**
     *
     * @return
     */
    boolean isTransient();


    /**
     *
     * @param newTransientValue boolean pass <code>true</code> if this Object
     *  will participate in state saving or restoring, otherwise
     */
    void setTransient(boolean newTransientValue);
}
