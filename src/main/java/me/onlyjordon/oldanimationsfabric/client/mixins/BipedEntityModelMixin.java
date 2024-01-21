package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity>  extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
    @Shadow @Final public ModelPart rightArm;


    @Inject(
            method = "positionRightArm",
            at = @At(value = "TAIL")
    )
    private void swordBlocking(T entity, CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.thirdPersonSwordBlocking()) return;
        if (entity.getMainHandStack().getItem() instanceof SwordItem &&
                ((entity.getActiveItem().getItem() instanceof SwordItem) ||
                        (entity == MinecraftClient.getInstance().player && entity.getActiveItem().getItem() instanceof SwordItem))) {
            this.rightArm.pitch = -0.75f;
        }
    }

    @Shadow @Override
    protected Iterable<ModelPart> getHeadParts() {
        return null;
    }

    @Shadow @Override
    protected Iterable<ModelPart> getBodyParts() {
        return null;
    }

    @Shadow @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Shadow @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {

    }

    @Shadow @Override
    public ModelPart getHead() {
        return null;
    }

    @Shadow protected abstract void positionLeftArm(T entity);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V",
            shift = At.Shift.BEFORE),method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void OldAnimationsFabric$blockingAnimation(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.shieldSwordBlocking()) return;
        if (livingEntity.getActiveItem().getItem() instanceof SwordItem) {
            this.positionLeftArm(livingEntity);
        }
    }
}
