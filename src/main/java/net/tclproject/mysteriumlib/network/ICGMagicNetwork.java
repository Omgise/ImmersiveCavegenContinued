package net.tclproject.mysteriumlib.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.common.network.*;

public final class ICGMagicNetwork
{
    public static final SimpleNetworkWrapper dispatcher;

    public static final void registerPackets() {
        ICGMagicNetwork.dispatcher.registerMessage((Class)BlockWebDestroy.Handler.class, (Class)BlockWebDestroy.class, 0, Side.SERVER);
    }

    static {
        dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("immersivecavegen");
    }
}
