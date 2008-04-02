/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 

package com.sun.faces.el.impl.jstl;

import java.io.PrintStream;
import java.text.MessageFormat;

/**
 *
 * <p>The evaluator may pass an instance of this class to operators
 * and expressions during evaluation.  They should use this to log any
 * warning or error messages that might come up.  This allows all of
 * our logging policies to be concentrated in one class.
 *
 * <p>Errors are conditions that are severe enough to abort operation.
 * Warnings are conditions through which the operation may continue,
 * but which should be reported to the developer.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: eburns $
 **/

public class Logger
{
  //-------------------------------------
  // Member variables
  //-------------------------------------

  PrintStream mOut;

  //-------------------------------------
  /**
   *
   * Constructor
   *
   * @param pOut the PrintStream to which warnings should be printed
   **/
  public Logger (PrintStream pOut)
  {
    mOut = pOut;
  }

  //-------------------------------------
  /**
   *
   * Returns true if the application should even bother to try logging
   * a warning.
   **/
  public boolean isLoggingWarning ()
  {
    return false;
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pMessage,
			  Throwable pRootCause)
    throws ELException
  {
    if (isLoggingWarning ()) {
      if (pMessage == null) {
	System.out.println (pRootCause);
      }
      else if (pRootCause == null) {
	System.out.println (pMessage);
      }
      else {
	System.out.println (pMessage + ": " + pRootCause);
      }
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning (pTemplate, null);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (Throwable pRootCause)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning (null, pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Object pArg0)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Throwable pRootCause,
			  Object pArg0)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Object pArg0,
			  Object pArg1)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Throwable pRootCause,
			  Object pArg0,
			  Object pArg1)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Throwable pRootCause,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2,
			  Object pArg3)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Throwable pRootCause,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2,
			  Object pArg3)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2,
			  Object pArg3,
			  Object pArg4)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Throwable pRootCause,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2,
			  Object pArg3,
			  Object pArg4)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2,
			  Object pArg3,
			  Object pArg4,
			  Object pArg5)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	    "" + pArg5,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs a warning
   **/
  public void logWarning (String pTemplate,
			  Throwable pRootCause,
			  Object pArg0,
			  Object pArg1,
			  Object pArg2,
			  Object pArg3,
			  Object pArg4,
			  Object pArg5)
    throws ELException
  {
    if (isLoggingWarning ()) {
      logWarning
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	    "" + pArg5,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Returns true if the application should even bother to try logging
   * an error.
   **/
  public boolean isLoggingError ()
  {
    return true;
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pMessage,
			Throwable pRootCause)
    throws ELException
  {
    if (isLoggingError ()) {
      if (pMessage == null) {
	throw new ELException (pRootCause);
      }
      else if (pRootCause == null) {
	throw new ELException (pMessage);
      }
      else {
	throw new ELException (pMessage, pRootCause);
      }
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate)
    throws ELException
  {
    if (isLoggingError ()) {
      logError (pTemplate, null);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (Throwable pRootCause)
    throws ELException
  {
    if (isLoggingError ()) {
      logError (null, pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Object pArg0)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Throwable pRootCause,
			Object pArg0)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Object pArg0,
			Object pArg1)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Throwable pRootCause,
			Object pArg0,
			Object pArg1)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Object pArg0,
			Object pArg1,
			Object pArg2)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Throwable pRootCause,
			Object pArg0,
			Object pArg1,
			Object pArg2)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Object pArg0,
			Object pArg1,
			Object pArg2,
			Object pArg3)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Throwable pRootCause,
			Object pArg0,
			Object pArg1,
			Object pArg2,
			Object pArg3)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Object pArg0,
			Object pArg1,
			Object pArg2,
			Object pArg3,
			Object pArg4)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Throwable pRootCause,
			Object pArg0,
			Object pArg1,
			Object pArg2,
			Object pArg3,
			Object pArg4)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Object pArg0,
			Object pArg1,
			Object pArg2,
			Object pArg3,
			Object pArg4,
			Object pArg5)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	    "" + pArg5,
	  }));
    }
  }

  //-------------------------------------
  /**
   *
   * Logs an error
   **/
  public void logError (String pTemplate,
			Throwable pRootCause,
			Object pArg0,
			Object pArg1,
			Object pArg2,
			Object pArg3,
			Object pArg4,
			Object pArg5)
    throws ELException
  {
    if (isLoggingError ()) {
      logError
	(MessageFormat.format
	 (pTemplate,
	  new Object [] {
	    "" + pArg0,
	    "" + pArg1,
	    "" + pArg2,
	    "" + pArg3,
	    "" + pArg4,
	    "" + pArg5,
	  }),
	 pRootCause);
    }
  }

  //-------------------------------------
}
