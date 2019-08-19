package org.magmafoundation.magma.configuration.value.values;

import org.magmafoundation.magma.configuration.ConfigBase;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * StringArrayValue
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:35 am
 */
public class StringArrayValue extends ArrayValue<String> {

    public StringArrayValue(ConfigBase config, String path, String key, String description) {
        super(config, path, key, description);
    }

    @Override
    public void initArray(String array) {
        String[] vals = values.split(",");

        this.valueArray = new ArrayList<String>(vals.length);
        this.valueSet = new HashSet<String>(vals.length);

        for (String val : vals) {
            if (val.length() == 0) {
                continue;
            }
            this.valueArray.add(val);
        }
        this.valueSet.addAll(this.valueArray);
    }
}
