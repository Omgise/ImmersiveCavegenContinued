package net.tclproject.mysteriumlib.asm.common;

import cpw.mods.fml.common.*;
import net.minecraft.launchwrapper.*;
import net.tclproject.mysteriumlib.asm.annotations.*;

public class BuiltinFixes
{
    @Fix
    public static void injectData(final Loader loader, final Object... data) {
        final ClassLoader classLoader = BuiltinFixes.class.getClassLoader();
        if (classLoader instanceof LaunchClassLoader) {
            ((LaunchClassLoader)classLoader).registerTransformer(CustomClassTransformer.class.getName());
        }
        else {
            System.out.println("MysteriumASM Lib was not loaded by LaunchClassLoader. Fixes for minecraft code will not have any effect.");
        }
    }
}
