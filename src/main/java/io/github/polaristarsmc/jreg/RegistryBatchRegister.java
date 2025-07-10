package io.github.polaristarsmc.jreg;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author baka4n
 * @code @Date 2025/7/10 16:15:31
 */
public class RegistryBatchRegister {
    private final ExecutorService executor;
    private final DeferredRegister<?>[] registers;

    public RegistryBatchRegister(DeferredRegister<?>... registers) {
        this.registers = registers;
        this.executor = Executors.newThreadPerTaskExecutor(VirtualThreadFactory.create());
    }

    public void registerAll(IEventBus modEventBus) {
        for (DeferredRegister<?> register : registers) {
            executor.submit(() -> {
                register.register(modEventBus);
            });
        }

        executor.shutdown();
    }
}
