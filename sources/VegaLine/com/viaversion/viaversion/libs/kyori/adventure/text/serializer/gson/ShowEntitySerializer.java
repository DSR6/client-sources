/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.TypeAdapter;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.SerializerFactory;
import java.io.IOException;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

final class ShowEntitySerializer
extends TypeAdapter<HoverEvent.ShowEntity> {
    private final Gson gson;

    static TypeAdapter<HoverEvent.ShowEntity> create(Gson gson) {
        return new ShowEntitySerializer(gson).nullSafe();
    }

    private ShowEntitySerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public HoverEvent.ShowEntity read(JsonReader in) throws IOException {
        in.beginObject();
        Key type2 = null;
        UUID id = null;
        Component name = null;
        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("type")) {
                type2 = (Key)this.gson.fromJson(in, SerializerFactory.KEY_TYPE);
                continue;
            }
            if (fieldName.equals("id")) {
                id = UUID.fromString(in.nextString());
                continue;
            }
            if (fieldName.equals("name")) {
                name = (Component)this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE);
                continue;
            }
            in.skipValue();
        }
        if (type2 == null || id == null) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        in.endObject();
        return HoverEvent.ShowEntity.showEntity(type2, id, name);
    }

    @Override
    public void write(JsonWriter out, HoverEvent.ShowEntity value) throws IOException {
        out.beginObject();
        out.name("type");
        this.gson.toJson((Object)value.type(), SerializerFactory.KEY_TYPE, out);
        out.name("id");
        out.value(value.id().toString());
        @Nullable Component name = value.name();
        if (name != null) {
            out.name("name");
            this.gson.toJson((Object)name, SerializerFactory.COMPONENT_TYPE, out);
        }
        out.endObject();
    }
}

