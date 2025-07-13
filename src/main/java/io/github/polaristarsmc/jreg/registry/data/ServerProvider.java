package io.github.polaristarsmc.jreg.registry.data;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author baka4n
 * @code @Date 2025/7/13 16:12:39
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ServerProvider<P extends JRegistries<P>> implements DataProvider {
    private final List<Consumer<IBlock<P>>> states = new ArrayList<>();
    private PackOutput output;
    private ExistingFileHelper exFileHelper;
    private final P p;

    public ServerProvider<P> item(Consumer<ItemModelProvider> consumer) {
        return block(blockStateProvider -> {
            consumer.accept(blockStateProvider.itemModels());
        });
    }

    public ServerProvider<P> block(Consumer<IBlock<P>> consumer) {
        states.add(consumer);
        return this;
    }

    public ServerProvider(P p) {
        this.p = p;
    }

    public ServerProvider<P> setOutput(PackOutput output) {
        this.output = output;
        return this;
    }

    public ServerProvider<P> setExFileHelper(ExistingFileHelper exFileHelper) {
        this.exFileHelper = exFileHelper;
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

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        int size = 0;
        if (!states.isEmpty()) size++;
        CompletableFuture<?>[] futures = new CompletableFuture<?>[size];
        int i = 0;
        if (!states.isEmpty()) {
            IBlock<P> blockStateProvider = new IBlock<>(output, p, exFileHelper, states);
            futures[i] = blockStateProvider.run(cachedOutput);
            i++;
        }
        return CompletableFuture.allOf(futures);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "(" + p.modid + ")";
    }
}
