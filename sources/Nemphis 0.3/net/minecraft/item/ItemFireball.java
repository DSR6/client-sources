/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.item;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFireball
extends Item {
    private static final String __OBFID = "CL_00000029";

    public ItemFireball() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        if (!playerIn.func_175151_a(pos = pos.offset(side), side, stack)) {
            return false;
        }
        if (worldIn.getBlockState(pos).getBlock().getMaterial() == Material.air) {
            worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "item.fireCharge.use", 1.0f, (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2f + 1.0f);
            worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
        }
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return true;
    }
}

