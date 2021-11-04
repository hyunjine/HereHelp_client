package com.example.herehelp;

import android.content.Context;
import android.util.Log;

import com.example.herehelp.activity_record.Info_Item;
import com.example.herehelp.chatting.Chat_Item;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    // 소켓
    public static Socket socket;
    // IP
    public static String SERVER_IP = "";
    // PORT
    public static int SERVER_PORT;
    // 서버 데이터 전송
    public static void sendToServer(JSONObject obj) {
        new Thread() {
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
                    out.println(obj);
                    out.flush();
                } catch (IOException e) {
                    printError(e);
                }
            }
        }.start();
    }
    // 채팅내용 String = id, ArrayList = 채팅 내용
    public static HashMap<String, ArrayList<Chat_Item>> chatData = new HashMap<>();
    // 활동내용 String = id, ArrayList = 활동 내용
    public static ArrayList<Info_Item> infoGive = new ArrayList<>();
    // 활동내용 String = id, ArrayList = 활동 내용
    public static ArrayList<Info_Item> infoReceive = new ArrayList<>();
    // 마커 정보
    public static HashMap<String, Marker> clientMarkers = new HashMap<String, Marker>();
    // 뷰타입
    public class ViewType {
        public static final int MY_MESSAGE = 1;
        public static final int OPPONENT_MESSAGE = 2;
    }
    // 아이디
    public static String my_id;
    // 비밀번호
    public static String my_password;
    // 닉네임
    public static String my_nickname;
    // Intro context
    public static Context introContext;
    // Login context
    public static Context loginContext;
    // CreateAccount context
    public static Context accountContext;
    // Main context
    public static Context mainContext;
    // Chatting context
    public static Context chattingContext;
    // ChattingList context
    public static Context chattingListContext;
    // ActivityRecord context
    public static Context recordContext;
    // 맵
    public static GoogleMap map;
    // 스피너 항목
    public static String[] items = {"기타", "상품판매", "전문지식", "심부름", "아르바이트"};

    public static void printError(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();

        Log.e("error", exceptionAsStrting);
    }
}
