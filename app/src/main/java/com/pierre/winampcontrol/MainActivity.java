package com.pierre.winampcontrol;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    {
        try{
            socket = IO.socket("http://192.168.0.40:3000/");
        }catch(URISyntaxException e){
            //@TODO: if no connection we should block the rest of the app
            Log.i("channel", "noconnection");
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        socket.connect();
        Button pause=(Button)findViewById(R.id.button_pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("PAUSE");
            }
        });
        Button play=(Button)findViewById(R.id.button_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("PLAY");
            }
        });
        Button previous=(Button)findViewById(R.id.button_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("PREV");
            }
        });
        Button next=(Button)findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("NEXT");
            }
        });
        Button volUp=(Button)findViewById(R.id.button_volUp);
        volUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("VOLUP");
            }
        });
        Button volDown=(Button)findViewById(R.id.button_volDown);
        volDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAction("VOLDOWN");
            }
        });


    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        socket.disconnect();
    }
    public void sendAction(String action){
        JSONObject send = new JSONObject();
        try {
            send.put("command", action);
            //send.put("msg", message.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json", send.toString());
        socket.emit("command", send);
    }
}
