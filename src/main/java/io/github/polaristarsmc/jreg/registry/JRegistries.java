package io.github.polaristarsmc.jreg.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/7/10 13:31:51
 */
public class JRegistries {
    public static final Map<ResourceKey<? extends Registry<?>>, JRegistry<?, ?>> REGISTRY = new HashMap<>();
    public final String modid;

    public JRegistries(String modid) {
        this.modid = modid;
    }

    @SuppressWarnings("unchecked")
    public <T> JRegistry<T, JRegistries> register(ResourceKey<? extends Registry<T>> key, Supplier<T> value) {
        JRegistry<T, JRegistries> registry = (JRegistry<T, JRegistries>) REGISTRY.getOrDefault(key, new JRegistry<>(DeferredRegister.create(key, modid), this));
        return registry;
    }
}
