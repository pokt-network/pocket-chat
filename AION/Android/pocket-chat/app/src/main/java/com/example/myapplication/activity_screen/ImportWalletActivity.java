package com.example.myapplication.activity_screen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import network.pocket.aion.PocketAion;
import network.pocket.core.errors.WalletPersistenceError;
import network.pocket.core.model.Wallet;

public class ImportWalletActivity extends AppCompatActivity {
    PocketAion pocketAion;
    Wallet wallet;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_wallet);

        Button import_W = (Button)findViewById(R.id.import_wallet_btn);
        Button create_W = (Button)findViewById(R.id.create_wallet_btn);

        import_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View importView) {
                importWallet();
            }
        } );

        create_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View cw) {
                showCreateWallet();
            }
        });

        this.appContext = ImportWalletActivity.this;
        // Instantiate PocketAion
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketAion.Networks.MASTERY.getNetID());
        this.pocketAion = new PocketAion(this.appContext,"", netIds,5,50000,"32");
    }
    protected void importWallet() {
        TextView private_key_text = (TextView)findViewById(R.id.private_key_text);
        String privateKey = private_key_text.getText().toString();

        if (privateKey.trim().length() > 0) {
            this.wallet = this.pocketAion.getMastery().importWallet(privateKey);

            if (wallet != null) {
                showPassphraseDialog(this.wallet);
            }else{
                showDialog("Failed to import the wallet, please try again.");
            }
        }else{
            showDialog("The private key field is empty, please add the private key.");
        }

    }
    protected void loadMessagesActivity() {
        Intent messageScreenIntent = new Intent(this, MessageScreenActivity.class);
        messageScreenIntent.putExtra("address", wallet.getAddress());
        messageScreenIntent.putExtra("privateKey", wallet.getPrivateKey());

        startActivity(messageScreenIntent);
    }
    protected void showPassphraseDialog(Wallet wallet) {
        // get passphrase_dialog.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ImportWalletActivity.this);
        View promptView = layoutInflater.inflate(R.layout.passphrase_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImportWalletActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView editText = (TextView) promptView.findViewById(R.id.private_key_text);
        final TextView titleText = (TextView) promptView.findViewById(R.id.dialog_title);
        // setup a dialog window
        titleText.setText("Enter a new passphrase.");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().trim().length() > 0){
                            String passphrase = editText.getText().toString();

                            wallet.save(passphrase, ImportWalletActivity.this, new Function1<WalletPersistenceError, Unit>() {
                                @Override
                                public Unit invoke(WalletPersistenceError walletPersistenceError) {
                                    ImportWalletActivity.this.loadMessagesActivity();
                                    return null;
                                }
                            });
                        }else{
                            ImportWalletActivity.this.showPassphraseDialog(wallet);
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
    public void showCreateWallet() {
        Intent create_wallet_intent = new Intent(this, CreateWalletActivity.class);
        startActivity(create_wallet_intent);
    }

}
