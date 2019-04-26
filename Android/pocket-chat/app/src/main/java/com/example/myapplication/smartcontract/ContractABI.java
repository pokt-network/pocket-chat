package com.example.myapplication.smartcontract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.liquidplayer.javascript.JSON;

import network.pocket.aion.PocketAion;
import network.pocket.aion.network.*;

public class ContractABI {

    PocketAion pocketAion;

    public String contractAddress = "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9";

    //public PocketAion pocketNetwork = new PocketAion().getMastery();
    // TODO make JSON ARRAY
    public String contractABI = "[{\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint128\"}],\"name\":\"getMessageByIndex\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"type\":\"function\"}]";


        //JSONObject abiObject = new JSONObject(contractABI);

       public JSONArray contractArray = new JSONArray(contractABI);


    public ContractABI() throws JSONException {
    }
}
