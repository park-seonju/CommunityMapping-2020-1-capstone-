package com.capstone.maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView welcomMsg, idText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView idText = (TextView)findViewById(R.id.idText);
        TextView welcomMsg = (TextView)findViewById(R.id.welcomeMsg);
        Button mapButton = (Button)findViewById(R.id.mapButton);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gogomap = new Intent(MainActivity.this, MapActivity.class);
                startActivity(gogomap);
            }
        });
        idText.setText("아이디: "+userID);
        welcomMsg.setText("안녕하세요 "+userName+"님");
    }
}