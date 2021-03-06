package org.apache.jmeter.modifiers.gui;
import javax.swing.JCheckBox;

import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.gui.layout.VerticalLayout;

public class CounterConfigGui extends AbstractPreProcessorGui
{
    private JLabeledTextField startField, incrField, endField, varNameField;
    private JCheckBox perUserField;

    public CounterConfigGui()
    {
        super();
        init();
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#getStaticLabel()
     */
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("counter_config_title");
    }
    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement()
    {
        CounterConfig config = new CounterConfig();
        modifyTestElement(config);
        return config;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement c)
    {
        if (c instanceof CounterConfig)
        {
            CounterConfig config = (CounterConfig) c;
            config.setStart(startField.getText());
            if (endField.getText().length() > 0)
            {
                config.setEnd(endField.getText());
            }
            config.setIncrement(incrField.getText());
            config.setVarName(varNameField.getText());
            config.setIsPerUser(perUserField.isSelected());
        }
        super.configureTestElement(c);
    }

    public void configure(TestElement element)
    {
        super.configure(element);
        CounterConfig config = (CounterConfig) element;
        startField.setText(config.getPropertyAsString(CounterConfig.START));
        endField.setText(config.getPropertyAsString(CounterConfig.END));
        incrField.setText(config.getPropertyAsString(CounterConfig.INCREMENT));
        varNameField.setText(config.getVarName());
        perUserField.setSelected(config.isPerUser());
    }

    private void init()
    {
        setBorder (makeBorder());
        setLayout(new VerticalLayout(5, VerticalLayout.LEFT));
        
        startField = new JLabeledTextField(JMeterUtils.getResString("start"), 5);
        incrField = new JLabeledTextField(JMeterUtils.getResString("increment"), 5);
        endField = new JLabeledTextField(JMeterUtils.getResString("max"), 5);
        varNameField = new JLabeledTextField(JMeterUtils.getResString("var_name"));
        perUserField = new JCheckBox(JMeterUtils.getResString("counter_per_user"));

        add(makeTitlePanel());
        add(startField);
        add(incrField);
        add(endField);
        add(varNameField);
        add(perUserField);
    }
}
