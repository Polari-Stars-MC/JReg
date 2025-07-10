package io.github.polaristarsmc.jreg.registry;

import com.google.common.base.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * @author baka4n
 * @code @Date 2025/7/10 13:31:51
 */
public class JRegistries implements AutoCloseable {
    // multi-thread support
    public final ConcurrentHashMap<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries = new ConcurrentHashMap<>();
    public final String modid;
    private final ExecutorService executor;

    public DeferredRegister.Items items() {
        if (registries.containsKey(Registries.ITEM)) {
            return (DeferredRegister.Items) registries.get(Registries.ITEM);
        }
        DeferredRegister.Items items = DeferredRegister.createItems(modid);
        registries.put(Registries.ITEM, items);
        return items;
    }

    public <T extends Item> DeferredItem<T> item(String name, Function<Item.Properties, T> function) {
        return items().registerItem(name, function);
    }

    public <T extends Item> List<DeferredItem<T>> items(Function<Item.Properties, T> function, String... names) {
        CompletableFuture<DeferredItem<T>>[] futures = new CompletableFuture[names.length];

        DeferredRegister.Items items = items();
        for (int i = 0; i < names.length; i++) {
            int finalI = i;
            futures[i] = CompletableFuture.supplyAsync(() -> items.registerItem(names[finalI], function), executor);
        }
        CompletableFuture.allOf(futures);
        return Arrays.stream(futures).map(CompletableFuture::join).toList();
    }


    public JRegistries(String modid) {
        this.modid = modid;
        executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void registerAll(IEventBus modEventBus) {
        for (DeferredRegister<?> register : registries.values()) {
            executor.submit(() -> register.register(modEventBus));
        }
        close();
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
