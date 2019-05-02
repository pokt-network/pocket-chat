package com.example.myapplication.activitycontrol;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import network.pocket.aion.*;
import network.pocket.core.model.Wallet;

public class LoginActivities extends AppCompatActivity {

    Context context;
    PocketAion pocketAion;
    List<String> netIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate PocketAion
        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        this.pocketAion = new PocketAion(context,"",netIds,5,50000,"32");

    }
    public LoginActivities(Context context) {
        this.context = context;
        this.netIds = new ArrayList<>();

    }

    public Wallet importWallet(String PrivateKey){
        Wallet importedWallet = this.pocketAion.getMastery().importWallet(PrivateKey);

        return importedWallet;
    }


    public Wallet newWallet(){
        Wallet newWallet = this.pocketAion.getMastery().createWallet();

        return newWallet;
    }


}
