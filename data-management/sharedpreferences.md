
# 설정(SharedPreferences)

* **프로그램의 설정 정보** (사용자의 옵션 선택 사항 이나 프로그램의 구성 정보)를 영구적으로 저장하는 용도로 사용
* XML 포맷의 텍스트 파일에 **키-값 세트**로 정보를 저장.
* [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences.html?hl=ko) 클래스
    - 프레프런스의 데이터를 관리하는 클래스
    - 응용 프로그램 내의 액티비티 간에 공유하며, 한쪽 액티비티에서 수정 시 다른 액티비티에서도 수정된 값을 읽을 수 있다.
    - 응용 프로그램의 고유한 정보이므로 외부에서는 읽을 수 없다.


## SharedPreferences 객체 얻기
* SharedPreferences 객체를 얻는 2 가지 방법
    - [public SharedPreferences getSharedPreferences (String name, int mode)](https://developer.android.com/reference/android/content/Context.html?hl=ko#getSharedPreferences(java.lang.String, int))
        + name : 프레프런스를 저장할 XML 파일의 이름이다.
        + mode : 파일의 공유 모드
            - MODE_PRIVATE: 호출하는 액티비티 내에서만 읽기 쓰기가 가능
    - [public SharedPreferences getPreferences (int mode)](https://developer.android.com/reference/android/app/Activity.html?hl=ko#getPreferences(int))
    	+ Activity 클래스에 정의된 메소드 이므로, Activity 인스턴스를 통해 접근 가능
    	+ 생성한 액티비티 전용이므로 같은 패키지의 다른 액티비티는 읽을 수 없다.
    	+ 액티비티와 동일한 이름의 XML 파일 생성
		
		```java
    	SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		```

## 프레퍼런스의 데이터 읽기
* 프레퍼런스에 저장된 여러 타입의 정보를 SharedPreferences 객체의 다음 메서드를 이용하여 읽을 수 있다
    - [int getInt (String key, int defValue)](https://developer.android.com/reference/android/content/SharedPreferences.html?hl=ko#getInt(java.lang.String, int))
    - [String getString (String key, String defValue)](https://developer.android.com/reference/android/content/SharedPreferences.html?hl=ko#getString(java.lang.String, java.lang.String))
    - [boolean getBoolean (String key, boolean defValue)](https://developer.android.com/reference/android/content/SharedPreferences.html?hl=ko#getBoolean(java.lang.String,%20boolean))    
        + *key* 인수 : 데이터의 이름 지정
        + *defValue* 인수 : 값이 없을 때 적용할 디폴트 지정.


## 프레퍼런스에 데이터 저장하기
* 프레프런스는 키와 값의 쌍으로 데이터를 저장
    - 키는 정보의 이름이며 값은 정보의 실제값
* [SharedPreferences.Editor](https://developer.android.com/reference/android/content/SharedPreferences.Editor.html?hl=ko) 이용하여 프레프런스에 값을 저장
    - 데이터 저장 시 [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences.html?hl=ko)의 **[edit()](https://developer.android.com/reference/android/content/SharedPreferences.html?hl=ko#edit()) 메서드를 호출하여 [SharedPreferences.Editor](https://developer.android.com/reference/android/content/SharedPreferences.Editor.html?hl=ko) 객체를 먼저 얻음.**
    - **SharedPreferences.Editor** 객체에는 값을 저장하고 관리하는 메서드가 제공됨
        + SharedPreferences.Editor putInt(String key, int value)
        + SharedPreferences.Editor putBoolean(String key, int value)
        + SharedPreferences.Editor putString(String key, String value)
        + SharedPreferences.Editor remove(String key)
        + SharedPreferences.Editor clear()
        + Boolean commit()
    - Editor는 모든 변경을 모아 두었다가 **commit() 메소드를 통해 한꺼번에 적용**


## SharedPreferences Example

```java
public class MainActivity extends AppCompatActivity {
*   public static final String	PREFERENCES_GROUP = "MyPreference";
    public static final String 	PREFERENCES_ATTR = "TextInput";
*   SharedPreferences	setting;

    public void onCreate(Bundle savedInstanceState) {
        ... 생략 ...
*       setting = getSharedPreferences(PREFERENCES_GROUP, MODE_PRIVATE);
        final EditText textInput = (EditText) findViewById(R.id.textInput1);
*       textInput.setText(retrieveText());

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                String name = textInput. getText().toString();
*               saveText(name);
            }    
        });
    }

    private String retrieveText() {
        String initText="";
        if (setting.contains(PREFERENCES_ATTR)) {
*           initText = setting.getString(PREFERENCES_ATTR, "");
        }
        return initText;
    }

    private void saveText(String text) {
*       SharedPreferences.Editor editor = setting.edit();
*       editor.putString(PREFERENCES_ATTR, text);
*       editor.commit();
    }
}
```

[https://github.com/kwanu70/AndroidExamples/blob/master/chap7/SharedPreferenceTest/app/src/main/java/com/example/kwanwoo/sharedpreferencetest/MainActivity.java](https://github.com/kwanu70/AndroidExamples/blob/master/chap7/SharedPreferenceTest/app/src/main/java/com/example/kwanwoo/sharedpreferencetest/MainActivity.java)