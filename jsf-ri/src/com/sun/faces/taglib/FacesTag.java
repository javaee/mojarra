/*
 * $Id: FacesTag.java,v 1.39 2003/08/13 02:08:05 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTag.java

package com.sun.faces.taglib;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletRequest;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIInput;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: FacesTag.java,v 1.39 2003/08/13 02:08:05 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

    protected boolean required = false;
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
    protected String commandClass = null;
    protected String graphicClass = null;
    protected String inputClass = null;
    protected String outputClass = null;
    protected String formClass = null;
    protected String selectbooleanClass = null;
    protected String selectmanyClass = null;
    protected String selectoneClass = null;
    protected String selectitemClass = null;
    protected String selectitemsClass = null;
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
    protected String converter = null;
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
    public void setRequired(boolean newVal) {
	required = newVal;
    }

    public String getValueRef()
    {
	return valueRef;
    }
    
    public void setValueRef(String newValueRef)
    {
	valueRef = newValueRef;
    }
    
    public void setConverter(String newConverter) {
        this.converter = newConverter;
    }

  
    public String getKey()
    {
	return key;
    }
    
    public void setKey(String newKey)
    {
	key = newKey;
    }

    public String getImageKey()
    {
	return imageKey;
    }
    
    public void setImageKey(String newImageKey)
    {
	imageKey = newImageKey;
    }

    public String getBundle()
    {
	return bundle;
    }
    
    public void setBundle(String newBundle)
    {
	bundle = newBundle;
    }

    public String getFormatStyle()
    {
	return formatStyle;
    }
    
    public void setFormatStyle(String newFormatStyle)
    {
	formatStyle = newFormatStyle;
    }
    
    public String getDateStyle()
    {
	return dateStyle;
    }
    
    public void setDateStyle(String newDateStyle)
    {
	dateStyle = newDateStyle;
    }
    
    public String getTimeStyle()
    {
	return timeStyle;
    }
    
    public void setTimeStyle(String newTimeStyle)
    {
	timeStyle = newTimeStyle;
    }

    public String getTimezone()
    {
	return timezone;
    }
    
    public void setTimezone(String newTimezone)
    {
	timezone = newTimezone;
    }
    
    public String getFormatPattern()
    {
	return formatPattern;
    }
    
    public void setFormatPattern(String newFormatPattern)
    {
	formatPattern = newFormatPattern;
    }

    // List of MIME types for file upload - 
    // For FORM/INPUT HTML elements

    public String getAccept()
    {
        return accept;
    }

    public void setAccept(String newAccept) 
    {
        accept = newAccept;
    }

    // List of supported char sets - For FORM HTML element

    public String getAcceptcharset()
    {
        return acceptcharset;
    }

    public void setAcceptcharset(String newAcceptcharset) 
    {
        acceptcharset = newAcceptcharset;
    }
    
    // Accessibility key char -
    // For "A" (Hyperlink)/BUTTON/INPUT/LABEL/TEXTAREA HTML elements

    public String getAccesskey()
    {
        return accesskey;
    }

    public void setAccesskey(String newAccesskey) 
    {
        accesskey = newAccesskey;
    }

    // Server-side form handler - For FORM HTML element

    public String getAction()
    {
        return action;
    }

    public void setAction(String newAction) 
    {
        action = newAction;
    }

    // Short description - For INPUT HTML element

    public String getAlt()
    {
        return alt;
    }

    public void setAlt(String newAlt)
    {
        alt = newAlt;
    }

    // Char encoding for linked resource -
    // For "A" (Hyperlink) HTML element

    public String getCharset() 
    {
        return charset;
    }

    public void setCharset(String newCharset) 
    {
        charset = newCharset;
    }

    // For radio buttons and checkboxes - For INPUT element

    public String getChecked()
    {
        return checked;
    }

    public void setChecked(String newChecked) 
    {
        checked = newChecked;
    }

    // Space seperated list of classes -
    // For all HTML elements pertaining to Faces

    public String getCommandClass()
    {
        return commandClass;
    }

    public void setCommandClass(String newCommandClass) 
    {
        commandClass = newCommandClass;
    }

    public String getGraphicClass()
    {
        return graphicClass;
    }

    public void setGraphicClass(String newGraphicClass) 
    {
        graphicClass = newGraphicClass;
    }

    public String getInputClass()
    {
        return inputClass;
    }

    public void setInputClass(String newInputClass) 
    {
        inputClass = newInputClass;
    }

    public String getOutputClass()
    {
        return outputClass;
    }

    public void setOutputClass(String newOutputClass) 
    {
        outputClass = newOutputClass;
    }

    public String getSelectbooleanClass()
    {
        return selectbooleanClass;
    }

    public void setSelectbooleanClass(String newSelectbooleanClass) 
    {
        selectbooleanClass = newSelectbooleanClass;
    }

    public String getSelectmanyClass()
    {
        return selectmanyClass;
    }
    
    public void setSelectmanyClass(String newSelectmanyClass) 
    {
        selectmanyClass = newSelectmanyClass;
    }
    
    public void setFormClass(String newFormClass) 
    {
        formClass = newFormClass;
    }

    public String getFormClass()
    {
        return formClass;
    }

    public String getSelectoneClass()
    {
        return selectoneClass;
    }

    public void setSelectoneClass(String newSelectoneClass) 
    {
        selectoneClass = newSelectoneClass;
    }

    public String getSelectitemClass()
    {
        return selectitemClass;
    }

    public void setSelectitemClass(String newSelectitemClass) 
    {
        selectitemClass = newSelectitemClass;
    }

    public String getSelectitemsClass()
    {
        return selectitemsClass;
    }

    public void setSelectitemsClass(String newSelectitemsClass) 
    {
        selectitemsClass = newSelectitemsClass;
    }

    // For TEXTAREA HTML element

    public String getCols()
    {
        return cols;
    }

    public void setCols(String newCols)
    {
        cols = newCols;
    }

    // For use with client side image maps - 
    // For "A" (Hyperlink) HTML element

    public String getCoords()
    {
        return coords;
    }

    public void setCoords(String newCoords)
    {
        coords = newCoords;
    }
  
    // Direction for weak/neutral text - For all HTML elements 
    // pertaining to Faces

    public String getDir() 
    {
        return dir;
    }
    
    public void setDir(String newDir) 
    {
        dir = newDir;
    }

    // Means "unavailable" in this context -
    // For BUTTON/INPUT/OPTGROUP/OPTION/SELECT/TEXTAREA HTML elements 

    public String getDisabled()
    {
        return disabled;
    }

    public void setDisabled(String newDisabled)
    {
        disabled = newDisabled;
    }

    // For FORM HTML element

    public String getEnctype() 
    {
        return enctype;
    }

    public void setEnctype(String newEnctype)
    {
        enctype = newEnctype;
    }

    // Matches field "id" value - For LABEL HTML element

    public String getHtmlFor()
    {
        return htmlFor;
    }

    public void setHtmlFor(String newHtmlFor)
    {
        htmlFor = newHtmlFor;
    }

    // Override height - For IMG HTML element

    public String getHeight() 
    {
        return height;
    }

    public void setHeight(String newHeight)
    {
        height = newHeight;
    }

    // For "A" (Hyperlink) HTML element

    public String getHref() 
    {
        return href;
    }

    public void setHref(String newHref)
    {
        href = newHref;
    }

    // Language Code - For "A" (Hyperlink) HTML element

    public String getHreflang() 
    {
        return hreflang;
    }

    public void setHreflang(String newHreflang)
    {
        hreflang = newHreflang;
    }

    // Horizontal space - For IMG HTML element

    public String getHspace()
    {
        return hspace;
    }

    public void setHspace(String newHspace)
    {
        hspace = newHspace;
    }

    // Use server side image map - For IMG/INPUT HTML elements

    public String getIsmap() 
    {
        return ismap;
    }

    public void setIsmap(String newIsmap)
    {
        ismap = newIsmap;
    }

    // For OPTION/OPTGROUP HTML elements

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String newLabel)
    {
        label = newLabel;
    }

    // Language Code - For all HTML elements pertaining to Faces

    public String getLang() 
    {
        return lang;
    }

    public void setLang(String newLang)
    {
        lang = newLang;
    }

    // Link to long description - For IMG HTML element

    public String getLongdesc()
    {
        return longdesc;
    }

    public void setLongdesc(String newLongdesc)
    {
        longdesc = newLongdesc;
    }

    // Maximum chars for text fields - For INPUT HTML elements

    public String getMaxlength() 
    {
        return maxlength;
    }

    public void setMaxlength(String newMaxlength) 
    {
        maxlength = newMaxlength;
    }

    // HTTP method used to submit the form -
    // For FORM HTML element

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String newMethod)
    {
        method = newMethod;
    }

    // Default is single selection - For SELECT HTML element

    public String getMultiple()
    {
        return multiple;
    }

    public void setMultiple(String newMultiple)
    {
        multiple = newMultiple;
    }
 
    // Element name - For BUTTON/TEXTAREA/SELECT/FORM/
    // IMG/"A" (Hyperlink)/INPUT HTML elements 

    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    // Element lost focus - For "A" (Hyperlink)/BUTTON/INPUT/
    // LABEL/SELECT/TEXTAREA HTML elements

    public String getOnblur() 
    {
        return onblur;
    }

    public void setOnblur(String newOnblur)
    {
        onblur = newOnblur;
    }

    // Element value was changed - For INPUT/SELECT/TEXTAREA HTML elements
    public String getOnchange() 
    {
        return onchange;
    }

    public void setOnchange(String newOnchange)
    {
        onchange = newOnchange;
    }

    // A pointer button was clicked - For all HTML elements pertaining
    // to Faces

    public String getOnclick() 
    {
        return onclick;
    }

    public void setOnclick(String newOnclick)
    {
        onclick = newOnclick;
    }

    // A pointer button was double clicked - For all HTML 
    // elements pertaining to Faces

    public String getOndblclick() 
    {
        return ondblclick;
    }

    public void setOndblclick(String newOndblclick)
    {
        ondblclick = newOndblclick;
    }
    
    // Element got the focus - For "A" (Hyperlink)/BUTTON/
    // INPUT/LABEL/SELECT/TEXTAREA HTML elements

    public String getOnfocus() 
    {
        return onfocus;
    }

    public void setOnfocus(String newOnfocus)
    {
        onfocus = newOnfocus;
    }

    // A key was pressed down - For all HTML elements pertaining
    // to Faces

    public String getOnkeydown() 
    {
        return onkeydown;
    }

    public void setOnkeydown(String newOnkeydown)
    {
        onkeydown = newOnkeydown;
    }
    
    // A key was pressed and released - For all HTML elements
    // pertaining to Faces.

    public String getOnkeypress() 
    {
        return onkeypress;
    }

    public void setOnkeypress(String newOnkeypress)
    {
        onkeypress = newOnkeypress;
    }

    // A key was released - For all HTML elements pertaining
    // to Faces

    public String getOnkeyup() 
    {
        return onkeyup;
    }

    public void setOnkeyup(String newOnkeyup)
    {
        onkeyup = newOnkeyup;
    }

    // A pointer button was pressed down - For all HTML
    // elements pertaining to Faces.

    public String getOnmousedown() 
    {
        return onmousedown;
    }

    public void setOnmousedown(String newOnmousedown)
    {
        onmousedown = newOnmousedown;
    }

    // A pointer was moved within - For all HTML elements
    // pertaining to Faces.

    public String getOnmousemove() 
    {
        return onmousemove;
    }

    public void setOnmousemove(String newOnmousemove)
    {
        onmousemove = newOnmousemove;
    }

    // A pointer was moved away - For all HTML elements
    // pertaining to Faces.

    public String getOnmouseout() 
    {
        return onmouseout;
    }

    public void setOnmouseout(String newOnmouseout)
    {
        onmouseout = newOnmouseout;
    }

    // A pointer was moved onto - For all HTML elements
    // pertaining to Faces.

    public String getOnmouseover() 
    {
        return onmouseover;
    }

    public void setOnmouseover(String newOnmouseover)
    {
        onmouseover = newOnmouseover;
    }

    // A pointer button was released - For all HTML elements
    // pertaining to Faces.

    public String getOnmouseup() 
    {
        return onmouseup;
    }

    public void setOnmouseup(String newOnmouseup)
    {
        onmouseup = newOnmouseup;
    }

    // The form was reset - For FORM HTML element

    public String getOnreset() 
    {
        return onreset;
    }

    public void setOnreset(String newOnreset)
    {
        onreset= newOnreset;
    }

    // Some text was selected - For SELECT HTML element.

    public String getOnselect() 
    {
        return onselect;
    }

    public void setOnselect(String newOnselect)
    {
        onselect = newOnselect;
    }

    // The form was submitted - For FORM HTML element

    public String getOnsubmit() {
        return onsubmit;
    }

    public void setOnsubmit(String newOnsubmit)
    {
        onsubmit = newOnsubmit;
    }

    // For TEXTAREA/INPUT HTML elements

    public String getReadonly() 
    {
        return readonly;
    }

    public void setReadonly(String newReadonly)
    {
        readonly = newReadonly;
    }

    // Forward link types - For "A" (Hyperlink) HTML element

    public String getRel() 
    {
        return rel;
    }

    public void setRel(String newRel)
    {
        rel = newRel;
    }

    // Reverse link types - For "A" (Hyperlink) HTML element

    public String getRev() 
    {
        return rev;
    }

    public void setRev(String newRev)
    {
        rev = newRev;
    }

    // For TEXTAREA HTML element

    public String getRows()
    {
        return rows;
    }

    public void setRows(String newRows)
    {
        rows = newRows;
    }
    
    // For OPTION HTML element

    public String getSelected() 
    {
        return selected;
    }

    public void setSelected(String newSelected)
    {
        selected = newSelected;
    }

    // For use with client-side image maps -
    // For "A" (Hyperlink) HTML element

    public String getShape()
    {
        return shape;
    }

    public void setShape(String newShape)
    {
        shape = newShape;
    }

    // For INPUT/SELECT HTML elements - (means rows visible
    // for SELECT)

    public String getSize() 
    {
        return size;
    }

    public void setSize(String newSize)
    {
        size = newSize;
    }

    // For INPUT/IMG HTML elements

    public String getSrc() 
    {
        return src;
    }

    public void setSrc(String newSrc)
    {
        src = newSrc;
    }

    // Associated style info - For all HTML elements pertaining
    // to Faces.

    public String getStyle() 
    {
        return style;
    }

    public void setStyle(String newStyle)
    {
        style = newStyle;
    }

    // Position in tabbing order - For "A" (Hyperlink)/
    // BUTTON/INPUT/SELECT/TEXTAREA HTML elements.

    public String getTabindex() 
    {
        return tabindex;
    }

    public void setTabindex(String newTabindex)
    {
        tabindex = newTabindex;
    }

    // Render in this frame - For "A" (Hyperlink)/ FORM
    // HTML elements

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String newTarget)
    {
        target = newTarget;
    }

    // Advisory title - For all HTML elements pertaining
    // to Faces.

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }
    
    // For "A" (Hyperlink)/INPUT/BUTTON HTML elements

    public String getType() 
    {
        return type;
    }

    public void setType(String newType)
    {
        type = newType;
    }

    // Use client-side image map - For IMG/INPUT HTML elements.

    public String getUsemap() 
    {
        return usemap;
    }

    public void setUsemap(String newUsemap)
    {
        usemap = newUsemap;
    }

    // For INPUT/OPTION/BUTTON HTML elements.
    // Required for INPUT type = radio/checkbox

    public String getValue()
    {
        return value;
    }

    public void setValue(String newValue)
    {
        value = newValue;
    }
    
    // HTML 4.0 table attributes
    
   public  void setSummary(String newSummary) {
       this.summary = newSummary;
   } 
   public String getSummary() {
        return summary;
   } 
   
   public  void setWidth(String newWidth) {
       this.width = newWidth;
   } 
   public String getWidth() {
        return width;
   }
   
   public  void setBgcolor(String newColor) {
       this.bgcolor = newColor;
   }
   public String getBgcolor() {
        return bgcolor;
   }
   
   public  void setFrame(String newFrame) {
       this.frame = newFrame;
   }
   public String getFrame() {
        return frame;
   }
   
   public  void setRules(String newRules) {
       this.rules = newRules;
   }
   public String getRules() {
        return rules;
   }
   
   public  void setBorder(String newBorder) {
       this.border = newBorder;
   }
   public String getBorder() {
        return border;
   }
   
   public  void setCellspacing(String newCellspacing) {
       this.cellspacing = newCellspacing;
   }
   public String getCellspacing() {
        return cellspacing;
   }
   
   public  void setCellpadding(String newCellpadding) {
       this.cellpadding = newCellpadding;
   }
   public String getCellpadding() {
        return cellpadding;
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

* This is implemented by faces subclasses to allow globally turing off
* the render kit.

*/

