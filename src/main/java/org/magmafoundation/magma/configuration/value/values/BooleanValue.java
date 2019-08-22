package org.magmafoundation.magma.configuration.value.values;

import org.apache.commons.lang.BooleanUtils;
import org.magmafoundation.magma.configuration.ConfigBase;
import org.magmafoundation.magma.configuration.value.Value;

/**
 * BooleanValue
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:27 am
 */
public class BooleanValue extends Value<Boolean> {

    private Boolean value;
    private ConfigBase configBase;

    public BooleanValue(ConfigBase configBase, String path, Boolean key, String description) {
        super(path, key, description);
        this.value = key;
        this.configBase = configBase;
    }

    @Override
    public Boolean getValues() {
        return value;
    }

    @Override
    public void setValues(String values) {
        this.value = BooleanUtils.toBooleanObject(values);
        this.value = this.value == null ? key : this.value;
        configBase.set(path, this.value);
    }
}
