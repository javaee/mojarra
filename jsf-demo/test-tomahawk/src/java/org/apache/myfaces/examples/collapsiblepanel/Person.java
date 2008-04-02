/**
 * Copyright 2004 by Irian Marinschek & Spiegl Software OEG
 */
package org.apache.myfaces.examples.collapsiblepanel;

import java.io.Serializable;

/**
 * @author Martin Marinschek
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:10 $
 *          <p/>
 *          $Log: Person.java,v $
 *          Revision 1.1  2005/11/08 06:08:10  edburns
 *          import_tomahawk_test_app
 *
 */
public class Person implements Serializable
{
    private String _surName;
    private String _firstName;
    private boolean _collapsed;

    public String getSurName()
    {
        return _surName;
    }

    public void setSurName(String surName)
    {
        _surName = surName;
    }

    public String getFirstName()
    {
        return _firstName;
    }

    public void setFirstName(String firstName)
    {
        _firstName = firstName;
    }

    public boolean isCollapsed()
    {
        return _collapsed;
    }

    public void setCollapsed(boolean collapsed)
    {
        _collapsed = collapsed;
    }

}
