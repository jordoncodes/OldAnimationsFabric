package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient.*;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Shadow protected abstract void applyEatOrDrinkTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack);

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Inject(method = "renderFirstPersonItem", at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
                )
            )
    public void renderEatingDrinking(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null) return;
        if (!OldAnimationsFabricClient.CONFIG.oldEatingDrinkingAnim()) return;
        if (player.handSwinging) {
            applySwingOffset(matrices, player.getActiveHand() == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite(), swingProgress);
        }

    }

    @Inject(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal = 5,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    public void renderBowAnim(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null) return;
        if (!OldAnimationsFabricClient.CONFIG.oldBow()) return;
        applySwingOffset(matrices, player.getActiveHand() == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite(), swingProgress);
    }

    @Inject(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    public void renderCrossbowAnim(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null) return;
        if (!OldAnimationsFabricClient.CONFIG.affectNewItems()) return;
        applySwingOffset(matrices, player.getActiveHand() == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite(), swingProgress);
    }

    @Inject(method = "renderFirstPersonItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal = 6,
            shift = At.Shift.AFTER
    )
    )
    public void renderTrident(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null) return;
        if (!OldAnimationsFabricClient.CONFIG.affectNewItems()) return;
        applySwingOffset(matrices, player.getActiveHand() == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite(), swingProgress);
    }



    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            ),
            method = "renderFirstPersonItem"
    )
    public void rescaleItems(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (item.getItem() instanceof BowItem && OldAnimationsFabricClient.CONFIG.oldBow()) {
            // https://github.com/warpedvoxels/old-animations/blob/9efa3e15c3b83cec7f33e9034df19f4e3b6039f9/src/main/java/gq/nkkx/oldanimations/registry/ItemRescalingRegistry.java#L25
            matrices.translate(0f, 0.05f, 0.04f);
            matrices.scale(0.93f, 1f, 1f);
        }
        if (item.getItem() instanceof FishingRodItem && OldAnimationsFabricClient.CONFIG.oldFishingRod()) {
            // https://github.com/warpedvoxels/old-animations/blob/9efa3e15c3b83cec7f33e9034df19f4e3b6039f9/src/main/java/gq/nkkx/oldanimations/registry/ItemRescalingRegistry.java#L26
            matrices.translate(0.08f, -0.027f, -0.33f);
            matrices.scale(0.93f, 1f, 1f);
        }
        if (item.getItem() instanceof SwordItem && player.getActiveItem().getItem() instanceof SwordItem && OldAnimationsFabricClient.CONFIG.swordBlocking()) {
            matrices.translate(-0.05f, 0.039f, -0.04f); // y = up and down, x = left and right, z = depth
//            matrices.scale(0.93f, 1f, 1f);
        }
        if (item.getItem() instanceof SwordItem && !(player.getActiveItem().getItem() instanceof SwordItem) && OldAnimationsFabricClient.CONFIG.swordBlocking()) {
            matrices.translate(-0.02f, 0, 0);
        }
    }

    @Inject(
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
        ),
        method = "renderFirstPersonItem"
    )
    public void renderSwordBlocking(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(player.getInventory().getMainHandStack().getItem() instanceof SwordItem)) return;
        if (OldAnimationsFabricClient.CONFIG.swordBlocking() && player.getActiveItem().getItem() instanceof SwordItem && player.getStackInHand(hand).getItem() instanceof SwordItem) {
            applySwordBlockingOffset(player, matrices);
            applySwordSwingOffset(matrices, hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite(), swingProgress);
        } else if (OldAnimationsFabricClient.CONFIG.shieldSwordBlocking() && player.getActiveItem().getItem() instanceof ShieldItem) {
            if (hand != Hand.MAIN_HAND) return;
            applySwordBlockingOffset(player, matrices);
            applySwordSwingOffset(matrices, player.getMainArm(), swingProgress);
        }
    }

    @Unique
    private void applySwordBlockingOffset(AbstractClientPlayerEntity player, MatrixStack matrices) {
        Arm arm = player.getMainArm();
        int i = arm == Arm.RIGHT ? 1 : -1;
        matrices.translate(-0.04F, 0.05F, 0.04142136F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-(275.5f))); // -102.25
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i * -(51.635f))); // 13.365F
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i * (106.55f))); // 78.05F
    }

    @Unique
    private void applySwordSwingOffset(MatrixStack matrices, Arm arm, float swingProgress) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * (45.0f + f * 20.0f)));
        float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)i * g * 75.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(g * -20.0f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)i * -45.0f));
    }


    @Inject(at = @At("HEAD"), cancellable = true, method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void OldAnimationsFabric$hideShield(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.shieldSwordBlocking()) return;
        if (stack.getItem() instanceof ShieldItem && entity.getMainHandStack().getItem() instanceof SwordItem) {
            ci.cancel();
            if (entity.getActiveItem().getItem() instanceof SwordItem) entity.setCurrentHand(Hand.OFF_HAND); // use shield instead of sword :/
        }
    }

    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
    public void OldAnimationsFabric$resetEquipProgress(CallbackInfo ci) {
        if (!OldAnimationsFabricClient.CONFIG.swordBlocking()) return;
        if (MinecraftClient.getInstance().player == null) return;
        if (MinecraftClient.getInstance().player.getActiveItem().getItem() instanceof SwordItem) ci.cancel();
    }



}
