package io.github.polaristarsmc.jreg.registry;

import io.github.polaristarsmc.jreg.registry.data.ClientProvider;
import io.github.polaristarsmc.jreg.registry.data.ServerProvider;
import io.github.polaristarsmc.jreg.registry.entry.BlockEntry;
import io.github.polaristarsmc.jreg.registry.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author baka4n
 * @code @Date 2025/7/10 13:31:51
 */
public class JRegistries<P extends JRegistries<P>> {
    public final ClientProvider<P> clients = new ClientProvider<>(self());
    public final ServerProvider<P> servers = new ServerProvider<>(self());

    // multi-thread support
    private final ConcurrentHashMap<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries = new ConcurrentHashMap<>();
    public final String modid;
    public final IEventBus modBus, forgeBus;

    public DeferredRegister.Items items() {
        if (registries.containsKey(Registries.ITEM)) {
            return (DeferredRegister.Items) registries.get(Registries.ITEM);
        }
        DeferredRegister.Items items = DeferredRegister.createItems(modid);
        registries.put(Registries.ITEM, items);
        return items;
    }

    @SuppressWarnings("unchecked")
    public DeferredRegister<BlockEntityType<?>> be() {
        if (registries.containsKey(Registries.BLOCK_ENTITY_TYPE)) {
            return (DeferredRegister<BlockEntityType<?>>) registries.get(Registries.BLOCK_ENTITY_TYPE);
        }
        var be = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modid);
        registries.put(Registries.BLOCK_ENTITY_TYPE, be);
        return be;
    }

    public DeferredRegister.Blocks blocks() {
        if (registries.containsKey(Registries.BLOCK)) {
            return (DeferredRegister.Blocks) registries.get(Registries.BLOCK);
        }
        DeferredRegister.Blocks blocks = DeferredRegister.createBlocks(modid);
        registries.put(Registries.BLOCK, blocks);
        return blocks;
    }


    public JRegistries(String modid, IEventBus modBus) {
        this.modid = modid;
        this.modBus = modBus;
        this.forgeBus = NeoForge.EVENT_BUS;
    }
    public JRegistries(String modid) {
        this(modid, Objects.requireNonNull(ModList.get().getModContainerById(modid).orElse(null)).getEventBus());
    }

    public <T extends Item> ItemEntry<T, P, ?> item(String name, Function<Item.Properties, T> function) {
        return ItemEntry.item(name, function, self());
    }
    public <T extends Block> BlockEntry<T, P, ?> block(String name, Function<BlockBehaviour.Properties, T> function) {
        return new BlockEntry<>(name, function, self());
    }


    @SuppressWarnings("UnusedReturnValue")
    public P registerAll() {
        for (DeferredRegister<?> register : registries.values()) {
            register.register(modBus);
        }
        if (DatagenModLoader.isRunningDataGen()) {
            modBus.addListener(GatherDataEvent.class, event -> {
                DataGenerator generator = event.getGenerator();
                PackOutput packOutput = generator.getPackOutput();
                ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
                generator.addProvider(event.includeClient(), clients.setOutput(packOutput).setExFileHelper(existingFileHelper));
                generator.addProvider(event.includeServer(), servers.setOutput(packOutput).setExFileHelper(existingFileHelper));
            });
        }
        return self();
    }

    public P init(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName());
            } catch (ClassNotFoundException ignored) {}
        }
        return self();
    }

    @SuppressWarnings("unchecked")
    public P self() {
        return (P) this;
    }
}
