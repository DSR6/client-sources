/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockHay
extends BlockRotatedPillar {
    private static final String __OBFID = "CL_00000256";

    public BlockHay() {
        super(Material.grass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176298_M, (Comparable)((Object)EnumFacing.Axis.Y)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing.Axis var2 = EnumFacing.Axis.Y;
        int var3 = meta & 12;
        if (var3 == 4) {
            var2 = EnumFacing.Axis.X;
        } else if (var3 == 8) {
            var2 = EnumFacing.Axis.Z;
        }
        return this.getDefaultState().withProperty(field_176298_M, (Comparable)((Object)var2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        EnumFacing.Axis var3 = (EnumFacing.Axis)((Object)state.getValue(field_176298_M));
        if (var3 == EnumFacing.Axis.X) {
            var2 |= 4;
        } else if (var3 == EnumFacing.Axis.Z) {
            var2 |= 8;
        }
        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176298_M);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(field_176298_M, (Comparable)((Object)facing.getAxis()));
    }
}

