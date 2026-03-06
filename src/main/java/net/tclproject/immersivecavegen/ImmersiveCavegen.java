package net.tclproject.immersivecavegen;

import java.util.*;
import org.apache.logging.log4j.*;
import net.tclproject.immersivecavegen.blocks.*;
import net.tclproject.immersivecavegen.items.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import net.tclproject.immersivecavegen.misc.*;
import net.minecraft.block.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.block.material.*;
import net.tclproject.immersivecavegen.world.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.tclproject.mysteriumlib.network.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;
import cpw.mods.fml.common.gameevent.*;
import net.minecraft.world.*;
import net.minecraft.potion.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.event.terraingen.*;
import cpw.mods.fml.common.network.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.common.*;
import net.minecraft.entity.*;
import net.tclproject.immersivecavegen.entities.*;
import net.minecraft.world.biome.*;
import net.minecraft.client.model.*;

@Mod(modid = "immersivecavegen", version = "1.2g-hotfix5", name = "Immersive Cavegen")
public class ImmersiveCavegen
{
    public static final String MODID = "immersivecavegen";
    public static final String VERSION = "1.2g-hotfix5";
    public static Random rand;
    public static int giganticCaveChance;
    public Logger logger;
    @Mod.Instance("immersivecavegen")
    public static ImmersiveCavegen instance;
    ResourceLocation rc;

    public ImmersiveCavegen() {
        this.rc = new ResourceLocation("immersivecavegen:textures/custom.png");
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.logger = event.getModLog();
        WGConfig.init(event.getModConfigurationDirectory().toString(), event);
        if (!WGConfig.serveronly) {
            BlockInit.init();
            ItemInit.init();
            if (WGConfig.enableEntities) {
                EntityInit.init();
                if (event.getSide().isClient()) {
                    EntityInit.clientInit();
                }
            }
        }
        ImmersiveCavegen.instance = this;
        MinecraftForge.EVENT_BUS.register((Object)this);
        FMLCommonHandler.instance().bus().register((Object)this);
        MinecraftForge.TERRAIN_GEN_BUS.register((Object)this);
        MinecraftForge.ORE_GEN_BUS.register((Object)this);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        if (!WGConfig.serveronly) {
            CraftInit.init();
            ICGMagicNetwork.registerPackets();
        }
        else {
            WGConfig.realisticCobwebs = false;
        }
        final String[] arr$;
        final String[] list = arr$ = "netherrack,stone,grass,dirt,cobblestone,gravel,gold_ore,iron_ore,coal_ore,lapis_ore,sandstone,diamond_ore,redstone_ore,lit_redstone_ore,ice,snow,clay,monster_egg,emerald_ore".split(",");
        for (int len = list.length, i$ = 0; i$ < len; ++i$) {
            final String txt = arr$[i$];
            try {
                final Block block = (Block)GameData.getBlockRegistry().func_82594_a(txt.trim());
                if (block != null && block.func_149688_o() != Material.field_151579_a) {
                    GenerateStoneStalactite.blockWhiteList.add(block);
                }
            }
            catch (Exception ex) {}
        }
        ImmersiveCavegen.giganticCaveChance = WGConfig.giantCaveChance;
        final CavesDecorator decor = new CavesDecorator();
        if (!WGConfig.serveronly && WGConfig.caveDecorations) {
            MinecraftForge.EVENT_BUS.register((Object)decor);
        }
    }

