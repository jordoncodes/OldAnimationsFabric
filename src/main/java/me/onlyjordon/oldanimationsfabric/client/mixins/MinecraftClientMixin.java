package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import me.onlyjordon.oldanimationsfabric.client.interfaces.IMinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IMinecraftClientMixin {


    @Shadow
    public HitResult crosshairTarget;

    @Shadow
    protected int attackCooldown;

    @Shadow
    public ClientWorld world;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Final
    @Shadow
    public ParticleManager particleManager;

    @Shadow
    public GameOptions options;


    @Shadow
    @Nullable
    public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Shadow public abstract float getTickDelta();

    @Shadow @Nullable public ClientPlayerEntity player;

    @Unique
    private void swingItem(Hand hand, ClientPlayerEntity player) {
        if (!player.handSwinging || player.handSwingTicks >= this.getHandSwingDuration(player) / 2 || player.handSwingTicks < 0) {
            player.handSwingTicks = -1;
            player.handSwinging = true;
            player.preferredHand = hand;
            if (OldAnimationsFabricClient.CONFIG.fullOldBreaking()) {
                ClientPlayNetworkHandler handler = this.getNetworkHandler();
                if (handler != null)
                    this.getNetworkHandler().sendPacket(new HandSwingC2SPacket(hand));
            }
        }
    }

    @Unique
    private int getHandSwingDuration(ClientPlayerEntity player) {
        if (StatusEffectUtil.hasHaste(player)) {
            return 6 - (1 + StatusEffectUtil.getHasteAmplifier(player));
        }
        if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            return 6 + (1 + player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) * 2;
        }
        return 6;
    }

    @Override
    public void attemptSwing(ClientPlayerEntity player) {
        if (!OldAnimationsFabricClient.CONFIG.oldBreakingAnim()) return;
        if (world == null) return;
        if (interactionManager == null) return;
        if (this.options.attackKey.isPressed() && this.options.useKey.isPressed()) {
            if (this.crosshairTarget != null && this.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                Direction direction;
                BlockHitResult blockHitResult = (BlockHitResult) this.crosshairTarget;
                BlockPos blockPos = blockHitResult.getBlockPos();
                Item item = player.getActiveItem().getItem();
                boolean isAnimatableOld = (
                        item instanceof ShieldItem || item instanceof SwordItem ||
                                item instanceof BowItem || item.isFood() || item instanceof PotionItem
                );
                boolean isAnimatableNew = (
                        item instanceof CrossbowItem || item instanceof TridentItem
                );
                boolean shouldAnimate = isAnimatableOld || (isAnimatableNew && OldAnimationsFabricClient.CONFIG.affectNewItems());
                boolean bl = player.getActiveItem().getItem() instanceof ShieldItem;
                if (!shouldAnimate) return;
                else if (player.getActiveHand() == Hand.MAIN_HAND && bl) return;
                if (bl && !(player.getMainHandStack().getItem() instanceof SwordItem)) return;
                swingItem(bl ? Hand.MAIN_HAND : player.getActiveHand(), player);
                if (OldAnimationsFabricClient.CONFIG.fullOldBreaking()) {
                        if (!this.world.getBlockState(blockPos).isAir() && this.interactionManager.updateBlockBreakingProgress(blockPos, direction = blockHitResult.getSide())) {
                            this.particleManager.addBlockBreakingParticles(blockPos, direction);
                        }
                } else {
                    this.particleManager.addBlockBreakingParticles(blockPos, blockHitResult.getSide());
                }
            }

        }
    }
}
