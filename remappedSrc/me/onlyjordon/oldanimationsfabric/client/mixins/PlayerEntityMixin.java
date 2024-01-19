package me.onlyjordon.oldanimationsfabric.client.mixins;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }
    @Inject(method = "tick",
            at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        this.lastAttackedTicks = 9999;
    }
    @ModifyConstant(method = "attack",
            constant = @Constant(intValue = 1),
            slice = @Slice(from = @At (value = "CONSTANT",
                    args = "classValue=net.minecraft.item.SwordItem")))
    private int constant(int tr_ue) {
        return 0;
    }
}
