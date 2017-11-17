# 위치 기반 서비스

---
## Location Service 개요
* Location APIs
    - Android framework location APIs ([android.location](https://developer.android.com/reference/android/location/package-summary.html))
    - [Google Play Services location APIs](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary) (**추천**)
* 이번 시간에 다룰 내용 (Google Play Service 이용)
    - [Google Play Services 설정](#1)
    - [위치 접근 권한 얻기](#2)
    - [마지막으로 알려진 위치 얻기](#3)
    - [주기적인 위치 업데이트 시작](#4)/[중단](#5) 하기
    - [주소 찾기](#6)

[출처: https://developer.android.com/training/location/index.html]

<a name="1"> </a>
---
##1. Google Play Services 설정
* Google Play Services SDK 다운로드 및 설치
    - Android Studio에서 Tools>Android>SDK Manager 이용
        + **SDK Tools** 탭에서
        + **Google Play services** 선택 후 OK
     <img src="figures/google-play-service.png">
     
* 프로젝트에 라이브러리 추가
    1. build.gradle (Module:app) 파일 오픈
    2. 새로운 빌드 규칙 추가
    
		```java
		dependencies {
		      ...
		      compile 'com.google.android.gms:play-services-location:11.0.4'
		}
		```
    3. 툴바에서 "Sync Project with Graddle Files(<img src="images/sync.png">)" 또는 Sync Now 클릭



.footnote[출처: https://developers.google.com/android/guides/setup]

<a name="2"> </a>
---
##2. 위치 접근에 필요한 권한 얻기
* 위치 접근에 필요한 권한
    - ACCESS\_COARSE\_LOCATION : 대략적 위치 접근 허용 (도시의 블럭 단위)
    - ACCESS\_FINE\_LOCATION : 정밀한 위치 접근 허용
* Android Manifest 파일에서 권한 설정

	```xml
	<manifest ...>
	    ...
	*   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
	</manifest>
	```

	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/AndroidManifest.xml#L5


* Android 6.0 (API level 23) 이상부터는
    - 앱 실행 중에 사용하려는 권한(permission)을 반드시 요청
    	
    	[참고자료: https://developer.android.com/training/permissions/requesting.html]

	```java
	...
		if (!checkLocationPermissions()) {
                requestLocationPermissions(REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
        } else
                // 권한 획득 후 수행할 일: 예,getLastLocation();
    ...
```

	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L60-L63

	```java	                    
	    private boolean checkLocationPermissions() {
	        int permissionState = ActivityCompat.checkSelfPermission(getApplicationContext(),
	                    Manifest.permission.ACCESS_FINE_LOCATION);
	        return permissionState == PackageManager.PERMISSION_GRANTED;
	    }
	
	    private void requestLocationPermissions(int requestCode) {
	        ActivityCompat.requestPermissions(
	                MainActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
	                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
	                requestCode    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
	        );
	    }
	```	  
	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L110-L122
	  
	```java    	
	    @Override
	    public void onRequestPermissionsResult(
	    										int requestCode, 
	    										String[] permissions, 
	    										int[] grantResults) {
	    										
	        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	
	        switch (requestCode) {
	            case MY_PERMISSION_REQUEST_LOCATION: {
	                if (grantResults.length > 0 
	                	&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	                     // 권한 획득 후 수행할 일: 예, getLastLocation(); 
	                } else {
	                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
	                }
	            }
	        }
	    }
	```
	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L126-L146

<a name="3"> </a>
---
##3. 마지막으로 알려진 위치 얻기
* Fused Location Provider
    - Google Play Services의 location API중의 하나
    - 디바이스의 배터리 사용을 최적화
    - 간단한 API 제공
        + 명시적인 위치 제공자 지정 없이, 상위 수준 요구사항 (높은 정확도, 저전력 등) 명세
        
    ```java
    private FusedLocationProviderClient mFusedLocationClient;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //...
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //...
    }
```

* Fused Location Provider의 [getLastLocation()](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#getLastLocation()) 메소드 이용
    1. [Task](https://developers.google.com/android/reference/com/google/android/gms/tasks/Task) 객체를 반환 
    2. Task가 성공적으로 완료 후 호출되는 [OnSuccessListener](https://developers.google.com/android/reference/com/google/android/gms/tasks/OnSuccessListener) 객체를 등록 
    3. onSuccess() 메소드를 통해 디바이스에 마지막으로 알려진 위치를 [Location](https://developer.android.com/reference/android/location/Location.html) 객체 (위도, 경도, 정확도, 고도 값 등을 얻을 수 있음)로 받음
    	- 위치가 가용하지 않으면 null값이 반환될 수 있음



	```java
	    @SuppressWarnings("MissingPermission")
	    private void getLastLocation() {
	        Task task = mFusedLocationClient.getLastLocation();       // Task<Location> 객체 반환
	        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
	                    @Override
	                    public void onSuccess(Location location) {
	                        // Got last known location. In some rare situations this can be null.
	                        if (location != null) {
	                            mCurrentLocation = location;
	                            updateUI();
	                        } else
	                            Toast.makeText(getApplicationContext(), 
	                            	getString(R.string.no_location_detected), 
	                            	Toast.LENGTH_SHORT)
	                            	.show();
	                    }
	                });
	    }
```

	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L49-L62

<a name="4"> </a>
---
##4. 주기적인 위치 업데이트 시작하기
1. [위치 요청 (Location Request) 설정](#4.1)
2. [위치 업데이트 콜백 정의](#4.2)
3. [위치 업데이트 요청](#4.3)

	```java
	    private void startLocationUpdates() {
	        LocationRequest locRequest = new LocationRequest();
	        locRequest.setInterval(10000);
	        locRequest.setFastestInterval(5000);
	        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	        mLocationCallback = new LocationCallback() {
	            @Override
	            public void onLocationResult(LocationResult locationResult) {
	                super.onLocationResult(locationResult);
	
	                mLastLocation = locationResult.getLastLocation();
	                updateUI();
	            }
	        };
	
	        mFusedLocationClient.requestLocationUpdates(locRequest,
	                mLocationCallback,
	                null /* Looper */);
	    }
	
	```

	https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L198-L217

---
<a name="4.1"> </a>
### [4](#4).1 위치 요청 설정
* Fused Location Provider에 위치 요청을 위한 파라미터를 설정
    - setInterval(): 요구되는 위치 업데이트 간격 설정 (더 빠를 수도, 더 느릴 수도 있음)
    - setFastestInterval(): 위치 업데이트를 처리하는 가장 빠른 주기
    - setPriority(): 어떤 위치 소스를 사용할 지에 대한 힌트
        + PRIORITY\_BALANCED\_POWER\_ACCURACY: 대략적인 정밀도 (100m, 도시 블럭), 적은 전력 소비, Wifi와 기지국 위치 사용.
        + PRIORITY\_HIGH_ACCURACY: 가능한 가장 정밀한 위치 요청, GPS 사용
        + PRIORITY\_LOW_POWER: 도시 수준의 정밀도 (10 km), 훨씬 낮은 전원 소비
        + PRIORITY\_NO\_POWER : 전원 소비가 무시될 정도, 해당 앱이 위치 업데이트를 요청하지 않고, 다른 앱에 의해 요청된 위치를 수신.

	```java
	locRequest = new LocationRequest();
	
	locRequest.setInterval(10000); 		// 10초 보다 빠를 수도 느릴 수도 있음
	locRequest.setFastestInterval(5000);		// 5초 보다 더 빠를 순 없음
	locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	```
[주기적인 위치 업데이트 시작하기](#4)

---
<a name="4.2"> </a>
### [4](#4).2 위치 업데이트 콜백 정의
* Fused Location Provider는 위치 정보가 가용할 때, LocationCallback의 [onLocationResult](https://developers.google.com/android/reference/com/google/android/gms/location/LocationCallback.html#onLocationResult(com.google.android.gms.location.LocationResult))([LocationResult](https://developers.google.com/android/reference/com/google/android/gms/location/LocationResult) result) 콜백 메소드를 호출
	* LocationResult 객체로부터 가장 최근 위치 및 [Location](https://developer.android.com/reference/android/location/Location.html) 객체 리스트를 얻을 수 있음. 	

	```java
	    mLocationCallback = new LocationCallback() {
	        @Override
	        public void onLocationResult(LocationResult locationResult) {
	            for (Location location : locationResult.getLocations()) {
	                // Update UI with location data
	                // ...
	            }
	        };
	    };
	```

---
<a name="4.3"> </a>
### [4](#4).3 위치 업데이트 요청
* Fused Location Provider의 **[requestLocationUpdates()](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#requestLocationUpdates(com.google.android.gms.location.LocationRequest, com.google.android.gms.location.LocationCallback, android.os.Looper))** 메소드 호출
* [사전 조건] 
	* LocationRequest 객체 준비
	* LocationCallback 객체 준비

	```java
	    mFusedLocationClient.requestLocationUpdates(
	    			mLocationRequest,     // LocationRequest 객체
	            	mLocationCallback,	  // LocationCallback 객체
	            	null                  // looper
	            	);
	```

---
##5. 주기적인 위치 업데이트 중단하기
* 위치 업데이트 중단

	```java
	public void stopLocationUpdate() {
	   mFusedLocationClient.removeLocationUpdates(mLocationCallback);
	}
	```

<a name="6"> </a>
---
##6. 주소 찾기
###6.1 Geocoding

- 위치좌표를 주소로 변경하거나 주소를 위치좌표로 변경하는 것
* 특별한 Permission은 필요치 않음
* Geocoding 방법
    - Geocoder 객체 생성
        + Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        + 두번째 생성자 파라미터는 로케일 값으로, 특정 지역 및 언어 영역을 나타냄
    - [Geocoder](https://developer.android.com/reference/android/location/Geocoder.html) class의 아래 함수들을 이용
        + List &lt;[Address](https://developer.android.com/reference/android/location/Address.html)&gt; [**getFromLocation**](https://developer.android.com/reference/android/location/Geocoder.html#getFromLocation(double, double, int))(double latitude, double longitude, int maxResults);
        + List &lt;[Address](https://developer.android.com/reference/android/location/Address.html)&gt; [**getFromLocationName**](https://developer.android.com/reference/android/location/Geocoder.html#getFromLocationName(java.lang.String, int, double, double, double, double))(String locationName, int maxResults);


* [Address](https://developer.android.com/reference/android/location/Address.html) class:
    - getLatitude() : double type의 위도
    - getLongitude(): double type의 경도
    - getMaxAddressLineIndex() : 주소가 표시줄 수
    - getAddressLine(int index): 행별 주소 문자열
    - getLocality():  locality(서울특별시)
    - getFeatureName():  장소명(395-3)
    - getThoroughfare():  길이름(삼선동2가)
    - getCountryName(): 국가명(대한민국)
    - getPostalCode(): 우편번호 (136-792)

---
###6.2 위치로부터 주소 얻기 예제

```java
try {
  	Geocoder geocoder = new Geocoder(this, Locale.KOREA);
*       List<Address> addresses = geocoder.getFromLocation(
  					mCurrentLocation.getLatitude(),
  					mCurrentLocation.getLongitude(),1);
        if (addresses.size() >0) {
            Address address = addresses.get(0);
            mAddressTextView.setText(String.format("\n[%s]\n[%s]\n[%s]\n[%s]",
                          address.getFeatureName(),
                          address.getThoroughfare(),
                          address.getLocality(),
                          address.getCountryName()
                          ));
        }
} catch (IOException e) {
   ...
}
```

https://github.com/kwanulee/Android/blob/master/examples/LocationService/app/src/main/java/com/kwanwoo/android/locationservice/MainActivity.java#L226-L240

---
### 6.3 주소 이름으로부터 위치 얻기 예제

```java
try {
    Geocoder geocoder = new Geocoder(this, Locale.KOREA);
*   List<Address> addresses = geocoder.getFromLocationName(input,1);
    if (addresses.size() >0) {
        Address bestResult = (Address) addresses.get(0);

        mResultText.setText(String.format("[ %s , %s ]",
            bestResult.getLatitude(),
            bestResult.getLongitude()));
     }
} catch (IOException e) {
    Log.e(getClass().toString(),"Failed in using Geocoder.", e);
    return;
}
```