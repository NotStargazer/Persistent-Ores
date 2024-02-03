package net.staraiz.persistent_ores.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.staraiz.persistent_ores.block.entity.ArdentDrillBlockEntity;
import net.staraiz.persistent_ores.block.entity.PersistentOresBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ArdentDrillBlock extends BaseEntityBlock
{
    public ArdentDrillBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ArdentDrillBlockEntity)
            {
                ((ArdentDrillBlockEntity) blockEntity).drop();
            }
        }

        super.onRemove(state, level, pos, state, moving);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving)
    {
        Block bellowBlock = level.getBlockState(pos.below()).getBlock();

        if (bellowBlock instanceof PersistentOreBlock)
        {
            PersistentOreBlock persistentOre = (PersistentOreBlock) bellowBlock;
            ArdentDrillBlockEntity drillBlock = (ArdentDrillBlockEntity) level.getBlockEntity(pos);
            drillBlock.setYield(persistentOre.getYield(), persistentOre.DensityLevel);
        }

        super.onPlace(state, level, pos, newState, moving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (!level.isClientSide())
        {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof ArdentDrillBlockEntity)
            {
                NetworkHooks.openScreen((ServerPlayer) player, (ArdentDrillBlockEntity) entity, pos);
            }
            else
            {
                throw new IllegalStateException("Persistent Ores Ardent Drill Container Provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new ArdentDrillBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return createTickerHelper(blockEntityType, PersistentOresBlockEntities.ARDENT_DRILL.get(), ArdentDrillBlockEntity::tick);
    }
}
