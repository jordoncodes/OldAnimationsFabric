package me.onlyjordon.oldanimationsfabric.client.mixins;

import me.onlyjordon.oldanimationsfabric.client.OldAnimationsFabricClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public class SwordItemMixin extends ToolItem {

    public SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return OldAnimationsFabricClient.CONFIG.swordBlocking() ? UseAction.BLOCK : UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return OldAnimationsFabricClient.CONFIG.swordBlocking() ? 72000 : 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (OldAnimationsFabricClient.CONFIG.swordBlocking() && hand == Hand.MAIN_HAND) {
            ItemStack itemStack = user.getStackInHand(hand);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }
}