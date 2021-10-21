package com.example.herehelp.init;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.herehelp.Data;
import com.example.herehelp.main.Main;
import com.example.herehelp.R;
import com.example.herehelp.main.ReceiveDataThread;

import org.json.JSONObject;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        // 컨텍스트 초기화
        Data.introContext = Intro.this;
        // 데이터 수신 클래스 생섣
        new ReceiveDataThread().start();
        // 자동 로그인 활성화 여부 확인
        SharedPreferences pref = getSharedPreferences("autoLogin", MODE_PRIVATE);
        boolean status = pref.getBoolean("status", false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 자동 로그인 활성화 시
                if (status) {
                    // Data 클래스에 아이디, 비밀번호, 닉네임 저장
                    Data.my_id = pref.getString("id", "null");
                    Data.my_password = pref.getString("password", "null");
                    Data.my_nickname = pref.getString("nickname", "null");
                    // 로그인 요청
                    requestLogin();
                }
                // 자동 로그인 비활성화 시
                else {
                    startActivity(new Intent(Intro.this, Login.class));
                    finish();
                }
            }
        }, 1000);
    }
    /*
    로그인 시도
     */
    private void requestLogin() {
        try {
            JSONObject obj = new JSONObject();

            obj.put("flag", "login");
            obj.put("activity", "Intro");
            obj.put("id", Data.my_id);
            obj.put("password", Data.my_password);

            Data.sendToServer(obj);

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    액티비티 전환
     */
    public void convertIntroToMain() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Intro.this, Main.class));
                finish();
            }
        });
    }
}

