/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

public class RecipesBanners {
    private static final String __OBFID = "CL_00002160";

    void func_179534_a(CraftingManager p_179534_1_) {
        for (EnumDyeColor var5 : EnumDyeColor.values()) {
            p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, var5.getDyeColorDamage()), "###", "###", " | ", Character.valueOf('#'), new ItemStack(Blocks.wool, 1, var5.func_176765_a()), Character.valueOf('|'), Items.stick);
        }
        p_179534_1_.func_180302_a(new RecipeDuplicatePattern(null));
        p_179534_1_.func_180302_a(new RecipeAddPattern(null));
    }

    static class RecipeAddPattern
    implements IRecipe {
        private static final String __OBFID = "CL_00002158";

        private RecipeAddPattern() {
        }

        @Override
        public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
            boolean var3 = false;
            for (int var4 = 0; var4 < p_77569_1_.getSizeInventory(); ++var4) {
                ItemStack var5 = p_77569_1_.getStackInSlot(var4);
                if (var5 == null || var5.getItem() != Items.banner) continue;
                if (var3) {
                    return false;
                }
                if (TileEntityBanner.func_175113_c(var5) >= 6) {
                    return false;
                }
                var3 = true;
            }
            if (!var3) {
                return false;
            }
            return this.func_179533_c(p_77569_1_) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
            ItemStack var2 = null;
            for (int var3 = 0; var3 < p_77572_1_.getSizeInventory(); ++var3) {
                ItemStack var4 = p_77572_1_.getStackInSlot(var3);
                if (var4 == null || var4.getItem() != Items.banner) continue;
                var2 = var4.copy();
                var2.stackSize = 1;
                break;
            }
            TileEntityBanner.EnumBannerPattern var8 = this.func_179533_c(p_77572_1_);
            if (var8 != null) {
                ItemStack var6;
                NBTTagList var11;
                int var9 = 0;
                for (int var5 = 0; var5 < p_77572_1_.getSizeInventory(); ++var5) {
                    var6 = p_77572_1_.getStackInSlot(var5);
                    if (var6 == null || var6.getItem() != Items.dye) continue;
                    var9 = var6.getMetadata();
                    break;
                }
                NBTTagCompound var10 = var2.getSubCompound("BlockEntityTag", true);
                var6 = null;
                if (var10.hasKey("Patterns", 9)) {
                    var11 = var10.getTagList("Patterns", 10);
                } else {
                    var11 = new NBTTagList();
                    var10.setTag("Patterns", var11);
                }
                NBTTagCompound var7 = new NBTTagCompound();
                var7.setString("Pattern", var8.func_177273_b());
                var7.setInteger("Color", var9);
                var11.appendTag(var7);
            }
            return var2;
        }

        @Override
        public int getRecipeSize() {
            return 10;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }

        @Override
        public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_) {
            ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];
            for (int var3 = 0; var3 < var2.length; ++var3) {
                ItemStack var4 = p_179532_1_.getStackInSlot(var3);
                if (var4 == null || !var4.getItem().hasContainerItem()) continue;
                var2[var3] = new ItemStack(var4.getItem().getContainerItem());
            }
            return var2;
        }

        private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_) {
            for (TileEntityBanner.EnumBannerPattern var5 : TileEntityBanner.EnumBannerPattern.values()) {
                boolean var6;
                block13 : {
                    int var9;
                    block12 : {
                        if (!var5.func_177270_d()) continue;
                        var6 = true;
                        if (!var5.func_177269_e()) break block12;
                        boolean var12 = false;
                        boolean var13 = false;
                        for (var9 = 0; var9 < p_179533_1_.getSizeInventory() && var6; ++var9) {
                            ItemStack var14 = p_179533_1_.getStackInSlot(var9);
                            if (var14 == null || var14.getItem() == Items.banner) continue;
                            if (var14.getItem() == Items.dye) {
                                if (var13) {
                                    var6 = false;
                                    break;
                                }
                                var13 = true;
                                continue;
                            }
                            if (var12 || !var14.isItemEqual(var5.func_177272_f())) {
                                var6 = false;
                                break;
                            }
                            var12 = true;
                        }
                        if (var12) break block13;
                        var6 = false;
                        break block13;
                    }
                    if (p_179533_1_.getSizeInventory() != var5.func_177267_c().length * var5.func_177267_c()[0].length()) {
                        var6 = false;
                    } else {
                        int var7 = -1;
                        for (int var8 = 0; var8 < p_179533_1_.getSizeInventory() && var6; ++var8) {
                            var9 = var8 / 3;
                            int var10 = var8 % 3;
                            ItemStack var11 = p_179533_1_.getStackInSlot(var8);
                            if (var11 != null && var11.getItem() != Items.banner) {
                                if (var11.getItem() != Items.dye) {
                                    var6 = false;
                                    break;
                                }
                                if (var7 != -1 && var7 != var11.getMetadata()) {
                                    var6 = false;
                                    break;
                                }
                                if (var5.func_177267_c()[var9].charAt(var10) == ' ') {
                                    var6 = false;
                                    break;
                                }
                                var7 = var11.getMetadata();
                                continue;
                            }
                            if (var5.func_177267_c()[var9].charAt(var10) == ' ') continue;
                            var6 = false;
                            break;
                        }
                    }
                }
                if (!var6) continue;
                return var5;
            }
            return null;
        }

        RecipeAddPattern(Object p_i45780_1_) {
            this();
        }
    }

    static class RecipeDuplicatePattern
    implements IRecipe {
        private static final String __OBFID = "CL_00002157";

        private RecipeDuplicatePattern() {
        }

        @Override
        public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
            ItemStack var3 = null;
            ItemStack var4 = null;
            for (int var5 = 0; var5 < p_77569_1_.getSizeInventory(); ++var5) {
                boolean var8;
                ItemStack var6 = p_77569_1_.getStackInSlot(var5);
                if (var6 == null) continue;
                if (var6.getItem() != Items.banner) {
                    return false;
                }
                if (var3 != null && var4 != null) {
                    return false;
                }
                int var7 = TileEntityBanner.getBaseColor(var6);
                boolean bl = var8 = TileEntityBanner.func_175113_c(var6) > 0;
                if (var3 != null) {
                    if (var8) {
                        return false;
                    }
                    if (var7 != TileEntityBanner.getBaseColor(var3)) {
                        return false;
                    }
                    var4 = var6;
                    continue;
                }
                if (var4 != null) {
                    if (!var8) {
                        return false;
                    }
                    if (var7 != TileEntityBanner.getBaseColor(var4)) {
                        return false;
                    }
                    var3 = var6;
                    continue;
                }
                if (var8) {
                    var3 = var6;
                    continue;
                }
                var4 = var6;
            }
            return var3 != null && var4 != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
            for (int var2 = 0; var2 < p_77572_1_.getSizeInventory(); ++var2) {
                ItemStack var3 = p_77572_1_.getStackInSlot(var2);
                if (var3 == null || TileEntityBanner.func_175113_c(var3) <= 0) continue;
                ItemStack var4 = var3.copy();
                var4.stackSize = 1;
                return var4;
            }
            return null;
        }

        @Override
        public int getRecipeSize() {
            return 2;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }

        @Override
        public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_) {
            ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];
            for (int var3 = 0; var3 < var2.length; ++var3) {
                ItemStack var4 = p_179532_1_.getStackInSlot(var3);
                if (var4 == null) continue;
                if (var4.getItem().hasContainerItem()) {
                    var2[var3] = new ItemStack(var4.getItem().getContainerItem());
                    continue;
                }
                if (!var4.hasTagCompound() || TileEntityBanner.func_175113_c(var4) <= 0) continue;
                var2[var3] = var4.copy();
                var2[var3].stackSize = 1;
            }
            return var2;
        }

        RecipeDuplicatePattern(Object p_i45779_1_) {
            this();
        }
    }

}

