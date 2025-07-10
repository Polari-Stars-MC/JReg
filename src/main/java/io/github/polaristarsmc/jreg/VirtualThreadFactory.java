package io.github.polaristarsmc.jreg;

import java.util.concurrent.ThreadFactory;

/**
 * @author baka4n
 * @code @Date 2025/7/10 16:15:04
 */
public class VirtualThreadFactory {
    public static ThreadFactory create() {
        return Thread.ofVirtual().factory();
    }
}
