package org.magmafoundation.magma.configuration.value.values;

import org.apache.commons.lang.math.NumberUtils;
import org.magmafoundation.magma.configuration.ConfigBase;
import org.magmafoundation.magma.configuration.value.Value;

/**
 * IntValue
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:34 am
 */
public class IntValue extends Value<Integer> {

    private Integer value;
    private ConfigBase configBase;

    public IntValue(ConfigBase configBase, String path, Integer key, String description) {
        super(path, key, description);
        this.value = key;
        this.configBase = configBase;
    }

    @Override
    public Integer getValues() {
        return this.value;
    }

    @Override
    public void setValues(String values) {
        this.value = NumberUtils.toInt(values, key);
        configBase.set(path, this.value);
    }
}
