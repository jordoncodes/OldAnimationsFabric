package me.onlyjordon.oldanimationsfabric.client.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Inject(method = "renderFirstPersonItem", at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
                )
            )
    public void renderEatingDrinking(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player == null) return;

        if (player.handSwinging) {
            applySwingOffset(matrices, player.getActiveHand() == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite(), swingProgress);
        }

    }


//    @Inject(method = "renderFirstPersonItem", at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z",
//            ordinal = 1,
//            shift = At.Shift.BEFORE
//    ))
    @Inject(
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
        ),
        method = "renderFirstPersonItem"
    )
    public void renderSwordBlocking(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (hand != Hand.MAIN_HAND) return;
        if (!MinecraftClient.getInstance().options.useKey.isPressed()) return;
        if (!(player.getInventory().getMainHandStack().getItem() instanceof SwordItem)) return;
        player.setSprinting(false);
        if (!(player.getActiveItem().getItem() instanceof ShieldItem)) {
        }
        applySwordBlockingOffset(player, matrices, equipProgress);
    }

    @Unique
    private void applySwordBlockingOffset(AbstractClientPlayerEntity player, MatrixStack matrices, float equipProgress) {
        Arm arm = player.getMainArm();
//        this.applyEquipOffset(matrices, arm, equipProgress);
        // https://github.com/Fuzss/swordblockingmechanics/blob/3280e9cb604c58b8538efb3466cf462fd89d2fc3/src/main/java/com/fuzs/swordblockingmechanics/client/element/SwordBlockingExtension.java#L143
        int horizontal = arm == Arm.RIGHT ? 1 : -1;
        matrices.translate(horizontal * -0.14142136F, 0.08F, 0.14142136F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(horizontal * 13.365F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(horizontal * 78.05F));
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void OldAnimationsFabric$hideShield(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (stack.getItem() instanceof ShieldItem) ci.cancel();
    }

}
