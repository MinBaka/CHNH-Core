package com.minbaka.chnhcore.precisemanufacturing.foundation;

import com.minbaka.chnhcore.precisemanufacturing.PreciseManufacturing;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.processing.casting_basin.CastingBasinBlockEntity;
import com.minbaka.chnhcore.precisemanufacturing.foundation.neo.complex.content.processing.casting_basin.CastingBasinRenderer;
import com.minbaka.chnhcore.precisemanufacturing.lib.Reference;
import net.createmod.catnip.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public class PrmaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);

//    public static final RegistryObject<BlockEntityType<DecomponentalizerBlockEntity>> DECOMPONENTALIZER =
//            BLOCK_ENTITIES.register("decomponentalizer_block_entity", () ->
//                    BlockEntityType.Builder.of(DecomponentalizerBlockEntity::new,
//                            ModBlocks.DECOMPONENTALIZER.get()).build(null));


//    public static final BlockEntityEntry<? extends BlockEntity> DECOMPONENTALIZER =
//            Main.REGISTRATE.blockEntity("decomponentalizer", DecomponentalizerBlockEntity::new).register();

    public static final BlockEntityEntry<CastingBasinBlockEntity> CASTING_BASIN = PreciseManufacturing.REGISTRATE
            .blockEntity("casting_basin", CastingBasinBlockEntity::new)
            .validBlocks()
            .renderer(() -> CastingBasinRenderer::new)
            .register();

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
