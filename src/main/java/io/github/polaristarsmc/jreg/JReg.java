package io.github.polaristarsmc.jreg;

import com.mojang.logging.LogUtils;
import io.github.polaristarsmc.jreg.registry.JRegistries;
import io.github.polaristarsmc.jreg.registry.entry.ItemEntry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JReg.MODID)
public class JReg {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jreg";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final JRegistries<?> REGISTRIES =
            new JRegistries<>(MODID);

    public static final Supplier<Item> TEST = REGISTRIES
            .item("test", Item::new)
            .lang("en_us", "test")
            .lang("zh_cn", "测试");
    public static final Supplier<Block> TEST_BLOCK = REGISTRIES
            .block("test_block", Block::new)
            .lang("en_us", "test")
            .lang("zh_cn", "测试");
            ;
//    public static final Supplier<Block> TEST_BLOCK = REGISTRIES


    public JReg(IEventBus bus) {
        REGISTRIES.registerAll();
    }


}
