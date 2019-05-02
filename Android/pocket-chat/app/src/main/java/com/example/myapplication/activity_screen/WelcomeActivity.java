package com.example.myapplication.activity_screen;

import android.arch.core.util.Function;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import network.pocket.core.model.Wallet;
import com.example.myapplication.R;
import android.content.Context;
import java.util.List;
import network.pocket.core.errors.WalletPersistenceError;
import kotlin.*;
import kotlin.jvm.functions.Function2;

public class WelcomeActivity extends AppCompatActivity {

    Wallet wallet;
    Context appContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        // Add buttons
        Button getStarted = (Button)findViewById(R.id.letsGetStarted);
        // Button actions
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cs) {
                loadMainActivity();
            }
        } );
        // Get app context
        Context appContext = this.getApplicationContext();
    }

    public void loadMainActivity(){

        List<String> walletRecordKeys = Wallet.Companion.getWalletsRecordKeys(this.appContext);
        // Check if a wallet is already stored
        if (walletRecordKeys.size() > 0) {
            String walletAddress = walletRecordKeys.get(walletRecordKeys.size() - 1);

            Wallet.Companion.retrieve("AION", "32", walletAddress, "", this.appContext, new Function2<WalletPersistenceError, Wallet, Unit>() {

            });
            Wallet.Companion.retrieve("","","","",this.appContext, new Function);
        }

        Intent csintent = new Intent(this, MessageScreenActivity.class);

        /*
        TODO verify imported wallet then allow user to move to next screen as well as store their address
        */
        startActivity(csintent);
    }

}
