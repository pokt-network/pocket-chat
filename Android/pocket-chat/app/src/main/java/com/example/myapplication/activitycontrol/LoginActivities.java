package com.example.myapplication.activitycontrol;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import network.pocket.aion.*;
import network.pocket.core.model.Wallet;

public class LoginActivities extends AppCompatActivity {


    Context context;
    List<String> netIds;

    public LoginActivities(Context context) {
        this.context = context;
        this.netIds = new ArrayList<>();

    }

    public Wallet importWallet(String PrivateKey){

        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        netIds.add(PocketAion.Networks.MAINNET.getNetID());

        PocketAion importedWallet = new PocketAion(context,"",netIds,5,50000,"32");

        // saves the imported key
        importedWallet.getMastery().importWallet(PrivateKey).isSaved(context);


        return importedWallet.getMastery().importWallet(PrivateKey);

    }


    public Wallet newWallet(){

        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        netIds.add(PocketAion.Networks.MAINNET.getNetID());

        PocketAion wallet = new PocketAion(context,"",netIds,5,50000,"32");

        // saves the generated keys
        wallet.getMastery().createWallet().isSaved(context);

        //returns the newly created wallet, as well as public and private keys
        return wallet.getMastery().createWallet();
    }


}
