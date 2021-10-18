package com.example.herehelp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Edits;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ReceiveDataThread extends Thread {
    private static final String TAG = "print";      // 로그용
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MarkerMethod markerMethod = new MarkerMethod();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void run() {
        try {
            Data.socket = new Socket(Data.SERVER_IP, Data.SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(Data.socket.getInputStream(), "UTF-8"));

            while (Data.socket.isConnected()) {
                String fromServer = in.readLine();

                JSONObject obj = new JSONObject(fromServer);
                String flag = obj.getString("flag");
                Log.d(TAG, obj.toString());

                switch (flag) {
                    case "idCheck":
                        ((CreateAccount) Data.accountContext).idCheck(obj);
                        break;
                    case "nicknameCheck":
                        ((CreateAccount) Data.accountContext).nicknameCheck(obj);
                        break;
                    case "createAccountCheck":
                        ((CreateAccount) Data.accountContext).createAccountCheck(obj);
                        break;
                    case "login":
                        login(obj);
                        break;
                    case "init":
                        init(obj);
                        break;
                    case "helpRequest":
                        helpRequest(obj);
                        break;
                    case "completeHelp":
                        completeHelp(obj);
                        break;
                    case "completeTransaction":
                        completeTransaction(obj);
                        break;
                    case "markerClicked":
                        markerClicked(obj);
                        break;
                    case "chatting":
                        chatting(obj);
                        break;
                    case "selectType":
                        selectType(obj);
                        break;
                    case "selectPrice":
                        selectPrice(obj);
                        break;
                    case "notice":
                        notice(obj);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            Data.printError(e);
        } finally {
            String errorMsg = "네트워크 연결 끊김";
            switch (getCurrentActivity()) {
                case "Main":
                    ((Main) Data.mainContext).pop(errorMsg);
                    break;
                case "Login":
                    ((Login) Data.loginContext).pop(errorMsg);
                    break;
                case "CreateAccount":
                    ((CreateAccount) Data.accountContext).pop(errorMsg);
                    break;
                case "ChattingWindow":
                    ((ChattingWindow) Data.chattingContext).pop(errorMsg);
                    break;
                case "ChattingList":
                    ((ChattingList) Data.chattingListContext).pop(errorMsg);
                    break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((Main) Data.mainContext).quitApp();
        }
    }
    /*
    login 메서드
     */
    private void login(JSONObject obj) {
        try {
            String loginStatus = obj.getString("loginStatus");
            String activity = obj.getString("activity");
            if (loginStatus.equals("success")) {
                if (activity.equals("Intro"))
                    ((Intro) Data.introContext).convertIntroToMain();
                else if (activity.equals("Login"))
                    ((Login) Data.loginContext).succeessLogin(obj.getString("nickname"));
            }
            else {
                // Intro에서 자동로그인시 실패할 경우가 없기에 제외
                if (activity.equals("Login"))
                    ((Login) Data.loginContext).pop("아이디 또는 비밀번호가 일치하지 않습니다.");
            }

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    1. init 메서드
     */
    private void init(JSONObject obj) {
        try {
            JSONArray xml = obj.getJSONArray("xml");
            JSONObject marker = obj.getJSONObject("marker");
            JSONObject infoXML = obj.getJSONObject("infoXML");
            Log.d("tag", infoXML.toString());
            setMarkers(marker);
            setAllXML(xml);
            setInfoXML(infoXML);
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    1-1. setMarkers  메서드
    */
    private void setMarkers(JSONObject markers) {
        try {
            Gson gson = new Gson();
            HashMap map = new HashMap();
            map = (HashMap) gson.fromJson(markers.toString(), map.getClass());

            for (Object id : map.keySet()) {
                Object location = map.get(id);

                double latitude = returnLatitude(location.toString());
                double longtitude = returnLongtitude(location.toString());

                markerMethod.setMarker(id.toString(), latitude, longtitude);
            }
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    private Double returnLatitude(String location) {
        String[] loc = location.toString().split("#");
        double latitude = Double.parseDouble(loc[0]);
        return latitude;
    }
    private Double returnLongtitude(String location) {
        String[] loc = location.toString().split("#");
        double longitude = Double.parseDouble(loc[1]);
        return longitude;
    }
    /*
    1-2. setAllXML 메서드
     */
    private void setAllXML(JSONArray xml) {
        try {
            for (int i = 0; i < xml.length(); i++) {
                String opponent_id = xml.getJSONObject(i).getString("opponent_id");
                String opponent_nickname = xml.getJSONObject(i).getString("opponent_nickname");
                JSONArray chattingXML = xml.getJSONObject(i).getJSONArray("chattingXML");
                JSONArray timeXML = xml.getJSONObject(i).getJSONArray("timeXML");
                ArrayList<Chat_Item> item = new ArrayList<>();

                for (int j = 0; j < chattingXML.length(); j++) {
                    String id = chattingXML.getJSONObject(j).getString("id");
                    String time = timeXML.getJSONObject(j).getString("time");
                    String msg = chattingXML.getJSONObject(j).getString("msg");
                    // 나의 채팅일 때
                    if (id.equals(Data.my_id))
                        item.add(new Chat_Item(Data.my_nickname, msg, time, Data.ViewType.MY_MESSAGE, opponent_nickname));
                    // 상대방의 채팅일 때
                    else if (id.equals(opponent_id))
                        item.add(new Chat_Item(opponent_nickname, msg, time, Data.ViewType.OPPONENT_MESSAGE, opponent_nickname));
                }
                Data.chatData.put(opponent_id, item);
            }
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    setInfoXML 메서드
     */
    private void setInfoXML(JSONObject obj) {
        try {
            JSONArray arrReceive = obj.getJSONArray("arrReceive");
            for (int i = 0; i < arrReceive.length(); i++) {
                String id = arrReceive.getJSONObject(i).getString("opponent_nickname");
                String time = arrReceive.getJSONObject(i).getString("time");
                int category = Integer.parseInt(arrReceive.getJSONObject(i).getString("category"));
                String price = arrReceive.getJSONObject(i).getString("price");
                Data.infoReceive.add(new Info_Item(id, category, price, time));
            }
            JSONArray arrGive = obj.getJSONArray("arrGive");
            for (int i = 0; i < arrGive.length(); i++) {
                String id = arrGive.getJSONObject(i).getString("opponent_nickname");
                String time = arrGive.getJSONObject(i).getString("time");
                int category = Integer.parseInt(arrGive.getJSONObject(i).getString("category"));
                String price = arrGive.getJSONObject(i).getString("price");
                Data.infoGive.add(new Info_Item(id, category, price, time));
            }

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    2. helpRequest 메서드
     */
    private void helpRequest(JSONObject obj) {
        try {
            String status = obj.getString("status");
            if (status.equals("success"))
                saveRequest(obj);
            else if (status.equals("fail")) {
                ((Main) Data.mainContext).pop("최대 한 번만 등록이 가능합니다.");
            }
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    2-2. helpRequest 등록 처리
     */
    private void saveRequest(JSONObject obj) {
        try {
            String ID = obj.getString("ID");
            String location = obj.getString("location");
            double latitude = returnLatitude(location);
            double longtitude = returnLongtitude(location);

            markerMethod.setMarker(ID, latitude, longtitude);
            // 내 등록요청일 경우
            if (ID.equals(Data.my_id))
                ((Main) Data.mainContext).toastMsg("요청이 등록 되었습니다.");
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    3. completeHelp 메서드
     */
    private void completeHelp(JSONObject obj) {
        try {
            String ID = obj.getString("id");
            // 정보 및 지도위에서 삭제
            markerMethod.removeMarker(ID);
        } catch (Exception e) {
            Data.printError(e);
        }
    }

    /*
    4. markerclicked 메서드
     */
    private void markerClicked(JSONObject obj) {
        try {
            String opponent_id = obj.getString("opponent_id");
            String opponent_nickname = obj.getString("opponent_nickname");
            String content = obj.getString("content");
            String price = obj.getString("price");
            String category = Data.items[obj.getInt("category")];

            ((Main) Data.mainContext).showBottomSheet(opponent_id, opponent_nickname, content, price, category);
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    5. chatting 메서드
     */
    private void chatting(JSONObject obj) {
        try {
            String sender = obj.getString("sender");
            String sender_nickname = obj.getString("sender_nickname");
            String receiver = obj.getString("receiver");
            String receiver_nickname = obj.getString("receiver_nickname");
            String msg = obj.getString("msg");
            String time = obj.getString("time");

            // 아이디 구별
            // 1. 내가 보낸 메세지일 때
            if (sender.equals(Data.my_id)) {
                // receiver가 상대 아이디
                setChatData(receiver, receiver_nickname, msg, time, Data.ViewType.MY_MESSAGE);
                chooseActivity(receiver, receiver_nickname, msg);
            }
            // 2. 상대가 보낸 메세지일 때
            else {
                // sender가 상대 아이디
                setChatData(sender, sender_nickname, msg, time, Data.ViewType.OPPONENT_MESSAGE);
                chooseActivity(sender, sender_nickname, msg);
            }
        } catch (JSONException e) {
            Data.printError(e);
        }
    }
    private void setChatData(String opponent_id, String opponent_nickname, String msg, String time, int viewType) {
        // 채팅내용 유무 확인
        // 1. 채팅 내용이 있을 때
        if (Data.chatData.containsKey(opponent_id)) {
            Data.chatData.get(opponent_id).add(new Chat_Item(opponent_nickname, msg, time, viewType, opponent_nickname));
        }
        // 2. 채팅 내용이 없을 때
        else {
            ArrayList<Chat_Item> list = new ArrayList<>();
            list.add(new Chat_Item(opponent_nickname, msg, time, viewType, opponent_nickname));
            Data.chatData.put(opponent_id, list);
        }
    }
    private void chooseActivity(String opponent_id, String opponent_nickname, String msg) {
        // 현재 액티비티 값 확인
        String ActivityName = getCurrentActivity();
        // 1. 메인 화면일 때
        if (ActivityName.equals("Main")) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new PopupChatting(Data.mainContext, opponent_id, opponent_nickname, msg, "Main").show();
                }
            }, 0);
        }
        // 2. 채팅 화면일 때
        else if (ActivityName.equals("ChattingWindow"))
            ((ChattingWindow) Data.chattingContext).setChattingContent();

        // 3. 채팅 목록화면일 때
        else if (ActivityName.equals("ChattingList")) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ChattingList) Data.chattingListContext).setChattingListContent();
                    new PopupChatting(Data.chattingListContext, opponent_id, opponent_nickname, msg, "ChattingList").show();;
                }
            }, 0);
        }
    }
    /*
    selectType 메서드
     */
    private void selectType(JSONObject obj) {
        try {
            Set<String> id_set = Data.clientMarkers.keySet();
            Iterator<String> iter = id_set.iterator();
            while (iter.hasNext()) {
                markerMethod.removeMarker(iter.next());
            }
            obj.remove("flag");
            Iterator iterator = obj.keys();
            while (iterator.hasNext()) {
                String id = iterator.next().toString();
                String location = obj.getString(id);
                markerMethod.setMarker(id, returnLatitude(location), returnLongtitude(location));
            }

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    selectPrice 메서드
     */
    private void selectPrice(JSONObject obj) {
        try {
            Set<String> id_set = Data.clientMarkers.keySet();
            Iterator<String> iter = id_set.iterator();
            while (iter.hasNext()) {
                markerMethod.removeMarker(iter.next());
            }
            obj.remove("flag");
            Iterator iterator = obj.keys();
            while (iterator.hasNext()) {
                String id = iterator.next().toString();
                String location = obj.getString(id);
                markerMethod.setMarker(id, returnLatitude(location), returnLongtitude(location));
            }

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    completeTransaction 메서드
     */
    private void completeTransaction(JSONObject obj) {
        try {
            String type = obj.getString("type");
            String opponent_nickname = obj.getString("opponent_nickname");
            int category = Integer.parseInt(obj.getString("category"));
            String price = obj.getString("price");
            String time = obj.getString("time");
            if (type.equals("give"))
                Data.infoGive.add(new Info_Item(opponent_nickname, category, price, time));
            else if (type.equals("receive"))
                Data.infoReceive.add(new Info_Item(opponent_nickname, category, price, time));

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    notice 메서드
     */
    private void notice(JSONObject obj) {
        try {
            ((Main) Data.mainContext).notice(obj.getString("title"), obj.getString("content"));
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    현재 실행 액티비티 가져오기
     */
    private String getCurrentActivity() {
        ActivityManager manager = (ActivityManager) Data.mainContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName = info.get(0).topActivity;
        String ActivityName = componentName.getShortClassName().substring(1);
        Log.d(TAG, ActivityName);

        return ActivityName;
    }
}
