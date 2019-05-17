package com.example.myapplication.activity_screen;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.smartcontract.SmartContract;
import com.example.myapplication.activitycontrol.MessageState;
import com.example.myapplication.R;
import com.example.myapplication.models.Message;

import org.json.JSONException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.eth.EthContract;
import network.pocket.eth.*;
import network.pocket.eth.exceptions.EthContractException;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;

public class MessageScreenActivity extends AppCompatActivity {

    private EditText editText;
    private MessageState messageState;
    private ListView messagesView;
    private SmartContract smartContract;
    public PocketEth pocketEth;

    EthContract ethContract;
    Wallet wallet;
    Context appContext;
    TreeMap<Integer, Message> messages = new TreeMap<Integer, Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set app context
        this.appContext = MessageScreenActivity.this;

        // chat_screen
        setContentView(R.layout.chat_screen);
        editText = (EditText) findViewById(R.id.editText);

        // Message setup
        messageState = new MessageState(this.appContext);
        messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageState);

        // Instantiate PocketAion
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this.appContext, "", netIds, 5, 50000, "4");

        // Set up the wallet
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String address = extras.getString("address");
            String privateKey = extras.getString("privateKey");

            this.wallet = new Wallet(privateKey, address, this.pocketEth.getRinkeby().getNet().toString(), this.pocketEth.getRinkeby().getNetID().toString());
        }

        // SmartContract setup
        this.smartContract = new SmartContract(this.appContext, this.wallet, this.pocketEth);
        this.ethContract = smartContract.ethContract;

        // Retrieve messages
        retrieveMessages();
    }

    // Sends a message
    public void sendText(View send) throws JSONException {
        // Create the message object
        final String myMessage = editText.getText().toString();
        final Message message = new Message(this.wallet.getAddress(), myMessage, true);

        // Clear text field
        editText.getText().clear();

        // Send the message to the smart contract
        try {
            smartContract.sendMessage(message, this.wallet);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update chat view with a message
        this.addMessage(messageState.messages.size() - 1, message);
    }
    // Add a single message to the messages array
    protected  void addMessage(Integer index, Message msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageState.add(index, msg);
                Log.d("Message", msg.getText());
                messagesView.setSelection(messagesView.getCount() - 1);
            }
        });
    }
    // Updates the UI view with the message
    protected void updateChatView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Integer count = messages.size();
                try {
                    messages.descendingKeySet();
                    for (TreeMap.Entry<Integer,Message> msg : messages.entrySet()) {
                        messageState.add(msg.getKey(), msg.getValue());
                        Log.d("Message", msg.getValue().getText());
                    }
                    messagesView.setSelection(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Retrieves a list of messages from the smart contract
    public void retrieveMessages() {
        // Retrieve messages count
        try {
            this.ethContract.executeConstantFunction("getTotalMessageCount", null, null, new BigInteger("50000"), new BigInteger("20000000000"), null, new Function2<PocketError, Object[], Unit>() {
                @Override
                public Unit invoke(PocketError pocketError, Object[] result) {
                    if (pocketError != null) {
                        pocketError.printStackTrace();
                        return null;
                    }
                    // Count
                    Integer count = new Integer(result[0].toString());
                    Log.d("messages count", count.toString());

                    // Retrieve individual message
                    while (count > 0) {
                        count = count - 1;
                        MessageScreenActivity.this.getMessageWith(count);
                    }
                    MessageScreenActivity.this.updateChatView();
                    return null;
                }
            });
        } catch (EthContractException e) {
            e.printStackTrace();
        }
    }

    protected void getMessageWith(Integer index) {
        // Add getMessageByIndex params
        ArrayList<Object> functionParams = new ArrayList<>();
        functionParams.add(new BigInteger(index.toString()));
        try {
            MessageScreenActivity.this.ethContract.executeConstantFunction("getMessageByIndex", functionParams, null, new BigInteger("50000"), new BigInteger("20000000000"), null, new Function2<PocketError, Object[], Unit>() {
                @Override
                public Unit invoke(PocketError pocketError, Object[] result) {
                    if (pocketError != null) {
                        pocketError.printStackTrace();
                        return null;
                    }

                    Message msg = new Message(result[0].toString(), result[1].toString(), false);
                    if (msg.getSender().equalsIgnoreCase(MessageScreenActivity.this.wallet.getAddress())) {
                        msg.setBelongsToCurrentUser(true);
                    }
                    Log.d("Message", msg.toString());
                    MessageScreenActivity.this.messages.put(index, msg);
                    return null;
                }
            });
        } catch (EthContractException e) {
            e.printStackTrace();
        }
    }
}


