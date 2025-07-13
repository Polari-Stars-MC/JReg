package io.github.polaristarsmc.jreg.registry.entry;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

/**
 * @author baka4n
 * @code @Date 2025/7/10 20:59:39
 */
public abstract class Entry<T, P extends JRegistries<P>, S extends Entry<T, P, S>> {
    private final T value;
    private final P parent;

    protected Entry(T value, P parent) {
        this.value = value;
        this.parent = parent;
    }

    public P getParent() {
        return parent;
    }

    public T getValue() {
        return value;
    }

    public abstract S lang(String locale, String lang);

    public abstract S self();
}
