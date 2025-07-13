package io.github.polaristarsmc.jreg;

import io.github.polaristarsmc.jreg.registry.JRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(JReg.MODID)
public class JReg {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "jreg";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final JRegistries<?> REGISTRIES = 
            new JRegistries<>(MODID)
                    .registerAll();
    public JReg(IEventBus bus) {}


}
