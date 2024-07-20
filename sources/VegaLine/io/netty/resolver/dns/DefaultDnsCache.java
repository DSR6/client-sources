/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.resolver.dns.DnsCache;
import io.netty.resolver.dns.DnsCacheEntry;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class DefaultDnsCache
implements DnsCache {
    private final ConcurrentMap<String, List<DnsCacheEntry>> resolveCache = PlatformDependent.newConcurrentHashMap();
    private final int minTtl;
    private final int maxTtl;
    private final int negativeTtl;

    public DefaultDnsCache() {
        this(0, Integer.MAX_VALUE, 0);
    }

    public DefaultDnsCache(int minTtl, int maxTtl, int negativeTtl) {
        this.minTtl = ObjectUtil.checkPositiveOrZero(minTtl, "minTtl");
        this.maxTtl = ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl");
        if (minTtl > maxTtl) {
            throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
        }
        this.negativeTtl = ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl");
    }

    public int minTtl() {
        return this.minTtl;
    }

    public int maxTtl() {
        return this.maxTtl;
    }

    public int negativeTtl() {
        return this.negativeTtl;
    }

    @Override
    public void clear() {
        Iterator i = this.resolveCache.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry e = i.next();
            i.remove();
            DefaultDnsCache.cancelExpiration((List)e.getValue());
        }
    }

    @Override
    public boolean clear(String hostname) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        boolean removed = false;
        Iterator i = this.resolveCache.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry e = i.next();
            if (!((String)e.getKey()).equals(hostname)) continue;
            i.remove();
            DefaultDnsCache.cancelExpiration((List)e.getValue());
            removed = true;
        }
        return removed;
    }

    private static boolean emptyAdditionals(DnsRecord[] additionals) {
        return additionals == null || additionals.length == 0;
    }

    @Override
    public List<DnsCacheEntry> get(String hostname, DnsRecord[] additionals) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        if (!DefaultDnsCache.emptyAdditionals(additionals)) {
            return null;
        }
        return (List)this.resolveCache.get(hostname);
    }

    private List<DnsCacheEntry> cachedEntries(String hostname) {
        ArrayList newEntries;
        ArrayList oldEntries = (ArrayList)this.resolveCache.get(hostname);
        ArrayList entries = oldEntries == null ? ((oldEntries = (List)this.resolveCache.putIfAbsent(hostname, newEntries = new ArrayList(8))) != null ? oldEntries : newEntries) : oldEntries;
        return entries;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cache(String hostname, DnsRecord[] additionals, InetAddress address, long originalTtl, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(address, "address");
        ObjectUtil.checkNotNull(loop, "loop");
        if (this.maxTtl == 0 || !DefaultDnsCache.emptyAdditionals(additionals)) {
            return;
        }
        int ttl = Math.max(this.minTtl, (int)Math.min((long)this.maxTtl, originalTtl));
        List<DnsCacheEntry> entries = this.cachedEntries(hostname);
        DnsCacheEntry e = new DnsCacheEntry(hostname, address);
        List<DnsCacheEntry> list = entries;
        synchronized (list) {
            DnsCacheEntry firstEntry;
            if (!entries.isEmpty() && (firstEntry = entries.get(0)).cause() != null) {
                assert (entries.size() == 1);
                firstEntry.cancelExpiration();
                entries.clear();
            }
            entries.add(e);
        }
        this.scheduleCacheExpiration(entries, e, ttl, loop);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void cache(String hostname, DnsRecord[] additionals, Throwable cause, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(cause, "cause");
        ObjectUtil.checkNotNull(loop, "loop");
        if (this.negativeTtl == 0 || !DefaultDnsCache.emptyAdditionals(additionals)) {
            return;
        }
        List<DnsCacheEntry> entries = this.cachedEntries(hostname);
        DnsCacheEntry e = new DnsCacheEntry(hostname, cause);
        List<DnsCacheEntry> list = entries;
        synchronized (list) {
            int numEntries = entries.size();
            for (int i = 0; i < numEntries; ++i) {
                entries.get(i).cancelExpiration();
            }
            entries.clear();
            entries.add(e);
        }
        this.scheduleCacheExpiration(entries, e, this.negativeTtl, loop);
    }

    private static void cancelExpiration(List<DnsCacheEntry> entries) {
        int numEntries = entries.size();
        for (int i = 0; i < numEntries; ++i) {
            entries.get(i).cancelExpiration();
        }
    }

    private void scheduleCacheExpiration(final List<DnsCacheEntry> entries, final DnsCacheEntry e, int ttl, EventLoop loop) {
        e.scheduleExpiration(loop, new Runnable(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                List list = entries;
                synchronized (list) {
                    entries.remove(e);
                    if (entries.isEmpty()) {
                        DefaultDnsCache.this.resolveCache.remove(e.hostname());
                    }
                }
            }
        }, ttl, TimeUnit.SECONDS);
    }

    public String toString() {
        return "DefaultDnsCache(minTtl=" + this.minTtl + ", maxTtl=" + this.maxTtl + ", negativeTtl=" + this.negativeTtl + ", cached resolved hostname=" + this.resolveCache.size() + ")";
    }
}

