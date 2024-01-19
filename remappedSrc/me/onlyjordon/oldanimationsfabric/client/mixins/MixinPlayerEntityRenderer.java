package me.onlyjordon.oldanimationsfabric.client.mixins;

import net.minecraft.client.MinecraftClient;
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
public class MixinPlayerEntityRenderer {
    @Inject(at = @At(value = "RETURN"), method = "getArmPose", cancellable = true)
    private static void OldAnimationsFabric$getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack handStack = player.getStackInHand(hand);
//        ItemStack offStack = player.getStackInHand(hand.equals(Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND);
        if (handStack.getItem() instanceof SwordItem && MinecraftClient.getInstance().options.useKey.isPressed()) {
            cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
        }

//        if (offStack.getItem() instanceof ShieldItem && player.isUsingItem()) {
//            cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
//        }
        if (handStack.getItem() instanceof ShieldItem) {
            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
        }
    }
}
