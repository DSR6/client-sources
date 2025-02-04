/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIAvoidEntity
extends EntityAIBase {
    public final Predicate field_179509_a;
    protected EntityCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    protected Entity closestLivingEntity;
    private float field_179508_f;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;
    private Predicate field_179510_i;
    private static final String __OBFID = "CL_00001574";

    public EntityAIAvoidEntity(EntityCreature p_i45890_1_, Predicate p_i45890_2_, float p_i45890_3_, double p_i45890_4_, double p_i45890_6_) {
        this.field_179509_a = new Predicate(){
            private static final String __OBFID = "CL_00001575";

            public boolean func_180419_a(Entity p_180419_1_) {
                if (p_180419_1_.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_180419_1_)) {
                    return true;
                }
                return false;
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_180419_a((Entity)p_apply_1_);
            }
        };
        this.theEntity = p_i45890_1_;
        this.field_179510_i = p_i45890_2_;
        this.field_179508_f = p_i45890_3_;
        this.farSpeed = p_i45890_4_;
        this.nearSpeed = p_i45890_6_;
        this.entityPathNavigate = p_i45890_1_.getNavigator();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        List var1 = this.theEntity.worldObj.func_175674_a(this.theEntity, this.theEntity.getEntityBoundingBox().expand(this.field_179508_f, 3.0, this.field_179508_f), Predicates.and((Predicate[])new Predicate[]{IEntitySelector.field_180132_d, this.field_179509_a, this.field_179510_i}));
        if (var1.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = (Entity)var1.get(0);
        Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
        if (var2 == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(var2.xCoord, var2.yCoord, var2.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(var2.xCoord, var2.yCoord, var2.zCoord);
        return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(var2);
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }

    @Override
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }

    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        } else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }

}

