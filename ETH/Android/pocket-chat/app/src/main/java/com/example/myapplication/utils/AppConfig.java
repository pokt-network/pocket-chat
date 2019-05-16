package com.example.myapplication.utils;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import network.pocket.aion.PocketAion;
import network.pocket.core.model.Wallet;

public class AppConfig extends AppCompatActivity {
    public  static String contractAddress = "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9";
    public  static String contractABI = "[{\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint128\"}],\"name\":\"getMessageByIndex\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"type\":\"function\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
