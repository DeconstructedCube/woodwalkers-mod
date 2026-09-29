package dev.tocraft.walkers.ability.impl.generic;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.tocraft.walkers.Walkers;
import dev.tocraft.walkers.ability.GenericShapeAbility;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class ShootDragonFireball<T extends LivingEntity> extends GenericShapeAbility<T> {
    public static final Identifier ID = Walkers.id("shoot_dragon_fireball");
    public static final MapCodec<ShootDragonFireball<?>> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.stable(new ShootDragonFireball<>()));

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
        Vec3 look = player.getLookAngle();
        DragonFireball dragonFireball = new DragonFireball(
                world,
                player,
                look.normalize()
        );
        dragonFireball.setPos(player.getX(), player.getEyeY(), player.getZ());
        dragonFireball.setOwner(player);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 1.0F, 1.0F);
        world.addFreshEntity(dragonFireball);
    }

    @Override
    public Item getIcon() {
        return Items.DRAGON_BREATH;
    }
}
