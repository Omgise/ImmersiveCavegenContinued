package net.tclproject.mysteriumlib.asm.common;

import cpw.mods.fml.common.asm.transformers.*;
import net.tclproject.mysteriumlib.asm.core.*;
import cpw.mods.fml.relauncher.*;
import org.apache.logging.log4j.*;
import cpw.mods.fml.common.*;
import java.lang.reflect.*;
import java.util.*;

@IFMLLoadingPlugin.TransformerExclusions({ "net.tclproject" })
public class CustomLoadingPlugin implements IFMLLoadingPlugin
{
    private static DeobfuscationTransformer deobfuscationTransformer;
    private static boolean checkedObfuscation;
    private static boolean obfuscated;
    private static MetaReader mcMetaReader;

    public static TargetClassTransformer getTransformer() {
        return FirstClassTransformer.instance.registeredBuiltinFixes ? CustomClassTransformer.instance : FirstClassTransformer.instance;
    }

    public static void registerFix(final ASMFix fix) {
        getTransformer().registerFix(fix);
    }

    public static void registerClassWithFixes(final String className) {
        getTransformer().registerClassWithFixes(className);
    }

    public static MetaReader getMetaReader() {
        return CustomLoadingPlugin.mcMetaReader;
    }

    static DeobfuscationTransformer getDeobfuscationTransformer() {
        if (isObfuscated() && CustomLoadingPlugin.deobfuscationTransformer == null) {
            CustomLoadingPlugin.deobfuscationTransformer = new DeobfuscationTransformer();
        }
        return CustomLoadingPlugin.deobfuscationTransformer;
    }

    public static boolean isObfuscated() {
        if (!CustomLoadingPlugin.checkedObfuscation) {
            try {
                final Field deobfuscatedField = CoreModManager.class.getDeclaredField("deobfuscatedEnvironment");
                deobfuscatedField.setAccessible(true);
                CustomLoadingPlugin.obfuscated = !deobfuscatedField.getBoolean(null);
            }
            catch (Exception e) {
                FMLLog.log("Mysterium Patches", Level.ERROR, "Error occured when checking obfuscation.", new Object[0]);
                FMLLog.log("Mysterium Patches", Level.ERROR, "THIS IS MOST LIKELY HAPPENING BECAUSE OF MOD CONFLICTS. PLEASE CONTACT ME TO LET ME KNOW.", new Object[0]);
                FMLLog.log("Mysterium Patches", Level.ERROR, e.getMessage(), new Object[0]);
            }
            CustomLoadingPlugin.checkedObfuscation = true;
        }
        return CustomLoadingPlugin.obfuscated;
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public String[] getASMTransformerClass() {
        return null;
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(final Map<String, Object> data) {
        this.registerFixes();
    }

    public void registerFixes() {
    }

    static {
        CustomLoadingPlugin.mcMetaReader = new MinecraftMetaReader();
    }
}
