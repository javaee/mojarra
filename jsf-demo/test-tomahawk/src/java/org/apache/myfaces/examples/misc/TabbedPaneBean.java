package org.apache.myfaces.examples.misc;

import java.io.Serializable;

/**
 * @author Manfred Geiler (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:35 $
 */
public class TabbedPaneBean
        implements Serializable
{
    //private static final Log log = LogFactory.getLog(TabbedPaneBean.class);

    private boolean _tab1Visible = true;
    private boolean _tab2Visible = true;
    private boolean _tab3Visible = true;

    public boolean isTab1Visible()
    {
        return _tab1Visible;
    }

    public void setTab1Visible(boolean tab1Visible)
    {
        _tab1Visible = tab1Visible;
    }

    public boolean isTab2Visible()
    {
        return _tab2Visible;
    }

    public void setTab2Visible(boolean tab2Visible)
    {
        _tab2Visible = tab2Visible;
    }

    public boolean isTab3Visible()
    {
        return _tab3Visible;
    }

    public void setTab3Visible(boolean tab3Visible)
    {
        _tab3Visible = tab3Visible;
    }
}
