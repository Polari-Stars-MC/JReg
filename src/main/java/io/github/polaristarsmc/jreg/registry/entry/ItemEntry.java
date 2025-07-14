package io.github.polaristarsmc.jreg.registry.entry;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author baka4n
 * @code @Date 2025/7/10 21:04:40
 */
public class ItemEntry<T extends Item, P extends JRegistries<P>, S extends ItemEntry<T, P, S>> extends RegistryEntry<Item, T, DeferredItem<T>,P, S> implements Supplier<T> {


    private ItemEntry(DeferredItem<T> tDeferredItem, P parent) {
        super(tDeferredItem, parent);
    }

    private ItemEntry(String name, Function<Item.Properties, T> function, P parent) {
        this(parent.items().registerItem(name, function), parent);
    }

    public static <B extends Block, P extends JRegistries<P>, S extends ItemEntry<BlockItem, P, S>> ItemEntry<BlockItem, P, S> block(BlockEntry<B, P, ?> block) {
        DeferredBlock<B> value = block.getValue();
        P parent = block.getParent();
        return new ItemEntry<>(parent.items().registerSimpleBlockItem(value), parent);
    }

    public static <T extends Item, P extends JRegistries<P>, S extends ItemEntry<T, P, S>> ItemEntry<T, P, S> item(String name, Function<Item.Properties, T> function, P parent) {
        return new ItemEntry<>(name, function, parent);
    }


    public S lang(String locale, String lang) {
        P parent = getParent();
        if (DatagenModLoader.isRunningDataGen()) {
            parent.clients.addItem(locale, getValue(), lang);
        }
        return self();
    }

    public S model(Consumer<ItemModelProvider> consumer) {
        P parent = getParent();
        if (DatagenModLoader.isRunningDataGen()) {
            parent.clients.item(consumer);
        }
        return self();
    }

    @SuppressWarnings("unchecked")
    public S self() {
        return (S) this;
    }

    @Override
    public T get() {
        return getValue().get();
    }
}
