/*
 * $Id: CommandDispatcherImpl.java,v 1.5 2002/01/10 22:20:08 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;

import javax.faces.CommandDispatcher;
import java.util.EventObject;
import javax.faces.CommandEvent;
import javax.faces.CommandListener;
import java.util.Vector;
import javax.faces.Command;
import javax.faces.Constants;
import javax.faces.ObjectManager;
import javax.servlet.ServletException;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.CommandFailedException;

import com.sun.faces.util.Util;

/**
 * A class which dispatches command events to appropriate target 
 * command listener objects.  This command dispatcher implements 
 * appropriate flow-control when it dispatches to listeners which 
 * implement the <code>Command</code>interface.
 *
 * @version $Id: CommandDispatcherImpl.java,v 1.5 2002/01/10 22:20:08 edburns Exp $
 * @author Jayashri Visvanathan
 *
 * @see CommandEvent
 * @see CommandListener
 * @see Command
 * @see WCommand#addCommandListener
 */
public class CommandDispatcherImpl extends CommandDispatcher {

    public CommandDispatcherImpl () {
    }

    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject event) throws IOException, FacesException {

        boolean cmd_failed = false;

        String redirectPath = null;
        
        ParameterCheck.nonNull(request);
        ParameterCheck.nonNull(response);
        ParameterCheck.nonNull(event);

        ObjectManager ot = ObjectManager.getInstance();
        Assert.assert_it( ot != null );
        
        CommandEvent cmd_event = null;
        if ( event instanceof CommandEvent)  {
            cmd_event = (CommandEvent) event;
        } else {
            throw new FacesException("Invalid event type. " + 
                    "Expected CommandEvent");
        }
        // invoke the doCommand on all listeners
        String srcName = cmd_event.getSourceName();  
        Assert.assert_it(srcName != null );
 
        String lis_name = srcName + Constants.REF_COMMANDLISTENERS;
        Vector lis_list = (Vector) ot.get(request, lis_name);
        if ( lis_list != null && lis_list.size() > 0 ) {
            for ( int i = 0; i < lis_list.size(); ++i) {
                // tags could have put in null for listener name.
                String lis_ref_name = (String) lis_list.elementAt(i);
                if ( lis_ref_name != null ) {
                    CommandListener cl = (CommandListener) 
                        ot.get(request, lis_ref_name);
                    Assert.assert_it ( cl != null );
                    try {
                        cl.doCommand(cmd_event);
                    } catch ( CommandFailedException e ) {
                        // PENDING ( visvan ) skip for now till
                        // we decide how to do error handling in listeners.
                    } 
                }   
            }    
        }
    
        // process commands if any
        String cmd_ref_name = srcName + Constants.REF_COMMAND;
        String cmd_name = (String) ot.get(request, cmd_ref_name);
        if (cmd_name != null ) {
            Command cmd = (Command)  ot.get(request, cmd_name);
            Assert.assert_it ( cmd != null );
            try {
                cmd.doCommand(cmd_event);
                redirectPath = cmd.getCompletionPath(cmd_event);
            } catch ( CommandFailedException e ) {
                redirectPath = cmd.getErrorPath(cmd_event);
            }
        }
 
        if ( redirectPath != null ) {
            try {
                HttpServletResponse res = (HttpServletResponse) response;
                RequestDispatcher reqD = 
                request.getRequestDispatcher(redirectPath);
		// At this point, we are certain we're forwarding a
		// request to another JSP page.  We must make it so the
		// query string is not processed by our code.  We wrap
		// the request in a special ParamBlockingRequestWrapper.
		// See bugtraq 4617032.
                reqD.forward(new 
			     ParamBlockingRequestWrapper((HttpServletRequest)
							 request), 
			     response); 
            } catch ( ServletException se ) {
                throw new FacesException( se.getMessage());
            }
        }
    }
}
