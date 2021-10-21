package com.example.herehelp.activity_record;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herehelp.Data;
import com.example.herehelp.R;

import java.text.DecimalFormat;

public class ActivityRecord extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Data.recordContext = ActivityRecord.this;
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ActivityRecordAdapter(Data.infoGive, "+"));
        TextView tv_totalPrice = findViewById(R.id.tv_totalPrice);

        tv_totalPrice.setText(getTotalPrice("give"));
        // 도움 준 내역 버튼
        Button btn_give = findViewById(R.id.btn_give);
        // 도움 받은 내역 버튼
        Button btn_receive = findViewById(R.id.btn_receive);
        btn_give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_receive.setTextColor(Color.parseColor("#B5B5B5"));
                btn_give.setTextColor(Color.parseColor("#0e1544"));
                recyclerView.setAdapter(new ActivityRecordAdapter(Data.infoGive,  "+"));
                tv_totalPrice.setText(getTotalPrice("give"));
            }
        });
        btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_receive.setTextColor(Color.parseColor("#0e1544"));
                btn_give.setTextColor(Color.parseColor("#B5B5B5"));
                recyclerView.setAdapter(new ActivityRecordAdapter(Data.infoReceive, "-"));
                tv_totalPrice.setText(getTotalPrice("receive"));
            }
        });
        // 뒤로가기 버튼
        ImageButton btn_goToBack = findViewById(R.id.btn_goToBack);
        btn_goToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private String getTotalPrice(String flag) {
        int totalPirce = 0;
        if (flag.equals("give")) {
            for (int i = 0; i < Data.infoGive.size(); i++) {
                int price = Integer.parseInt(Data.infoGive.get(i).getPrice());
                totalPirce += price;
            }
            return  "총 수익 : " + new DecimalFormat("###,###").format(totalPirce) + "원";
        }
        else if (flag.equals("receive")) {
            for (int i = 0; i < Data.infoReceive.size(); i++) {
                int price = Integer.parseInt(Data.infoReceive.get(i).getPrice());
                totalPirce += price;
            }
            return  "총 지출 : " + new DecimalFormat("###,###").format(totalPirce) + "원";
        }
        return  "";
    }
}
