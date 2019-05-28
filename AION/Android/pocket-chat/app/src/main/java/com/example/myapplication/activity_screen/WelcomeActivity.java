package com.example.myapplication.activity_screen;

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
//
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;


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
                loadInitialActivity();
            }
        } );
        // Get app context
        this.appContext = this.getApplicationContext();
    }

    public void loadInitialActivity(){
        List<String> walletRecordKeys = Wallet.Companion.getWalletsRecordKeys(this.appContext);
        // Check if a wallet is already stored
        if (walletRecordKeys.size() > 0) {
            String[] addressInfo = walletRecordKeys.get(0).split("/");
            String address = addressInfo[2];
            this.showPassphraseDialog(address, "Please enter the wallet passphrase");
        }else {
            this.loadCreateImportWalletActivity();
        }
    }

    protected void loadCreateImportWalletActivity() {
        Intent iwIntent = new Intent(this, ImportWalletActivity.class);
        startActivity(iwIntent);
    }

    protected void loadMessagesActivity() {
        Intent messageScreenIntent = new Intent(this, MessageScreenActivity.class);
        messageScreenIntent.putExtra("address", wallet.getAddress());
        messageScreenIntent.putExtra("privateKey", wallet.getPrivateKey());

        startActivity(messageScreenIntent);
    }

    protected synchronized void getWallet(String address,String passphrase) {
        Wallet.Companion.retrieve("AION", "32", address, passphrase, appContext, new Function2<WalletPersistenceError, Wallet, Unit>() {
            @Override
            public Unit invoke(WalletPersistenceError walletPersistenceError, Wallet retrievedWallet) {
                if (retrievedWallet != null) {
                    WelcomeActivity.this.wallet = retrievedWallet;
                    WelcomeActivity.this.loadMessagesActivity();
                }else{
                    WelcomeActivity.this.showPassphraseDialog(address, "Try again, wrong passphrase");
                }
                return null;
            }
        });
    }

    protected void showPassphraseDialog(String address, String message) {
        // get passphrase_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(WelcomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.passphrase_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeActivity.this);
        alertDialogBuilder.setView(promptView);
        // Dialog elements
        final TextView editText = (TextView) promptView.findViewById(R.id.passphrase_text);
        final TextView titleText = (TextView) promptView.findViewById(R.id.dialog_title);
        // Set title
        titleText.setText(message);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().trim().length() > 0){
                            WelcomeActivity.this.getWallet(address,editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
