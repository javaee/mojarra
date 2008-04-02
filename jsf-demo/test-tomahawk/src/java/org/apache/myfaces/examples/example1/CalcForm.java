/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.example1;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:15 $
 */
public class CalcForm
    implements Serializable
{
    private BigDecimal number1 = new BigDecimal(0);
    private BigDecimal number2 = new BigDecimal(0);
    private BigDecimal result = new BigDecimal(0);

    public void add()
    {
        result = number1.add(number2);
    }

    public void subtract()
    {
        result = number1.subtract(number2);
    }

    public BigDecimal getNumber1()
    {
        return number1;
    }

    public void setNumber1(BigDecimal number1)
    {
        this.number1 = number1;
    }

    public BigDecimal getNumber2()
    {
        return number2;
    }

    public void setNumber2(BigDecimal number2)
    {
        this.number2 = number2;
    }

    public BigDecimal getResult()
    {
        return result;
    }

    public void setResult(BigDecimal result)
    {
        this.result = result;
    }

}
