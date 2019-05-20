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
import network.pocket.eth.*;
import network.pocket.eth.exceptions.EthContractException;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;

import com.example.myapplication.utils.AppConfig;

public class SmartContract {

    PocketEth pocketEth;
    Context appContext;
    Wallet wallet;
    public EthContract ethContract;
    ArrayList<Message> messages;

    public SmartContract(Context context, Wallet wallet, PocketEth pocketEth){
        this.pocketEth = pocketEth;
        this.appContext = context;
        this.wallet = wallet;
        // Setup AionContract
        String contractAddress = AppConfig.contractAddress;
        String contractABI = AppConfig.contractABI;

        try {
            JSONArray contractABIArray = new JSONArray(contractABI);
            this.ethContract = new EthContract(pocketEth.getRinkeby(),contractAddress,contractABIArray);
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
            this.ethContract.executeFunction("sendMessage", wallet, functionParams, null, new BigInteger("200000"), new BigInteger("10000000000"), new BigInteger("0"), new Function2<PocketError, String, Unit>() {
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
        } catch (EthContractException e) {
            e.printStackTrace();
        }

    }

}
