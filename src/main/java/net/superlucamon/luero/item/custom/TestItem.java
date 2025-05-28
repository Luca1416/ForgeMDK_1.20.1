package net.superlucamon.luero.item.custom;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.superlucamon.luero.entity.projectile.MicromissileEntity;
import net.superlucamon.luero.test.CustomFlyingRocket;
import org.joml.Random;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult;
import static net.superlucamon.luero.test.CustomEntityRegister.CUSTOM_SMALL_FIREBALL;


@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "heromod")
public class TestItem extends Item {

    private final ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(25);

    private static boolean test = false;
    //private static Player mPlayer;
    //private static Level mLevel;
    //private static LivingEntity mEntity;
    private static final ResourceLocation SPRITE_TEXTURE = new ResourceLocation("heromod", "textures/item/strawberry.png");
  //  private static EntityRendererProvider.Context rendererContext;
    public static Set<LivingEntity> map = new HashSet<>();
    public static final ConcurrentHashMap<LivingEntity, Integer> renderDataMap = new ConcurrentHashMap<>();
    public static int pRocketCountToGet;
    public static Player mPlayer;
//    long windowHandle = Minecraft.getInstance().getWindow().getWindow();
    public static final KeyMapping SHIFT_IF = new KeyMapping("key.heromod.shift", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories.heromod");
    public CustomFlyingRocket rocket;

    public TestItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);

        mPlayer = pPlayer;

        /*if (rendererContext == null) {
            renderDataMap.clear();
            rendererContext = new EntityRendererProvider.Context(
                    Minecraft.getInstance().getEntityRenderDispatcher(),
                    Minecraft.getInstance().getItemRenderer(),
                    Minecraft.getInstance().getBlockRenderer(),
                    Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(),
                    Minecraft.getInstance().getResourceManager(),
                    Minecraft.getInstance().getEntityModels(),
                    Minecraft.getInstance().font);
        }

         */

        if (!pLevel.isClientSide()) {
            renderDataMap.forEach((livingEntity, rocketsToGet) -> {
                final int lSpawnDelay = 250;
                if (livingEntity.isAlive()) {
                    for (int i = 0; i < rocketsToGet; i++) {
                        SpawnRocketTask lTask = new SpawnRocketTask(pPlayer, livingEntity, pLevel, (ServerLevel) pLevel);
                        mScheduler.schedule(lTask, i * lSpawnDelay, TimeUnit.MILLISECONDS);
                        System.out.println("test");
                    }
                }
            });
            renderDataMap.clear();
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }


    public static void addRenderData(LivingEntity pLivingEntity, int pInt) {
        renderDataMap.put(pLivingEntity, pInt);
    }

    public static void removeRenderData(LivingEntity livingEntity) {
        renderDataMap.remove(livingEntity);
    }

    @SubscribeEvent
    public static void renderTargetedEntitySprite(RenderLivingEvent.Post<LivingEntity, ?> event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        LivingEntity lEntity = checkIfLookingAtEntity(player, player.level());
        if (lEntity != null && !renderDataMap.containsKey(lEntity)) {
            addRenderData(lEntity, 1);
        }

        /*if (player != null && rendererContext != null && !Minecraft.getInstance().isPaused()) {
            renderDataMap.forEach((livingEntity, rocketsToGet) -> {
                if (livingEntity.isAlive() && event.getEntity() == livingEntity) {
                    TargetedEntitySpriteRenderer renderer = TargetedEntitySpriteRenderer.getInstance(rendererContext);
                    renderer.renderSpriteAboveEntity(livingEntity, player.getYRot(), event.getPartialTick(), event.getPoseStack(), minecraft.renderBuffers().bufferSource(), minecraft.getEntityRenderDispatcher().getPackedLightCoords(livingEntity, event.getPartialTick()));
                } else if (!livingEntity.isAlive()) {
                    removeRenderData(livingEntity);
                }
            });
        }

         */
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        double scrollDelta = event.getScrollDelta();
        if (SHIFT_IF.isDown() && checkIfLookingAtEntity(Minecraft.getInstance().player, Minecraft.getInstance().player.level()) != null) {
            event.setCanceled(true);
            LivingEntity entity = checkIfLookingAtEntity(Minecraft.getInstance().player, Minecraft.getInstance().player.level());
            pRocketCountToGet = renderDataMap.get(checkIfLookingAtEntity(Minecraft.getInstance().player, Minecraft.getInstance().player.level()));
          //  System.out.println(entity);
           // System.out.println(renderDataMap);

            if (renderDataMap.containsKey(entity)){

                if (scrollDelta > 0) {
                    pRocketCountToGet += 1;
                    renderDataMap.remove(entity, pRocketCountToGet);
                    renderDataMap.put(entity, pRocketCountToGet);
                    System.out.println(renderDataMap.get(entity));
                    System.out.println(pRocketCountToGet);
                }

                if (scrollDelta < 0 && pRocketCountToGet > 0) {
                    pRocketCountToGet -= 1;
                    renderDataMap.remove(entity, pRocketCountToGet);
                    renderDataMap.put(entity, pRocketCountToGet);
                    System.out.println(renderDataMap.get(entity));
                    System.out.println(pRocketCountToGet);
                }
            }
        }
    }


    /*
    private static void renderQuad(PoseStack matrixStack, MultiBufferSource buffer, int light) {
        Matrix4f matrix = matrixStack.last().pose();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(SPRITE_TEXTURE));

        float size = 0.5F;

        vertexConsumer.vertex(matrix, -size, -size, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        vertexConsumer.vertex(matrix, size, -size, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        vertexConsumer.vertex(matrix, size, size, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
        vertexConsumer.vertex(matrix, -size, size, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
    }
     */

