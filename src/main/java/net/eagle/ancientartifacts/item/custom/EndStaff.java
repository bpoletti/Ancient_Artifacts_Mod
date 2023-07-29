package net.eagle.ancientartifacts.item.custom;

import net.eagle.ancientartifacts.item.ModItems;
import net.eagle.ancientartifacts.block.custom.DragonPedestal;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // Check if the player is holding the magic staff
        ItemStack heldItem = user.getMainHandStack();
        if (heldItem.getItem() != ModItems.END_STAFF) {
            return TypedActionResult.pass(heldItem);
        }

        ItemCooldownManager cooldownManager = user.getItemCooldownManager();
        Item staff = user.getMainHandStack().getItem(); // Replace with the item you want to set cooldown for
        cooldownManager.set(staff, 40); // 60 ticks = 3 seconds (assuming 20 ticks per second)

        // Calculate the end position of the lightning beam based on the player's look direction and range
        double range = 25.0; //25 blocks
        Vec3d playerPos = user.getPos();
        Vec3d lookVec = user.getRotationVec(1.0F);
        Vec3d endPos = playerPos.add(lookVec.multiply(range));

        // Create a new RayTraceContext to raycast from the player's position to the end position
        RaycastContext context = new RaycastContext(playerPos, endPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user);

        // Raycast to find the first block or entity in the beam's path
        BlockHitResult blockHitResult = world.raycast(context);

        // If the raycast hit a block, create a lightning bolt at the hit position
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitPos = blockHitResult.getBlockPos();

// Spawn particles along the path of the beam
            double stepSize = 0.4; // adjust this to change the density of the particle trail
            Vec3d particlePos = playerPos.add(lookVec.multiply(2)); // add an initial offset of 2 blocks in the direction the player is facing
            Vec3d step = lookVec.normalize().multiply(stepSize);
            while (particlePos.distanceTo(endPos) > stepSize) {
                world.addParticle(ParticleTypes.PORTAL, particlePos.x, particlePos.y + 0.7, particlePos.z, 0.5, 0, 0.5);
                world.addParticle(ParticleTypes.PORTAL, particlePos.x + 0.1, particlePos.y + 0.8, particlePos.z, 0.5, 0, 0.5);
                world.addParticle(ParticleTypes.PORTAL, particlePos.x, particlePos.y + 0.8, particlePos.z, 0.5, 0, 0.5);
                world.addParticle(ParticleTypes.PORTAL, particlePos.x - 0.1, particlePos.y + 0.8, particlePos.z, 0.5, 0, 0.5);
                particlePos = particlePos.add(step);
            }


            // Spawn a particle effect at the hit position to make it look like the lightning is hitting the block
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, endPos.getX() + 0.5, endPos.getY() + 0.5, endPos.getZ() + 0.5, 0, 0, 0);
            world.playSound(null, hitPos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.NEUTRAL, 0.7f, 0.3f);
            world.playSound(null,user.getBlockPos() , SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 0.5f, 0.3f);
        }

        return TypedActionResult.pass(heldItem);
    }
}

