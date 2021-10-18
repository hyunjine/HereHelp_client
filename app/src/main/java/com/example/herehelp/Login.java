package com.example.herehelp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Login extends AppCompatActivity {
    private boolean isAutoLogin = false;
    private EditText et_id;
    private EditText et_passwrod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Login 컨텍스트 초기화
        Data.loginContext = Login.this;

        et_id = findViewById(R.id.et_id);
        et_passwrod = findViewById(R.id.et_password);
        // 로그인 버튼
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 시도
                requestLogin();
            }
        });
        // 회원가입 버튼
        Button btn_createAccount = findViewById(R.id.btn_createAccount);
        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, CreateAccount.class));
            }
        });
        // 자동로그인 체크박스
        CheckBox cb_autoLogin = findViewById(R.id.cb_autoLogin);
        cb_autoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 자동 로그인을 선택할 시
                if (cb_autoLogin.isChecked())
                    isAutoLogin = true;
                // 2. 자동 로그인을 해제할 시
                else
                    isAutoLogin = false;
            }
        });
    }

    /*
    로그인 성공 여부 확인
     */
    private void requestLogin() {
        try {
            JSONObject obj = new JSONObject();

            obj.put("flag", "login");
            obj.put("activity", "Login");
            obj.put("id", et_id.getText().toString());
            obj.put("password", et_passwrod.getText().toString());

            Data.sendToServer(obj);

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    로그인 성공 시
     */
    public void succeessLogin(String nickname) {
        // 아이디, 닉네임 초기화
        Data.my_id = et_id.getText().toString();
        Data.my_password = et_passwrod.getText().toString();
        Data.my_nickname = nickname;
        // 자동 로그인
        SharedPreferences pref = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        // 자동 로그인 체크
        if (isAutoLogin) {
            editor.putBoolean("status", isAutoLogin);
            editor.putString("id", Data.my_id);
            editor.putString("password", Data.my_password);
            editor.putString("nickname", Data.my_nickname);

        } else
            editor.putBoolean("status", isAutoLogin);

        editor.commit();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Login.this, Main.class));
                finish();
            }
        });
    }
    /*
    알림(팝업)
     */
    public void pop(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Popup(Login.this, msg);
            }
        });
    }
}
