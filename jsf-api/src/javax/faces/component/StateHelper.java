package javax.faces.component;

import java.io.Serializable;

/**
 * Docs
 *
 * @since 2.0
 */
public interface StateHelper extends StateHolder {


    /**
     * Store the specified key/value pair.
     * @param key
     * @param value
     * @return the value previously associated with <code>key</code>, if any
     */
    Object put(Serializable key, Object value);


    /**
     * Remove the key/value pair from the helper.
     * @param key
     * @return
     */
    Object remove(Serializable key);


    /**
     * Store the specified <code>mapKey</code>/<code>value</code>
     * in an <code>Map</code> that is internal to the helper.  The
     * <code>Map</code> will then be associated with <code>key</code>.
     *
     * It's important to note for delta tracking that any modifications
     * to the internal <code>Map</code> be made through this method or
     * {@link StateHelper#remove(java.io.Serializable, Object)}.
     *
     * @param key
     * @param mapKey
     * @param value
     * @return the value previously associated with <code>mapKey</code>,
     *  if any
     */
    Object put(Serializable key, String mapKey, Object value);


    /**
     * @param key
     * @return the value currently associated with the specified <code>key</code>
     *  if any
     */
    Object get(Serializable key);


    /**
     * Attempts to find a value associated with the specified key.  If none is
     * found, eval may be used an a way to evaluate the value using other methods
     * such as {@link javax.el.ValueExpression} and return that result.
     * @param key
     * @return
     */
    Object eval(Serializable key);


    /**
     * Performs the same logic as {@link #eval(java.io.Serializable)} } but if no
     * value is found, this will return the specified <code>defaultValue</code>
     * @param key
     * @param defaultValue
     * @return
     */
    Object eval(Serializable key, Object defaultValue);


    /**
     * Store the specified <code>value</code> in a <code>List</code> that is
     * internal to the <code>StateHelper</code>.
     *
     * It's important to note for delta tracking that any modifications
     * to the internal <code>List</code> be made through this method or
     * {@link StateHelper#remove(java.io.Serializable, Object)}.
     *
     * @param key
     * @param value
     */
    void add(Serializable key, Object value);


    /**
     * If the value associated with <code>key</code> is a <code>Map</code>
     * call <code>Map.remove()</code> passing in <code>valueOrKey</code>.
     * If the value associated with <code>key</code> is a <code>List</code>
     * call <code>List.remove()</code> passing in <code>valueOrKey</code>
     *
     * @param key
     * @param valueOrKey
     * @return the value previously, if any, associated with <code>valueOrKey</code> in
     *  the case the value associated with <code>Key</code> is a <code>Map</code>
     *  otherwise <code>null</code>
     */
    Object remove(Serializable key, Object valueOrKey);


}
