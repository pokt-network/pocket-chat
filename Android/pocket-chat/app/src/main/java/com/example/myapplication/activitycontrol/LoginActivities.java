package com.example.myapplication.activitycontrol;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import network.pocket.aion.*;
import network.pocket.core.model.Wallet;

public class LoginActivities {

    Context context;

    public LoginActivities(Context context) {
        this.context = context;

    }



    public Wallet newWallet(){

        List<String> netIds = new ArrayList<>();
        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        netIds.add(PocketAion.Networks.MAINNET.getNetID());

        PocketAion wallet = new PocketAion(context,"",netIds,5,50000,"32");

        return wallet.getMastery().createWallet();
    }


}
