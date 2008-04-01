/*
 * $Id: ResponseWriter.java,v 1.1 2002/06/05 03:01:55 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.io.Writer;


/**
 * <p><strong>ResponseWriter</strong> is an interface describing an adapter
 * to an underlying output mechanism for character-based output.</p>
 *
 * <p><strong>FIXME</strong> - Consider adding back some of the convenience
 * mechanisms from <code>javax.faces.OutputMethod</code> in the .03 version
 * of the JSF specification.</p>
 */

public abstract class ResponseWriter extends Writer {

}
