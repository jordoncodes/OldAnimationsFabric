package me.onlyjordon.oldanimationsfabric.client;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = "oldanimationsfabric")
@Config(name = "old-animations", wrapperName = "OldAnimations")
public class  Options {
    @SectionHeader("safe")
    public boolean oldBreakingAnim = true;
    public boolean swordBlocking = true;
    public boolean oldFishingRod = true;
    public boolean oldBow = true;
    public boolean oldEatingDrinkingAnim = true;
    public boolean shieldSwordBlocking = true;
    public boolean thirdPersonSwordBlocking = true;

    public boolean affectNewItems = true;

    @SectionHeader("unsafe")
    public boolean swordBlockingSlowdown = false;
    public boolean fullOldBreaking = false;
    public boolean disableAttackCooldown = false;


}
