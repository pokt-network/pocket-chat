package com.example.myapplication.activity_screen;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.smartcontract.SendToContract;
import com.example.myapplication.activitycontrol.MessageState;
import com.example.myapplication.R;
import com.example.myapplication.models.Message;

import org.json.JSONException;

import network.pocket.aion.PocketAion;

public class MessageScreenActivity extends AppCompatActivity {

    private EditText editText;
    private MessageState messageState;
    private ListView messagesView;
    private SendToContract sendToContract;

    public PocketAion pocketAion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
        editText = (EditText) findViewById(R.id.editText);
        messageState = new MessageState(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageState);
        //new SendToContract();

    }

        public void sendText (View send) throws JSONException {

            Context appContext = this.getApplicationContext();

            SendToContract sendToContract = new SendToContract(appContext);

            final String myMessage = editText.getText().toString();

            //TODO add "belongs to user by matching stored wallet address to load on the right"

            boolean belongsToMe = true;

            final Message message = new Message(myMessage, belongsToMe);

            // updates the UI view with the message
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    messageState.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                    editText.getText().clear();

                    try {
                        sendToContract.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("test", myMessage);
                }
            });



            //TODO add the pocket Android Code


        }


/*
        private void sendMessage(Message message) throws JSONException {

            //PocketAion pocketAion = new PocketAion();

            String contractAddress = "0xA0dC0a5E880F2ea7fb74eA9fB5319fe9ee98968F0B06bCAC535e7EF0152e8aC9";
            String contractABI = "[{\"outputs\":[{\"name\":\"\",\"type\":\"address\"},{\"name\":\"\",\"type\":\"string\"}],\"constant\":true,\"payable\":false,\"inputs\":[{\"name\":\"_index\",\"type\":\"uint128\"}],\"name\":\"getMessageByIndex\",\"type\":\"function\"},{\"outputs\":[{\"name\":\"\",\"type\":\"uint128\"}],\"constant\":true,\"payable\":false,\"inputs\":[],\"name\":\"getTotalMessageCount\",\"type\":\"function\"},{\"outputs\":[],\"constant\":false,\"payable\":false,\"inputs\":[{\"name\":\"_sender\",\"type\":\"address\"},{\"name\":\"_content\",\"type\":\"string\"}],\"name\":\"sendMessage\",\"type\":\"function\"}]";


            //JSONObject abiObject = new JSONObject(contractABI);

            JSONArray contractArray = new JSONArray(contractABI);


            //ContractABI messageContract = new ContractABI();

            boolean istrue = message.belongsToCurrentUser();
            String userText = message.getText();

            Log.d("userText", userText);

            List<String> netIds = new ArrayList<>();
            netIds.add(PocketAion.Networks.MASTERY.getNetID());
            netIds.add(PocketAion.Networks.MAINNET.getNetID());

            Context appContext = this.getApplicationContext();

            this.pocketAion = new PocketAion(appContext, "DEVx0rUzoU9jykzEyjuafsr", netIds, 5, 50000, "32");

            ArrayList<Object> functionParams = new ArrayList<>();
            functionParams.add("0xa0500bbb5b3d4556a4f93bf536a29d1edca0e4f50d7df3749053e03660c16a91");
            functionParams.add(userText);

            AionContract aionContract = new AionContract(pocketAion.getMastery(), contractAddress, contractArray);


            //execute contract
            try {
                aionContract.executeConstantFunction("sendmessage", functionParams, "0xa0500bbb5b3d4556a4f93bf536a29d1edca0e4f50d7df3749053e03660c16a91", new BigInteger("5000"), new BigInteger("20000000000"), null, new Function2<PocketError, Object[], Unit>() {
                    @Override
                    public Unit invoke(PocketError pocketError, Object[] objects) {

                        Log.d("pocket error", pocketError.toString());

                        Log.d("object", objects.toString());
                        return null;
                    }
                });
            } catch (AionContractException e) {
                e.printStackTrace();
            }


        }
*/


}


