package com.example.myapplication.activity_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_login);

        Button import_W = (Button)findViewById(R.id.import_Button);
        Button create_W = (Button)findViewById(R.id.createWalletButton);

            import_W.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View cs) {
                    openCS();
                }

            } );

            create_W.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View cw) {
                    openCW();
                }
            });

    }

    public void openCS(){
        Intent csintent = new Intent(this, MessageScreenActivity.class);

        /*
        TODO verify imported wallet then allow user to move to next screen as well as store their address
        */
        startActivity(csintent);
    }

    public void openCW() {
        Intent cwintent = new Intent(this, CreateWalletActivity.class);

        startActivity(cwintent);

    }


}
