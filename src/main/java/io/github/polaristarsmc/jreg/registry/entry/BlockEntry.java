package io.github.polaristarsmc.jreg.registry.entry;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import io.github.polaristarsmc.jreg.registry.data.ClientProvider;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/7/14 08:13:18
 */
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class BlockEntry<T extends Block, P extends JRegistries<P>, S extends BlockEntry<T, P, S>> extends RegistryEntry<Block, T, DeferredBlock<T>, P, S> implements Supplier<Block>, ItemLike {
    final AtomicReference<ItemEntry<BlockItem, ?, ?>> blockItem = new AtomicReference<>();
    public BlockEntry(String name, Function<BlockBehaviour.Properties, T> function, P parent) {
        super(parent.blocks().registerBlock(name, function), parent);
    }

    public S item(Consumer<ItemEntry<BlockItem, ?, ?>> consumer) {
        ItemEntry<BlockItem, P, ?> block = ItemEntry.block(this);
        consumer.accept(block);
        blockItem.set(block);
        return self();
    }

    public S model(Consumer<ClientProvider.IBlock<P>> consumer) {
        P parent = getParent();
        if (DatagenModLoader.isRunningDataGen()) {
            parent.clients.block(consumer);
        }
        return self();
    }

    @Override
    public S lang(String locale, String lang) {
        P parent = getParent();
        if (DatagenModLoader.isRunningDataGen()) {
            parent.clients.addBlock(locale, getValue(), lang);
        }
        return self();
    }

    @SuppressWarnings("unchecked")
    @Override
    public S self() {
        return (S) this;
    }

    @Override
    public Block get() {
        return getValue().get();
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
