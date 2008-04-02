/*
 * $Id: UISelectOneBase.java,v 1.4 2003/08/28 21:17:26 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.Iterator;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


/**
 * <p><strong>UISelectOneBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UISelectOne}.</p>
 */

public class UISelectOneBase extends UIInputBase implements UISelectOne {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectOneBase} instance with default property
     * values.</p>
     */
    public UISelectOneBase() {

        super();
        setRendererType("Menu");

    }


    // ------------------------------------------------------ Validation Methods


    public void validate(FacesContext context) {

        Object value = getValue();

        // Skip validation if it is not necessary
        if ((value == null) || !isValid()) {
            super.validate(context);
            return;
        }

        // Ensure that the value matches one of the available options
        boolean found = false;
        Iterator items = new SelectItemsIterator(this);
        while (items.hasNext()) {
            SelectItem item = (SelectItem) items.next();
            if (value.equals(item.getValue())) {
                found = true;
                break;
            }
        }

        // Enqueue an error message if an invalid value was specified
        if (!found) {
            Message message =
                context.getApplication().
                getMessageResources(MessageResources.FACES_API_MESSAGES).
                getMessage(context, INVALID_MESSAGE_ID);
            context.addMessage(this, message);
            setValid(false);
        }
        super.validate(context);

    }


}
