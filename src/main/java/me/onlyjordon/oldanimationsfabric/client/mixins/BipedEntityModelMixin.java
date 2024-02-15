package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {

    @Shadow protected abstract void positionLeftArm(T entity);

    @Shadow @Final public ModelPart rightArm;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V",
            shift = At.Shift.BEFORE),method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void OldAnimationsFabric$blockingAnimation(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.shieldSwordBlocking()) return;

        if (livingEntity.getActiveItem().getItem() instanceof SwordItem) {
            this.positionLeftArm(livingEntity);
        }
    }
    @Inject(
            method = "positionRightArm",
            at = @At(value = "TAIL")
    )
    private void swordBlocking(T entity, CallbackInfo ci) {
        if (!(OldAnimationsFabricClient.CONFIG.thirdPersonSwordBlocking() || OldAnimationsFabricClient.CONFIG.swordBlocking() || OldAnimationsFabricClient.CONFIG.shieldSwordBlocking())) return;
        if (entity.getMainHandStack().getItem() instanceof SwordItem &&
                ((entity.getActiveItem().getItem() instanceof SwordItem) ||
                        (entity == MinecraftClient.getInstance().player && entity.getActiveItem().getItem() instanceof SwordItem))) {
            this.rightArm.pitch = -0.75f;
            this.rightArm.yaw = 0.0f;
        }
    }

}
