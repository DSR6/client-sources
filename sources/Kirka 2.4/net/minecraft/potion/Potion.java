/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.potion;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionAbsoption;
import net.minecraft.potion.PotionAttackDamage;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHealth;
import net.minecraft.potion.PotionHealthBoost;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class Potion {
    public static final Potion[] potionTypes = new Potion[32];
    private static final Map field_180150_I = Maps.newHashMap();
    public static final Potion field_180151_b = null;
    public static final Potion moveSpeed = new Potion(1, new ResourceLocation("speed"), false, 8171462).setPotionName("potion.moveSpeed").setIconIndex(0, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224, 2);
    public static final Potion moveSlowdown = new Potion(2, new ResourceLocation("slowness"), true, 5926017).setPotionName("potion.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448, 2);
    public static final Potion digSpeed = new Potion(3, new ResourceLocation("haste"), false, 14270531).setPotionName("potion.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5);
    public static final Potion digSlowdown = new Potion(4, new ResourceLocation("mining_fatigue"), true, 4866583).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
    public static final Potion damageBoost = new PotionAttackDamage(5, new ResourceLocation("strength"), false, 9643043).setPotionName("potion.damageBoost").setIconIndex(4, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5, 2);
    public static final Potion heal = new PotionHealth(6, new ResourceLocation("instant_health"), false, 16262179).setPotionName("potion.heal");
    public static final Potion harm = new PotionHealth(7, new ResourceLocation("instant_damage"), true, 4393481).setPotionName("potion.harm");
    public static final Potion jump = new Potion(8, new ResourceLocation("jump_boost"), false, 2293580).setPotionName("potion.jump").setIconIndex(2, 1);
    public static final Potion confusion = new Potion(9, new ResourceLocation("nausea"), true, 5578058).setPotionName("potion.confusion").setIconIndex(3, 1).setEffectiveness(0.25);
    public static final Potion regeneration = new Potion(10, new ResourceLocation("regeneration"), false, 13458603).setPotionName("potion.regeneration").setIconIndex(7, 0).setEffectiveness(0.25);
    public static final Potion resistance = new Potion(11, new ResourceLocation("resistance"), false, 10044730).setPotionName("potion.resistance").setIconIndex(6, 1);
    public static final Potion fireResistance = new Potion(12, new ResourceLocation("fire_resistance"), false, 14981690).setPotionName("potion.fireResistance").setIconIndex(7, 1);
    public static final Potion waterBreathing = new Potion(13, new ResourceLocation("water_breathing"), false, 3035801).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
    public static final Potion invisibility = new Potion(14, new ResourceLocation("invisibility"), false, 8356754).setPotionName("potion.invisibility").setIconIndex(0, 1);
    public static final Potion blindness = new Potion(15, new ResourceLocation("blindness"), true, 2039587).setPotionName("potion.blindness").setIconIndex(5, 1).setEffectiveness(0.25);
    public static final Potion nightVision = new Potion(16, new ResourceLocation("night_vision"), false, 2039713).setPotionName("potion.nightVision").setIconIndex(4, 1);
    public static final Potion hunger = new Potion(17, new ResourceLocation("hunger"), true, 5797459).setPotionName("potion.hunger").setIconIndex(1, 1);
    public static final Potion weakness = new PotionAttackDamage(18, new ResourceLocation("weakness"), true, 4738376).setPotionName("potion.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0, 0);
    public static final Potion poison = new Potion(19, new ResourceLocation("poison"), true, 5149489).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25);
    public static final Potion wither = new Potion(20, new ResourceLocation("wither"), true, 3484199).setPotionName("potion.wither").setIconIndex(1, 2).setEffectiveness(0.25);
    public static final Potion field_180152_w = new PotionHealthBoost(21, new ResourceLocation("health_boost"), false, 16284963).setPotionName("potion.healthBoost").setIconIndex(2, 2).registerPotionAttributeModifier(SharedMonsterAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0, 0);
    public static final Potion absorption = new PotionAbsoption(22, new ResourceLocation("absorption"), false, 2445989).setPotionName("potion.absorption").setIconIndex(2, 2);
    public static final Potion saturation = new PotionHealth(23, new ResourceLocation("saturation"), false, 16262179).setPotionName("potion.saturation");
    public static final Potion field_180153_z = null;
    public static final Potion field_180147_A = null;
    public static final Potion field_180148_B = null;
    public static final Potion field_180149_C = null;
    public static final Potion field_180143_D = null;
    public static final Potion field_180144_E = null;
    public static final Potion field_180145_F = null;
    public static final Potion field_180146_G = null;
    public final int id;
    private final Map attributeModifierMap = Maps.newHashMap();
    private final boolean isBadEffect;
    private final int liquidColor;
    private String name = "";
    private int statusIconIndex = -1;
    private double effectiveness;
    private boolean usable;
    private static final String __OBFID = "CL_00001528";

    protected Potion(int p_i45897_1_, ResourceLocation p_i45897_2_, boolean p_i45897_3_, int p_i45897_4_) {
        this.id = p_i45897_1_;
        Potion.potionTypes[p_i45897_1_] = this;
        field_180150_I.put(p_i45897_2_, this);
        this.isBadEffect = p_i45897_3_;
        this.effectiveness = p_i45897_3_ ? 0.5 : 1.0;
        this.liquidColor = p_i45897_4_;
    }

    public static Potion func_180142_b(String p_180142_0_) {
        return (Potion)field_180150_I.get(new ResourceLocation(p_180142_0_));
    }

    public static String[] func_180141_c() {
        String[] var0 = new String[field_180150_I.size()];
        int var1 = 0;
        for (ResourceLocation var3 : field_180150_I.keySet()) {
            var0[var1++] = var3.toString();
        }
        return var0;
    }

    protected Potion setIconIndex(int p_76399_1_, int p_76399_2_) {
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_) {
        if (this.id == Potion.regeneration.id) {
            if (p_76394_1_.getHealth() < p_76394_1_.getMaxHealth()) {
                p_76394_1_.heal(1.0f);
            }
        } else if (this.id == Potion.poison.id) {
            if (p_76394_1_.getHealth() > 1.0f) {
                p_76394_1_.attackEntityFrom(DamageSource.magic, 1.0f);
            }
        } else if (this.id == Potion.wither.id) {
            p_76394_1_.attackEntityFrom(DamageSource.wither, 1.0f);
        } else if (this.id == Potion.hunger.id && p_76394_1_ instanceof EntityPlayer) {
            ((EntityPlayer)p_76394_1_).addExhaustion(0.025f * (float)(p_76394_2_ + 1));
        } else if (this.id == Potion.saturation.id && p_76394_1_ instanceof EntityPlayer) {
            if (!p_76394_1_.worldObj.isRemote) {
                ((EntityPlayer)p_76394_1_).getFoodStats().addStats(p_76394_2_ + 1, 1.0f);
            }
        } else if (!(this.id == Potion.heal.id && !p_76394_1_.isEntityUndead() || this.id == Potion.harm.id && p_76394_1_.isEntityUndead())) {
            if (this.id == Potion.harm.id && !p_76394_1_.isEntityUndead() || this.id == Potion.heal.id && p_76394_1_.isEntityUndead()) {
                p_76394_1_.attackEntityFrom(DamageSource.magic, 6 << p_76394_2_);
            }
        } else {
            p_76394_1_.heal(Math.max(4 << p_76394_2_, 0));
        }
    }

    public void func_180793_a(Entity p_180793_1_, Entity p_180793_2_, EntityLivingBase p_180793_3_, int p_180793_4_, double p_180793_5_) {
        if (!(this.id == Potion.heal.id && !p_180793_3_.isEntityUndead() || this.id == Potion.harm.id && p_180793_3_.isEntityUndead())) {
            if (this.id == Potion.harm.id && !p_180793_3_.isEntityUndead() || this.id == Potion.heal.id && p_180793_3_.isEntityUndead()) {
                int var7 = (int)(p_180793_5_ * (double)(6 << p_180793_4_) + 0.5);
                if (p_180793_1_ == null) {
                    p_180793_3_.attackEntityFrom(DamageSource.magic, var7);
                } else {
                    p_180793_3_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p_180793_1_, p_180793_2_), var7);
                }
            }
        } else {
            int var7 = (int)(p_180793_5_ * (double)(4 << p_180793_4_) + 0.5);
            p_180793_3_.heal(var7);
        }
    }

    public boolean isInstant() {
        return false;
    }

    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        if (this.id == Potion.regeneration.id) {
            int var3 = 50 >> p_76397_2_;
            return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
        }
        if (this.id == Potion.poison.id) {
            int var3 = 25 >> p_76397_2_;
            return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
        }
        if (this.id == Potion.wither.id) {
            int var3 = 40 >> p_76397_2_;
            return var3 > 0 ? p_76397_1_ % var3 == 0 : true;
        }
        return this.id == Potion.hunger.id;
    }

    public Potion setPotionName(String p_76390_1_) {
        this.name = p_76390_1_;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasStatusIcon() {
        return this.statusIconIndex >= 0;
    }

    public int getStatusIconIndex() {
        return this.statusIconIndex;
    }

    public boolean isBadEffect() {
        return this.isBadEffect;
    }

    public static String getDurationString(PotionEffect p_76389_0_) {
        if (p_76389_0_.getIsPotionDurationMax()) {
            return "**:**";
        }
        int var1 = p_76389_0_.getDuration();
        return StringUtils.ticksToElapsedTime(var1);
    }

    protected Potion setEffectiveness(double p_76404_1_) {
        this.effectiveness = p_76404_1_;
        return this;
    }

    public double getEffectiveness() {
        return this.effectiveness;
    }

    public boolean isUsable() {
        return this.usable;
    }

    public int getLiquidColor() {
        return this.liquidColor;
    }

    public Potion registerPotionAttributeModifier(IAttribute p_111184_1_, String p_111184_2_, double p_111184_3_, int p_111184_5_) {
        AttributeModifier var6 = new AttributeModifier(UUID.fromString(p_111184_2_), this.getName(), p_111184_3_, p_111184_5_);
        this.attributeModifierMap.put(p_111184_1_, var6);
        return this;
    }

    public Map func_111186_k() {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase p_111187_1_, BaseAttributeMap p_111187_2_, int p_111187_3_) {
        for (Map.Entry var5 : this.attributeModifierMap.entrySet()) {
            IAttributeInstance var6 = p_111187_2_.getAttributeInstance((IAttribute)var5.getKey());
            if (var6 == null) continue;
            var6.removeModifier((AttributeModifier)var5.getValue());
        }
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase p_111185_1_, BaseAttributeMap p_111185_2_, int p_111185_3_) {
        for (Map.Entry var5 : this.attributeModifierMap.entrySet()) {
            IAttributeInstance var6 = p_111185_2_.getAttributeInstance((IAttribute)var5.getKey());
            if (var6 == null) continue;
            AttributeModifier var7 = (AttributeModifier)var5.getValue();
            var6.removeModifier(var7);
            var6.applyModifier(new AttributeModifier(var7.getID(), String.valueOf(this.getName()) + " " + p_111185_3_, this.func_111183_a(p_111185_3_, var7), var7.getOperation()));
        }
    }

    public double func_111183_a(int p_111183_1_, AttributeModifier p_111183_2_) {
        return p_111183_2_.getAmount() * (double)(p_111183_1_ + 1);
    }
}

