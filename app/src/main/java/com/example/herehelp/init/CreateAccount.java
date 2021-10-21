package com.example.herehelp.init;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.herehelp.Data;
import com.example.herehelp.main.Popup;
import com.example.herehelp.R;

import org.json.JSONObject;

public class CreateAccount extends AppCompatActivity {
    private boolean idStatus = false;
    private boolean passwordStatus = false;
    private boolean nicknameStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        // 컨텍스트 초기화
        Data.accountContext = CreateAccount.this;
        // 이름
        EditText et_name = findViewById(R.id.et_name);
        // 아이디
        EditText et_id = findViewById(R.id.et_id);
        // 아이디 중복확인 버튼
        Button btn_checkOverlapID = findViewById(R.id.btn_checkOverlapID);
        btn_checkOverlapID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json("idCheck", et_id.getText().toString());
            }
        });
        // 비밀번호
        EditText et_password = findViewById(R.id.et_password);
        // 비밀번호 확인
        EditText et_checkPassword = findViewById(R.id.et_checkPassword);
        TextView tv_passwordStatus = findViewById(R.id.tv_passwordStatus);
        et_checkPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    tv_passwordStatus.setText("");
                    passwordStatus = false;
                } else if (s.toString().equals(et_password.getText().toString())) {
                    tv_passwordStatus.setText("일치");
                    tv_passwordStatus.setTextColor(Color.parseColor("#00FF1A"));
                    passwordStatus = true;
                } else {
                    tv_passwordStatus.setText("불일치");
                    tv_passwordStatus.setTextColor(Color.parseColor("#FFFF0000"));
                    passwordStatus = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 닉네임
        EditText et_nickname = findViewById(R.id.et_nickname);
        // 닉네임 중복확인 버튼
        Button btn_checkOverlapNickname = findViewById(R.id.btn_checkOverlapNickname);
        btn_checkOverlapNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json("nicknameCheck", et_nickname.getText().toString());
            }
        });
        // 휴대폰 번호
        EditText et_phonenumber = findViewById(R.id.et_phonenumber);
        // 회원가입 버튼
        Button btn_createAccount = findViewById(R.id.btn_createAccount);
        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String id = et_id.getText().toString();
                String password = et_password.getText().toString();
                String nickname = et_nickname.getText().toString();
                String phonenumber = et_phonenumber.getText().toString();

                createAccountRequest(name, id, password, nickname, phonenumber);
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
    /*
    JSON 생성
     */
    private void json(String flag, String value) {
        try {
            JSONObject obj = new JSONObject();

            if (flag.equals("idCheck")) {
                if (value.length() < 6) {
                    pop("최소 6글자 이상 입력해주세요.");
                    return;
                }

                obj.put("flag", flag);
                obj.put("id", value);
            } else if (flag.equals("nicknameCheck")) {
                if (value.length() < 6) {
                    pop("최소 6글자 이상 입력해주세요.");
                    return;
                }
                obj.put("flag", flag);
                obj.put("nickname", value);
            } else if (flag.equals("createAccount")) {
                obj.put("flag", flag);
            }
            Data.sendToServer(obj);
        } catch (Exception e) {
            Data.printError(e);
        }
    }

    /*
    idCheck 메서드
     */
    public void idCheck(JSONObject obj) {
        try {
            String status = obj.getString("status");
            if (status.equals("success")) {
                idStatus = true;
                pop("사용 가능한 아이디 입니다.");
            } else
                pop("중복된 아이디 입니다.");

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    nicknameCheck 메서드
     */
    public void nicknameCheck(JSONObject obj) {
        try {
            String status = obj.getString("status");
            if (status.equals("success")) {
                nicknameStatus = true;
                pop("사용 가능한 닉네임 입니다.");

            } else
                pop("중복된 닉네임 입니다.");

        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    createAccountCheck 메서드
     */
    public void createAccountCheck(JSONObject obj) {
        try {
            String status = obj.getString("status");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (status.equals("success")) {
                        Toast.makeText(CreateAccount.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // 로그인 화면 전환
                        Intent intent = new Intent(CreateAccount.this, Login.class);
                        startActivity(intent);
                        finish();

                    } else
                        pop("회원가입이 실패했습니다.");
                }
            });
        } catch (Exception e) {
            Data.printError(e);
        }
    }
    /*
    createAccount 메서드
     */
    private void createAccountRequest(String name, String id, String password, String nickname, String phonenumber) {
        // 아이디 중복 확인을 안했을 때
        if (!idStatus)
            pop("아이디 중복상태를 확인해주세요.");
            // 닉네임 중복 확인을 안했을 때
        else if (!nicknameStatus)
            pop("닉네임 중복상태를 확인해주세요.");
            // 비밀번호 일치 확인이 안됐을 때
        else if (!passwordStatus)
            pop("비밀번호가 일치하지 않습니다.");
            // 회원가입이 가능할 때
        else {
            try {
                JSONObject obj = new JSONObject();
                obj.put("flag", "createAccountCheck");
                obj.put("name", name);
                obj.put("id", id);
                obj.put("password", password);
                obj.put("nickname", nickname);
                obj.put("phonenumber", phonenumber);

                Data.sendToServer(obj);
            } catch (Exception e) {
                Data.printError(e);
            }
        }
    }
    /*
    알림(팝업)
     */
    public void pop(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Popup(CreateAccount.this, msg);
            }
        });
    }
}
