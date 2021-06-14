package com.capstone.maps;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_email, et_phone;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        //아이디값 찾아주기
        et_id = findViewById( R.id.idText );
        et_pass = findViewById( R.id.passwordText );
        et_name = findViewById( R.id.nameText );
        et_email = findViewById( R.id.emailText );
        et_phone = findViewById(R.id.phoneText);


        //회원가입 버튼 클릭 시 수행
        btn_register = findViewById( R.id.btnRegister );
        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = et_id.getText().toString();
                final String userPass = et_pass.getText().toString();
                final String userName = et_name.getText().toString();
                final String userPhone = et_phone.getText().toString();
                final String userEmail = et_email.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );


                            //회원가입 성공시
                            if(success&&!userID.equals("")&&!userPass.equals("")&&!userName.equals("")&&!userPhone.equals("")&&!userEmail.equals("")) {

                                Toast.makeText( getApplicationContext(), "성공", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( RegisterActivity.this, LoginActivity.class );
                                startActivity( intent );

                                //회원가입 실패시
                            } else {
                                Toast.makeText( getApplicationContext(), "실패", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                //서버로 Volley를 이용해서 요청
                RegisterRequest registerRequest = new RegisterRequest( userID, userPass, userName, userPhone, userEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue( RegisterActivity.this );
                queue.add( registerRequest );
            }
        });
    }
}