package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.api.item.SpellPrismLens;
import com.hollingsworth.arsnouveau.common.block.SpellPrismBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

public class AdvancedPrism extends SpellPrismBlock implements EntityBlock {

    public AdvancedPrism(Properties blockProps, String registry) {
        super(blockProps.noOcclusion(), registry);
    }

    static final SpellPrismLens defaultLent = spell -> true;

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.level.isClientSide) {
            super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        if (pPlayer.getItemInHand(pHand).getItem() instanceof SpellPrismLens && pLevel.getBlockEntity(pPos) instanceof AdvancedPrismTile tile) {
            tile.setLent(pPlayer.getItemInHand(pHand).split(1), pPlayer);
            return InteractionResult.SUCCESS;
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public static void redirectSpell(ServerLevel world, BlockPos pos, EntityProjectileSpell spell) {
        if (!(world.getBlockEntity(pos) instanceof AdvancedPrismTile tile)) return;
        Position iposition = getDispensePosition(new BlockSourceImpl(world, pos), tile);
        spell.setPos(iposition.x(), iposition.y(), iposition.z());
        if (spell.spellResolver == null) {
            spell.remove(Entity.RemovalReason.DISCARDED);
            return;
        }
        Vec3 vec3d = tile.getShootAngle().normalize();
        if (tile.getLens().getItem() instanceof SpellPrismLens lens && lens.canConvert(spell)) {
            lens.shoot(world, pos, spell, vec3d);
        } else {
            defaultLent.shoot(world, pos, spell, vec3d);
        }
        for(Direction d : Direction.values()){
            BlockPos adjacentPos = pos.relative(d);
            if(world.getBlockState(adjacentPos).getBlock() instanceof ObserverBlock){
                BlockState observer = world.getBlockState(adjacentPos);
                if(adjacentPos.relative(observer.getValue(FACING)).equals(pos)) { // Make sure the observer is facing us.
                    world.scheduleTick(pos.relative(d), world.getBlockState(pos.relative(d)).getBlock(), 2);
                }
            }
        }
    }

    public static Position getDispensePosition(BlockSource coords, AdvancedPrismTile tile) {
        Vec3 direction = tile.getShootAngle().normalize();
        double d0 = coords.x() + 0.5D * direction.x();
        double d1 = coords.y() + 0.5D * direction.y();
        double d2 = coords.z() + 0.5D * direction.z();
        return new PositionImpl(d0, d1, d2);
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction orientation = placer == null ? Direction.WEST : Direction.orderedByNearest(placer)[0].getOpposite();

        if (!(world.getBlockEntity(pos) instanceof AdvancedPrismTile prismTile)) return;
        switch (orientation) {
            case DOWN:
                prismTile.rotationY = -90F;
                break;
            case UP:
                prismTile.rotationY = 90F;
                break;
            case NORTH:
                prismTile.rotationX = 270F;
                break;
            case SOUTH:
                prismTile.rotationX = 90F;
                break;
            case WEST:
                break;
            case EAST:
                prismTile.rotationX = 180F;
                break;
        }
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rot) {
        if (level.getBlockEntity(pos) instanceof AdvancedPrismTile prismTile) {
            prismTile.setRotationX(prismTile.getRotationX() + switch (rot) {
                        case NONE -> 0;
                        case CLOCKWISE_90 -> 90;
                        case CLOCKWISE_180 -> 180;
                        case COUNTERCLOCKWISE_90 -> -90;
                    }
            );
            prismTile.updateBlock();
        }
        return super.rotate(state, level, pos, rot);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return super.rotate(state, rot);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AdvancedPrismTile(pPos, pState);
    }
}
