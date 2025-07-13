package io.github.polaristarsmc.jreg.registry.data;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author baka4n
 * @code @Date 2025/7/12 09:40:00
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ClientProvider<P extends JRegistries<P>> implements DataProvider {
    private PackOutput output;
    private  final P p;
    private final ConcurrentHashMap<String, List<Consumer<ILang<P>>>> data = new ConcurrentHashMap<>();
    public ClientProvider(P p) {
        this.p = p;
    }

    public ClientProvider<P> add(String locale, Consumer<ILang<P>> consumer) {
        if (!data.containsKey(locale))
            data.put(locale, new ArrayList<>());
        data.get(locale).add(consumer);
        return this;
    }public ClientProvider<P> add(String locale, String key, String name) {
        return add(locale, l -> l.add(key, name));
    }

    public <T extends Item> ClientProvider<P> addItem(String locale, DeferredItem<T> item, String name) {
        return add(locale, l -> l.add(item.get(), name));
    }

    public static class ILang<P extends JRegistries<P>> extends LanguageProvider {
        private final List<Consumer<ILang<P>>> list;
        public ILang(PackOutput output, String locale, P p, List<Consumer<ILang<P>>> list) {
            super(output, p.modid, locale);
            this.list = list;
        }

        @Override
        protected void addTranslations() {
            for (var c : list) {
                c.accept(this);
            }
        }
    }

    public ClientProvider<P> setOutput(PackOutput output) {
        this.output = output;
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        int size = 0;
        if (!data.isEmpty()) {
            size += data.size();
        }
        CompletableFuture<?>[] futures = new CompletableFuture[size];
        int i = 0;
        if (!data.isEmpty()) {
            for (var e : data.entrySet()) {
                futures[i] = new ILang<>(output, e.getKey(), p, e.getValue())
                        .run(cachedOutput);
                i++;
            }
        }

        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName()  + "(" + p.modid + ")";
    }
}
