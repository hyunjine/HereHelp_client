package com.example.herehelp.main;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.herehelp.Data;
import com.example.herehelp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;

public class BottomSheet extends BottomSheetDialogFragment {
    private Context context;
    private String opponent_id;
    private String opponent_nickname;
    private String content;
    private String price;
    private String category;

    public BottomSheet(Context context, String opponent_id, String opponet_nickname, String content, String price, String category) {
        this.context = context;
        this.opponent_id = opponent_id;
        this.opponent_nickname = opponet_nickname;
        this.content = content;
        this.price = price;
        this.category = category;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet, container,false);
        TextView tv_nickname = v.findViewById(R.id.tv_nickname);
        tv_nickname.setText(opponent_nickname);
        TextView tv_content = v.findViewById(R.id.tv_content);
        tv_content.setText(content);
        /*
        버튼
         */
        // 요청 버튼
        Button btn_sendMessage = v.findViewById(R.id.btn_sendMessage);
        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) context).goToChatting(opponent_id, opponent_nickname);
            }
        });
        // 거리 표시
        double markerLatitude = Data.clientMarkers.get(opponent_id).getPosition().latitude;
        double markerLongitude = Data.clientMarkers.get(opponent_id).getPosition().longitude;
        // 현재 위치
        Location loc = new GpsTracker(context).getLocation();
        TextView tv_distance = v.findViewById(R.id.tv_distance);
        tv_distance.setText("거리 : " + getDistance(markerLatitude, markerLongitude, loc));
        // 가격 표시
        TextView tv_price = v.findViewById(R.id.tv_price);
        tv_price.setText("₩ " + new DecimalFormat("###,###").format(Integer.parseInt(price)));
        // 카테고리 표시
        TextView tv_category = v.findViewById(R.id.tv_category);
        tv_category.setText("# " + category);

        return v;
    }
    private String getDistance(double markerLatitude,  double markerLongitude, Location loc) {
        Location a = new Location("a");
        a.setLatitude(markerLatitude);
        a.setLongitude(markerLongitude);
        // 단위 변환
        if (a.distanceTo(loc) > 1000)
            return String.format("%.1f", a.distanceTo(loc) / 1000) + "km";
        else
            return String.format("%.0f", a.distanceTo(loc)) + "m";
    }
}