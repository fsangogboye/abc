/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.testelement.property;

import org.apache.jmeter.testelement.TestElement;

/**
 * For JMeterProperties that hold multiple properties within, provides a simple
 * interface for retrieving a property iterator for the sub values.
 * 
 * @version $Revision: 1.1 $
 */
public abstract class MultiProperty extends AbstractProperty
{
    public MultiProperty()
    {
        super();
    }

    public MultiProperty(String name)
    {
        super(name);
    }
    
    /**
     * Get the property iterator to iterate through the sub-values of this
     * JMeterProperty.
     * 
     * @return an iterator for the sub-values of this property
     */
    public abstract PropertyIterator iterator();
    
    /**
     * Add a property to the collection.
     */
    public abstract void addProperty(JMeterProperty prop);
    
    /**
     * Clear away all values in the property.
     */
    public abstract void clear();

    public void setTemporary(boolean temporary, TestElement owner)
    {
        super.setTemporary(temporary, owner);
        PropertyIterator iter = iterator();
        while (iter.hasNext())
        {
            iter.next().setTemporary(temporary, owner);
        }
    }

    public void setRunningVersion(boolean running)
    {
        super.setRunningVersion(running);
        PropertyIterator iter = iterator();
        while (iter.hasNext())
        {
            iter.next().setRunningVersion(running);
        }
    }

    protected void recoverRunningVersionOfSubElements(TestElement owner)
    {
        PropertyIterator iter = iterator();
        while (iter.hasNext())
        {
            JMeterProperty prop = iter.next();
            if (prop.isTemporary(owner) || prop.isTemporary(null))
            {
                iter.remove();
            }
            else
            {
                prop.recoverRunningVersion(owner);
            }
        }
    }

    public void mergeIn(JMeterProperty prop)
    {
        if (prop.getObjectValue() == getObjectValue())
        {
            return;
        }
        log.debug("merging in " + prop.getClass());
        if (prop instanceof MultiProperty)
        {
            PropertyIterator iter = ((MultiProperty) prop).iterator();
            while (iter.hasNext())
            {
                JMeterProperty item = iter.next();
                addProperty(item);
            }
        }
        else
        {
            addProperty(prop);
        }
    }
}
