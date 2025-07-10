package io.github.polaristarsmc.jreg;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JReg.MODID)
public class JReg {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jreg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final JRegistries REGISTRIES = new JRegistries(MODID);
    public static final List<DeferredItem<Item>> TEST =
            REGISTRIES.items(Item::new, "test", "test2", "test5", "test6", "test7", "test8", "test9", "test10");
    public JReg(IEventBus bus) {
        REGISTRIES.registerAll(bus);

    }


}
