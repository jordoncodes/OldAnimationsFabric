package me.onlyjordon.oldanimationsfabric.client.mixins;


import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin<T extends LivingEntity> {

    //    https://github.com/warpedvoxels/old-animations/blob/9efa3e15c3b83cec7f33e9034df19f4e3b6039f9/src/main/java/gq/nkkx/oldanimations/features/SwordBlockingFeature.java#L21
    @Unique
    private static final Quaternionf THIRD_PERSON_QUATERNION = RotationAxis.POSITIVE_Y.rotationDegrees(-45f);
    @Unique
    private static final Quaternionf NEGATIVE_THIRD_PERSON_QUATERNION = RotationAxis.POSITIVE_Y.rotationDegrees(45f);

    // https://github.com/warpedvoxels/old-animations/blob/9efa3e15c3b83cec7f33e9034df19f4e3b6039f9/src/main/java/gq/nkkx/oldanimations/features/SwordBlockingFeature.java#L61
    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
    private void old_animations$renderItem(LivingEntity livingEntity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.thirdPersonSwordBlocking()) return;
        if (livingEntity.getMainHandStack().getItem() instanceof SwordItem &&
                ((livingEntity.getActiveItem().getItem() instanceof SwordItem
                    || livingEntity.getActiveItem().getItem() instanceof ShieldItem))) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            if (livingEntity.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem) {
                matrices.translate(i*-0.14142136f, -0.05f, i*0.14142136f);
                matrices.multiply(THIRD_PERSON_QUATERNION);
            } else {
                matrices.translate(i*0.14142136f, -0.05f, i*0.14142136f);
                matrices.multiply(NEGATIVE_THIRD_PERSON_QUATERNION);
            }
        }
    }

}
