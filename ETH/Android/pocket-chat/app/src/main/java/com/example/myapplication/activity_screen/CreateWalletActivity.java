package com.example.myapplication.activity_screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import network.pocket.eth.*;
import network.pocket.core.errors.WalletPersistenceError;
import network.pocket.core.model.Wallet;

public class CreateWalletActivity extends Activity {

    PocketEth pocketEth;
    Context appContext;
    Wallet wallet;
    TextView publicView;
    TextView privateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_wallet);

        // Create wallet and continue button
        Button create_W = (Button)findViewById(R.id.generate_wallet_btn);
        Button continue_Btn = (Button)findViewById(R.id.continue_btn);

        // display addresses:
        publicView = (TextView)findViewById(R.id.public_key_text);
        privateView = (TextView)findViewById(R.id.private_key_text);

        // Creates a wallet
        create_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View iw) {
                createWallet();
            }
        });
        // The continue button action shows the message activity
        continue_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View cs) {
                loadMessagesActivity();
            }
        } );
        // Set app context
        this.appContext = CreateWalletActivity.this;
        // Instantiate PocketEth
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this.appContext,"", netIds,5,50000,"4");
    }
    protected void createWallet() {
        wallet = this.pocketEth.getRinkeby().createWallet();

        publicView.setText(wallet.getAddress());
        privateView.setText(wallet.getPrivateKey());

        showPassphraseDialog(wallet);
    }

    protected void loadMessagesActivity() {
        if (this.wallet != null ){
            Intent messageScreenIntent = new Intent(this, MessageScreenActivity.class);
            messageScreenIntent.putExtra("address", wallet.getAddress());
            messageScreenIntent.putExtra("privateKey", wallet.getPrivateKey());

            startActivity(messageScreenIntent);
        }else {
            showDialog("No wallet has been created, please create a new wallet to continue.");
        }
    }

    protected void showPassphraseDialog(Wallet wallet) {
        // App context
        Context appContext = this.appContext;
        // get passphrase_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CreateWalletActivity.this);
        View promptView = layoutInflater.inflate(R.layout.passphrase_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateWalletActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView editText = (TextView) promptView.findViewById(R.id.private_key_text);
        final TextView titleText = (TextView) promptView.findViewById(R.id.dialog_title);
        // setup a dialog window
        titleText.setText("Enter a new passphrase.");
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().trim().length() > 0){
                            String passphrase = editText.getText().toString();

                            wallet.save(passphrase, appContext, new Function1<WalletPersistenceError, Unit>() {
                                @Override
                                public Unit invoke(WalletPersistenceError walletPersistenceError) {
                                    CreateWalletActivity.this.loadMessagesActivity();
                                    return null;
                                }
                            });
                        }else{
                            CreateWalletActivity.this.showPassphraseDialog(wallet);
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
    public void showDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.appContext);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
