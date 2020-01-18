package org.magmafoundation.magma.mixin.core.minecraft.entity.ai.attributes;

import net.minecraft.entity.ai.attributes.IAttribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MixinAttributeInstance
 *
 * @author Redned
 * @since 16/01/2019 - 07:31 am
 */
@Mixin(net.minecraft.entity.ai.attributes.IAttributeInstance.class)
@Implements(@Interface(iface = AttributeInstance.class, prefix = "instance$"))
public abstract class MixinAttributeInstance implements AttributeInstance {

    @Shadow abstract IAttribute shadow$getAttribute();
    @Shadow abstract double shadow$getBaseValue();
    @Shadow abstract void shadow$setBaseValue(double value);
    @Shadow abstract double shadow$getValue();
    @Shadow abstract void applyModifier(net.minecraft.entity.ai.attributes.AttributeModifier modifier);
    @Shadow abstract void shadow$removeModifier(net.minecraft.entity.ai.attributes.AttributeModifier modifier);
    @Shadow abstract Collection<net.minecraft.entity.ai.attributes.AttributeModifier> shadow$getModifiers();

    @Override
    public Attribute getAttribute() {
        return Attribute.valueOf(shadow$getAttribute().getName().toUpperCase());
    }

    @Intrinsic
    public double instance$getBaseValue() {
        return shadow$getBaseValue();
    }

    @Intrinsic
    public void instance$setBaseValue(double value) {
        shadow$setBaseValue(value);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
       Collection<AttributeModifier> modifiers = new ArrayList<>();
       for (net.minecraft.entity.ai.attributes.AttributeModifier modifier : shadow$getModifiers()) {
           modifiers.add(getBukkitAttribute(modifier));
       }
       return modifiers;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        applyModifier(getVanillaAttribute(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        shadow$removeModifier(getVanillaAttribute(modifier));
    }

    @Intrinsic
    public double instance$getValue() {
        return shadow$getValue();
    }

    @Override
    public double getDefaultValue() {
        return shadow$getAttribute().getDefaultValue();
    }

    private net.minecraft.entity.ai.attributes.AttributeModifier getVanillaAttribute(AttributeModifier modifier) {
        return new net.minecraft.entity.ai.attributes.AttributeModifier(modifier.getUniqueId(),
                modifier.getName(),
                modifier.getAmount(),
                net.minecraft.entity.ai.attributes.AttributeModifier.Operation.byId(
                        modifier.getOperation().ordinal()
                )
        );
    }

    private AttributeModifier getBukkitAttribute(net.minecraft.entity.ai.attributes.AttributeModifier modifier) {
        return new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount(), AttributeModifier.Operation.values()[modifier.getOperation().ordinal()]);
    }
}
