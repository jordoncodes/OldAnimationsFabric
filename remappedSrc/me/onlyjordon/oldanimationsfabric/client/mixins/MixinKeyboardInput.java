package me.onlyjordon.oldanimationsfabric.client.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput extends Input {

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (!(player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem)) return;
        if (!MinecraftClient.getInstance().options.useKey.isPressed()) return;
        if (OldAnimationsFabricClient.CONFIG.disableSwordBlocking()) return;
        if (!(player.isUsingItem() && !player.hasVehicle())) {
            movementSideways *= 0.2f;
            movementForward *= 0.2f;
        }
    }
}
