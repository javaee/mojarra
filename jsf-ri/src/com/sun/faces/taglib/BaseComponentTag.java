/*
 * $Id: BaseComponentTag.java,v 1.8 2003/10/17 03:47:14 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BaseComponentTag.java

package com.sun.faces.taglib;

import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.JspVariableResolver;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.ConvertableValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

/**
 *
 *  <B>BaseComponentTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: BaseComponentTag.java,v 1.8 2003/10/17 03:47:14 eburns Exp $ 
 */

public abstract class BaseComponentTag extends UIComponentTag
{
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(BaseComponentTag.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    // each attribute instance variable has a matching <name>_ variable to
    // store the original value. The set method may not be called in a container
    // that supports tag pooling. Must be able to get original value in order
    // to re-evaluate the same expression if present.

    // core attrs
    protected String styleClass = null;
    protected String styleClass_ = null;
    protected String title = null;
    protected String title_ = null;

    // events attrs
    protected String onclick = null;
    protected String onclick_ = null;
    protected String ondblclick = null;
    protected String ondblclick_ = null;
    protected String onmousedown = null;
    protected String onmousedown_ = null;
    protected String onmouseover = null;
    protected String onmouseover_ = null;
    protected String onmousemove = null;
    protected String onmousemove_ = null;
    protected String onmouseout = null;
    protected String onmouseout_ = null;
    protected String onkeypress = null;
    protected String onkeypress_ = null;
    protected String onkeydown = null;
    protected String onkeydown_ = null;
    protected String onkeyup = null;
    protected String onkeyup_ = null;

    // i18n attrs
    protected String lang = null;
    protected String lang_ = null;
    protected String dir = null;
    protected String dir_ = null;
    protected String key = null;
    protected String key_ = null;
    protected String bundle = null;
    protected String bundle_ = null;
    protected String height = null; 
    protected String height_ = null;
    protected String width = null;
    protected String width_ = null;
    protected String cellspacing = null;
    protected String cellspacing_ = null;
    protected String cellpadding = null;
    protected String cellpadding_ = null;
    
    // non-String attrs
    protected boolean disabled = false;
    protected int size = Integer.MIN_VALUE;
    protected int tabindex = Integer.MIN_VALUE;
    protected boolean checked = false;
    
    protected int border = Integer.MIN_VALUE;
    protected boolean readonly = false;
    protected boolean ismap = false;
    protected int maxlength = Integer.MIN_VALUE;
    protected int rows = Integer.MIN_VALUE;
    protected int cols = Integer.MIN_VALUE;

    protected String imageKey = null;
    protected String imageKey_ = null;
    protected String formatStyle = null;
    protected String formatStyle_ = null;
    protected String dateStyle = null;
    protected String dateStyle_ = null;
    protected String timeStyle = null;
    protected String timeStyle_ = null;
    protected String timezone = null;
    protected String timezone_ = null;
    protected String formatPattern = null;
    protected String formatPattern_ = null;

    protected String accept = null;
    protected String accept_ = null;
    protected String acceptcharset = null;
    protected String acceptcharset_ = null;
    protected String accesskey = null;
    protected String accesskey_ = null;
    protected String action = null;
    protected String action_ = null;
    protected String alt = null;
    protected String alt_ = null;
    protected String charset = null;
    protected String charset_ = null;
    protected String coords = null;
    protected String coords_ = null;
    protected String enctype = null;
    protected String enctype_ = null;
    protected String htmlFor = null;
    protected String htmlFor_ = null;
    protected String href = null;
    protected String href_ = null;
    protected String hreflang = null;
    protected String hreflang_ = null;
    protected String hspace = null;
    protected String hspace_ = null;
    
    protected String label = null;
    protected String label_ = null;
    protected String longdesc = null;
    protected String longdesc_ = null;
    protected String method = null;
    protected String method_ = null;
    protected String multiple = null;
    protected String multiple_ = null;
    protected String name = null;
    protected String name_ = null;
    protected String onblur = null;
    protected String onblur_ = null;
    protected String onchange = null;
    protected String onchange_ = null;
    protected String onfocus = null;
    protected String onfocus_ = null;
    protected String onmouseup = null;
    protected String onmouseup_ = null;
    protected String onreset = null;
    protected String onreset_ = null;
    protected String onselect = null;
    protected String onselect_ = null;
    protected String onsubmit = null;
    protected String onsubmit_ = null;
    protected String rel = null;
    protected String rel_ = null;
    protected String rev = null;
    protected String rev_ = null;
    protected String selected = null;
    protected String selected_ = null;
    protected String shape = null;
    protected String shape_ = null;
    protected String src = null;
    protected String src_ = null;
    protected String style = null;
    protected String style_ = null;
    protected String target = null;
    protected String target_ = null;
    protected String type = null;
    protected String type_ = null;
    protected String usemap = null;
    protected String usemap_ = null;
    protected String value = null;
    protected String value_ = null;

    // HTML 4.0 table attributes
    protected String summary = null;
    protected String summary_ = null;
    protected String bgcolor = null;
    protected String bgcolor_ = null;
    protected String frame = null;
    protected String frame_ = null;
    protected String rules = null;
    protected String rules_ = null;

    protected String valueRef = null;
    protected String valueRef_ = null;
    protected Converter converter = null;

    protected String id_ = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public BaseComponentTag()
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
	valueRef_ = newValueRef;
    }
    
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

  
    public void setKey(String newKey)
    {
	key_ = newKey;
    }

