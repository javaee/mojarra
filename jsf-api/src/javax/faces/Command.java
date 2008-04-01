package javax.faces;

/**
 * An interface which provides command navigation during
 * the processing of command events.  
 */
public interface Command extends CommandListener {

    /**
     * The completion-path redirection property.  If <code>doCommand</code>
     * executes successfully on this command object (no exceptions are
     * thrown) then control should be redirected to the path specified
     * in this property. If this property is null, then no redirection
     * should occur. 
     * @e the CommandEvent object which invoked this command listener
     * @return String containing the completion redirection path, 
     *         or null if no completion path is specified for this command
     */
    public String getCompletionPath(CommandEvent e);

    /**
     * The error-path redirection property.  If <code>doCommand</code>
     * fails to execute successfully on this command object (an exception
     * is thrown) then control should be redirected to the path specified
     * in this property. If this property is null, then no redirection
     * should occur. 
     * @e the CommandEvent object which invoked this command listener
     * @return String containing the error-redirection path, 
     *         or null if no error-path is specified for this command
     */
    public String getErrorPath(CommandEvent e);
 
}
