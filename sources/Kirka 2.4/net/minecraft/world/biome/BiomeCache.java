/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class BiomeCache {
    private final WorldChunkManager chunkManager;
    private long lastCleanupTime;
    private LongHashMap cacheMap = new LongHashMap();
    private List cache = Lists.newArrayList();
    private static final String __OBFID = "CL_00000162";

    public BiomeCache(WorldChunkManager p_i1973_1_) {
        this.chunkManager = p_i1973_1_;
    }

    public Block getBiomeCacheBlock(int p_76840_1_, int p_76840_2_) {
        long var3 = (long)(p_76840_1_ >>= 4) & 0xFFFFFFFFL | ((long)(p_76840_2_ >>= 4) & 0xFFFFFFFFL) << 32;
        Block var5 = (Block)this.cacheMap.getValueByKey(var3);
        if (var5 == null) {
            var5 = new Block(p_76840_1_, p_76840_2_);
            this.cacheMap.add(var3, var5);
            this.cache.add(var5);
        }
        var5.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
        return var5;
    }

    public BiomeGenBase func_180284_a(int p_180284_1_, int p_180284_2_, BiomeGenBase p_180284_3_) {
        BiomeGenBase var4 = this.getBiomeCacheBlock(p_180284_1_, p_180284_2_).getBiomeGenAt(p_180284_1_, p_180284_2_);
        return var4 == null ? p_180284_3_ : var4;
    }

    public void cleanupCache() {
        long var1 = MinecraftServer.getCurrentTimeMillis();
        long var3 = var1 - this.lastCleanupTime;
        if (var3 > 7500L || var3 < 0L) {
            this.lastCleanupTime = var1;
            for (int var5 = 0; var5 < this.cache.size(); ++var5) {
                Block var6 = (Block)this.cache.get(var5);
                long var7 = var1 - var6.lastAccessTime;
                if (var7 <= 30000L && var7 >= 0L) continue;
                this.cache.remove(var5--);
                long var9 = (long)var6.xPosition & 0xFFFFFFFFL | ((long)var6.zPosition & 0xFFFFFFFFL) << 32;
                this.cacheMap.remove(var9);
            }
        }
    }

    public BiomeGenBase[] getCachedBiomes(int p_76839_1_, int p_76839_2_) {
        return this.getBiomeCacheBlock((int)p_76839_1_, (int)p_76839_2_).biomes;
    }

    public class Block {
        public float[] rainfallValues = new float[256];
        public BiomeGenBase[] biomes = new BiomeGenBase[256];
        public int xPosition;
        public int zPosition;
        public long lastAccessTime;
        private static final String __OBFID = "CL_00000163";

        public Block(int p_i1972_2_, int p_i1972_3_) {
            this.xPosition = p_i1972_2_;
            this.zPosition = p_i1972_3_;
            BiomeCache.this.chunkManager.getRainfall(this.rainfallValues, p_i1972_2_ << 4, p_i1972_3_ << 4, 16, 16);
            BiomeCache.this.chunkManager.getBiomeGenAt(this.biomes, p_i1972_2_ << 4, p_i1972_3_ << 4, 16, 16, false);
        }

        public BiomeGenBase getBiomeGenAt(int p_76885_1_, int p_76885_2_) {
            return this.biomes[p_76885_1_ & 15 | (p_76885_2_ & 15) << 4];
        }
    }

}

