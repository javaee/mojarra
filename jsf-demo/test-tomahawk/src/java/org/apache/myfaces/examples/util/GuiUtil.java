package org.apache.myfaces.examples.util;

import javax.faces.context.FacesContext;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;

/**
 * @author Thomas Spiegl (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:44 $
 */
public class GuiUtil
{
    private static String BUNDLE_NAME = "org.apache.myfaces.examples.resource.example_messages";

    public static String getMessageResource(String key, Object[] arguments)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        String resourceString;
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, context.getViewRoot().getLocale());
            resourceString = bundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            return key;
        }

        if (arguments == null) return resourceString;

        MessageFormat format = new MessageFormat(resourceString, context.getViewRoot().getLocale());
        return format.format(arguments);
    }

}
