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
    private PackOutput output;
    private ExistingFileHelper exFileHelper;
    private final P p;

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


    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        int size = 0;

        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "(" + p.modid + ")";
    }
}
