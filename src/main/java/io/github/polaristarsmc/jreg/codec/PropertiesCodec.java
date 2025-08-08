package io.github.polaristarsmc.jreg.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * @author baka4n
 * @code @Date 2025/8/8 09:26:23
 */
public class PropertiesCodec {
   public static final Codec<Item.Properties> ITEM_PROPERTIES_CODEC = RecordCodecBuilder
           .create(instance -> instance.group(
                   DataComponentMap.CODEC.optionalFieldOf("components").forGetter(p -> {
                       if (p.components != null) {
                           return Optional.of(p.components.build());
                       }
                       return Optional.empty();
                   }),
                   ItemStack.CODEC.optionalFieldOf("craftingRemainingItem").forGetter(p -> {
                       if (p.craftingRemainingItem != null) {
                           return Optional.of(p.craftingRemainingItem.getDefaultInstance());
                       }
                       return Optional.empty();
                   }),
                   FeatureFlags.CODEC.optionalFieldOf("requiredFeatures").forGetter(p -> Optional.of(p.requiredFeatures)),
                   Codec.BOOL.optionalFieldOf("canRepair").forGetter(p -> Optional.of(p.canRepair))
           ).apply(instance, (components, craftingRemainingItem, requiredFeatures, canRepair) -> {
               Item.Properties properties = new Item.Properties();
               components.ifPresent(map -> {
                   //noinspection rawtypes
                   for (TypedDataComponent typedDataComponent : map) {
                       //noinspection unchecked
                       properties.component(() -> typedDataComponent.type(), typedDataComponent.value());
                   }
               });
               craftingRemainingItem.ifPresent(p -> {
                  properties.craftRemainder(p.getItem());
               });
               canRepair.ifPresent(p -> {
                   if (!p) {
                       properties.setNoRepair();
                   }
               });
               return properties;
           }));
}