        /*
            private static void renderSpriteAboveEntity(Entity entity, PoseStack matrixStack, MultiBufferSource buffer, int light) {
                matrixStack.pushPose();

                // Translate to the entity's position
                matrixStack.translate(0.0D, entity.getBbHeight() + 0.5D, 0.0D);

                // Rotate to face the player
                EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
                matrixStack.mulPose(renderManager.cameraOrientation());
                matrixStack.scale(-1.0F, -1.0F, 1.0F);

                // Render the sprite
                renderQuad(matrixStack, buffer, light);

                matrixStack.popPose();
            }

            @SubscribeEvent
            public static void onRenderLiving(RenderLivingEvent.Post<LivingEntity, ?> event) {
                if (mEntity != null) {
                    LivingEntity entity = mEntity;
                    PoseStack matrixStack = event.getPoseStack();
                    MultiBufferSource buffer = event.getMultiBufferSource();
                    int light = event.getPackedLight();

                    renderSpriteAboveEntity(entity, matrixStack, buffer, light);
                }
            }
         */
/*
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<LivingEntity, ?> event) {
        System.out.println(mBla++ +  ": onRenderLivingEvent" + event);
        if (mEntity != null) {
            LivingEntity entity = mEntity;
            PoseStack matrixStack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            int light = event.getPackedLight();

            matrixStack.pushPose();

            // Translate to the entity's position
            matrixStack.translate(0.0D, entity.getBbHeight() + 0.5D, 0.0D);

            // Rotate to face the player
            EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
            matrixStack.mulPose(renderManager.cameraOrientation());
            matrixStack.scale(-1.0F, -1.0F, 1.0F);

            // Render the sprite
            Matrix4f matrix = matrixStack.last().pose();
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(SPRITE_TEXTURE));

            float size = 0.5F;

            vertexConsumer.vertex(matrix, -size, -size, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
            vertexConsumer.vertex(matrix, size, -size, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
            vertexConsumer.vertex(matrix, size, size, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();
            vertexConsumer.vertex(matrix, -size, size, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1, 0, 0).endVertex();

            matrixStack.popPose();
        }
    }
 */
        private class SpawnRocketTask implements Runnable {

            private final Player mPlayer;
            private final LivingEntity mLivingEntity;
            private final Level mLevel;
            private final ServerLevel mServerLevel;

            public SpawnRocketTask(Player pPlayer, LivingEntity pLivingEntity, Level pLevel, ServerLevel pServerLevel) {
                this.mPlayer = pPlayer;
                this.mLivingEntity = pLivingEntity;
                this.mLevel = pLevel;
                this.mServerLevel = pServerLevel;
            }
            public static double getRandomInRange(double min, double max) {
                Random random = new Random();
                return min + (max - min) * random.nextFloat();
            }
            @Override
            public void run() {
                    mServerLevel.getServer().execute(() -> {
                    //if (world instanceof ServerLevel) {
                    double x = getRandomInRange(-5, 5);
                    double y = getRandomInRange(3, 7);
                    double z = getRandomInRange(-5, 5);
                    MicromissileEntity lRocket = new MicromissileEntity(CUSTOM_SMALL_FIREBALL.get(), mServerLevel);
                    lRocket.setTargetEntity(mLivingEntity);
                    lRocket.setPos(mPlayer.position().add(x,y,z));

                    /*lRocket.setPos(mPlayer.getPosition(1).x(), mPlayer.getEyeY(), mPlayer.getPosition(1).z());
                    mLevel.addFreshEntity(lRocket);


                     */
                    //}
                    // Initial position



                    mLevel.addFreshEntity(lRocket);
                    mServerLevel.playSound(null, mPlayer.getX(), mPlayer.getY(), mPlayer.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.AMBIENT, 0.8f,(float) getRandomInRange(0.8F, 1.2F));
                });
            }
        }
    private static boolean isBlockBetween(Level level, Vec3 startVec, Entity targetEntity) {
        Vec3 endVec = targetEntity.position();
        ClipContext context = new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
        BlockHitResult blockHitResult = level.clip(context);

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos hitBlockPos = blockHitResult.getBlockPos();
            Vec3 hitVec = blockHitResult.getLocation();


            double distanceToBlock = startVec.distanceTo(hitVec);
            double distanceToEntity = startVec.distanceTo(endVec);
            return distanceToBlock < distanceToEntity;
        }

        return false;
    }

        public static LivingEntity checkIfLookingAtEntity(Player pPlayer, Level pLevel) {
            Vec3 startVec = pPlayer.getEyePosition(1.0F);
            Vec3 lookVec = pPlayer.getLookAngle();
            Vec3 endVec = startVec.add(lookVec.scale(80.0));
            if (pLevel != null && pPlayer != null) {
                Predicate<Entity> entityFilter = (entity) -> entity != pPlayer;

                AABB aabb = pPlayer.getBoundingBox().expandTowards(lookVec.scale(80.0)).inflate(1.0);
                EntityHitResult entityHitResult = getEntityHitResult(pLevel, pPlayer, startVec, endVec, aabb, entityFilter);

                if (entityHitResult != null && entityHitResult.getEntity() != null && entityHitResult.getEntity() instanceof LivingEntity && !isBlockBetween(pLevel, startVec, entityHitResult.getEntity())) {
                    //mEntity = (LivingEntity) entityHitResult.getEntity();
                    Entity lookedAtEntity = entityHitResult.getEntity();
                   // pPlayer.displayClientMessage(Component.literal("Looking at: " + lookedAtEntity.getName().getString() + (int) pPlayer.distanceTo(lookedAtEntity)), true);
                    return (LivingEntity) entityHitResult.getEntity();
                }
            }

            return null;
        }


    @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END && test) {
                System.out.println("yo");
            }
        }
    }