    public void setImageKey(String newImageKey)
    {
	imageKey_ = newImageKey;
    }

    public void setBundle(String newBundle)
    {
	bundle_ = newBundle;
    }

    public void setFormatStyle(String newFormatStyle)
    {
	formatStyle_ = newFormatStyle;
    }
    
    public void setDateStyle(String newDateStyle)
    {
	dateStyle_ = newDateStyle;
    }
    
    public void setTimeStyle(String newTimeStyle)
    {
	timeStyle_ = newTimeStyle;
    }

    public void setTimezone(String newTimezone)
    {
	timezone_ = newTimezone;
    }
    
    public void setFormatPattern(String newFormatPattern)
    {
	formatPattern_ = newFormatPattern;
    }

    // List of MIME types for file upload - 
    // For FORM/INPUT HTML elements

    public void setAccept(String newAccept) 
    {
        accept_ = newAccept;
    }

    // List of supported char sets - For FORM HTML element

    public void setAcceptcharset(String newAcceptcharset) 
    {
        acceptcharset_ = newAcceptcharset;
    }
    
    // Accessibility key char -
    // For "A" (Hyperlink)/BUTTON/INPUT/LABEL/TEXTAREA HTML elements

    public void setAccesskey(String newAccesskey) 
    {
        accesskey_ = newAccesskey;
    }

    // Server-side form handler - For FORM HTML element

    public void setAction(String newAction) 
    {
        action_ = newAction;
    }

    // Short description - For INPUT HTML element

    public void setAlt(String newAlt)
    {
        alt_ = newAlt;
    }

    // Char encoding for linked resource -
    // For "A" (Hyperlink) HTML element

    public void setCharset(String newCharset) 
    {
        charset_ = newCharset;
    }

    // For radio buttons and checkboxes - For INPUT element

    public void setChecked(boolean newChecked) 
    {
        checked = newChecked;
    }

    // Space seperated list of classes -
    // For all HTML elements pertaining to Faces

    // For TEXTAREA HTML element

    public void setCols(int newCols)
    {
        cols = newCols;
    }

    // For use with client side image maps - 
    // For "A" (Hyperlink) HTML element

    public void setCoords(String newCoords)
    {
        coords_ = newCoords;
    }
  
    // Direction for weak/neutral text - For all HTML elements 
    // pertaining to Faces

    public void setDir(String newDir) 
    {
        dir_ = newDir;
    }

    // Means "unavailable" in this context -
    // For BUTTON/INPUT/OPTGROUP/OPTION/SELECT/TEXTAREA HTML elements 

    public void setDisabled(boolean newDisabled)
    {
        disabled = newDisabled;
    }

    // For FORM HTML element

    public void setEnctype(String newEnctype)
    {
        enctype_ = newEnctype;
    }

    // Matches field "id" value - For LABEL HTML element

    public void setHtmlFor(String newHtmlFor)
    {
        htmlFor_ = newHtmlFor;
    }

