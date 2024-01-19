package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class MixinBipedEntityModel<T extends LivingEntity> {
    @Shadow
    protected abstract void positionRightArm(T entity);

    @Shadow protected abstract void positionLeftArm(T entity);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V",
            shift = At.Shift.BEFORE),method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void OldAnimationsFabric$blockingAnimation(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.shieldSwordBlocking()) return;

        if (livingEntity.getActiveItem().getItem() instanceof SwordItem) {
//            if (livingEntity.getOffHandStack().getItem() instanceof ShieldItem)
//                this.positionRightArm(livingEntity);
            this.positionLeftArm(livingEntity);
        }
    }
}
