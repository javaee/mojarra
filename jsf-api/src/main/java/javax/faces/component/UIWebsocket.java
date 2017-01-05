package javax.faces.component;

import static java.util.Collections.unmodifiableList;
import static javax.faces.push.PushContext.ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.websocket.CloseReason.CloseCodes;

/**
 * <p class="changed_added_2_3">
 * The <code>&lt;f:websocket&gt;</code> tag opens an one-way (server to client) websocket based push connection in client
 * side which can be reached from server side via {@link PushContext} interface injected in any CDI/container managed
 * artifact via <code>&#64;</code>{@link Push} annotation.
 * </p>
 * 
 * <p>
 * By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Websocket</code>".
 * This value can be changed by calling the <code>setRendererType()</code> method.
 * </p>
 * 
 * <p>
 * For detailed usage instructions, see <code>&#64;</code>{@link Push} javadoc.
 * </p>
 *
 * @see Push
 * @since 2.3
 */
public class UIWebsocket extends UIComponentBase implements ClientBehaviorHolder {

    // ---------------------------------------------------------------------------------------------- Manifest Constants

    /**
     * <p>
     * The standard component type for this component.
     * </p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Websocket";

    /**
     * <p>
     * The standard component family for this component.
     * </p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Script";

    /**
     * <p>
     * Properties that are tracked by state saving.
     * </p>
     */
    enum PropertyKeys {
        port, channel, scope, user, onopen, onmessage, onclose, connected;
    }

    private static final Pattern PATTERN_CHANNEL_NAME = Pattern.compile("[\\w.-]+");

    private static final String ERROR_ENDPOINT_NOT_ENABLED =
        "f:websocket endpoint is not enabled."
            + " You need to set web.xml context param '" + ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME + "' with value 'true'.";
    private static final String ERROR_INVALID_CHANNEL =
        "f:websocket 'channel' attribute '%s' does not represent a valid channel name. It is required, it may not be an"
               + "  EL expression and it may only contain alphanumeric characters, hyphens, underscores and periods.";
    private static final String ERROR_INVALID_USER =
        "f:websocket 'user' attribute '%s' does not represent a valid user identifier. It must implement Serializable and"
            + " preferably have low memory footprint. Suggestion: use #{request.remoteUser} or #{someLoggedInUser.id}.";

    // ---------------------------------------------------------------------------------------------------- Constructors

