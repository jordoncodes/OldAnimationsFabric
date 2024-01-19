package me.onlyjordon.oldanimationsfabric.client;

import me.onlyjordon.oldanimationsfabric.client.interfaces.IMinecraftClientMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class OldAnimationsFabricClient implements ClientModInitializer {

    MinecraftClient mc = MinecraftClient.getInstance();
    public static me.onlyjordon.oldanimationsfabric.client.OldAnimations CONFIG = me.onlyjordon.oldanimationsfabric.client.OldAnimations.createAndLoad();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mc.player != null) {
                MinecraftClient mc = MinecraftClient.getInstance();
                IMinecraftClientMixin m = (IMinecraftClientMixin) mc;
                m.attemptSwing(mc.player);
            }
        });
    }




}
