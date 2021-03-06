package org.apache.jmeter.testelement.property;

/**
 * @version $Revision: 1.1 $
 */
public class DoubleProperty extends NumberProperty
{
    double value;
    
    public DoubleProperty(String name,double value)
    {
        super(name);
        this.value = value;
    }
    
    public DoubleProperty()
    {
    }
    
    public void setValue(float value)
    {
        this.value = value;
    }
    
    protected void setNumberValue(Number n)
    {
        value = n.doubleValue();
    }

    protected void setNumberValue(String n) throws NumberFormatException
    {
        value = Double.parseDouble(n);
    }

    /**
     * @see JMeterProperty#getStringValue()
     */
    public String getStringValue()
    {
        return Double.toString(value);
    }

    /**
     * @see JMeterProperty#getObjectValue()
     */
    public Object getObjectValue()
    {
        return new Double(value);
    }

    /**
     * @see Object#clone()
     */
    public Object clone() 
    {
        DoubleProperty prop = (DoubleProperty)super.clone();
        prop.value = value;
        return prop;
    }

    /**
     * @see JMeterProperty#getBooleanValue()
     */
    public boolean getBooleanValue()
    {
        return value > 0 ? true : false;
    }

    /**
     * @see JMeterProperty#getDoubleValue()
     */
    public double getDoubleValue()
    {
        return value;
    }

    /**
     * @see JMeterProperty#getFloatValue()
     */
    public float getFloatValue()
    {
        return (float)value;
    }

    /**
     * @see JMeterProperty#getIntValue()
     */
    public int getIntValue()
    {
        return (int)value;
    }

    /**
     * @see JMeterProperty#getLongValue()
     */
    public long getLongValue()
    {
        return (long)value;
    }
}