    /**
     * <p>
     * Create a new {@link UIWebsocket} instance with default property values.
     * </p>
     * 
     * @throws IllegalStateException When Websocket endpoint is not enabled.
     */
    public UIWebsocket() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        if (!Boolean.parseBoolean(externalContext.getInitParameter(ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME))) {
            throw new IllegalStateException(ERROR_ENDPOINT_NOT_ENABLED);
        }
    }

    // --------------------------------------------------------------------------------------------- UIComponent Methods

    /**
     * <p>
     * Returns {@link UIWebsocket#COMPONENT_FAMILY}.
     * </p>
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * <p>
     * Set the {@link ValueExpression} used to calculate the value for the specified attribute or property name, if any.
     * If a {@link ValueExpression} is set for the <code>channel</code> or <code>scope</code> property, regardless of
     * the value, throw an illegal argument exception. If a {@link ValueExpression} is set for the <code>user</code>
     * property, and the non-null value is not an instance of <code>Serializable</code>, throw an illegal argument
     * exception.
     * </p>
     *
     * @throws IllegalArgumentException If <code>name</code> is one of <code>id</code>, <code>parent</code>, 
     * <code>channel</code> or <code>scope</code>, or it <code>name</code> is <code>user</code> and the non-null value
     * is not an instance of <code>Serializable</code>.
     * @throws NullPointerException If <code>name</code> is <code>null</code>.
     */
    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if (PropertyKeys.channel.toString().equals(name) || PropertyKeys.scope.toString().equals(name)) {
            throw new IllegalArgumentException(name);
        }

        if (PropertyKeys.user.toString().equals(name)) {
            Object user = binding.getValue(getFacesContext().getELContext());

            if (user != null && !(user instanceof Serializable)) {
                throw new IllegalArgumentException(String.format(ERROR_INVALID_USER, user));
            }
        }

        super.setValueExpression(name, binding);
    }

    // ------------------------------------------------------------------------------------ ClientBehaviorHolder Methods

    /**
     * <p>
     * Returns a non-null, empty, unmodifiable <code>Collection</code> which returns <code>true</code> on any 
     * <code>Collection#contains()</code> invocation, indicating that all client behavior event names are acceptable.
     * </p>
     */
    @Override
    public Collection<String> getEventNames() {
        return CONTAINS_EVERYTHING;
    }

    private static final Collection<String> CONTAINS_EVERYTHING = unmodifiableList(new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean contains(Object object) {
            return true;
        }
    });

    // ------------------------------------------------------------------------------------------------------ Properties

    /**
     * Returns the port number of the websocket host.
     * @return The port number of the websocket host.
     */
    public Integer getPort() {
        return (Integer) getStateHelper().eval(PropertyKeys.port);
    }

    /**
     * Sets the port number of the websocket host, in case it is different from the port number in the request URI.
     * Defaults to the port number of the request URI.
     * @param port The port number of the websocket host.
     */
    public void setPort(Integer port) {
        getStateHelper().put(PropertyKeys.port, port);
    }

    /**
     * Returns the name of the websocket channel.
     * @return The name of the websocket channel.
     */
    public String getChannel() {
        return (String) getStateHelper().eval(PropertyKeys.channel);
    }

    /**
     * Sets the name of the websocket channel.
     * It may not be an EL expression and it may only contain alphanumeric characters, hyphens, underscores and periods.
     * All open websockets on the same channel will receive the same push message from the server.
     * @param channel The name of the websocket channel.
     * @throws IllegalArgumentException When the value does not represent a valid channel name.
     */
    public void setChannel(String channel) {
        if (channel == null || !PATTERN_CHANNEL_NAME.matcher(channel).matches()) {
            throw new IllegalArgumentException(String.format(ERROR_INVALID_CHANNEL, channel));
        }

        getStateHelper().put(PropertyKeys.channel, channel);
    }

    /**
     * Returns the scope of the websocket channel.
     * @return The scope of the websocket channel.
     */
    public String getScope() {
        return (String) getStateHelper().eval(PropertyKeys.scope);
    }

    /**
     * Sets the scope of the websocket channel.
     * It may not be an EL expression and allowed values are <code>application</code>, <code>session</code> and
     * <code>view</code>, case insensitive. When the value is <code>application</code>, then all channels with the same
     * name throughout the application will receive the same push message. When the value is <code>session</code>, then
     * only the channels with the same name in the current user session will receive the same push message. When the
     * value is <code>view</code>, then only the channel in the current view will receive the push message. The default
     * scope is <code>application</code>. When the <code>user</code> attribute is specified, then the default scope is
     * <code>session</code>.
     * @param scope The scope of the websocket channel.
     */
    public void setScope(String scope) {
        getStateHelper().put(PropertyKeys.scope, scope);
    }

    /**
     * Returns the user identifier of the websocket channel.
     * @return The user identifier of the websocket channel.
     */
    public Serializable getUser() {
        return (Serializable) getStateHelper().eval(PropertyKeys.user);
    }

    /**
     * Sets the user identifier of the websocket channel, so that user-targeted push messages can be sent.
     * All open websockets on the same channel and user will receive the same push message from the server.
     * It must implement <code>Serializable</code> and preferably have low memory footprint.
     * Suggestion: use <code>#{request.remoteUser}</code> or <code>#{someLoggedInUser.id}</code>.
     * @param user The user identifier of the websocket channel.
     */
    public void setUser(Serializable user) {
        getStateHelper().put(PropertyKeys.user, user);
    }

    /**
     * Returns the JavaScript event handler function that is invoked when the websocket is opened.
     * @return The JavaScript event handler function that is invoked when the websocket is opened.
     */
    public String getOnopen() {
        return (String) getStateHelper().eval(PropertyKeys.onopen);
    }

    /**
     * Sets the JavaScript event handler function that is invoked when the websocket is opened.
     * The function will be invoked with one argument: the channel name.
     * @param onopen The JavaScript event handler function that is invoked when the websocket is opened.
     */
    public void setOnopen(String onopen) {
        getStateHelper().put(PropertyKeys.onopen, onopen);
    }

    /**
     * Returns the JavaScript event handler function that is invoked when a push message is received from the server.
     * @return The JavaScript event handler function that is invoked when a push message is received from the server.
     */
    public String getOnmessage() {
        return (String) getStateHelper().eval(PropertyKeys.onmessage);
    }

    /**
     * Sets the JavaScript event handler function that is invoked when a push message is received from the server. The
     * function will be invoked with three arguments: the push message, the channel name and the raw MessageEvent itself.
     * @param onmessage The JavaScript event handler function that is invoked when a push message is received from the server.
     */
    public void setOnmessage(String onmessage) {
        getStateHelper().put(PropertyKeys.onmessage, onmessage);
    }

    /**
     * Returns the JavaScript event handler function that is invoked when the websocket is closed.
     * @return The JavaScript event handler function that is invoked when the websocket is closed.
     */
    public String getOnclose() {
        return (String) getStateHelper().eval(PropertyKeys.onclose);
    }

    /**
     * Sets the JavaScript event handler function that is invoked when the websocket is closed.
     * The function will be invoked with three arguments: the close reason code, the channel name and the raw
     * <code>CloseEvent</code> itself. Note that this will also be invoked on errors and that you can inspect the close
     * reason code if an error occurred and which one (i.e. when the code is not 1000). See also
     * <a href="http://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455 section 7.4.1</a> and {@link CloseCodes} API
     * for an elaborate list of all close codes.
     * @param onclose The JavaScript event handler function that is invoked when the websocket is closed.
     */
    public void setOnclose(String onclose) {
        getStateHelper().put(PropertyKeys.onclose, onclose);
    }

    /**
     * Returns whether to (auto)connect the websocket or not.
     * @return Whether to (auto)connect the websocket or not.
     */
    public boolean isConnected() {
        return (Boolean) getStateHelper().eval(PropertyKeys.connected, Boolean.TRUE);
    }

    /**
     * Sets whether to (auto)connect the websocket or not. Defaults to <code>true</code>. It's interpreted as a
     * JavaScript instruction whether to open or close the websocket push connection. Note that this attribute is
     * re-evaluated on every ajax request. You can also explicitly set it to <code>false</code> and then manually
     * control in JavaScript by <code>OmniFaces.Push.open("channelName")</code> and
     * <code>OmniFaces.Push.close("channelName")</code>.
     * @param connected Whether to (auto)connect the websocket or not.
     */
    public void setConnected(boolean connected) {
        getStateHelper().put(PropertyKeys.connected, connected);
    }

}
