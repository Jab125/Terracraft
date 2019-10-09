package com.github.n_slash_a.terracraft.tileentity;

import com.github.n_slash_a.terracraft.init.ModTileEntityTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author N_slash_A
 */
public class MiniModelTileEntity extends TileEntity {

    // Constructor without hardcoded TileEntityType so that subclasses can use their own.
    // Alternatively, subclasses can also override getType if a hardcoded type is used in a superclass' constructor
    public MiniModelTileEntity(final TileEntityType<?> type) {
        super(type);
    }

    public MiniModelTileEntity() {
        this(ModTileEntityTypes.MINI_MODEL);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        // This, combined with isGlobalRenderer in the TileEntityRenderer makes it so that the
        // render does not disappear if the player can't see the block
        // This is useful for rendering larger models or dynamically sized models
        return INFINITE_EXTENT_AABB;
    }

}