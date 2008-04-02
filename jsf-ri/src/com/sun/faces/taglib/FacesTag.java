/*
 * $Id: FacesTag.java,v 1.50 2003/09/24 23:17:04 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTag.java

package com.sun.faces.taglib;

import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.JspVariableResolver;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.servlet.jsp.JspException;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: FacesTag.java,v 1.50 2003/09/24 23:17:04 horwat Exp $ 
 */

public abstract class FacesTag extends javax.faces.webapp.UIComponentTag
{
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(FacesTag.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    protected String key = null;
    protected String imageKey = null;
    protected String bundle = null;
    protected String formatStyle = null;
    protected String dateStyle = null;
    protected String timeStyle = null;
    protected String timezone = null;
    protected String formatPattern = null;

    protected String accept = null;
    protected String acceptcharset = null;
    protected String accesskey = null;
    protected String action = null;
    protected String alt = null;
    protected String charset = null;
    protected String checked = null;
    protected String cols = null;
    protected String coords = null;
    protected String dir = null;
    protected String disabled = null;
    protected String enctype = null;
    protected String htmlFor = null;
    protected String height = null;
    protected String href = null;
    protected String hreflang = null;
    protected String hspace = null;
    protected String ismap = null;
    protected String label = null;
    protected String lang = null;
    protected String longdesc = null;
    protected String maxlength = null;
    protected String method = null;
    protected String multiple = null;
    protected String name = null;
    protected String onblur = null;
    protected String onchange = null;
    protected String onclick = null;
    protected String ondblclick = null;
    protected String onfocus = null;
    protected String onkeydown = null;
    protected String onkeypress = null;
    protected String onkeyup = null;
    protected String onmousedown = null;
    protected String onmousemove = null;
    protected String onmouseout = null;
    protected String onmouseover = null;
    protected String onmouseup = null;
    protected String onreset = null;
    protected String onselect = null;
    protected String onsubmit = null;
    protected String readonly = null;
    protected String rel = null;
    protected String rev = null;
    protected String rows = null;
    protected String selected = null;
    protected String shape = null;
    protected String size = null;
    protected String src = null;
    protected String style = null;
    protected String styleClass = null;    
    protected String tabindex = null;
    protected String target = null;
    protected String title = null;
    protected String type = null;
    protected String usemap = null;
    protected String value = null;

    // HTML 4.0 table attributes
    protected String summary = null;
    protected String width = null;
    protected String bgcolor = null;
    protected String frame = null;
    protected String rules = null;
    protected String border = null;
    protected String cellspacing = null;
    protected String cellpadding = null;

    protected String valueRef = null;
    protected Converter converter = null;
     // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public FacesTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //
    public void setValueRef(String newValueRef)
    {
	valueRef = newValueRef;
    }
    
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

  
    public void setKey(String newKey)
    {
	key = newKey;
    }

    public void setImageKey(String newImageKey)
    {
	imageKey = newImageKey;
    }

    public void setBundle(String newBundle)
    {
	bundle = newBundle;
    }

    public void setFormatStyle(String newFormatStyle)
    {
	formatStyle = newFormatStyle;
    }
    
    public void setDateStyle(String newDateStyle)
    {
	dateStyle = newDateStyle;
    }
    
    public void setTimeStyle(String newTimeStyle)
    {
	timeStyle = newTimeStyle;
    }

    public void setTimezone(String newTimezone)
    {
	timezone = newTimezone;
    }
    
    public void setFormatPattern(String newFormatPattern)
    {
	formatPattern = newFormatPattern;
    }

    // List of MIME types for file upload - 
    // For FORM/INPUT HTML elements

    public void setAccept(String newAccept) 
    {
        accept = newAccept;
    }

    // List of supported char sets - For FORM HTML element

    public void setAcceptcharset(String newAcceptcharset) 
    {
        acceptcharset = newAcceptcharset;
    }
    
    // Accessibility key char -
    // For "A" (Hyperlink)/BUTTON/INPUT/LABEL/TEXTAREA HTML elements

    public void setAccesskey(String newAccesskey) 
    {
        accesskey = newAccesskey;
    }

    // Server-side form handler - For FORM HTML element

    public void setAction(String newAction) 
    {
        action = newAction;
    }

    // Short description - For INPUT HTML element

    public void setAlt(String newAlt)
    {
        alt = newAlt;
    }

    // Char encoding for linked resource -
    // For "A" (Hyperlink) HTML element

    public void setCharset(String newCharset) 
    {
        charset = newCharset;
    }

    // For radio buttons and checkboxes - For INPUT element

    public void setChecked(String newChecked) 
    {
        checked = newChecked;
    }

    // Space seperated list of classes -
    // For all HTML elements pertaining to Faces

    // For TEXTAREA HTML element

    public void setCols(String newCols)
    {
        cols = newCols;
    }

    // For use with client side image maps - 
    // For "A" (Hyperlink) HTML element

    public void setCoords(String newCoords)
    {
        coords = newCoords;
    }
  
    // Direction for weak/neutral text - For all HTML elements 
    // pertaining to Faces

    public void setDir(String newDir) 
    {
        dir = newDir;
    }

    // Means "unavailable" in this context -
    // For BUTTON/INPUT/OPTGROUP/OPTION/SELECT/TEXTAREA HTML elements 

    public void setDisabled(String newDisabled)
    {
        disabled = newDisabled;
    }

    // For FORM HTML element

    public void setEnctype(String newEnctype)
    {
        enctype = newEnctype;
    }

    // Matches field "id" value - For LABEL HTML element

    public void setHtmlFor(String newHtmlFor)
    {
        htmlFor = newHtmlFor;
    }

    // Override height - For IMG HTML element

    public void setHeight(String newHeight)
    {
        height = newHeight;
    }

    // For "A" (Hyperlink) HTML element

    public void setHref(String newHref)
    {
        href = newHref;
    }

    // Language Code - For "A" (Hyperlink) HTML element

    public void setHreflang(String newHreflang)
    {
        hreflang = newHreflang;
    }

    // Horizontal space - For IMG HTML element

    public void setHspace(String newHspace)
    {
        hspace = newHspace;
    }

    // Use server side image map - For IMG/INPUT HTML elements

    public void setIsmap(String newIsmap)
    {
        ismap = newIsmap;
    }

    // For OPTION/OPTGROUP HTML elements

    public void setLabel(String newLabel)
    {
        label = newLabel;
    }

    // Language Code - For all HTML elements pertaining to Faces

    public void setLang(String newLang)
    {
        lang = newLang;
    }

    // Link to long description - For IMG HTML element

    public void setLongdesc(String newLongdesc)
    {
        longdesc = newLongdesc;
    }

    // Maximum chars for text fields - For INPUT HTML elements

    public void setMaxlength(String newMaxlength) 
    {
        maxlength = newMaxlength;
    }

    // HTTP method used to submit the form -
    // For FORM HTML element

    public void setMethod(String newMethod)
    {
        method = newMethod;
    }

    // Default is single selection - For SELECT HTML element

    public void setMultiple(String newMultiple)
    {
        multiple = newMultiple;
    }
 
    // Element name - For BUTTON/TEXTAREA/SELECT/FORM/
    // IMG/"A" (Hyperlink)/INPUT HTML elements 

    public void setName(String newName)
    {
        name = newName;
    }

    // Element lost focus - For "A" (Hyperlink)/BUTTON/INPUT/
    // LABEL/SELECT/TEXTAREA HTML elements

    public void setOnblur(String newOnblur)
    {
        onblur = newOnblur;
    }

    // Element value was changed - For INPUT/SELECT/TEXTAREA HTML elements

    public void setOnchange(String newOnchange)
    {
        onchange = newOnchange;
    }

    // A pointer button was clicked - For all HTML elements pertaining
    // to Faces

    public void setOnclick(String newOnclick)
    {
        onclick = newOnclick;
    }

    // A pointer button was double clicked - For all HTML 
    // elements pertaining to Faces

    public void setOndblclick(String newOndblclick)
    {
        ondblclick = newOndblclick;
    }
    
    // Element got the focus - For "A" (Hyperlink)/BUTTON/
    // INPUT/LABEL/SELECT/TEXTAREA HTML elements

    public void setOnfocus(String newOnfocus)
    {
        onfocus = newOnfocus;
    }

    // A key was pressed down - For all HTML elements pertaining
    // to Faces

    public void setOnkeydown(String newOnkeydown)
    {
        onkeydown = newOnkeydown;
    }
    
    // A key was pressed and released - For all HTML elements
    // pertaining to Faces.

    public void setOnkeypress(String newOnkeypress)
    {
        onkeypress = newOnkeypress;
    }

    // A key was released - For all HTML elements pertaining
    // to Faces

    public void setOnkeyup(String newOnkeyup)
    {
        onkeyup = newOnkeyup;
    }

    // A pointer button was pressed down - For all HTML
    // elements pertaining to Faces.

    public void setOnmousedown(String newOnmousedown)
    {
        onmousedown = newOnmousedown;
    }

    // A pointer was moved within - For all HTML elements
    // pertaining to Faces.

    public void setOnmousemove(String newOnmousemove)
    {
        onmousemove = newOnmousemove;
    }

    // A pointer was moved away - For all HTML elements
    // pertaining to Faces.

    public void setOnmouseout(String newOnmouseout)
    {
        onmouseout = newOnmouseout;
    }

    // A pointer was moved onto - For all HTML elements
    // pertaining to Faces.

    public void setOnmouseover(String newOnmouseover)
    {
        onmouseover = newOnmouseover;
    }

    // A pointer button was released - For all HTML elements
    // pertaining to Faces.

    public void setOnmouseup(String newOnmouseup)
    {
        onmouseup = newOnmouseup;
    }

    // The form was reset - For FORM HTML element

    public void setOnreset(String newOnreset)
    {
        onreset= newOnreset;
    }

    // Some text was selected - For SELECT HTML element.

    public void setOnselect(String newOnselect)
    {
        onselect = newOnselect;
    }

    // The form was submitted - For FORM HTML element

    public void setOnsubmit(String newOnsubmit)
    {
        onsubmit = newOnsubmit;
    }

    // For TEXTAREA/INPUT HTML elements

    public void setReadonly(String newReadonly)
    {
        readonly = newReadonly;
    }

    // Forward link types - For "A" (Hyperlink) HTML element

    public void setRel(String newRel)
    {
        rel = newRel;
    }

    // Reverse link types - For "A" (Hyperlink) HTML element

    public void setRev(String newRev)
    {
        rev = newRev;
    }

    // For TEXTAREA HTML element

    public void setRows(String newRows)
    {
        rows = newRows;
    }
    
    // For OPTION HTML element

    public void setSelected(String newSelected)
    {
        selected = newSelected;
    }

    // For use with client-side image maps -
    // For "A" (Hyperlink) HTML element

    public void setShape(String newShape)
    {
        shape = newShape;
    }

    // For INPUT/SELECT HTML elements - (means rows visible
    // for SELECT)

    public void setSize(String newSize)
    {
        size = newSize;
    }

    // For INPUT/IMG HTML elements

    public void setSrc(String newSrc)
    {
        src = newSrc;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    // Position in tabbing order - For "A" (Hyperlink)/
    // BUTTON/INPUT/SELECT/TEXTAREA HTML elements.

    public void setTabindex(String newTabindex)
    {
        tabindex = newTabindex;
    }

    // Render in this frame - For "A" (Hyperlink)/ FORM
    // HTML elements

    public void setTarget(String newTarget)
    {
        target = newTarget;
    }

    // Advisory title - For all HTML elements pertaining
    // to Faces.

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }
    
    // For "A" (Hyperlink)/INPUT/BUTTON HTML elements

    public void setType(String newType)
    {
        type = newType;
    }

    // Use client-side image map - For IMG/INPUT HTML elements.

    public void setUsemap(String newUsemap)
    {
        usemap = newUsemap;
    }

    // For INPUT/OPTION/BUTTON HTML elements.
    // Required for INPUT type = radio/checkbox

    public void setValue(String newValue)
    {
        value = newValue;
    }
    
    // HTML 4.0 table attributes
    
   public  void setSummary(String newSummary) {
       this.summary = newSummary;
   } 

   public  void setWidth(String newWidth) {
       this.width = newWidth;
   } 
   
   public  void setBgcolor(String newColor) {
       this.bgcolor = newColor;
   }
   
   public  void setFrame(String newFrame) {
       this.frame = newFrame;
   }
   
   public  void setRules(String newRules) {
       this.rules = newRules;
   }
   
   public  void setBorder(String newBorder) {
       this.border = newBorder;
   }
   
   public  void setCellspacing(String newCellspacing) {
       this.cellspacing = newCellspacing;
   }
   
   public  void setCellpadding(String newCellpadding) {
       this.cellpadding = newCellpadding;
   }

    /**

    * PENDING(edburns): for some VERY strange reason, sometimes the id
    * gets set with double double quotes, even though the jsp has single
    * double quotes.  WHAAAAT!

    */ 
    public void setId(String newId) { 
	int i = 0;

	if (-1 != newId.indexOf("\"")) {
	    id = newId.substring(1, newId.length() - 1);
	}
	else {
	    id = newId;
	}
    }
    
    public String getId() {
	return id;
    }



//
// General Methods
//

    /**
     * <p>Evaluate the ID attribute as an EL expression if
     * the value is surrounded by the standard EL delimiters.</p>
     * @throws JspException if an error occurs during evaluation.
     */
    private void evaluateId() throws JspException {        
        if (id != null) {
            String expression = id;
            ExpressionInfo exprInfo = new ExpressionInfo();
            exprInfo.setExpressionString(expression);
            exprInfo.setExpectedType(String.class);
            exprInfo.setVariableResolver(new JspVariableResolver(pageContext));
            try {
                id = (String)
                    Util.getExpressionEvaluator(RIConstants.JSP_EL_PARSER).evaluate(exprInfo);
            } catch (ElException ele) {
                throw new JspException(ele.getMessage(), ele);
            }
        }
    }


//
// Methods from Superclass
// 


protected void overrideProperties(UIComponent component) 
{
    super.overrideProperties(component);
    String keyAttr = null;

    if ( component instanceof ValueHolder ) {
        ValueHolder valueHolder = (ValueHolder)component;
        if (null != valueRef) {
            valueHolder.setValueRef(valueRef);
        }    
	if (null != value) {
	    valueHolder.setValue(value);
	}
        if (null != converter) {           
            valueHolder.setConverter(converter);
        }
    }    

    if (null != key) {
	component.getAttributes().put("key", key);
    }
    if (null != imageKey) {
	component.getAttributes().put("imageKey", imageKey);
    }
    if (null != bundle) {
	component.getAttributes().put(RIConstants.BUNDLE_ATTR, bundle);
    }
    if (null != formatPattern) {
	component.getAttributes().put("formatPattern", formatPattern);
    }
    if (null != dateStyle) {
	component.getAttributes().put("dateStyle", dateStyle);
    }
    if (null != timeStyle) {
	component.getAttributes().put("timeStyle", timeStyle);
    }
    if (null != timezone) {
	component.getAttributes().put("timezone", timezone);
    }

    // HTML 4.0 event handlers common to most BODY-content elements.
    if (null != onclick) {
	component.getAttributes().put("onclick", onclick);
    }
    if (null != ondblclick) {
	component.getAttributes().put("ondblclick", ondblclick);
    }

    if (null != onkeydown) {
	component.getAttributes().put("onkeydown", onkeydown);
    }
    if (null != onkeypress) {
	component.getAttributes().put("onkeypress", onkeypress);
    }
    if (null != onkeyup) {
	component.getAttributes().put("onkeyup", onkeyup);
    }
    if (null != onmousedown) {
	component.getAttributes().put("onmousedown", onmousedown);
    }
    if (null != onmousemove) {
	component.getAttributes().put("onmousemove", onmousemove);
    }
    if (null != onmouseout) {
	component.getAttributes().put("onmouseout", onmouseout);
    }
    if (null != onmouseover) {
	component.getAttributes().put("onmouseover", onmouseover);
    }
    if (null != onmouseup) {
	component.getAttributes().put("onmouseup", onmouseup);
    }
    if (null != onfocus) {
        component.getAttributes().put("onfocus", onfocus); 
    }
    if (null != onblur) {
        component.getAttributes().put("onblur", onblur);
    }
    
    // common HTML 4.0 attributes.
    // PENDING (visvan) id attribute clashes with faces id attribute
    if (null != title) {
        component.getAttributes().put("title", title);
    }
    if (null != disabled) {
	component.getAttributes().put("disabled", disabled);
    }
    if (null != tabindex) {
	component.getAttributes().put("tabindex", tabindex);
    }
    if (null != accesskey) {
	component.getAttributes().put("accesskey", accesskey);
    }
    if (null != lang) {
	component.getAttributes().put("lang", lang);
    }
    if (null != dir) {
	component.getAttributes().put("dir", dir);
    }
    if (null != style) {
        component.getAttributes().put("style", style);
    }
    if (null != styleClass) {
        component.getAttributes().put("styleClass", styleClass);
    }   
    if (null != dateStyle) {
	component.getAttributes().put("dateStyle", dateStyle);
    }
    if (null != timeStyle) {
	component.getAttributes().put("timeStyle", timeStyle);
    }
  
}

protected String getDebugString() {
    String result = 
	"id: " + this.getId() + "\n " +
	"class: " + this.getClass().getName() + "\n ";
    return result;
}

// 
// Methods From TagSupport
//

public int doStartTag() throws JspException {
    int rc = 0;
    try {        
        evaluateId();
	rc = super.doStartTag();
    }
    catch (JspException e) {
	if (log.isDebugEnabled()) {
	    log.debug(getDebugString());
	}
	throw e;
    }
    catch (Throwable t) {
	if (log.isDebugEnabled()) {
	    log.debug(getDebugString());
	}
	throw new JspException(t);
    }
    return rc;
}

public int doEndTag() throws JspException {
    int rc = 0;
    try {
	rc = super.doEndTag();
    }
    catch (JspException e) {
	if (log.isDebugEnabled()) {
	    log.debug(getDebugString());
	}
	throw e;
    }
    catch (Throwable t) {
	if (log.isDebugEnabled()) {
	    log.debug(getDebugString());
	}
	throw new JspException(t);
    }
    return rc;
}

	

} // end of class FacesTag
