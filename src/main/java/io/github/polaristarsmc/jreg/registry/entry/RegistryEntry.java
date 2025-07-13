package io.github.polaristarsmc.jreg.registry.entry;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * @author baka4n
 * @code @Date 2025/7/10 21:01:47
 */
public abstract class RegistryEntry<E,T extends E,H extends DeferredHolder<E, T>, P extends JRegistries<P>, S extends RegistryEntry<E, T, H, P, S>> extends Entry<H, P, S> {
    protected RegistryEntry(H h, P parent) {
        super(h, parent);
    }
}
