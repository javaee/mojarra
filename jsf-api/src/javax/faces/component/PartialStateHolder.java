package javax.faces.component;

import java.io.Serializable;

/**This interface is implemented
 * by StateHolders which can save
 * a partial state between requests.
 *
 * This interface extends from
 * StateHolder as implementations
 * are expected to also provide
 * implementations of restoreState/saveState.
 * However, these implementations should
 * return a partial state only if applicable.
 *
 * The partial state is the state which
 * has been changed after notifyStoreState
 * has been called. It will be returned only
 * if notifyStoreState has been called - if it
 * has not been called, the full state will be
 * returned.
 *
 * For components created from a PDL-template, notifyStoreState
 * should be called, for dynamically
 * created components (in Java), notifyStoreState
 * should not be called.
 * 
 */
public interface PartialStateHolder extends StateHolder {

    /**Put a new value to the state.
     *
     * @param key
     * @param value
     * @return
     */
    Object put(Serializable key, Object value);

    /**Get a value from the state
     *
     * @param key
     * @return
     */
    Object get(Serializable key);

    /**Get a value from the state, return
     * default value if value in the state
     * is null.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Object get(Serializable key, Object defaultValue);

    /**Notify this state-holder that it
     * a) should start collecting delta-state
     * b) should return the delta-state only from now own if
     * save-state gets called.
     *
     */
    void notifyStoreState();
}
