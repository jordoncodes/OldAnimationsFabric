package me.onlyjordon.oldanimationsfabric.client;

import me.onlyjordon.oldanimationsfabric.client.interfaces.IMinecraftClientMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class OldAnimationsFabricClient implements ClientModInitializer {

    MinecraftClient mc = MinecraftClient.getInstance();

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
