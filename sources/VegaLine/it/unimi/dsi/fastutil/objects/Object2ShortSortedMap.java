/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.Object2ShortMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ShortSortedMap<K>
extends Object2ShortMap<K>,
SortedMap<K, Short> {
    @Override
    public ObjectSortedSet<Map.Entry<K, Short>> entrySet();

    @Override
    public ObjectSortedSet<Object2ShortMap.Entry<K>> object2ShortEntrySet();

    @Override
    public ObjectSortedSet<K> keySet();

    @Override
    public ShortCollection values();

    @Override
    public Comparator<? super K> comparator();

    public Object2ShortSortedMap<K> subMap(K var1, K var2);

    public Object2ShortSortedMap<K> headMap(K var1);

    public Object2ShortSortedMap<K> tailMap(K var1);

    public static interface FastSortedEntrySet<K>
    extends ObjectSortedSet<Object2ShortMap.Entry<K>>,
    Object2ShortMap.FastEntrySet<K> {
        public ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> fastIterator(Object2ShortMap.Entry<K> var1);
    }
}

