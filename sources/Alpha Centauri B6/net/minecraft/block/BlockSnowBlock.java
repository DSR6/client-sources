package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockSnowBlock extends Block {
   protected BlockSnowBlock() {
      super(Material.craftedSnow);
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if(worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11) {
         this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
         worldIn.setBlockToAir(pos);
      }

   }

   public int quantityDropped(Random random) {
      return 4;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.snowball;
   }
}
