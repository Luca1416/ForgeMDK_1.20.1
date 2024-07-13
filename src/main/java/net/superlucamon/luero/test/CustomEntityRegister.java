package net.superlucamon.luero.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.entity.types.nonhostile.SentryEntity;

public abstract class CustomEntityRegister{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MOD_ID);

   public static final RegistryObject<EntityType> CUSTOM_SMALL_FIREBALL = ENTITY_TYPES.register("custom_small_fireball",
            () -> EntityType.Builder.<CustomFlyingRocket>of(CustomFlyingRocket::new, MobCategory.MISC)
                    .sized(0.45F, 0.12F)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(128)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("custom_small_fireball"));
    public static final RegistryObject<EntityType> CUSTOM_LINE = ENTITY_TYPES.register("custom_line",
            () -> EntityType.Builder.<BeamEntity>of(BeamEntity::new, MobCategory.MISC)
            .sized(0.02F, 0.02F)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(128)
                    .clientTrackingRange(4)
            .build("custom_line"));
    public static final RegistryObject<EntityType> SENTRY_ENTITY = ENTITY_TYPES.register("sentry_entity",
            () -> EntityType.Builder.<SentryEntity>of(SentryEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("sentry_entity"));
    public static final RegistryObject<EntityType> TARGET_MARKER = ENTITY_TYPES.register("custom_target_marker",
            () -> EntityType.Builder.<TargetMarkerEntity>of(TargetMarkerEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("custom_target_marker"));

 /*public final EntityType<CustomSmallFireball> CUSTOM_SMALL_FIREBALL =
         register("dragon_fireball", () -> EntityType.Builder.of(EntityType.DRAGON_FIREBALL::new, MobCategory.MISC)
                 .sized(1.0F, 1.0F)
                 .clientTrackingRange(4)
                 .updateInterval(10)
                 .build("dragon_fireball"));





  */
/*
    public CustomEntityRegister(EntityFactory pFactory, MobCategory pCategory, boolean pSerialize, boolean pSummon, boolean pFireImmune, boolean pCanSpawnFarFromPlayer, ImmutableSet pImmuneTo, EntityDimensions pDimensions, int pClientTrackingRange, int pUpdateInterval, FeatureFlagSet pRequiredFeatures) {
        super(pFactory, pCategory, pSerialize, pSummon, pFireImmune, pCanSpawnFarFromPlayer, pImmuneTo, pDimensions, pClientTrackingRange, pUpdateInterval, pRequiredFeatures);
    }

 */
    /*
    public static<T extends Entity> EntityType<T> register(String pKey, EntityType.Builder<T> pBuilder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, pKey, pBuilder.build(pKey));
    }

     */
/*
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }


 */
}
