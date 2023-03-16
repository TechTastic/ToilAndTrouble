package net.techtastic.tat.util;

import net.minecraft.world.entity.Entity;

public interface ITaglockedBlock {
    default Entity getTaggedEntity() {
        return null;
    }

    default void setTaggedEntity(Entity entity) {

    }

    default void resetTaggedEntity() {

    }
}
