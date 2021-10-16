/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
 
package meteordevelopment.meteorclient.systems.modules.combat;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class SuperKnockback extends Module  {
	private final SettingGroup sgGeneral = settings.getDefaultGroup();
	
	private final Setting<Integer> hurtTimeValue = sgGeneral.add(new IntSetting.Builder()
        .name("hurt-time")
        .defaultValue(10)
        .min(0)
        .max(10)
        .build()
    );
	
	public SuperKnockback() {
		super(Categories.Combat, "super-knockback", "Increases knockback dealt to other entities.");
	}
	
	@EventHandler
	private void onAttack(AttackEntityEvent event) {
		if (event.entity instanceof LivingEntity) {
			if (((LivingEntity) event.entity).hurtTime > hurtTimeValue.get())
				return;
			
			Entity player = mc.player;
			
			if (mc.player.isSprinting())
				mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(player, Mode.STOP_SPRINTING));
			
			mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(player, Mode.START_SPRINTING));
			mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(player, Mode.STOP_SPRINTING));
			mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(player, Mode.START_SPRINTING));
			mc.player.setSprinting(true);
		}
	}
}