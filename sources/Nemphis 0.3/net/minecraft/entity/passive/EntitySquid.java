/*
 * Decompiled with CFR 0_118.
 */
package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySquid
extends EntityWaterMob {
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float field_70871_bB;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
    private static final String __OBFID = "CL_00001651";

    public EntitySquid(World worldIn) {
        super(worldIn);
        this.setSize(0.95f, 0.95f);
        this.rand.setSeed(1 + this.getEntityId());
        this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.tasks.addTask(0, new AIMoveRandom());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return null;
    }

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    protected Item getDropItem() {
        return null;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var3 = this.rand.nextInt(3 + p_70628_2_) + 1;
        int var4 = 0;
        while (var4 < var3) {
            this.entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeColorDamage()), 0.0f);
            ++var4;
        }
    }

    @Override
    public boolean isInWater() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.6000000238418579, 0.0), Material.water, this);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if ((double)this.squidRotation > 6.283185307179586) {
            if (this.worldObj.isRemote) {
                this.squidRotation = 6.2831855f;
            } else {
                this.squidRotation = (float)((double)this.squidRotation - 6.283185307179586);
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
                }
                this.worldObj.setEntityState(this, 19);
            }
        }
        if (this.inWater) {
            float var1;
            if (this.squidRotation < 3.1415927f) {
                var1 = this.squidRotation / 3.1415927f;
                this.tentacleAngle = MathHelper.sin(var1 * var1 * 3.1415927f) * 3.1415927f * 0.25f;
                if ((double)var1 > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                    this.field_70871_bB = 1.0f;
                } else {
                    this.field_70871_bB *= 0.8f;
                }
            } else {
                this.tentacleAngle = 0.0f;
                this.randomMotionSpeed *= 0.9f;
                this.field_70871_bB *= 0.99f;
            }
            if (!this.worldObj.isRemote) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += ((- (float)Math.atan2(this.motionX, this.motionZ)) * 180.0f / 3.1415927f - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw = (float)((double)this.squidYaw + 3.141592653589793 * (double)this.field_70871_bB * 1.5);
            this.squidPitch += ((- (float)Math.atan2(var1, this.motionY)) * 180.0f / 3.1415927f - this.squidPitch) * 0.1f;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927f * 0.25f;
            if (!this.worldObj.isRemote) {
                this.motionX = 0.0;
                this.motionY -= 0.08;
                this.motionY *= 0.9800000190734863;
                this.motionZ = 0.0;
            }
            this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0f - this.squidPitch) * 0.02);
        }
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.posY > 45.0 && this.posY < 63.0 && super.getCanSpawnHere()) {
            return true;
        }
        return false;
    }

    @Override
    public void handleHealthUpdate(byte p_70103_1_) {
        if (p_70103_1_ == 19) {
            this.squidRotation = 0.0f;
        } else {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public void func_175568_b(float p_175568_1_, float p_175568_2_, float p_175568_3_) {
        this.randomMotionVecX = p_175568_1_;
        this.randomMotionVecY = p_175568_2_;
        this.randomMotionVecZ = p_175568_3_;
    }

    public boolean func_175567_n() {
        if (this.randomMotionVecX == 0.0f && this.randomMotionVecY == 0.0f && this.randomMotionVecZ == 0.0f) {
            return false;
        }
        return true;
    }

    class AIMoveRandom
    extends EntityAIBase {
        private EntitySquid field_179476_a;
        private static final String __OBFID = "CL_00002232";

        AIMoveRandom() {
            this.field_179476_a = EntitySquid.this;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            int var1 = this.field_179476_a.getAge();
            if (var1 > 100) {
                this.field_179476_a.func_175568_b(0.0f, 0.0f, 0.0f);
            } else if (this.field_179476_a.getRNG().nextInt(50) == 0 || !this.field_179476_a.inWater || !this.field_179476_a.func_175567_n()) {
                float var2 = this.field_179476_a.getRNG().nextFloat() * 3.1415927f * 2.0f;
                float var3 = MathHelper.cos(var2) * 0.2f;
                float var4 = -0.1f + this.field_179476_a.getRNG().nextFloat() * 0.2f;
                float var5 = MathHelper.sin(var2) * 0.2f;
                this.field_179476_a.func_175568_b(var3, var4, var5);
            }
        }
    }

}

