package com.example.herehelp.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.herehelp.Data;
import com.example.herehelp.R;
import com.example.herehelp.activity_record.ActivityRecord;
import com.example.herehelp.chatting.ChattingList;
import com.example.herehelp.chatting.ChattingWindow;
import com.example.herehelp.completehelp.CompleteHelp;
import com.example.herehelp.completehelp.SelectCompleteHelp;
import com.example.herehelp.init.Login;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONObject;

import java.util.HashMap;

public class Main extends AppCompatActivity implements AutoPermissionsListener, GoogleMap.OnMarkerClickListener {
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // ?????????????????????
        // ???????????? ?????????
        Data.mainContext = Main.this;
        // ?????? ?????? ??????
        askPermission();
        // ??????
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.main);
        View drawerView = (View) findViewById(R.id.drawer);
        try {
            MapsInitializer.initialize(this);
            JSONObject obj = new JSONObject();
            obj.put("flag", "init");
            Data.sendToServer(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Data.map = googleMap;
                if (ActivityCompat.checkSelfPermission(Main.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Main.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Data.map.setMyLocationEnabled(true);
                Data.map.getUiSettings().setMyLocationButtonEnabled(false);
                Data.map.setOnMarkerClickListener(Main.this);

                moveCamera();
            }
        });
        // drawer??? ????????? ????????? ?????????
        TextView tv_mainText = findViewById(R.id.tv_mainText);
        tv_mainText.setText(Data.my_nickname + "???");
        //?????? ?????? ??????
        ImageButton btn_currentLocation = findViewById(R.id.btn_currentLocation);
        btn_currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateCamera();
            }
        });
        // ?????? ?????? ??????
        ImageButton btn_drawerOpen = findViewById(R.id.btn_drawerOpen);
        btn_drawerOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });
        // ??????: ?????? ?????? ??????
        Button btn_helpRequest = findViewById(R.id.btn_helpRequest);
        btn_helpRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, HelpRequest.class));
            }
        });
        // ??????: ????????? ??????
        Button btn_login_menu = findViewById(R.id.btn_login_menu);
        btn_login_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.chatData.clear();
                Data.clientMarkers.clear();
                startActivity(new Intent(Main.this, Login.class));
                finish();
            }
        });
        // ??????: ?????? ?????? ??????
        Button btn_helpComplete = findViewById(R.id.btn_helpComplete);
        btn_helpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCompleteHelp bottomsheet = new SelectCompleteHelp(Main.this);
                bottomsheet.show(getSupportFragmentManager(), "tag");
            }
        });
        // ?????? : ?????? ?????? ??????
        Button btn_chattingList = findViewById(R.id.btn_chattingList);
        btn_chattingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, ChattingList.class));
            }
        });
        // ?????? ???????????? ??????
        ImageButton btn_category = findViewById(R.id.btn_category);
        Button btn_selectType = findViewById(R.id.btn_selectType);
        Button btn_selectPrice = findViewById(R.id.btn_selectPrice);
        // ???????????? : ?????? ??? ??????
        btn_selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Category_Type(Main.this).show();
            }
        });
        // ???????????? : ?????? ??? ??????
        btn_selectPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Category_Price(Main.this).show();
            }
        });
        // ???????????? ??????
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. ????????? ?????? ???
                if (btn_selectType.getVisibility() == View.VISIBLE) {
                    btn_selectType.setVisibility(View.GONE);
                    btn_selectPrice.setVisibility(View.GONE);
                }
                // 2. ????????? ??? ?????? ???
                else {
                    btn_selectType.setVisibility(View.VISIBLE);
                    btn_selectPrice.setVisibility(View.VISIBLE);
                }
            }
        });
        // ?????? ?????? ??????
        Button btn_acitivityRecord = findViewById(R.id.btn_acitivityRecord);
        btn_acitivityRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this, ActivityRecord.class));
            }
        });
    }

    /*
    ??????????????? ??????
     */
    public void goToChatting(String opponent_id, String opponent_nickname) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main.this, ChattingWindow.class);
                intent.putExtra("opponent_id", opponent_id);
                intent.putExtra("opponent_nickname", opponent_nickname);
                startActivity(intent);
            }
        });
    }

    /*
    ?????? ??????????????? ??????
     */
    public void goToCompleteHelp(String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main.this, CompleteHelp.class);
                intent.putExtra("content", content);
                startActivity(intent);
            }
        });
    }

    /*
    ?????? ??????
     */
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        try {
            String opponent_id = getKey(Data.clientMarkers, marker);
            if (opponent_id == null) {
                Toast.makeText(Main.this, "????????? ???????????????", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject obj = new JSONObject();
                obj.put("flag", "markerClicked");
                obj.put("opponent_id", opponent_id);
                Data.sendToServer(obj);
            }

        } catch (Exception e) {
            Data.printError(e);
        }
        return false;
    }
    /*
    marker??? key??? ??????
     */
    public String getKey(HashMap<String, com.google.android.gms.maps.model.Marker> m, Object value) {
        for (String str : m.keySet()) {
            if (m.get(str).equals(value)) {
                return str;
            }
        }
        return null;
    }
    /*
    ?????? ?????? ??????
     */
    private void animateCamera() {
        GpsTracker gps = new GpsTracker(Main.this);
        LatLng curPoint = new LatLng(gps.getLatitude(), gps.getLongitude());
        Data.map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17));
    }

    /*
    ?????? ?????? ?????? ??????
     */
    public void moveCamera() {
        GpsTracker gps = new GpsTracker(Main.this);
        LatLng curPoint = new LatLng(gps.getLatitude(), gps.getLongitude());
        Data.map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17));
    }

    /*
    ?????? ?????? ??? ?????? ???????????????
     */
    public void showBottomSheet(String opponent_id, String opponent_nickname, String content, String price, String category) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BottomSheet bottomsheet = new BottomSheet(Main.this, opponent_id, opponent_nickname, content, price, category);
                bottomsheet.show(getSupportFragmentManager(), "tag");
            }
        });
    }

    /*
    ????????? ?????????
     */
    public void toastMsg(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    ??????(??????)
     */
    public void pop(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Popup(Main.this, msg);
            }
        });
    }

    /*
    ??????
     */
    public void notice(String title, String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Notice(Main.this, title, content);
            }
        });
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        askPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        askPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            Data.socket.close();
//        } catch (IOException e) {
//            Data.printError(e);
//        }
    }

    /*
    ?????? ?????? ??????
     */
    private void askPermission() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }
    }

    public boolean checkLoactionPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Main.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Main.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    /*
    ????????? ????????? ?????? ??????
     */
    public void checkRunTimePermission() {
        if (!checkLoactionPermission()) {
            // 1. ???????????? ????????? ????????? ??? ?????? ?????? ??????
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main.this, REQUIRED_PERMISSIONS[0]))
                Toast.makeText(Main.this, "?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            // onRequestPermissionsResult?????? ????????????
            ActivityCompat.requestPermissions(Main.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    /*
    ????????? ????????? ????????? ???????????? ??????
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????
            boolean check_result = true;
            // ?????? ???????????? ??????????????? ??????
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            // ???????????? ??????????????? ???
            if (check_result == false) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(Main.this, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    quitApp();
                } else {
                    Toast.makeText(Main.this, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
    GPS????????? ??????
     */
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ??????????????? ?????? ?????? ????????? ???????????????.");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // ?????? ???????????? ?????? ????????? ?????? ???
                quitApp();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_ENABLE_REQUEST_CODE) {
            if (checkLocationServicesStatus())
                checkRunTimePermission();
        }
    }

    /*
    ?????? ?????? ?????? ??????
     */
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /*
    ??? ?????? ??????
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void quitApp() {
        moveTaskToBack(true);                        // ???????????? ?????????????????? ??????
        finishAndRemoveTask();                        // ???????????? ?????? + ????????? ??????????????? ?????????
        android.os.Process.killProcess(android.os.Process.myPid());    // ??? ???????????? ??????
    }
}