package com.brandonjank.jankyblaster;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

/**
 * Created by Phelps on 4/17/2016.
 */
public class GameConnectionListener implements ConnectionRequestListener {

    WarpClient warp;
    public GameConnectionListener(WarpClient warp) {
        this.warp = warp;
    }

    @Override
    public void onConnectDone(ConnectEvent connectEvent) {
        if(connectEvent.getResult() == WarpResponseResultCode.SUCCESS){
            System.out.println("yipee I have connected");
            //warp.initUDP();
            warp.joinLobby();
            warp.subscribeLobby();
        }
    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onInitUDPDone(byte b) {

    }

}
