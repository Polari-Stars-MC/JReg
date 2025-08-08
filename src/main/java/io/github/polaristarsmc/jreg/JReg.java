package io.github.polaristarsmc.jreg;

import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import io.github.polaristarsmc.jreg.codec.PropertiesCodec;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JReg.MODID)
public class JReg {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jreg";
    public static final Logger LOGGER = LogUtils.getLogger();


    public JReg(IEventBus bus) {
        PropertiesCodec.ITEM_PROPERTIES_CODEC.parse(JsonOps.INSTANCE, new JsonArray());
    }


}
