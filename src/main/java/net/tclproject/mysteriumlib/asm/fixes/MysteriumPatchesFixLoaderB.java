package net.tclproject.mysteriumlib.asm.fixes;

import net.tclproject.mysteriumlib.asm.common.*;
import java.io.*;
import java.util.*;

public class MysteriumPatchesFixLoaderB extends CustomLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { FirstClassTransformer.class.getName() };
    }

    @Override
    public void registerFixes() {
        final ArrayList<String> lines2 = new ArrayList<String>();
        try {
            final BufferedReader br = new BufferedReader(new FileReader(new File(new File("."), "config/immersivecavegen.cfg")));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                lines2.add(line);
            }
            br.close();
        }
        catch (Exception ex) {}
        boolean oldGen = false;
        boolean mineshafts = true;
        boolean sand = true;
        boolean caverns = true;
        boolean popChanges = true;
        for (final String str : lines2) {
            if (str.contains("\"Old Cave Gen\"=true")) {
                oldGen = true;
            }
            if (str.contains("\"Enable Better Mineshafts\"=false")) {
                mineshafts = false;
            }
            if (str.contains("\"Enable Better Sand Generation\"=false")) {
                sand = false;
            }
            if (str.contains("\"Enable Caverns Replacer\"=false")) {
                caverns = false;
            }
            if (str.contains("\"Enable World Population Changes\"=false")) {
                popChanges = false;
            }
        }
        if (!oldGen) {
            if (mineshafts) {
                CustomLoadingPlugin.registerClassWithFixes("net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesB");
            }
            if (sand) {
                CustomLoadingPlugin.registerClassWithFixes("net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesSand");
            }
            if (caverns) {
                CustomLoadingPlugin.registerClassWithFixes("net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesRavine");
            }
            if (popChanges) {
                CustomLoadingPlugin.registerClassWithFixes("net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesPop");
            }
            CustomLoadingPlugin.registerClassWithFixes("net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesCave");
        }
        else if (oldGen) {
            CustomLoadingPlugin.registerClassWithFixes("net.tclproject.mysteriumlib.asm.fixes.MysteriumPatchesFixesOld");
        }
    }
}