    // Override height - For IMG HTML element

    public void setHeight(String newHeight)
    {
        height_ = newHeight;
    }

    // For "A" (Hyperlink) HTML element

    public void setHref(String newHref)
    {
        href_ = newHref;
    }

    // Language Code - For "A" (Hyperlink) HTML element

    public void setHreflang(String newHreflang)
    {
        hreflang_ = newHreflang;
    }

    // Horizontal space - For IMG HTML element

    public void setHspace(String newHspace)
    {
        hspace_ = newHspace;
    }

    // Use server side image map - For IMG/INPUT HTML elements

    public void setIsmap(boolean newIsmap)
    {
        ismap = newIsmap;
    }

    // For OPTION/OPTGROUP HTML elements

    public void setLabel(String newLabel)
    {
        label_ = newLabel;
    }

    // Language Code - For all HTML elements pertaining to Faces

    public void setLang(String newLang)
    {
        lang_ = newLang;
    }

    // Link to long description - For IMG HTML element

    public void setLongdesc(String newLongdesc)
    {
        longdesc_ = newLongdesc;
    }

    // Maximum chars for text fields - For INPUT HTML elements

    public void setMaxlength(int newMaxlength) 
    {
        maxlength = newMaxlength;
    }

    // HTTP method used to submit the form -
    // For FORM HTML element

    public void setMethod(String newMethod)
    {
        method_ = newMethod;
    }

    // Default is single selection - For SELECT HTML element

    public void setMultiple(String newMultiple)
    {
        multiple_ = newMultiple;
    }
 
    // Element name - For BUTTON/TEXTAREA/SELECT/FORM/
    // IMG/"A" (Hyperlink)/INPUT HTML elements 

    public void setName(String newName)
    {
        name_ = newName;
    }

    // Element lost focus - For "A" (Hyperlink)/BUTTON/INPUT/
    // LABEL/SELECT/TEXTAREA HTML elements

    public void setOnblur(String newOnblur)
    {
        onblur_ = newOnblur;
    }

    // Element value was changed - For INPUT/SELECT/TEXTAREA HTML elements

    public void setOnchange(String newOnchange)
    {
        onchange_ = newOnchange;
    }

    // A pointer button was clicked - For all HTML elements pertaining
    // to Faces

    public void setOnclick(String newOnclick)
    {
        onclick_ = newOnclick;
    }

    // A pointer button was double clicked - For all HTML 
    // elements pertaining to Faces

    public void setOndblclick(String newOndblclick)
    {
        ondblclick_ = newOndblclick;
    }
    
    // Element got the focus - For "A" (Hyperlink)/BUTTON/
    // INPUT/LABEL/SELECT/TEXTAREA HTML elements

    public void setOnfocus(String newOnfocus)
    {
        onfocus_ = newOnfocus;
    }

    // A key was pressed down - For all HTML elements pertaining
    // to Faces

    public void setOnkeydown(String newOnkeydown)
    {
        onkeydown_ = newOnkeydown;
    }
    
    // A key was pressed and released - For all HTML elements
    // pertaining to Faces.

    public void setOnkeypress(String newOnkeypress)
    {
        onkeypress_ = newOnkeypress;
    }

    // A key was released - For all HTML elements pertaining
    // to Faces

    public void setOnkeyup(String newOnkeyup)
    {
        onkeyup_ = newOnkeyup;
    }

    // A pointer button was pressed down - For all HTML
    // elements pertaining to Faces.

    public void setOnmousedown(String newOnmousedown)
    {
        onmousedown_ = newOnmousedown;
    }

    // A pointer was moved within - For all HTML elements
    // pertaining to Faces.

    public void setOnmousemove(String newOnmousemove)
    {
        onmousemove_ = newOnmousemove;
    }

    // A pointer was moved away - For all HTML elements
    // pertaining to Faces.

    public void setOnmouseout(String newOnmouseout)
    {
        onmouseout_ = newOnmouseout;
    }

    // A pointer was moved onto - For all HTML elements
    // pertaining to Faces.

    public void setOnmouseover(String newOnmouseover)
    {
        onmouseover_ = newOnmouseover;
    }

