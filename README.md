# 여기 도움
**사용자 간 필요한 정보나 상황들을 지도 위 마커를 통하여 제시한 금액에 맞춰 도움을 줄 수 있는 안드로이드 애플리케이션**
## Preview
Image
<img src="https://user-images.githubusercontent.com/92709137/138462695-c4af8b78-4b2b-41c6-bad8-e69daaa93d8c.png" width="225"/>
<img src="https://user-images.githubusercontent.com/92709137/138464170-08f0acf2-5be6-4973-bc90-28770b9cf640.png" width="225"/>
<img src="https://user-images.githubusercontent.com/92709137/138464206-ea9c1af2-b9e4-432e-936b-b1ee6892a3b9.png" width="225"/>
<img src="https://user-images.githubusercontent.com/92709137/138466281-eff9be67-10b0-4378-babd-2adf94323f09.png" width="225"/>

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
### 1. 로그인 및 회원가입

> 로그인 
<img src="https://user-images.githubusercontent.com/92709137/138470161-81451897-85f5-486e-8c93-5790145fff22.png" width="450"/>

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
> 회원가입
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138470154-34474877-406c-43ca-96c9-176ee9da2b9e.png" width="350"/>
<img src="https://user-images.githubusercontent.com/92709137/138470160-867a8d18-f775-4c1f-b697-76f3a7231f0e.png" width="350"/>
</p>

* 아이디와 닉네임은 중복확인 버튼을 눌러 중복상태를 확인했습니다.
* 비밀번호는 addTextChangedListener를 사용하여 텍스트가 바뀔 때 마다 상태를 표시하였습니다.

### 2. 메인화면

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138476866-b25b4cf7-4a1b-43c4-a078-d9936f06c3c0.png" width="350"/>
<img src="https://user-images.githubusercontent.com/92709137/138476883-78f5cdf1-16aa-4686-a9b4-c33828782ec4.png" width="350"/>
</p>

* 로그인 시 현재 위치를 기반으로 카메라가 설정되고 다른 사용자들이 등록한 마커가 보여집니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138476882-d2c517da-aec9-4a1b-a52a-27f6f07be1b5.png" width="350"/>
<img src="https://user-images.githubusercontent.com/92709137/138476880-03dfd982-8db9-4f9d-bdc9-f9fc997c69eb.png" width="350"/>
</p>

* 우측하단 +모양 버튼을 누르면 유형, 가격별 카테고리를 설정하여 원하는 마커만 볼 수 있습니다.

### 3. 메뉴
<img src="https://user-images.githubusercontent.com/92709137/138462672-a096ad9c-7be7-4267-8757-4274f2d1ee60.png" width="450"/>
