package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public interface ArmorStand extends LivingEntity {

    /**
     * Returns the item the armor stand is
     * currently holding
     *
     * @return the held item
     */
    ItemStack getItemInHand();

    /**
     * Sets the item the armor stand is currently
     * holding
     *
     * @param item the item to hold
     */
    void setItemInHand(ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its feet
     *
     * @return the worn item
     */
    ItemStack getBoots();

    /**
     * Sets the item currently being worn
     * by the armor stand on its feet
     *
     * @param item the item to wear
     */
    void setBoots(ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its legs
     *
     * @return the worn item
     */
    ItemStack getLeggings();

    /**
     * Sets the item currently being worn
     * by the armor stand on its legs
     *
     * @param item the item to wear
     */
    void setLeggings(ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its chest
     *
     * @return the worn item
     */
    ItemStack getChestplate();

    /**
     * Sets the item currently being worn
     * by the armor stand on its chest
     *
     * @param item the item to wear
     */
    void setChestplate(ItemStack item);

    /**
     * Returns the item currently being worn
     * by the armor stand on its head
     *
     * @return the worn item
     */
    ItemStack getHelmet();

    /**
     * Sets the item currently being worn
     * by the armor stand on its head
     *
     * @param item the item to wear
     */
    void setHelmet(ItemStack item);

    /**
     * Returns the armor stand's body's
     * current pose as a {@link EulerAngle}
     *
     * @return the current pose
     */
    EulerAngle getBodyPose();

    /**
     * Sets the armor stand's body's
     * current pose as a {@link EulerAngle}
     *
     * @param pose the current pose
     */
    void setBodyPose(EulerAngle pose);

    /**
     * Returns the armor stand's left arm's
     * current pose as a {@link EulerAngle}
     *
     * @return the current pose
     */
    EulerAngle getLeftArmPose();

    /**
     * Sets the armor stand's left arm's
     * current pose as a {@link EulerAngle}
     *
     * @param pose the current pose
     */
    void setLeftArmPose(EulerAngle pose);

    /**
     * Returns the armor stand's right arm's
     * current pose as a {@link EulerAngle}
     *
     * @return the current pose
     */
    EulerAngle getRightArmPose();

    /**
     * Sets the armor stand's right arm's
     * current pose as a {@link EulerAngle}
     *
     * @param pose the current pose
     */
    void setRightArmPose(EulerAngle pose);

    /**
     * Returns the armor stand's left leg's
     * current pose as a {@link EulerAngle}
     *
     * @return the current pose
     */
    EulerAngle getLeftLegPose();

    /**
     * Sets the armor stand's left leg's
     * current pose as a {@link EulerAngle}
     *
     * @param pose the current pose
     */
    void setLeftLegPose(EulerAngle pose);

    /**
     * Returns the armor stand's right leg's
     * current pose as a {@link EulerAngle}
     *
     * @return the current pose
     */
    EulerAngle getRightLegPose();

    /**
     * Sets the armor stand's right leg's
     * current pose as a {@link EulerAngle}
     *
     * @param pose the current pose
     */
    void setRightLegPose(EulerAngle pose);

    /**
     * Returns the armor stand's head's
     * current pose as a {@link EulerAngle}
     *
     * @return the current pose
     */
    EulerAngle getHeadPose();

    /**
     * Sets the armor stand's head's
     * current pose as a {@link EulerAngle}
     *
     * @param pose the current pose
     */
    void setHeadPose(EulerAngle pose);

    /**
     * Returns whether the armor stand has
     * a base plate
     *
     * @return whether it has a base plate
     */
    boolean hasBasePlate();

    /**
     * Sets whether the armor stand has a
     * base plate
     *
     * @param basePlate whether is has a base plate
     */
    void setBasePlate(boolean basePlate);

    /**
     * Returns whether the armor stand should be
     * visible or not
     *
     * @return whether the stand is visible or not
     */
    boolean isVisible();

    /**
     * Sets whether the armor stand should be
     * visible or not
     *
     * @param visible whether the stand is visible or not
     */
    void setVisible(boolean visible);

    /**
     * Returns whether this armor stand has arms
     *
     * @return whether this has arms or not
     */
    boolean hasArms();

    /**
     * Sets whether this armor stand has arms
     *
     * @param arms whether this has arms or not
     */
    void setArms(boolean arms);

    /**
     * Returns whether this armor stand is scaled
     * down
     *
     * @return whether this is scaled down
     */
    boolean isSmall();

    /**
     * Sets whether this armor stand is scaled
     * down
     *
     * @param small whether this is scaled down
     */
    void setSmall(boolean small);

    /**
     * Returns whether this armor stand is a marker,
     * meaning it has a very small collision box
     *
     * @return whether this is a marker
     */
    boolean isMarker();

    /**
     * Sets whether this armor stand is a marker,
     * meaning it has a very small collision box
     *
     * @param marker whether this is a marker
     */
    void setMarker(boolean marker);

    // Paper start
    /**
     * Tests if this armor stand can move.
     *
     * <p>The default value is {@code true}.</p>
     *
     * @return {@code true} if this armour stand can move, {@code false} otherwise
     */
    boolean canMove();
    /**
     * Sets if this armor stand can move.
     *
     * @param move {@code true} if this armour stand can move, {@code false} otherwise
     */
    void setCanMove(boolean move);

    /**
     * Tests if this armor stand can tick.
     *
     * <p>The default value is defined in {@code paper.yml}.</p>
     *
     * @return {@code true} if this armour stand can tick, {@code false} otherwise
     */
    boolean canTick();
    /**
     * Sets if this armor stand can tick.
     *
     * @param tick {@code true} if this armour stand can tick, {@code false} otherwise
     */
    void setCanTick(final boolean tick);
    // Paper end
}
