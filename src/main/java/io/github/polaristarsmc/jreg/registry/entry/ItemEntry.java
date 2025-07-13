package io.github.polaristarsmc.jreg.registry.entry;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author baka4n
 * @code @Date 2025/7/10 21:04:40
 */
public class ItemEntry<T extends Item, P extends JRegistries<P>, S extends ItemEntry<T, P, S>> extends RegistryEntry<Item, T, DeferredItem<T>,P, S> {

    public ItemEntry(String name, Function<Item.Properties, T> function, P parent) {
        super(parent.items().registerItem(name, function), parent);
    }


    public S lang(String locale, String lang) {
        P parent = getParent();
        if (DatagenModLoader.isRunningDataGen()) {
            parent.langs.addItem(locale, getValue(), lang);
        }
        return self();
    }

    public S model(Consumer<ItemModelProvider> consumer) {
        P parent = getParent();
        if (DatagenModLoader.isRunningDataGen()) {
            parent.servers.item(consumer);
        }
        return self();
    }

    @SuppressWarnings("unchecked")
    public S self() {
        return (S) this;
    }
}