    // A pointer button was released - For all HTML elements
    // pertaining to Faces.

    public void setOnmouseup(String newOnmouseup)
    {
        onmouseup_ = newOnmouseup;
    }

    // The form was reset - For FORM HTML element

    public void setOnreset(String newOnreset)
    {
        onreset= newOnreset;
    }

    // Some text was selected - For SELECT HTML element.

    public void setOnselect(String newOnselect)
    {
        onselect_ = newOnselect;
    }

    // The form was submitted - For FORM HTML element

    public void setOnsubmit(String newOnsubmit)
    {
        onsubmit_ = newOnsubmit;
    }

    // For TEXTAREA/INPUT HTML elements

    public void setReadonly(boolean newReadonly)
    {
        readonly = newReadonly;
    }

    // Forward link types - For "A" (Hyperlink) HTML element

    public void setRel(String newRel)
    {
        rel_ = newRel;
    }

    // Reverse link types - For "A" (Hyperlink) HTML element

    public void setRev(String newRev)
    {
        rev_ = newRev;
    }

    // For TEXTAREA HTML element

    public void setRows(int newRows)
    {
        rows = newRows;
    }
    
    // For OPTION HTML element

    public void setSelected(String newSelected)
    {
        selected_ = newSelected;
    }

    // For use with client-side image maps -
    // For "A" (Hyperlink) HTML element

    public void setShape(String newShape)
    {
        shape_ = newShape;
    }

    // For INPUT/SELECT HTML elements - (means rows visible
    // for SELECT)

    public void setSize(int newSize)
    {
        size = newSize;
    }

    // For INPUT/IMG HTML elements

    public void setSrc(String newSrc)
    {
        src_ = newSrc;
    }

    public void setStyle(String style) {
        this.style_ = style;
    }
    
    public void setStyleClass(String styleClass) {
        this.styleClass_ = styleClass;
    }

    // Position in tabbing order - For "A" (Hyperlink)/
    // BUTTON/INPUT/SELECT/TEXTAREA HTML elements.

    public void setTabindex(int newTabindex)
    {
        tabindex = newTabindex;
    }

    // Render in this frame - For "A" (Hyperlink)/ FORM
    // HTML elements

    public void setTarget(String newTarget)
    {
        target_ = newTarget;
    }

    // Advisory title - For all HTML elements pertaining
    // to Faces.

    public void setTitle(String newTitle)
    {
        title_ = newTitle;
    }
    
    // For "A" (Hyperlink)/INPUT/BUTTON HTML elements

    public void setType(String newType)
    {
        type_ = newType;
    }

    // Use client-side image map - For IMG/INPUT HTML elements.

    public void setUsemap(String newUsemap)
    {
        usemap_ = newUsemap;
    }

    // For INPUT/OPTION/BUTTON HTML elements.
    // Required for INPUT type = radio/checkbox

    public void setValue(String newValue)
    {
        value_ = newValue;
    }
    
    // HTML 4.0 table attributes
    
   public  void setSummary(String newSummary) {
       this.summary_ = newSummary;
   } 

   public  void setWidth(String newWidth) {
       this.width_ = newWidth;
   } 
   
   public  void setBgcolor(String newColor) {
       this.bgcolor_ = newColor;
   }
   
   public  void setFrame(String newFrame) {
       this.frame_ = newFrame;
   }
   
   public  void setRules(String newRules) {
       this.rules_ = newRules;
   }
   
   public  void setBorder(int newBorder) {
       this.border = newBorder;
   }
   
   public  void setCellspacing(String newCellspacing) {
       this.cellspacing_ = newCellspacing;
   }
   
   public  void setCellpadding(String newCellpadding) {
       this.cellpadding_ = newCellpadding;
   }

    /**

    * PENDING(edburns): for some VERY strange reason, sometimes the id
    * gets set with double double quotes, even though the jsp has single
    * double quotes.  WHAAAAT!

    */ 
    public void setId(String newId) { 	

	if (-1 != newId.indexOf("\"")) {
	    id_ = newId.substring(1, newId.length() - 1);
	}
	else {
	    id_ = newId;
	}
    }
    
    public String getId() {
	return id;
    }



