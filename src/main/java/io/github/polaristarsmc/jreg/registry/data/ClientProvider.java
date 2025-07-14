package io.github.polaristarsmc.jreg.registry.data;

import com.sun.jna.platform.win32.DBT;
import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
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
    public ExistingFileHelper exFileHelper;
    private final ConcurrentHashMap<String, List<Consumer<ILang<P>>>> data = new ConcurrentHashMap<>();
    private final List<Consumer<ClientProvider.IBlock<P>>> states = new ArrayList<>();

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
    public <T extends Block> ClientProvider<P> addBlock(String locale, DeferredBlock<T> block, String name) {
        return add(locale, l -> l.add(block.get(), name));
    }


    public ClientProvider<P> item(Consumer<ItemModelProvider> consumer) {
        return block(blockStateProvider -> {
            consumer.accept(blockStateProvider.itemModels());
        });
    }

    public ClientProvider<P> block(Consumer<ClientProvider.IBlock<P>> consumer) {
        states.add(consumer);
        return this;
    }

    public static class IBlock<P extends JRegistries<P>> extends BlockStateProvider {
        private final List<Consumer<IBlock<P>>> states;
        public IBlock(PackOutput output, P p, ExistingFileHelper exFileHelper, List<Consumer<IBlock<P>>> states) {
            super(output, p.modid, exFileHelper);
            this.states = states;
        }

        @Override
        protected void registerStatesAndModels() {
            for (Consumer<IBlock<P>> consumer : states) {
                consumer.accept(this);
            }
        }
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

    public ClientProvider<P> setExFileHelper(ExistingFileHelper exFileHelper) {
        this.exFileHelper = exFileHelper;
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        int size = 0;
        if (!data.isEmpty()) {
            size += data.size();
        }
        if (!states.isEmpty()) size++;

        CompletableFuture<?>[] futures = new CompletableFuture[size];
        int i = 0;
        if (!data.isEmpty()) {
            for (var e : data.entrySet()) {
                futures[i] = new ILang<>(output, e.getKey(), p, e.getValue())
                        .run(cachedOutput);
                i++;
            }
        }

        if (!states.isEmpty()) {
            var blockStateProvider = new ClientProvider.IBlock<>(output, p, exFileHelper, states);
            futures[i] = blockStateProvider.run(cachedOutput);
            i++;
        }
        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName()  + "(" + p.modid + ")";
    }
}
