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
import com.example.myapplication.utils.AppConfig;

public class SendToContract {

    public PocketAion pocketAion;

    Context context;

    public SendToContract(Context context){
        this.context = context;
    }

    public void sendMessage(Message message) throws JSONException {

        String contractAddress = AppConfig.contractAddress;
        String contractABI = AppConfig.contractABI;
        JSONArray contractArray = new JSONArray(contractABI);

        boolean istrue = message.belongsToCurrentUser();
        String userText = message.getText();

        Log.d("userText", userText);

        List<String> netIds = new ArrayList<>();
        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        netIds.add(PocketAion.Networks.MAINNET.getNetID());

        //Context appContext = this.getApplicationContext();

        this.pocketAion = new PocketAion(context,"DEVx0rUzoU9jykzEyjuafsr",netIds,5,50000,"32");

        ArrayList<Object> functionParams = new ArrayList<>();
        functionParams.add("0xa0500bbb5b3d4556a4f93bf536a29d1edca0e4f50d7df3749053e03660c16a91");
        functionParams.add(userText);

        AionContract aionContract = new AionContract(pocketAion.getMastery(),contractAddress,contractArray);


        //execute contract
        try {
            aionContract.executeConstantFunction("sendmessage", functionParams, "0xa0500bbb5b3d4556a4f93bf536a29d1edca0e4f50d7df3749053e03660c16a91", new BigInteger("5000"), new BigInteger("20000000000"), null, new Function2<PocketError, Object[], Unit>() {
                @Override
                public Unit invoke(PocketError pocketError, Object[] objects) {

                    Log.d("pocket error", pocketError.toString());

                    Log.d("object",objects.toString());
                    return null;
                }
            });
        } catch (AionContractException e) {
            e.printStackTrace();
        }

    }


}
