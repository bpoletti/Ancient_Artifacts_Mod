package net.eagle.ancientartifacts.item.custom;

import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.block.custom.DragonPedestal;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld; // 1. Add this import!
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult; // 2. TypedActionResult is gone, just use ActionResult
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class EndStaff extends Item {
    public EndStaff(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockState state = context.getWorld().getBlockState(blockPos);
        if (playerEntity instanceof ServerPlayerEntity && state.get(DragonPedestal.ORB_INFINIUM)) {
            Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
        }

        return super.useOnBlock(context);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        ItemStack heldItem = user.getMainHandStack();
        if (heldItem.getItem() != ModItems.END_STAFF) {
            return ActionResult.PASS;
        }

        ItemCooldownManager cooldownManager = user.getItemCooldownManager();
        cooldownManager.set(heldItem, 40);

        // Calculate the end position of the lightning beam based on the player's look direction and range
        double range = 25.0; // 25 blocks
        Vec3d playerPos = user.getPos();
        Vec3d lookVec = user.getRotationVec(1.0F);
        Vec3d endPos = playerPos.add(lookVec.multiply(range));

        // Create a new RayTraceContext
        RaycastContext context = new RaycastContext(playerPos, endPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user);
        BlockHitResult blockHitResult = world.raycast(context);

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

            if (world instanceof ServerWorld serverWorld) {
                double stepSize = 0.4;
                Vec3d particlePos = playerPos.add(lookVec.multiply(2));
                Vec3d step = lookVec.normalize().multiply(stepSize);

                while (particlePos.distanceTo(endPos) > stepSize) {
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, particlePos.x, particlePos.y + 0.7, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, particlePos.x + 0.1, particlePos.y + 0.8, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, particlePos.x, particlePos.y + 0.8, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, particlePos.x - 0.1, particlePos.y + 0.8, particlePos.z, 0, 0.5, 0, 0.5, 1.0);
                    particlePos = particlePos.add(step);
                }

                // Spawn the final hit particle
                serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, endPos.getX() + 0.5, endPos.getY() + 0.5, endPos.getZ() + 0.5, 1, 0, 0, 0, 0);
            }

            world.playSound(null, hitPos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.NEUTRAL, 0.7f, 0.3f);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 0.5f, 0.3f);
        }

        return ActionResult.SUCCESS;
    }
}