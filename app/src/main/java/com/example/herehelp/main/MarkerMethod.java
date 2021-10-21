package com.example.herehelp.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.herehelp.Data;
import com.example.herehelp.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MarkerMethod extends AppCompatActivity {
    /*
     마커 등록 및 hashmap저장
     */
    public void setMarker(String id, double latitude, double longitude) {
        runOnUiThread(new Runnable() {
            public void run() {
                MarkerOptions myLocationMarker = setMarkerOption(latitude, longitude);
                Marker marker = Data.map.addMarker(myLocationMarker);
                Data.clientMarkers.put(id, marker);
            }
        });
    }
    /*
    마커 삭제
     */
    public void removeMarker(String id) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    // 1. 지도에서 삭제
                    Marker marker = Data.clientMarkers.get(id);
                    marker.remove();

                    // 2. 해쉬맵에서 삭제
                    Data.clientMarkers.remove(id);
                    // 내 등록요청일 경우
                    if (id.equals(Data.my_id))
                        ((Main) Data.mainContext).toastMsg("요청이 해제 되었습니다.");
                } catch (NullPointerException e) {
                    if (id.equals(Data.my_id))
                        ((Main) Data.mainContext).toastMsg("요청한 도움이 없습니다!");
                }
            }
        });
    }
    private MarkerOptions setMarkerOption(double latitude, double longitude) {
        MarkerOptions myLocationMarker = new MarkerOptions(); // 마커 객체 생성
        myLocationMarker.position(new LatLng(latitude, longitude));
        myLocationMarker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(Data.mainContext, LayoutInflater.from(Data.mainContext).inflate(R.layout.marker, null))));

        return myLocationMarker;
    }
    /*
    View를 Bitmap으로 변환
     */
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
