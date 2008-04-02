/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import org.mozilla.util.Assert;

import javax.faces.convert.NumberConverter;

public class TestConverter extends NumberConverter
{

    public String getConverterId() { return "TestConverter"; }

}
