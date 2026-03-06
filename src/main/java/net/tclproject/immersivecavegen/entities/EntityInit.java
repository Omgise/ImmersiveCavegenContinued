package net.tclproject.immersivecavegen.entities;

import cpw.mods.fml.common.registry.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.world.biome.*;
import java.util.*;
import cpw.mods.fml.client.registry.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.model.*;
import cpw.mods.fml.relauncher.*;

public class EntityInit
{
    public static void init() {
        final int spiderId = (WGConfig.entityBrSpID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entityBrSpID;
        EntityRegistry.registerGlobalEntityID((Class)EntityBrownSpider.class, "BrownSpider", spiderId);
        EntityRegistry.registerModEntity((Class)EntityBrownSpider.class, "BrownSpider", spiderId, (Object)ImmersiveCavegen.instance, 64, 1, true);
        final int spiderId2 = (WGConfig.entitySmBrSpID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entitySmBrSpID;
        EntityRegistry.registerGlobalEntityID((Class)EntityBrownSpiderSmall.class, "BrownSpiderSmall", spiderId2);
        EntityRegistry.registerModEntity((Class)EntityBrownSpiderSmall.class, "BrownSpiderSmall", spiderId2, (Object)ImmersiveCavegen.instance, 64, 1, true);
        final int spiderId3 = (WGConfig.entityLBrSpID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entityLBrSpID;
        EntityRegistry.registerGlobalEntityID((Class)EntityBrownSpiderLarge.class, "BrownSpiderLarge", spiderId3);
        EntityRegistry.registerModEntity((Class)EntityBrownSpiderLarge.class, "BrownSpiderLarge", spiderId3, (Object)ImmersiveCavegen.instance, 64, 1, true);
        final int glowSlimeID = (WGConfig.entityGlSlID == -1) ? EntityRegistry.findGlobalUniqueEntityId() : WGConfig.entityGlSlID;
        EntityRegistry.registerGlobalEntityID((Class)EntityGlowSlime.class, "GlowSlime", glowSlimeID);
        EntityRegistry.registerModEntity((Class)EntityGlowSlime.class, "GlowSlime", glowSlimeID, (Object)ImmersiveCavegen.instance, 64, 1, true);
    }

    public static BiomeGenBase[] getNotNullBiomeGenArray() {
        final BiomeGenBase[] arrayWithNullValues = BiomeGenBase.func_150565_n();
        final List<BiomeGenBase> list = new ArrayList<BiomeGenBase>();
        for (final BiomeGenBase s : arrayWithNullValues) {
            if (s != null) {
                list.add(s);
            }
        }
        final BiomeGenBase[] returnedArray = list.toArray(new BiomeGenBase[list.size()]);
        return returnedArray;
    }

    @SideOnly(Side.CLIENT)
    public static void clientInit() {
        RenderingRegistry.registerEntityRenderingHandler((Class)EntityBrownSpider.class, (Render)new RenderBrownSpider());
        RenderingRegistry.registerEntityRenderingHandler((Class)EntityBrownSpiderSmall.class, (Render)new RenderBrownSpiderSmall());
        RenderingRegistry.registerEntityRenderingHandler((Class)EntityBrownSpiderLarge.class, (Render)new RenderBrownSpiderLarge());
        RenderingRegistry.registerEntityRenderingHandler((Class)EntityGlowSlime.class, (Render)new RenderGlowSlime((ModelBase)new ModelSlime(16), (ModelBase)new ModelSlime(0), 0.25f));
    }
}
