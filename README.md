# 여기 도움
**사용자 간 필요한 정보나 상황들을 지도 위 마커를 통하여 제시한 금액에 맞춰 도움을 줄 수 있는  안드로이드 애플리케이션**
## Preview
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138462695-c4af8b78-4b2b-41c6-bad8-e69daaa93d8c.png" width="22%"/>
<img src="https://user-images.githubusercontent.com/92709137/138464170-08f0acf2-5be6-4973-bc90-28770b9cf640.png" width="22%"/>
<img src="https://user-images.githubusercontent.com/92709137/138464206-ea9c1af2-b9e4-432e-936b-b1ee6892a3b9.png" width="22%"/>
<img src="https://user-images.githubusercontent.com/92709137/138466281-eff9be67-10b0-4378-babd-2adf94323f09.png" width="22%"/>
</p>

## 개발 기간
**2021-05-01 ~ 2021-09-23**

## 개발 환경 및 라이브러리
> **Server**
> * JAVA - Eclipse
> * GUI
> * Multi-Thread SocketIO
> * JSON

> **DataBase**
> * SQL Developer    
> * XML

> **Client**
> * JAVA - Android Stuio
> * Google Map API
> * JSON

## 어플 소개

### 목차

1. 로그인 및 회원가입
    * 로그인
    * 회원가입

2. 메인화면

3. 로그아웃

4. 도움요청

5. 도움완료
    + 등록해제
    + 거래완료
6. 채팅목록

7. 활동내역

## 1. 로그인 및 회원가입

> **1-1. 로그인**
<img src="https://user-images.githubusercontent.com/92709137/138470161-81451897-85f5-486e-8c93-5790145fff22.png" width="50%"/>

* 자동 로그인은 SharedPreferences클래스를 사용하여 저장하였습니다.

```java
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
```
> **1-2. 회원가입**
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138470154-34474877-406c-43ca-96c9-176ee9da2b9e.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138470160-867a8d18-f775-4c1f-b697-76f3a7231f0e.png" width="45%"/>
</p>

* 아이디와 닉네임은 중복확인 버튼을 눌러 중복상태를 확인했습니다.
* 비밀번호는 addTextChangedListener를 사용하여 텍스트가 바뀔 때 마다 상태를 표시하였습니다.
###
## 2. 메인화면

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138476866-b25b4cf7-4a1b-43c4-a078-d9936f06c3c0.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138476883-78f5cdf1-16aa-4686-a9b4-c33828782ec4.png" width="45%"/>
</p>

* 로그인 시 현재 위치를 기반으로 카메라가 설정되고 다른 사용자들이 등록한 마커가 보여집니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138476882-d2c517da-aec9-4a1b-a52a-27f6f07be1b5.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138476880-03dfd982-8db9-4f9d-bdc9-f9fc997c69eb.png" width="45%"/>
</p>

* 우측하단 +모양 버튼을 누르면 유형, 가격별 카테고리를 설정하여 원하는 마커만 볼 수 있습니다.

## 메뉴 목록
<img src="https://user-images.githubusercontent.com/92709137/138462672-a096ad9c-7be7-4267-8757-4274f2d1ee60.png" width="50%"/>

## 3. 로그아웃
* 로그인화면으로 전환됩니다.

## 4. 도움요청
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138554974-af73e4b9-076a-44e4-b382-5d594a692d5f.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138554819-404ad65c-4bd0-4d6f-8ae0-46ea6f26edd5.png" width="45%"/>
</p>
<p align="center">
<img src="https://user-images.githubusercontent.com/92709137/138554816-194aa60a-6c61-48e3-9a28-98dcd98567a5.png" width="50%"/>
</p>

* 상단부터 금액제시, 카테고리 설정, 글 내용을 입력 후 하단의 등록버튼을 통해 글을 등록합니다.
* 현재위치에 기반하여 지도 위에 등록한 글의 마커가 표시됩니다.
* 마커 클릭 시 하단 다이얼로그가 생성되고 **닉네임**, **본인과 상대방까지의 거리**, **카테고리**, **금액**, **내용**이 담겨있습니다.
* 거리는 Location클래스의 distanceto메서드를 활용하였습니다. 거리가 1000m이상 이 될 경우 km로 변환하였습니다.
```java
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
```
* 상대방이 마커를 클릭한 후 도움을 줄 수 있다는 생각이 들면 연락버튼을 눌러 해당 글 작성자와 채팅을 할 수 있습니다.
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138554806-2b997a5f-9c4d-47c2-8527-47ea71be38a7.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138554809-e830ff49-c77d-4bc2-a8ac-0dd0e96770f7.png" width="45%"/>
</p>

## 5. 도움완료
<img src="https://user-images.githubusercontent.com/92709137/138554820-9294deb3-a6e1-4e29-ae12-ce180d2d68ca.png" width="45%"/>

* 등록해제는 등록된 마커와 글을 삭제하는 기능입니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138555619-cf883619-e5e7-4385-9ff8-c64e01924d65.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/138555621-1027e3a7-505b-4bfb-bcfd-6671ed63a75f.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/138554812-3161b0b9-3da1-4b79-a560-bc2897065bf5.png" width="30%"/>
</p>

* 거래 완료는 사용자끼리 만나 도움을 주고 받으면 글 작성자가 거래완료 버튼을 눌러 도움을 준 계정을 선택해 저장하는 기능입니다.

## 6. 채팅목록
<img src="https://user-images.githubusercontent.com/92709137/138554811-1dfc6c86-f497-43e3-a92f-d59bc774c78f.png" width="45%"/>

* 채팅목록을 볼 수 있는 기능입니다.

## 7. 활동내역
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138554813-9e582d38-85f5-4a87-ae54-f0a36eb1c2fb.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138554815-887b4008-545f-4684-8079-14bc36338545.png" width="45%"/>
</p>

* 현재까지 활동 내역을 볼 수 있는 기능입니다. 내역 별 우측 하단에 총 지출 및 수익금이 표시되어있습니다.

## 
