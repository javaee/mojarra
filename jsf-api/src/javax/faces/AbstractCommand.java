/*
 * $Id: AbstractCommand.java,v 1.2 2001/12/20 22:25:43 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * An abstract implementation of a command object which stores
 * the completionPath and errorPath property values.
 */
public abstract class AbstractCommand implements Command {

    private String completionPath;
    private String errorPath;

    protected AbstractCommand() {
    }

    /**
     * Constructs a command object with the specified completionPath
     * and errorPath.
     * @param completionPath String containing the completion redirection 
     *        path, or null if no completion path is specified for this command
     * @param errorPath String containing the error redirection path, or
     *        null if no error path is specified for this command
     */
    protected AbstractCommand(String completionPath, String errorPath) {
	this.completionPath = completionPath;
	this.errorPath = errorPath;
    }

    /**
     * Invoked in response to a command event.  Subclasses
     * should override this method to provide the application
     * specific command behavior.
     * @param event the CommandEvent object which invoked this command listener
     * @throws CommandFailedException if the command could not
     *         be performed successfully
     */
    public abstract void doCommand(CommandEvent event) 
	                           throws CommandFailedException; 
    

    /**
     * The completion-path redirection property.  
     * @see Command#getCompletionPath
     * @param event the CommandEvent object which invoked this command listener
     * @return String containing the completion redirection path, 
     *         or null if no completion path is specified for this command
     */
    public String getCompletionPath(CommandEvent event) {
	return completionPath;
    }

    /**
     * The error-path redirection property.  
     * @see Command#getErrorPath
     * @param event the CommandEvent object which invoked this command listener
     * @return String containing the error-redirection path, 
     *         or null if no error-path is specified for this command
     */
    public String getErrorPath(CommandEvent event) {
	return errorPath;
    }
 
}
