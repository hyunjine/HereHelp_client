package com.example.herehelp.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.herehelp.Data;
import com.example.herehelp.R;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class HelpRequest extends AppCompatActivity {
    private String beforeStr;
    private int category = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_request);

        EditText et_content = findViewById(R.id.et_content);
        EditText et_price = findViewById(R.id.et_price);
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(beforeStr)) {
                    beforeStr = new DecimalFormat("###,###").format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    et_price.setText(beforeStr);
                    Editable editable = et_price.getText();
                    Selection.setSelection(editable, beforeStr.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        // 선택 박스
        Spinner sp_selectCategory = findViewById(R.id.sp_selectCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HelpRequest.this, android.R.layout.simple_spinner_dropdown_item, Data.items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_selectCategory.setAdapter(adapter);
        sp_selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = 0;
            }
        });
        // 등록 버튼
        Button btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String content = et_content.getText().toString();
                    String price = et_price.getText().toString().trim();

                    double latitude = new GpsTracker(HelpRequest.this).getLatitude();
                    double longitude = new GpsTracker(HelpRequest.this).getLongitude();
                    String loc = latitude + "#" + longitude;

                    JSONObject data = new JSONObject();

                    data.put("flag", "helpRequest");
                    data.put("content", content);
                    data.put("price", price.replace(",", ""));
                    data.put("location", loc);
                    data.put("category", category);

                    Data.sendToServer(data);

                    finish();
                } catch (Exception e) {
                    Data.printError(e);
                }
            }
        });
        // 뒤로 가기 버튼
        ImageButton btn_goToBack = findViewById(R.id.btn_goToBack);
        btn_goToBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}