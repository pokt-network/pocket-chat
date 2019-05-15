package com.example.myapplication.smartcontract;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.models.Message;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.aion.*;
import network.pocket.aion.exceptions.AionContractException;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;

import com.example.myapplication.utils.AppConfig;

public class SmartContract {

    PocketAion pocketAion;
    Context appContext;
    Wallet wallet;
    public AionContract aionContract;
    ArrayList<Message> messages;

    public SmartContract(Context context, Wallet wallet, PocketAion pocketAion){
        this.pocketAion = pocketAion;
        this.appContext = context;
        this.wallet = wallet;
        // Setup AionContract
        String contractAddress = AppConfig.contractAddress;
        String contractABI = AppConfig.contractABI;

        try {
            JSONArray contractABIArray = new JSONArray(contractABI);
            this.aionContract = new AionContract(pocketAion.getMastery(),contractAddress,contractABIArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //
        this.messages = new ArrayList<>();
    }

    public void sendMessage(Message message, Wallet wallet) throws JSONException {
        String userText = message.getText();

        ArrayList<Object> functionParams = new ArrayList<>();
        functionParams.add(wallet.getAddress());
        functionParams.add(userText);

        //execute contract function
        try {
            this.aionContract.executeFunction("sendMessage", wallet, functionParams, null, new BigInteger("300000"), new BigInteger("20000000000"), new BigInteger("0"), new Function2<PocketError, String, Unit>() {
                @Override
                public Unit invoke(PocketError pocketError, String result) {
                    if (pocketError != null) {
                        pocketError.printStackTrace();
                    }else{
                        Log.d("txHash", result);
                    }
                    return null;
                }
            });
        } catch (AionContractException e) {
            e.printStackTrace();
        }

    }

}
