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
> 
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
<img src="https://user-images.githubusercontent.com/92709137/138470154-34474877-406c-43ca-96c9-176ee9da2b9e.png" width="337"/>
<img src="https://user-images.githubusercontent.com/92709137/138470154-34474877-406c-43ca-96c9-176ee9da2b9e.png" width="337"/>

* 아이디와 닉네임은 중복확인 처리를 하였고 비밀번호 일치확인은 addTextChangedListener를 활용하여 텍스트가 변경될 때 마다 일치여부를 확인하였습니다.