    //
    // General Methods
    //

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (id_ != null) {
            id = Util.evaluateElExpression(id_, pageContext);
        }
        if (key_ != null) {
            key = Util.evaluateElExpression(key_, pageContext);
        }
        if (imageKey_ != null) {
            imageKey = Util.evaluateElExpression(imageKey_, pageContext);
        }
        if (bundle_ != null) {
            bundle = Util.evaluateElExpression(bundle_, pageContext);
        }
        if (formatStyle_ != null) {
            formatStyle = Util.evaluateElExpression(formatStyle_, pageContext);
        }
        if (dateStyle_ != null) {
            dateStyle = Util.evaluateElExpression(dateStyle_, pageContext);
        }
        if (timeStyle_ != null) {
            timeStyle = Util.evaluateElExpression(timeStyle_, pageContext);
        }
        if (timezone_ != null) {
            timezone = Util.evaluateElExpression(timezone_, pageContext);
        }
        if (formatPattern_ != null) {
            formatPattern = Util.evaluateElExpression(formatPattern_, pageContext);
        }
        if (accept_ != null) {
            accept = Util.evaluateElExpression(accept_, pageContext);
        }
        if (acceptcharset_ != null) {
            acceptcharset = Util.evaluateElExpression(acceptcharset_, pageContext);
        }
        if (accesskey_ != null) {
            accesskey = Util.evaluateElExpression(accesskey_, pageContext);
        }
        if (action_ != null) {
            action = Util.evaluateElExpression(action_, pageContext);
        }
        if (alt_ != null) {
            alt = Util.evaluateElExpression(alt_, pageContext);
        }
        if (charset_ != null) {
            charset = Util.evaluateElExpression(charset_, pageContext);
        }
        if (coords_ != null) {
            coords = Util.evaluateElExpression(coords_, pageContext);
        }
        if (dir_ != null) {
            dir = Util.evaluateElExpression(dir_, pageContext);
        }
        if (enctype_ != null) {
            enctype = Util.evaluateElExpression(enctype_, pageContext);
        }
        if (htmlFor_ != null) {
            htmlFor = Util.evaluateElExpression(htmlFor_, pageContext);
        }
        if (href_ != null) {
            href = Util.evaluateElExpression(href_, pageContext);
        }
        if (hreflang_ != null) {
            hreflang = Util.evaluateElExpression(hreflang_, pageContext);
        }
        if (hspace_ != null) {
            hspace = Util.evaluateElExpression(hspace_, pageContext);
        }
        if (label_ != null) {
            label = Util.evaluateElExpression(label_, pageContext);
        }
        if (lang_ != null) {
            lang = Util.evaluateElExpression(lang_, pageContext);
        }
        if (longdesc_ != null) {
            longdesc = Util.evaluateElExpression(longdesc_, pageContext);
        }
        if (method_ != null) {
            method = Util.evaluateElExpression(method_, pageContext);
        }
        if (multiple_ != null) {
            multiple = Util.evaluateElExpression(multiple_, pageContext);
        }
        if (name_ != null) {
            name = Util.evaluateElExpression(name_, pageContext);
        }
        if (onblur_ != null) {
            onblur = Util.evaluateElExpression(onblur_, pageContext);
        }
        if (onchange_ != null) {
            onchange = Util.evaluateElExpression(onchange_, pageContext);
        }
        if (onclick_ != null) {
            onclick = Util.evaluateElExpression(onclick_, pageContext);
        }
        if (ondblclick_ != null) {
            ondblclick = Util.evaluateElExpression(ondblclick_, pageContext);
        }
        if (onfocus_ != null) {
            onfocus = Util.evaluateElExpression(onfocus_, pageContext);
        }
        if (onkeydown_ != null) {
            onkeydown = Util.evaluateElExpression(onkeydown_, pageContext);
        }
        if (onkeypress_ != null) {
            onkeypress = Util.evaluateElExpression(onkeypress_, pageContext);
        }
        if (onkeyup_ != null) {
            onkeyup = Util.evaluateElExpression(onkeyup_, pageContext);
        }
        if (onmousedown_ != null) {
            onmousedown = Util.evaluateElExpression(onmousedown_, pageContext);
        }
        if (onmouseover_ != null) {
            onmouseover = Util.evaluateElExpression(onmouseover_, pageContext);
        }
        if (onmouseup_ != null) {
            onmouseup = Util.evaluateElExpression(onmouseup_, pageContext);
        }
        if (onreset_ != null) {
            onreset = Util.evaluateElExpression(onreset_, pageContext);
        }
        if (onselect_ != null) {
            onselect = Util.evaluateElExpression(onselect_, pageContext);
        }
        if (onsubmit_ != null) {
            onsubmit = Util.evaluateElExpression(onsubmit_, pageContext);
        }
        if (rel_ != null) {
            rel = Util.evaluateElExpression(rel_, pageContext);
        }
        if (rev_ != null) {
            rev = Util.evaluateElExpression(rev_, pageContext);
        }
        if (selected_ != null) {
            selected = Util.evaluateElExpression(selected_, pageContext);
        }
        if (shape_ != null) {
            shape = Util.evaluateElExpression(shape_, pageContext);
        }
        if (src_ != null) {
            src = Util.evaluateElExpression(src_, pageContext);
        }
        if (style_ != null) {
            style = Util.evaluateElExpression(style_, pageContext);
        }
        if (styleClass_ != null) {
            styleClass = Util.evaluateElExpression(styleClass_, pageContext);
        }
        if (target_ != null) {
            target = Util.evaluateElExpression(target_, pageContext);
        }
        if (title_ != null) {
            title = Util.evaluateElExpression(title_, pageContext);
        }
        if (type_ != null) {
            type = Util.evaluateElExpression(type_, pageContext);
        }
        if (usemap_ != null) {
            usemap = Util.evaluateElExpression(usemap_, pageContext);
        }
        if (value_ != null) {
            value = Util.evaluateElExpression(value_, pageContext);
        }
        if (summary_ != null) {
            summary = Util.evaluateElExpression(summary_, pageContext);
        }
        if (bgcolor_ != null) {
            bgcolor = Util.evaluateElExpression(bgcolor_, pageContext);
        }
        if (frame_ != null) {
            frame = Util.evaluateElExpression(frame_, pageContext);
        }
        if (rules_ != null) {
            rules = Util.evaluateElExpression(rules_, pageContext);
        }
        if (valueRef_ != null) {
            valueRef = Util.evaluateElExpression(valueRef_, pageContext);
        }
        if (height_ != null) {
            height = Util.evaluateElExpression(height_, pageContext);
        }
        if (width_ != null) {
            width = Util.evaluateElExpression(width_, pageContext);
        }
        if (cellspacing_ != null) {
            cellspacing = Util.evaluateElExpression(cellspacing_, pageContext);
        }
        if (cellpadding_ != null) {
            cellpadding = Util.evaluateElExpression(cellpadding_, pageContext);
        }
    }


    //
    // Methods from Superclass
    // 
    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);

        if ( component instanceof ValueHolder ) {
            ValueHolder valueHolder = (ValueHolder)component;
            if (null != valueRef) {
                valueHolder.setValueRef(valueRef);
            }
            if (null != value) {
                valueHolder.setValue(value);
	    }
        }	

        if (component instanceof ConvertableValueHolder) {
            if (null != converter) {
                ((ConvertableValueHolder) component).setConverter(converter);
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
        if (disabled) {
            component.getAttributes().put("disabled",
                                       disabled ? Boolean.TRUE : Boolean.FALSE);
        }
        if (tabindex != Integer.MIN_VALUE) {
            component.getAttributes().put("tabindex", new Integer(tabindex));
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
            evaluateExpressions();
	    rc = super.doStartTag();
        }
        catch (JspException e) {
	    if (log.isDebugEnabled()) {
	        log.debug(getDebugString(), e);
	    }
	    throw e;
        }
        catch (Throwable t) {
	    if (log.isDebugEnabled()) {
	        log.debug(getDebugString(), t);
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
	        log.debug(getDebugString(), e);
	    }
	    throw e;
        }
        catch (Throwable t) {
	    if (log.isDebugEnabled()) {
	        log.debug(getDebugString(), t);
	    }
	    throw new JspException(t);
        }
        return rc;
    }


} // end of class BaseComponentTag
