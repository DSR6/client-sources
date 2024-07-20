/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaTypeTemplate;
import io.netty.buffer.ByteBuf;

public abstract class ModernMetaType
extends MetaTypeTemplate {
    private static final int END = 255;

    @Override
    public Metadata read(ByteBuf buffer) throws Exception {
        short index = buffer.readUnsignedByte();
        if (index == 255) {
            return null;
        }
        MetaType type2 = this.getType(Type.VAR_INT.readPrimitive(buffer));
        return new Metadata(index, type2, type2.type().read(buffer));
    }

    protected abstract MetaType getType(int var1);

    @Override
    public void write(ByteBuf buffer, Metadata object) throws Exception {
        if (object == null) {
            buffer.writeByte(255);
        } else {
            buffer.writeByte(object.id());
            MetaType type2 = object.metaType();
            Type.VAR_INT.writePrimitive(buffer, type2.typeId());
            type2.type().write(buffer, object.getValue());
        }
    }
}

