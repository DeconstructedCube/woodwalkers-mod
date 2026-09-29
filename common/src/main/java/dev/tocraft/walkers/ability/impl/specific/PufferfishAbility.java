package dev.tocraft.walkers.ability.impl.specific;

import dev.tocraft.walkers.Walkers;
import dev.tocraft.walkers.ability.ShapeAbility;
import dev.tocraft.walkers.mixin.accessor.PufferfishAccessor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PufferfishAbility<T extends Pufferfish> extends ShapeAbility<T> {
    public static final Identifier ID = Walkers.id("pufferfish");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void onUse(ServerPlayer player, T shape, ServerLevel world) {
        if (!world.isClientSide()) {
            if (shape.getPuffState() == 0) {
                ((PufferfishAccessor) shape).setInflateCounter(1);
                ((PufferfishAccessor) shape).setDeflateTimer(0);
                shape.setPuffState(1);
                player.refreshDimensions();
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                ((PufferfishAccessor) shape).setInflateCounter(0);
                ((PufferfishAccessor) shape).setDeflateTimer(61);
            }
        }
    }

    @Override
    public Item getIcon() {
        return Items.PUFFERFISH;
    }
}
