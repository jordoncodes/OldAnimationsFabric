package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow private boolean breakingBlock;

    /**
     * @author onlyjordon
     * @reason couldn't find a better way
     */
    @Overwrite
    public boolean isBreakingBlock() {
        if (OldAnimationsFabricClient.CONFIG.oldBreakingAnim()) return false;
        return this.breakingBlock;
    }
}
