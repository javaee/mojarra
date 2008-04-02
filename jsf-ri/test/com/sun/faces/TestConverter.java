/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import com.sun.faces.util.Util;

import javax.faces.convert.NumberConverter;

public class TestConverter extends NumberConverter
{

    public String getConverterId() { return "TestConverter"; }

}
