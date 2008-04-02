/*
 * $Id: RenderKit.java,v 1.20 2003/08/18 22:45:25 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import java.io.Writer;
import java.io.OutputStream;


/**
 * <p><strong>RenderKit</strong> represents a collection of
 * {@link Renderer} instances that, together, know how to render
 * JavaServer Faces {@link UIComponent} instances for a specific
 * client.  Typically, {@link RenderKit}s are specialized for
 * some combination of client device type, markup language, and/or
 * user <code>Locale</code>.  A {@link RenderKit} also acts as
 * a Factory for associated {@link Renderer} instances, which perform the
 * actual rendering process for each component.</p>
 *
 * <p>A typical JavaServer Faces implementation will configure one or more
 * {@link RenderKit} instances at web application startup.  They are
 * made available through calls to the <code>getRenderKit()</code> methods
 * of {@link RenderKitFactory}.  Because {@link RenderKit} instances
 * are shared, they must be implemented in a thread-safe manner.</p>
 *
 * <p>The <code>RenderKit</code> instance must also vend a {@link
 * ResponseStateManager} instance, which is used in the process of
 * saving and restoring tree structure and state.</p>
 */

public abstract class RenderKit {


    /**
     * <p>Add a new {@link Renderer} instance, associated with the
     * specified <code>rendererType</code>, to the set of
     * {@link Renderer}s registered with this {@link RenderKit}.
     *
     * @param rendererType Renderer type of the new {@link Renderer}
     * @param renderer The new {@link Renderer} instance
     *
     * @exception IllegalArgumentException if a {@link Renderer} with the
     *  specified <code>rendererType</code> has already been registered
     * @exception NullPointerException if <code>rendererType</code> or
     *  <code>renderer</code> is null
     */
    public abstract void addRenderer(String rendererType, Renderer renderer);


    /**
     * <p>Create (if necessary) and return a {@link Renderer} instance
     * with the specified renderer type.  Subsequent calls to this method
     * with the same <code>rendererType</code>, from the same web application,
     * must return the same instance.</p>
     *
     * @param rendererType Renderer type to be returned
     *
     * @exception IllegalArgumentException if the requested renderer type
     *  is not supported by this {@link RenderKit}
     * @exception NullPointerException if <code>rendererType</code>
     *  is <code>null</code>
     */
    public abstract Renderer getRenderer(String rendererType);

    /**
     *
     * <p>This interface is where all the rendering-technology specific
     * state management decisions are made.</p>
     *
     * @return the RenderKit's {@link ResponseStateManager}.
     */

    public abstract ResponseStateManager getResponseStateManager();

    /**
     * <p>Use the provided <code>Writer</code> to create a new {@link
     * ResponseWriter} instance for the specified (optional) content
     * type, and character encoding.</p>
     *
     * <p>Implementors are advised to consult the
     * <code>getCharacterEncoding()</code> method of class {@link
     * javax.servlet.ServletResponse} to get the required value for the
     * characterEncoding for this method.  Since the <code>Writer</code>
     * for this response will already have been obtained (due to it
     * ultimately being passed to this method), we know that the
     * character encoding cannot change during the rendering of the
     * response.</p>
     *
     * @param writer the Writer around which this {@link ResponseWriter}
     * must be built.
     *
     * @param contentTypeList an "Accept header style" list of content
     * types for this response, or <code>null</code> if the
     * <code>RenderKit</code> should choose the best fit.  The RenderKit
     * must support a value for this argument that comes straight from
     * the <code>Accept</code> HTTP header, and therefore requires
     * parsing according to the specification of the <code>Accept</code>
     * header.  Please see <a
     * href="http://www.ietf.org/rfc/rfc2616.txt?number=2616">Section
     * 14.1 of RFC 2616</a> for the specification of the
     * <code>Accept</code> header.
     *
     * @param characterEncoding such as "ISO-8859-1" for this
     * ResponseWriter.  Please see <a
     * href="http://www.iana.org/assignments/character-sets">the
     * IANA</a> for a list of character encodings.
     *
     * @return a new {@link ResponseWriter}.
     */

    public abstract ResponseWriter createResponseWriter(Writer writer,
							String contentTypeList,
							String characterEncoding);

    /** 
     * <p>Use the provided <code>OutputStream</code> to create a new
     * {@link ResponseStream} instance.</p>
     *
     */ 

    public abstract ResponseStream getResponseStream(OutputStream out);

}
