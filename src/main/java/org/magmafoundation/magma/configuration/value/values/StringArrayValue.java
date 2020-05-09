package org.magmafoundation.magma.configuration.value.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.magmafoundation.magma.configuration.ConfigBase;

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
        array = array.replaceAll("\\[(.*)\\]", "$1");
        String[] vals = array.split(",");

        this.valueArray = new ArrayList<String>(vals.length);
        this.valueSet = new HashSet<String>(vals.length);

        Arrays.stream(vals).filter(val -> val.length() != 0).map(String::trim).forEach(val -> this.valueArray.add(val));
        this.valueSet.addAll(this.valueArray);
    }
}
