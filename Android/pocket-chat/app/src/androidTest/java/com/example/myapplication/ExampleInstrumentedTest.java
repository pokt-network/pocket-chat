package com.example.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.myapplication.models.Message;
import com.example.myapplication.smartcontract.ContractABI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.aion.AionContract;
import network.pocket.aion.PocketAion;
import network.pocket.aion.exceptions.AionContractException;
import network.pocket.core.errors.PocketError;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private PocketAion pocketAion;
    private ContractABI messageContract;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.myapplication", appContext.getPackageName());

        Log.d("hey",appContext.toString());
    }

    @Test
    public void sendMessage() throws JSONException {

         String contractAddress = "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9";
         String contractABI = "[{\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint128\"}],\"name\":\"getMessageByIndex\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"type\":\"function\"}]";


        //JSONObject abiObject = new JSONObject(contractABI);

         JSONArray contractArray = new JSONArray(contractABI);

       // boolean istrue = message.isBelongsToCurrentUser();
        //String userText = message.getText();

        List<String> netIds = new ArrayList<>();
        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        netIds.add(PocketAion.Networks.MAINNET.getNetID());

        Context appContext = InstrumentationRegistry.getContext();

        this.pocketAion = new PocketAion(appContext,"DEVx0rUzoU9jykzEyjuafsr",netIds,5,50000,"32");

        ArrayList<Object> functionParams = new ArrayList<>();
        functionParams.add("0xa0500bbb5b3d4556a4f93bf536a29d1edca0e4f50d7df3749053e03660c16a91");
        functionParams.add("hey");

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


