package org.magmafoundation.magma.configuration.value;

/**
 * Value
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 19/08/2019 - 05:19 am
 */
public abstract class Value<T> {

    public final String path;
    public final T key;
    public final String description;

    public Value(String path, T key, String description) {
        this.path = path;
        this.key = key;
        this.description = description;
    }

    public abstract T getValues();

    public abstract void setValues(String values);
}
