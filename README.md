# 여기 도움
**사용자 간 필요한 정보나 상황들을 지도 위 마커를 통하여 제시한 금액에 맞춰 도움을 줄 수 있는    
안드로이드 애플리케이션**
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
> * JSON GSON

## 어플 소개


## 어플 특징
> 지도위에서 필요한 글을 작성하는 방식입니다.

> 다양한 카테고리가 있어 넓은 용도로 어플을 사용할 수 있습니다.   
> Ex - 중고거래, 급한 도움(비가 와서 우산이 필요, 짐이 많아 같이 들어줄 사람 필요 등), 구인구직 등

> 지도의 마커를 확인하거나 상대방과의 거리가 나와있어 근처 사용자가 빠르게 도움을 주고 보수를 얻을 수 있습니다.

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

8. 서버 

## 1. 로그인 및 회원가입
* 앱 실행 시 ReceiveDataThread클래스가 생성되어 데이터를 받게 설정했습니다.

```java
while (Data.socket.isConnected()) {
   // string 변환
   String fromServer = in.readLine();

   JSONObject obj = new JSONObject(fromServer);
   String flag = obj.getString("flag");
   // 타입별 
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
      case "getContent":
         getContent(obj);
         break;
      case "notice":
         notice(obj);
         break;
      default:
         break;
      }
}
    
```

> **1-1. 로그인**
<img src="https://user-images.githubusercontent.com/92709137/138470161-81451897-85f5-486e-8c93-5790145fff22.png" width="50%"/>

* 자동 로그인은 SharedPreferences클래스를 사용하여 저장하였습니다.

```java
// 자동 로그인
SharedPreferences pref = getSharedPreferences("autoLogin", MODE_PRIVATE);
SharedPreferences.Editor editor = pref.edit();
// 상태 초기화
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
* 마커는 앱 실행 시 또는 추가적인 등록이 있을 때 마다 서버에서 등록된 아이디와 위치를 받아 파싱하여 표시합니다
* 서버에서 마커 위치를 HashMap으로 저장하고 클라이언트에게 JSON형태가 아닌 HashMap을 그대로 전송하기에 Gson클래스를 사용하여 파싱했습니다.
```java
private void setMarkers(JSONObject markers) {
        try {
            Gson gson = new Gson();
            HashMap map = new HashMap();
            map = (HashMap) gson.fromJson(markers.toString(), map.getClass());

            for (Object id : map.keySet()) {
                Object location = map.get(id);
                // location이 String형태고 위도와 경도가 '#'으로 나눠져 있어 파싱하는 코드
                double latitude = returnLatitude(location.toString());
                double longtitude = returnLongtitude(location.toString());
                // 맵에 마커 
                markerMethod.setMarker(id.toString(), latitude, longtitude);
            }
        } catch (Exception e) {
            Data.printError(e);
        }
    }
