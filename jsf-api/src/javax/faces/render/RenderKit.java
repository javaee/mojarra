/*
 * $Id: RenderKit.java,v 1.25 2004/01/21 03:50:28 eburns Exp $
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
 * <p>A typical JavaServer Faces implementation will configure one or
 * more {@link RenderKit} instances at web application startup.  They
 * are made available through calls to the <code>getRenderKit()</code>
 * methods of {@link RenderKitFactory}.  Because {@link RenderKit}
 * instances are shared, they must be implemented in a thread-safe
 * manner.  Due to limitations in the current specification having
 * multiple <code>RenderKit</code> instances at play in the same
 * application requires a custom {@link
 * javax.faces.application.ViewHandler} instance that is aware of how to
 * deal with this case.  This limitation will be lifted in a future
 * version of the spec.</p>
 *
 * <p>The <code>RenderKit</code> instance must also vend a {@link
 * ResponseStateManager} instance, which is used in the process of
 * saving and restoring tree structure and state.</p>
 */
    // PENDING(edburns): remove limitation
public abstract class RenderKit {


    /**
     * <p>Register the specified {@link Renderer} instance, associated with the
     * specified <code>rendererType</code>, to the set of
     * {@link Renderer}s registered with this {@link RenderKit}, replacing
     * any previously registered {@link Renderer} for this identifier.
     *
     * @param rendererType Renderer type of the {@link Renderer} to register
     * @param renderer {@link Renderer} instance we are registering
     *
     * @exception NullPointerException if <code>rendererType</code> or
     *  <code>renderer</code> is null
     */
    public abstract void addRenderer(String rendererType, Renderer renderer);


    /**
     * <p>Return the {@link Renderer} instance most recently registered for
     * the specified <code>rendererType</code>, if any; otherwise, return
     * <code>null</code>.  The set of available renderer types is
     * available via the <code>getRendererTypes()</code> method.</p>
     *
     * @param rendererType Renderer type of the requested
     *  {@link Renderer} instance
     *
     * @exception NullPointerException if <code>rendererType</code>
     *  is <code>null</code>
     */
    public abstract Renderer getRenderer(String rendererType);


    /**
     * <p>Return an instance of {@link ResponseStateManager} to handle
     * rendering technology specific state management decisions.</p>
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
     * ResponseWriter, or <code>null</code> if the
     * <code>RenderKit</code> should choose the best fit.  Please see <a
     * href="http://www.iana.org/assignments/character-sets">the
     * IANA</a> for a list of character encodings.
     *
     * @return a new {@link ResponseWriter}.
     *
     * @exception IllegalArgumentException if no matching content type
     * can be found in <code>contentTypeList</code>, or no matching
     * character encoding can be found for the argument
     * <code>characterEncoding</code>.
     *
     */
    public abstract ResponseWriter createResponseWriter(Writer writer,
							String contentTypeList,
							String characterEncoding);


    /** 
     * <p>Use the provided <code>OutputStream</code> to create a new
     * {@link ResponseStream} instance.</p>
     *
     */ 
    public abstract ResponseStream createResponseStream(OutputStream out);


}
