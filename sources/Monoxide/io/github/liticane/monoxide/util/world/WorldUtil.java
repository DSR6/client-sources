package io.github.liticane.monoxide.util.world;

import io.github.liticane.monoxide.util.interfaces.Methods;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class WorldUtil implements Methods {

    public static IBlockState getBlockState(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos);
    }

    public static Block getBlock(double posX, double posY, double posZ) {
        return getBlock(new BlockPos(posX, posY, posZ));
    }

    public static Block getBlock(final BlockPos pos) {
        return getBlockState(pos).getBlock();
    }

}
