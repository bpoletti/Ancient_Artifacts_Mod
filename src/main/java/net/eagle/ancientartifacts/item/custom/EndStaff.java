package net.eagle.ancientartifacts.item.custom;

import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.block.custom.DragonPedestal;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class EndStaff extends Item {

    public EndStaff(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockState state = context.getLevel().getBlockState(blockPos);

        if (player instanceof ServerPlayer serverPlayer && state.getValue(DragonPedestal.ORB_INFINIUM)) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);
        }

        return super.useOn(context);
    }

    @Override
    public @NonNull InteractionResult use(Level level, Player player, InteractionHand hand) {

        ItemStack heldItem = player.getItemInHand(hand);

        if (!heldItem.is(ModItems.END_STAFF)) {
            return InteractionResult.PASS;
        }

        player.getCooldowns().addCooldown(heldItem, 40);

        double range = 25.0;
        Vec3 playerPos = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1.0F);

        Vec3 endPos = playerPos.add(lookVec.scale(range));

        ClipContext context = new ClipContext(playerPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);

        BlockHitResult blockHitResult = level.clip(context);

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            if (level instanceof ServerLevel serverLevel) {
                double stepSize = 0.4;
                Vec3 particlePos = playerPos.add(lookVec.scale(2));
                Vec3 step = lookVec.normalize().scale(stepSize);

                while (particlePos.distanceTo(endPos) > stepSize) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL, particlePos.x, particlePos.y + 0.7, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, particlePos.x + 0.1, particlePos.y + 0.8, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, particlePos.x, particlePos.y + 0.8, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    serverLevel.sendParticles(ParticleTypes.PORTAL, particlePos.x - 0.1, particlePos.y + 0.8, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    particlePos = particlePos.add(step);
                }

                serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, hitPos.getX() + 0.5, hitPos.getY() + 0.5, hitPos.getZ() + 0.5, 1, 0, 0, 0, 0);
            }

            // Sound event names dropped the "ENTITY_" prefix in MojMap
            level.playSound(null, hitPos, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.NEUTRAL, 0.7f, 0.3f);
            level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 0.5f, 0.3f);
        }

        // Return a successful InteractionResultHolder
        return InteractionResult.SUCCESS;
    }
}