    @SubscribeEvent
    public void onInteract(final PlayerInteractEvent e) {
        if (!WGConfig.serveronly && WGConfig.realisticCobwebs && e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.entityPlayer.func_70694_bm() != null && e.world.func_147439_a(e.x, e.y, e.z) == Blocks.field_150321_G && (e.entityPlayer.func_70694_bm().func_77973_b() == Items.field_151033_d || e.entityPlayer.func_70694_bm().func_77973_b() == Item.func_150898_a(Blocks.field_150478_aa))) {
            e.world.func_147465_d(e.x, e.y, e.z, Blocks.field_150350_a, 0, 3);
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                this.sendWebDestroyPacket(e.x, e.y, e.z);
            }
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.1, 0.1, 0.1);
            e.world.func_72869_a("flame", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.4f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("flame", (double)(e.x + 0.4f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.4f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("flame", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("flame", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("flame", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("flame", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("smoke", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.world.func_72869_a("flame", (double)(e.x + 0.5f), (double)(e.y + 0.5f), (double)(e.z + 0.5f), 0.0, 0.0, 0.0);
            e.entityPlayer.func_71038_i();
            e.setCanceled(true);
        }
    }

    @SideOnly(Side.CLIENT)
    private void sendWebDestroyPacket(final int x, final int y, final int z) {
        ICGMagicNetwork.dispatcher.sendToServer((IMessage)new BlockWebDestroy(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderEvent(final RenderPlayerEvent.Specials.Pre event) {
        try {
            final EntityPlayer player = event.entityPlayer;
            if ("Nlghtwing".equalsIgnoreCase(player.getDisplayName())) {
                this.render(player);
            }
        }
        catch (Throwable var4) {
            var4.printStackTrace();
        }
    }

    private void render(final EntityPlayer player) {
        if (!player.func_82150_aj()) {
            Minecraft.func_71410_x().func_110434_K().func_110577_a(this.rc);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0f, 0.0f, 0.125f);
            final double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * 0.0625 - (player.field_70169_q + (player.field_70165_t - player.field_70169_q) * 0.0625);
            final double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * 0.0625 - (player.field_70167_r + (player.field_70163_u - player.field_70167_r) * 0.0625);
            final double d5 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * 0.0625 - (player.field_70166_s + (player.field_70161_v - player.field_70166_s) * 0.0625);
            final float f4 = player.field_70760_ar + (player.field_70761_aq - player.field_70760_ar) * 0.0625f;
            final double d6 = MathHelper.func_76126_a(f4 * 3.1415927f / 180.0f);
            final double d7 = -MathHelper.func_76134_b(f4 * 3.1415927f / 180.0f);
            float f5 = (float)d4 * 10.0f;
            if (f5 < -6.0f) {
                f5 = -6.0f;
            }
            if (f5 > 32.0f) {
                f5 = 32.0f;
            }
            float f6 = (float)(d3 * d6 + d5 * d7) * 100.0f;
            final float f7 = (float)(d3 * d7 - d5 * d6) * 100.0f;
            if (f6 < 0.0f) {
                f6 = 0.0f;
            }
            final float f8 = player.field_71107_bF + (player.field_71109_bG - player.field_71107_bF) * 0.0625f;
            f5 += MathHelper.func_76126_a((player.field_70141_P + (player.field_70140_Q - player.field_70141_P) * 0.0625f) * 6.0f) * 32.0f * f8;
            if (player.func_70093_af()) {
                f5 += 25.0f;
            }
            GL11.glRotatef(6.0f + f6 / 2.0f + f5, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(f7 / 2.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(-f7 / 2.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.0f, 0.0f, 0.125f);
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            this.getCapeModel().cape.func_78785_a(0.0625f);
            GL11.glPopMatrix();
        }
    }

    @SideOnly(Side.CLIENT)
    private CapeModel getCapeModel() {
        return new CapeModel();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void playerBlind(final TickEvent.PlayerTickEvent event) {
        if (event.player.func_82114_b().field_71572_b < 35 && WGConfig.jumpscare && ImmersiveCavegen.rand.nextFloat() > 0.99993f && !event.player.field_70170_p.field_72995_K && event.player.field_70170_p.field_73011_w.field_76577_b != WorldType.field_77138_c && !event.player.func_70644_a(Potion.field_76440_q)) {
            event.player.func_70690_d(new PotionEffect(Potion.field_76440_q.field_76415_H, 100, 0, false));
            event.player.func_85030_a("random.fizz", 0.75f, 1.0f);
        }
    }

    @SubscribeEvent
    public void onOreGen(final OreGenEvent.GenerateMinable event) {
        if (event.type == OreGenEvent.GenerateMinable.EventType.DIRT && WGConfig.turnOffVanillaUndergroundDirt) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load event) {
        WGConfig.loadConfiguration();
    }

    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload event) {
        WGConfig.loadConfiguration();
    }

    @SubscribeEvent
    public void onMapInit(final InitMapGenEvent event) {
        WGConfig.loadConfiguration();
    }

    @SubscribeEvent
    public void onNoiseGenInit(final InitNoiseGensEvent event) {
        WGConfig.loadConfiguration();
    }

    @SubscribeEvent
    public void onFMLNetworkJoin(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        WGConfig.loadConfiguration();
    }

    @SubscribeEvent
    public void onFMLNetworkLeave(final FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        WGConfig.loadConfiguration();
    }

    @SubscribeEvent
    public void entitySpawnEvent(final LivingSpawnEvent.CheckSpawn event) {
        if (!WGConfig.serveronly && event.world.field_73011_w.field_76577_b != WorldType.field_77138_c && event.y < 34.0f && WGConfig.enableEntities && event.getResult() != Event.Result.DENY && !(event.entity instanceof ICaveEntity) && ImmersiveCavegen.rand.nextInt(WGConfig.mobSpawnChance) == 0 && event.world.func_72957_l((int)event.x, (int)event.y, (int)event.z) <= ImmersiveCavegen.rand.nextInt(8)) {
            final BiomeGenBase biome = event.world.func_72807_a((int)event.x, (int)event.z);
            if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MUSHROOM) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.DENSE)) {
                final EntityGlowSlime entityToSpawn = new EntityGlowSlime(event.world);
                entityToSpawn.func_70012_b((double)event.x, (double)event.y, (double)event.z, ImmersiveCavegen.rand.nextFloat() * 360.0f, 0.0f);
                event.world.func_72838_d((Entity)entityToSpawn);
            }
            else if (!BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER) && !BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.END)) {
                final float randomValue = ImmersiveCavegen.rand.nextFloat();
                if (randomValue < 0.5) {
                    final EntityBrownSpiderSmall entityToSpawn2 = new EntityBrownSpiderSmall(event.world);
                    entityToSpawn2.func_70012_b((double)event.x, (double)event.y, (double)event.z, ImmersiveCavegen.rand.nextFloat() * 360.0f, 0.0f);
                    event.world.func_72838_d((Entity)entityToSpawn2);
                }
                else if (randomValue < 0.83) {
                    final EntityBrownSpider entityToSpawn3 = new EntityBrownSpider(event.world);
                    entityToSpawn3.func_70012_b((double)event.x, (double)event.y, (double)event.z, ImmersiveCavegen.rand.nextFloat() * 360.0f, 0.0f);
                    event.world.func_72838_d((Entity)entityToSpawn3);
                }
                else {
                    final EntityBrownSpiderLarge entityToSpawn4 = new EntityBrownSpiderLarge(event.world);
                    entityToSpawn4.func_70012_b((double)event.x, (double)event.y, (double)event.z, ImmersiveCavegen.rand.nextFloat() * 360.0f, 0.0f);
                    event.world.func_72838_d((Entity)entityToSpawn4);
                }
            }
        }
    }

    static {
        ImmersiveCavegen.rand = new Random();
    }

    @SideOnly(Side.CLIENT)
    public class CapeModel extends ModelBiped
    {
        public ModelRenderer cape;

        public CapeModel() {
            this.field_78090_t = 64;
            this.field_78089_u = 32;
            (this.cape = new ModelRenderer((ModelBase)this, 0, 0)).func_78789_a(-5.0f, 0.0f, -1.0f, 10, 16, 1);
            this.cape.func_78793_a(0.0f, 0.0f, 2.0f);
            this.cape.func_78787_b(64, 32);
            this.setRotation(this.cape, 0.0f, 0.0f, 0.0f);
        }

        public void func_78088_a(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
            super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
            this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
            this.cape.func_78785_a(f5);
        }

        private void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
            model.field_78795_f = x;
            model.field_78796_g = y;
            model.field_78808_h = z;
        }

        public void func_78087_a(final float f, final float f1, final float f2, final float f3, final float f4, final float f5, final Entity entity) {
            super.func_78087_a(f, f1, f2, f3, f4, f5, entity);
        }
    }
}
