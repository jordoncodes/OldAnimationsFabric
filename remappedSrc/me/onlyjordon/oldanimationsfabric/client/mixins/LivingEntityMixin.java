package me.onlyjordon.oldanimationsfabric.client.mixins;


import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean isUsingItem();

    @Shadow protected ItemStack activeItemStack;

    @Shadow protected int itemUseTimeLeft;


}
