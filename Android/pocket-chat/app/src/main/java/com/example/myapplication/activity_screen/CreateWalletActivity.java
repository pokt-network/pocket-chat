package com.example.myapplication.activity_screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activitycontrol.LoginActivities;

import network.pocket.aion.*;

public class CreateWalletActivity extends Activity {

    PocketAion pocketAion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        // create the import and create wallet button
        Button import_W = (Button)findViewById(R.id.ImportScreen);
        Button create_W = (Button)findViewById(R.id.CreateWallet);

        Context appContext = this.getApplicationContext();

        LoginActivities loginActivities = new LoginActivities(appContext);

        /* calls the loginActivity class and returns the address of the
           wallet as well as the private key
        */

        String showpublickey = loginActivities.newWallet().getAddress();
        String showprivatekey = loginActivities.newWallet().getPrivateKey();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Automatically displayes public and private key on launch
                TextView publicKey = findViewById(R.id.publicKey);
                TextView privateKey = findViewById(R.id.privateKey);
                publicKey.setText(showpublickey);
                privateKey.setText(showprivatekey);

            }
        });

        // in case someone has a key to import, allow them to go back

        import_W.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View iw) {
                openImportWalletActivity();
            }
        });

        // after keys are generated, let the user go to the chat screen
        create_W.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View cs)

            {
                openChatActivity();
            }

        } );

    }

    public void openImportWalletActivity() {

        Intent importwalletIntent = new Intent(this, Main2Activity.class);

        startActivity(importwalletIntent);

    }

    public void openChatActivity() {

        Intent chatActivityIntent = new Intent(this, MessageScreenActivity.class);
//        chatActivityIntent.putExtra("Wallet", )
        //TODO save the newly created wallet private key to use for the app then send to chat-screen
        startActivity(chatActivityIntent);

    }
}
