package me.onlyjordon.oldanimationsfabric.client.mixins;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    /**
     * @author onlyjordon
     * @reason couldn't find a better way
     */
    @Overwrite
    public boolean isBreakingBlock() {
        return false;
    }
}
