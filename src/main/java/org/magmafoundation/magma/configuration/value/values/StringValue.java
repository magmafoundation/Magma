package org.magmafoundation.magma.configuration.value.values;

import org.magmafoundation.magma.configuration.ConfigBase;
import org.magmafoundation.magma.configuration.value.Value;

/**
 * StringValue
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:38 am
 */
public class StringValue extends Value<String> {

    private String value;
    private ConfigBase config;

    public StringValue(ConfigBase config, String path, String key, String description) {
        super(path, key, description);
        this.value = key;
        this.config = config;
    }

    @Override
    public String getValues() {
        return this.value;
    }

    @Override
    public void setValues(String values) {
        config.set(path, this.value = value);
    }
}
