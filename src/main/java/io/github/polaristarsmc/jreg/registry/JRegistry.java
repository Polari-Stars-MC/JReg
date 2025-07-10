package io.github.polaristarsmc.jreg.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/7/10 13:31:09
 */
public class JRegistry<T, P> {
    private final DeferredRegister<T> register;
    private final P parent;

    public JRegistry(DeferredRegister<T> register, P parent) {
        this.register = register;
        this.parent = parent;
    }

    public DeferredHolder<T, T> register(String name, Supplier<T> supplier) {
        return register.register(name, supplier);
    }
}