public abstract String getLocalRendererType();

//
// Methods from Superclass
// 

public final String getRendererType()
{
    String disableRenderers =System.getProperty(RIConstants.DISABLE_RENDERERS);
    
    if (null != disableRenderers &&
	disableRenderers.equals(RIConstants.DISABLE_RENDERERS)) {
	return null;
    }
    
    return getLocalRendererType();
}

protected void overrideProperties(UIComponent component) 
{
    super.overrideProperties(component);
    String keyAttr = null;

    if ( component instanceof UIOutput ) {
        UIOutput output = (UIOutput)component;
        if (null != valueRef) {
            output.setValueRef(valueRef);
        }    
	if (null != value) {
	    output.setValue(value);
	}
        if (null != converter) {
            output.setConverter(converter);
        }
    }    

    // PENDING(edburns): move this into a class that is the superclass
    // of *all* tags that have components that are UIInput.

    if (component instanceof UIInput) {
	((UIInput)component).setRequired(required);
    }

    if (null != getKey()) {
	component.setAttribute("key", getKey());
    }
    if (null != getImageKey()) {
	component.setAttribute("imageKey", getImageKey());
    }
    if (null != getBundle()) {
	component.setAttribute(RIConstants.BUNDLE_ATTR, getBundle());
    }
    if (null != getFormatPattern()) {
	component.setAttribute("formatPattern", getFormatPattern());
    }
    if (null != getDateStyle()) {
	component.setAttribute("dateStyle", getDateStyle());
    }
    if (null != getTimeStyle()) {
	component.setAttribute("timeStyle", getTimeStyle());
    }
    if (null != getTimezone()) {
	component.setAttribute("timezone", getTimezone());
    }

    // HTML 4.0 event handlers common to most BODY-content elements.
    if (null != getOnclick()) {
	component.setAttribute("onclick", getOnclick());
    }
    if (null != getOndblclick()) {
	component.setAttribute("ondblclick", getOndblclick());
    }

    if (null != getOnkeydown()) {
	component.setAttribute("onkeydown", getOnkeydown());
    }
    if (null != getOnkeypress()) {
	component.setAttribute("onkeypress", getOnkeypress());
    }
    if (null != getOnkeyup()) {
	component.setAttribute("onkeyup", getOnkeyup());
    }
    if (null != getOnmousedown()) {
	component.setAttribute("onmousedown", getOnmousedown());
    }
    if (null != getOnmousemove()) {
	component.setAttribute("onmousemove", getOnmousemove());
    }
    if (null != getOnmouseout()) {
	component.setAttribute("onmouseout", getOnmouseout());
    }
    if (null != getOnmouseover()) {
	component.setAttribute("onmouseover", getOnmouseover());
    }
    if (null != getOnmouseup()) {
	component.setAttribute("onmouseup", getOnmouseup());
    }
    if (null != getOnfocus()) {
        component.setAttribute("onfocus", getOnfocus()); 
    }
    if (null != getOnblur()) {
        component.setAttribute("onblur", getOnblur());
    }
    
    // common HTML 4.0 attributes.
    // PENDING (visvan) id attribute clashes with faces id attribute
    if (null != getTitle()) {
        component.setAttribute("title", getTitle());
    }
    if (null != getDisabled()) {
	component.setAttribute("disabled", getDisabled());
    }
    if (null != getTabindex()) {
	component.setAttribute("tabindex", getTabindex());
    }
    if (null != getAccesskey()) {
	component.setAttribute("accesskey", getAccesskey());
    }
    if (null != getLang()) {
	component.setAttribute("lang", getLang());
    }
    if (null != getDir()) {
	component.setAttribute("dir", getDir());
    }
    if (null != getCommandClass()) {
	component.setAttribute("commandClass", getCommandClass());
    }
    if (null != getGraphicClass()) {
	component.setAttribute("graphicClass", getGraphicClass());
    }
    if (null != getInputClass()) {
	component.setAttribute("inputClass", getInputClass());
    }
    if (null != getOutputClass()) {
	component.setAttribute("outputClass", getOutputClass());
    }
    if (null != getSelectbooleanClass()) {
	component.setAttribute("selectbooleanClass", getSelectbooleanClass());
    }
    if (null != getSelectmanyClass()) {
	component.setAttribute("selectmanyClass", getSelectmanyClass());
    }
    if (null != getSelectoneClass()) {
	component.setAttribute("selectoneClass", getSelectoneClass());
    }
    if (null != getSelectitemClass()) {
	component.setAttribute("selectitemClass", getSelectitemClass());
    }
    if (null != getSelectitemsClass()) {
	component.setAttribute("selectitemsClass", getSelectitemsClass());
    }
    if (null != getStyle()) {
	component.setAttribute("style", getStyle());
    }
    if (null != getDateStyle()) {
	component.setAttribute("dateStyle", getDateStyle());
    }
    if (null != getTimeStyle()) {
	component.setAttribute("timeStyle", getTimeStyle());
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
