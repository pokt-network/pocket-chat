package com.example.myapplication.activitityscreen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activitycontrol.LoginActivities;
import com.example.myapplication.activitycontrol.LoginActivities;

import java.util.ArrayList;
import java.util.List;

import network.pocket.aion.*;

public class CreateWalletActivity extends AppCompatActivity {

    PocketAion pocketAion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        List<String> netIds = new ArrayList<>();
        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        netIds.add(PocketAion.Networks.MAINNET.getNetID());

        Button import_W = (Button)findViewById(R.id.ImportScreen);
        Button chat_Screen = (Button)findViewById(R.id.CreateWallet);

        //print public key

        Context appContext = this.getApplicationContext();

        LoginActivities loginActivities = new LoginActivities(appContext);

        String showpublickey = loginActivities.newWallet().getAddress();
        String showprivatekey = loginActivities.newWallet().getPrivateKey();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView publicKey = findViewById(R.id.publicKey);
                TextView privateKey = findViewById(R.id.privateKey);
                publicKey.setText(showpublickey);
                privateKey.setText(showprivatekey);

            }
        });

        import_W.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View iw) {
                openIW();
            }
        });

        chat_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cs) {
                openCS();
            }

        } );

    }

    public void openIW() {

        Intent iwintent = new Intent(this, Main2Activity.class);

        startActivity(iwintent);

    }

    public void openCS() {

        Intent csintent = new Intent(this, MessageScreenActivity.class);

        //TODO save the newly created wallet private key to use for the app then send to chat-screen
        startActivity(csintent);

    }
}
