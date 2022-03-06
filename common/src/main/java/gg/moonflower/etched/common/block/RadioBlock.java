package gg.moonflower.etched.common.block;

import gg.moonflower.etched.common.blockentity.RadioBlockEntity;
import gg.moonflower.etched.common.menu.RadioMenu;
import gg.moonflower.etched.common.network.EtchedMessages;
import gg.moonflower.etched.common.network.play.ClientboundSetUrlPacket;
import gg.moonflower.etched.core.Etched;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RadioBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape X_SHAPE = Block.box(5.0D, 0.0D, 2.0D, 11.0D, 8.0D, 14.0D);
    private static final VoxelShape Z_SHAPE = Block.box(2.0D, 0.0D, 5.0D, 14.0D, 8.0D, 11.0D);
    private static final Component CONTAINER_TITLE = new TranslatableComponent("container." + Etched.MOD_ID + ".radio");

    public RadioBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;
        player.openMenu(blockState.getMenuProvider(level, pos)).ifPresent(__ -> {
            String url = "";
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RadioBlockEntity)
                url = ((RadioBlockEntity) be).getUrl();
            EtchedMessages.PLAY.sendTo((ServerPlayer) player, new ClientboundSetUrlPacket(url));
        });
        return InteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide()) {
            boolean bl2 = blockState.getValue(POWERED);
            if (bl2 != level.hasNeighborSignal(pos)) {
                level.setBlock(pos, blockState.cycle(POWERED), 2);
                level.sendBlockUpdated(pos, blockState, level.getBlockState(pos), 3);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RadioBlockEntity) {
                if (((RadioBlockEntity) blockEntity).isPlaying())
                    level.levelEvent(1010, pos, 0);
                Clearable.tryClear(blockEntity);
            }

            super.onRemove(state, level, pos, newState, moving);
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        return new SimpleMenuProvider((menuId, playerInventory, player) -> new RadioMenu(menuId, playerInventory, ContainerLevelAccess.create(level, blockPos), blockEntity instanceof RadioBlockEntity ? ((RadioBlockEntity) blockEntity)::setUrl : url -> {
        }), CONTAINER_TITLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        switch (state.getValue(FACING).getAxis()) {
            case X:
                return X_SHAPE;
            case Z:
                return Z_SHAPE;
        }
        return Shapes.block(); // Not possible
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter level) {
        return new RadioBlockEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
}