```
* 좌측상단의 버튼은 메뉴목록을 보여주는 버튼입니다.
* 우측상단의 버튼은 현재위치로 카메라를 이동하는 버튼입니다.

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
* 계정 선택 액티비티에서 선택 결과를 onActivityResult메서드로 받아와 처리하였습니다
```java
public void selectItem(String opponent_nickname) {
   intent.putExtra("opponent_nickname" , opponent_nickname);
   setResult(RESULT_OK, intent);
   finish();
}
    
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
super.onActivityResult(requestCode, resultCode, data);
if (requestCode == 100) {
   if (resultCode != Activity.RESULT_OK) {
      return;
   }
   String opponent_nickname = data.getExtras().getString("opponent_nickname");
   setNickname(opponent_nickname);
   }
}
```

## 6. 채팅목록
<img src="https://user-images.githubusercontent.com/92709137/138554811-1dfc6c86-f497-43e3-a92f-d59bc774c78f.png" width="45%"/>

* 채팅목록을 볼 수 있는 기능입니다.
* 채팅목록과 채팅 내용은 앱 실행 시 또는 추가 채팅이 발생할 때마다 서버에서 데이터를 받아 파싱합니다.
* 서버에서 XML파일로 데이터가 저장되어 있습니다.
```java
private void setChatXML(JSONArray xml) {
        try {
            for (int i = 0; i < xml.length(); i++) {
                // array별 데이터 
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
```

## 7. 활동내역
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/138554813-9e582d38-85f5-4a87-ae54-f0a36eb1c2fb.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138554815-887b4008-545f-4684-8079-14bc36338545.png" width="45%"/>
</p>

* 현재까지 활동 내역을 볼 수 있는 기능입니다. 내역 별 우측 하단에 총 지출 및 수익금이 표시되어있습니다.
* 활동내역은 앱 실행 시 또는 추가적인 거래완료가 이뤄질 때 마다 서버에서 데이터를 받아와 파싱합니다.
* 서버에서 XML파일로 데이터가 저장되어 있습니다.
```java
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
```

## 8. 서버 공지
<img src="https://user-images.githubusercontent.com/92709137/138567497-e8544412-c8a4-41de-8389-7093fcc9c404.PNG" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/138567490-01cc72d3-3c27-4983-b6b2-be8394aad2d7.png" width="45%"/>

* 서버에서 제목과 내용을 입력 후 전송하면 현재 접속해 있는 모든 클라이언트들에게 내용을 전달합니다.
* 자세한 서버의 구동내용은 HereHelp_Server repo에서 기술하겠습니다.

## 히스토리
> **1. Gson 라이브러리**   
> 전에 서버관련 프로젝트를 할 때는 데이터를 string으로 주고받고 데이터 사이사이에 '#'을 넣어 파싱을 subString으로 했었는데 이번에 처음으로 JSON을 사용했었다.
> 서버에서 마커의 위,경도를 해당 ID랑 묶어 HashMamp으로 저장하는데 JSON 데이터를 Log로 확인해보았더니 HashMap이랑 형태가 같아 그대로 전송 후 JSON파싱 방식으로 했었지만 에러가 발생했다.
> HashMap데이터를 서버에서 일일이 String으로 변환하고 다시 JSON에 담아야 하나 생각을 했었지만 뭔가 형태가 같으니 방법이 있을 것 같아 구글링을 하였고 GSON이라는 라이브러리를 통해 Object를
> JSON 문자열로 변환하는 법을 알아내어 효율적으로 처리하였다.

> **2. Thread**   
> 원래는 데이터를 받아 처리해야하는 액티비티들은 각각 스레드를 키고 BufferedReader로 받아왔었다. 다른 액티비티들은 그냥 데이터를 받기만 하면 끝나는 형태라 별 문제가 없었는데
> 실시간으로 데이터를 받아야 하는 BufferedReaderd는 while문 안에 있고 Main액티비티 안에서 동작중이었는데 로그아웃화면으로 넘어가면 Main액티비티를 finish하니 자동으로 while문도 끝날 줄
> 알았는데 죽지않고 살아있어서 이 후 데이터를 수신하는 과정에서 장애가 발생했었다. 처음에는 그냥 컴퓨터 문제인줄 알았는데 테스트겸 계속 로그아웃 로그인을
> 반복하니 서버와 통신이 점점 불량이 되어가 문제가 있음을 느꼈다. 근데 문제를 인식만하고 원인은 도저히 알 수가 없어 하루종일 모니터만 본 기억이 난다. 그러다 Log를 계속 확인하는 도중 혹시
> while문이 계속 살아있나?를 생각해보고 while문을 지워본 결과 문제가 해결되어 액티비티를 finish해도 스레드는 살아있구나를 이 때 알게되었고 그럼 액티비티가 종료되도 스레드는 살아있으니까
> 데이터 수신은 while문 한 곳에서 통합적으로 받을 수 있게 코드를 수정하였다.

> **3. 채팅 및 활동 내역 데이터 저장 방법**      
> 앱에 접속하면 전에 대화했던 채팅 내용들이나 활동내역들을 불러와야하는데 첨엔 막연히 데이터베이스에 저장하면 되겠지라는 생각을 하고 있었다. 근데 막상 코드를 작성하려고 하니 내가 아는
> 관계형 데이터베이스는 같은 데이터를 반복적으로 저장하기에 적합하지 않다는 생각이 들었다. 한 아이디에 채팅내용을 저장하려면 칼럼을 수 도없이 생성했어야 하니까. 그래서 안드로이드 채팅관련 
> 어플은 어떤방식으로 동작하나 봤었는데 firebase라는 프로그램을 사용하여 채팅앱을 제작한 글들이 많았다. 근데 대부분의 글들이 안드로이드(클라이언트)에서 firebase로 접속하는 방식이라
> 뭔가 서버가 2개가 된 느낌이 들어 이건 아니다 라고 생각을 하였고 반복된 데이터를 저장하는건 어떤 방식이 좋을까 생각하며 또 모니터만 몇시간 바라보다가 떠오른게 XML파일 이었다.
> 채팅하나당 노드를 생성하면 뭔가 될 것 같아 한 번 써보기로 했었다. XML도 보기만하고 이번에 처음 써본 언어라 우여곡절이 많았지만 아무쪼록 성공은 한 듯하다. 이 데이터는 서버에서 다루는거라
> 서버 repo에서 추가적으로 작성할 예정이다.

> 첫 안드로이드 개발이라 사실상 모든 부분이 답답했다. 하지만 늘 그랬듯 하다보니 익숙해지고 UI꾸미는 맛도 있고 어플로 결과가 나오다 보니 뭔가 더 실용성 있어보여
> 다른 언어보다 더 재밌었다.원래 새로운 기술이나 라이브러리에 두려움이 있어 알고 있는 코드지식으로만 프로그램을 만들었었는데 이번에 새로운 것에 도전해보고 싶어 여러가지를 사용해봤다. 
> (사실상 별거아닌 JSON, XML, 그 외 라이브러리..) 별거 아닌 것들이지만 새로움을 사용함에 있어 어렵지 않다 느꼈고 역시 쓰는덴 이유가 있구나 할 정도로 너무 좋았다.
> 사실 깃허브도 이번에 처음 계정을 만들고 사용했다.. 이 것도 짱
> 이번을 계기로 새로운 기술이나 라이브러리에 관심이 엄청 생기며 공부하고 싶은 생각이 드는 프로젝트라 의미가 뜻 깊다. 유후~

## 참고 어플
> 당근마켓

> 카카오톡

> Lime

> GCooter
