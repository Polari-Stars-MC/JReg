package io.github.polaristarsmc.jreg;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JReg.MODID)
public class JReg {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jreg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredBlock<Block> TEST1BLOCK = BLOCKS.registerBlock("test1block", Block::new);
    public static final DeferredItem<BlockItem> TEST1BLOCK$ITEM = ITEMS.registerSimpleBlockItem(TEST1BLOCK);
    public static final ExecutorService threadFactory = Executors.newVirtualThreadPerTaskExecutor();
    public static final CompletableFuture<DeferredItem<Item>>[] TEST = items(Item::new, "test", "test1", "test2", "test3", "test4", "test5", "test6");
    public static final RegistryBatchRegister registryBatchRegister = new RegistryBatchRegister(BLOCKS,ITEMS);
    public JReg(IEventBus bus) {
        CompletableFuture.allOf(TEST);
        registryBatchRegister.registerAll(bus);
    }

    public static <T extends Item> CompletableFuture<DeferredItem<T>>[] items(Function<Item.Properties, T> function, String... names) {
        CompletableFuture<DeferredItem<T>>[] future = new CompletableFuture[names.length];
        for (int i = 0; i < names.length; i++) {
            int finalI = i;
            future[i] = CompletableFuture.supplyAsync(() -> ITEMS.registerItem(names[finalI], function), threadFactory);
        }
        return future;
    }
}
