package io.github.simplycmd.terracraft.items.util.accessories;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class AttackSpeedAccessoryItem extends AccessoryItem{

    public AttackSpeedAccessoryItem(FabricItemSettings settings) {
        super(settings);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // attack speed
        modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(uuid, "terracraft:attack_speed", 0.12, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return modifiers;
    }
}
