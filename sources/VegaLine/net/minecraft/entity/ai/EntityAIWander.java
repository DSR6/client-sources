/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIWander
extends EntityAIBase {
    protected final EntityCreature entity;
    protected double xPosition;
    protected double yPosition;
    protected double zPosition;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;

    public EntityAIWander(EntityCreature creatureIn, double speedIn) {
        this(creatureIn, speedIn, 120);
    }

    public EntityAIWander(EntityCreature creatureIn, double speedIn, int chance) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        Vec3d vec3d;
        if (!this.mustUpdate) {
            if (this.entity.getAge() >= 100) {
                return false;
            }
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }
        if ((vec3d = this.func_190864_f()) == null) {
            return false;
        }
        this.xPosition = vec3d.xCoord;
        this.yPosition = vec3d.yCoord;
        this.zPosition = vec3d.zCoord;
        this.mustUpdate = false;
        return true;
    }

    @Nullable
    protected Vec3d func_190864_f() {
        return RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
    }

    @Override
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}

