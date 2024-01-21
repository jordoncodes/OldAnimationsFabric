package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(at = @At(value = "RETURN"), method = "getArmPose", cancellable = true)
    private static void OldAnimationsFabric$getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack handStack = player.getStackInHand(hand);
        if (!(OldAnimationsFabricClient.CONFIG.shieldSwordBlocking() || OldAnimationsFabricClient.CONFIG.swordBlocking() || OldAnimationsFabricClient.CONFIG.thirdPersonSwordBlocking())) return;
//        if (hand == Hand.MAIN_HAND && handStack.getItem() instanceof SwordItem &&
//                (player.isBlocking() ||
//                        (player == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.useKey.isPressed()))) {
//            cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
//        }
        if (!OldAnimationsFabricClient.CONFIG.shieldSwordBlocking()) return;
        if (handStack.getItem() instanceof ShieldItem && hand == Hand.OFF_HAND && player.getMainHandStack().getItem() instanceof SwordItem) {
            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
        }
    }
}
