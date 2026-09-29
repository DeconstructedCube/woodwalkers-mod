package dev.tocraft.walkers.ability.impl.generic;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.tocraft.walkers.Walkers;
import dev.tocraft.walkers.ability.GenericShapeAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
public class TeleportationAbility<T extends LivingEntity> extends GenericShapeAbility<T> {
    public static final Identifier ID = Walkers.id("teleportation");
    public static final MapCodec<TeleportationAbility<?>> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.stable(new TeleportationAbility<>()));

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MapCodec<? extends GenericShapeAbility<?>> codec() {
        return CODEC;
    }

    @Override
    public void onUse(ServerPlayer player, T shape, ServerLevel world) {
        HitResult lookingAt = player.pick(Walkers.CONFIG.endermanAbilityTeleportDistance, 0, true);
        double originX = player.getX();
        double originY = player.getY();
        double originZ = player.getZ();
        double targetX = lookingAt.getLocation().x;
        double targetY = lookingAt.getLocation().y;
        double targetZ = lookingAt.getLocation().z;

        if (player.isPassenger()) {
            player.stopRiding();
        }

        player.teleportTo(targetX, targetY, targetZ);
        player.resetFallDistance();

        world.playSound(null, originX, originY, originZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        world.playSound(null, targetX, targetY, targetZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);

        world.broadcastEntityEvent(player, (byte) 46);

        for (int i = 0; i < 32; i++) {
            double rx = (world.random.nextDouble() - 0.5) * 2.0;
            double ry = (world.random.nextDouble() - 0.5) * 2.0;
            double rz = (world.random.nextDouble() - 0.5) * 2.0;
            world.sendParticles(ParticleTypes.PORTAL, originX + (world.random.nextDouble() - 0.5) * player.getBbWidth() * 2.0, originY + world.random.nextDouble() * player.getBbHeight(), originZ + (world.random.nextDouble() - 0.5) * player.getBbWidth() * 2.0, 1, rx, ry, rz, 0.0);
            world.sendParticles(ParticleTypes.PORTAL, targetX + (world.random.nextDouble() - 0.5) * player.getBbWidth() * 2.0, targetY + world.random.nextDouble() * player.getBbHeight(), targetZ + (world.random.nextDouble() - 0.5) * player.getBbWidth() * 2.0, 1, rx, ry, rz, 0.0);
        }

        world.gameEvent(GameEvent.TELEPORT, new Vec3(originX, originY, originZ), GameEvent.Context.of(player));
    }

    @Override
    public Item getIcon() {
        return Items.ENDER_PEARL;
    }

    @Override
    public int getDefaultCooldown() {
        return 100;
    }
}
