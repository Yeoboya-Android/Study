# Android Lifecycle

## 1. Activity

* onCreate()
    - Activity가 생성될 때 호출 됨
    - 생명주기 동안 한 번만 호출 되기 때문에 최초 한 번만 수행해야할 작업에 적합

* onStart()
    - Activity가 사용자에게 노출 되기 직전에 호출 됨
    - 애니메이션 실행 등 시각적인 작업에 적합

* onResume()
    - 사용자와 Activity간에 상호작용이 시작될 때 호출 됨
    - 앱 내에 해당 Activity가 포커스를 받고 있는 동안 이 생명주기가 유지됨
    - 포커스를 잃게 되는 순간(백그라운드, 다른 액티비티가 올라오는 경우 등)에는 onPause()가 호출되고 다시 해당 Activity로 돌아왔을 때 다시 onResume()이 호출됨
    - Activity가 멈췄다가 다시 돌아와서 해야할 작업에 적합(데이터 및 뷰 갱신 등)

* onPause()
    - 다른 Activity를 실행하여 Activity를 덮거나, 백그라운드로 앱을 내리는 등 Activity가 포커스를 잃게 되면 호출 됨
    - 백그라운드에서 실행시켜야할 필요성이 없거나 리소스를 소모하는 기능들을 정지 시켜주는 작업이 필요
    - onPause()에서는 간단한 작업 실행에 적합하며, 데이터를 저장하거나 네트워크를 호출하는 등 비교적 무거운 작업에는 비적합

* onStop()
    - Activity가 사용자에게 더 이상 보이지 않을 때 호출 됨
    - 데이터를 저장하거나 네트워크를 호출하는 등 onPause() 에서 실행하는 작업에 비해 비교적 무거운 작업에 적합

* onDestroy()
    - Activity가 종료될 때 호출 됨
    - Activity가 최종적으로 종료되기 전에 수행해야할 작업에 적합
    - 메모리가 부족하면 onDestroy()가 호출이 되지 않고 Activity가 종료됨에 주의

* MainActivity 에서 SubActivity 호출 될 때 Lifecycle log
    - MainActivity.onCreate()
MainActivity.onStart()
MainActivity.onResume()
MainActivity.onPause()
SubActivity.onCreate()
SubActivity.onStart()
SubActivity.onResume()
MainActivity.onStop()

* SubActivity 에서 MainActivity 호출 될 때 Lifecycle log
    - SubActivity.onPause()
MainActivity.onRestart()
MainActivity.onStart()
MainActivity.onResume()
SubActivity.onStop()
SubActivity.onDestroy()


## 2. Fragment

* onCreate()
    - Fragment가 CREATED 될 때 호출 됨
    - View가 생성되지 않았기 때문에 View에 관련된 작업에는 부적합

* onCreateView()
    - View를 생성하고 인플레이트하는 작업을 수행
    - 생성된 View를 반환하여야 함

* onViewCreated()
    - onCreateView()에서 반환된 View를 전달 받을 때 호출 됨
    - View의 생명주기가 INITIALIZED 상태로 변경됨을 알림
    - View에 관련된 작업에 적합

* onViewStateRestored()
    - 저장해둔 모든 state 값이 Fragment 의 View 계층구조에 복원 됐을 때 호출 됨
    - View의 생명주기가 CREATE 상태로 변경됐음을 알림

* onStart()
    - Fragment 가 사용자에게 보여질 수 있을 때 호출 됨
    - Activity onStart()와 유사한 작업을 수행

* onResume()
    - 사용자와 Fragment간에 상호작용이 시작될 때 호출 됨
    - Activity onResume()과 유사한 작업을 수행

* onPause()
    - Fragment가 포커스를 잃게 되면 호출 됨
    - Activity onPause()과 유사한 작업을 수행

* onStop()
    - Fragment 가 더이상 화면에 보여지지 않게 되면 호출 됨
    - Activity onStart()와 유사한 작업을 수행

* onSaveInstanceState()
    - Fragment의 State를 저장할 때 호출 됨
    - 해당 함수가 호출된 후에 State Lose를 우려하여 트렌젝션에 commit() 할 수 없음
    - SDK 28버전 이상에서는 onStop() -> onSaveInstanceState() 순서가 되고 이전 버전에서는 반대로 onSaveInstanceState() -> onStop()가 됨

* onDestroyView()
    - 모든 exit animation 과 transition 이 완료되고, Fragment 가 화면으로부터 벗어났을 경우 호출 됨

* onDestroy()
    - Fragment 가 제거되거나 FragmentManager 가 destroy 됐을 경우 호출 됨
  
  
## 3. 화면 회전 관련 Lifecycle

### 화면 회전
세로모드에서 가로모드로, 혹은 가로모드에서 세로모드로 회전 시켜
Activity의 Orientation 값을 변경 될 때 Activity는 다시 시작된다.

```
onPaues() -> onStop() -> onDestroy() -> onCreate() -> onStart() -> onResume()
```

이렇게 onDestroy()가 불리고, 이내 다시 onCreate()가 불린다.
단순히 정보만 노출시키는 Activity라면 Lifecycle 보다 View 쪽에 신경이 더 써지게 되겠지만,
많은 데이터를 관리하고 처리하게 되는 Activity라면 Lifecycle에 더 신경이 쓰이는 게 맞다.
  
가장 부담스럽게 느끼는 문제는 onCreate()가 다시 호출 된다는 것이다.
이미 onCreate()는 한 번만 호출 된다는 가정하에 onCreate()부터 코드를 짜고 
이후 다른 모든 로직 또한 완벽하게 설계해 놓았는데,
이를 다시 갈아엎고 다른 방식으로 Activity의 로직을 수정할 수 밖에 없다.
또한 예상하지 못한 에러와 오류 때문에 신경쓸 것도 많고 피로는 늘 수 밖에 없다.
그냥 화면 회전을 사용하지 않고, 맘 편하게 Orientation 값을 portrait으로 고정시킨다.

### 구성 변경 직접 처리
맘 편하게 Orientation 값을 portrait 혹은 landscape로 고정시키면 좋겠지만,
사용자에게 앱을 제공하다 보면 가로,세로 모든 뷰를 사용자에게 제공해야만 할 때가 있다.
어느 한 쪽만 제공하여 사용자가 불편하다고 생각할 화면에서는 양 쪽 모두를 제공해주는 게 맞다고 생각한다.  
  
하지만 앞서 말한 것과 같이 화면 회전을 지원한다는 것은 여간 까다로운 작업이 아닐 수 없다.
그러나 Lifecycle을 고려하지 않아도 되고, 그 시간을 View 쪽 작업에 좀 더 할애할 수 있다면 
개발자의 육체적,정신적 자원을 좀 더 아낄 수 있을 것이다.
실제로 [구성 변경 처리](https://developer.android.com/guide/topics/resources/runtime-changes?hl=ko#HandlingTheChange)에서 보면 매니페스트에 android:configChanges 속성을 추가하는 것으로 이를 현실 시킬 수가 있다.
  
```
<activity 
  android:name=".MainActivity" 
  android:configChanges="keyboardHidden|orientation|screenSize"/>
```
  
해당 설정중 android:configChanges="orientation" 는 Orientation 값이 변경 될 때
Activity를 재시작하지 않고 구성 변경을 개발자가 직접 처리한다고 선언하는 속성이다.
Orientation이 변경 되어도 Lifecycle이 새로 돌아가지 않는다는 뜻이다.
  
또한 onConfigurationChanged()라는 함수로 구성이 변경 되었음을 콜백받을 수 있어
화면 회전에 대응할 작업을 수행할 수 있다.
Lifecycle을 신경 쓰지 않아도 화면 회전을 지원할 수 있다는 것이다.

### 화면 회전 2
그렇다면 configChanges 속성을 사용하여 Lifecycle를 배제하고 
가로모드 View에 대한 작업만 하면 되는 것인가라고 생각하였지만
[구성 변경 처리](https://developer.android.com/guide/topics/resources/runtime-changes?hl=ko#HandlingTheChange)에 설명 되어 있는 주의 문구가 마음에 걸렸다.
  
>주의: 구성 변경을 직접 처리하면 대체 리소스를 사용하는 것이 훨씬 더 까다로워질 수 있습니다. 시스템이 개발자 대신 자동으로 이를 적용해주지 않기 때문입니다. 이 기법은 구성 변경으로 인한 재시작을 반드시 피해야만 하는 경우 최후의 수단으로서만 고려해야 하며 대부분 애플리케이션에는 권장하지 않습니다.

View에 관련된 작업을 할 때 좀 더 신경 써줘야 할 부분이 늘어날 것이라고 추정된다.
서비스중인 클럽라이브에 실제로 화면 전환을 적용시켜 가로,세로 모드로 테스트를 해보았더니
방송방에서 일부 뷰가 의도대로 구성되어 있지 않음을 확인 할 수 있었다.
View에 고정 크기를 주어서 화면이 잘릴 수 있는 부분은 이해할 수 있었지만,
width, height를 계산하여 뷰를 구성하거나 하는 부분에서는 
일단 세로모드 기준으로 View가 생성이 된다면 가로모드로 돌렸을 때도 Activity를 재시작하지 않으니 
세로모드 기준으로 생성되었던 뷰가 그대로 남아 있어 마치 버그 처럼 보이고 있었다.
  
물론 이러한 것들은 Lifecycle을 대응하는 것보다 훨씬 쉬운 부분이다.
현재 클럽라이브의 방송방은 MainActivity에서 돌아가고 있다.
MainActivity의 Lifecycle 로직을 다시 손보는 것보다 
뷰 몇개를 고치는 게 보다 수월한 방법이 분명하긴 했다.
  
하지만 요지는, 덮어놓고 configChanges 속성으로 Lifecycle를 배제하는 것보다
두 방법 중 어느것이 나은 점인지 고려를 해봐야할 필요성이 있을 것 같다.
분명 Activity를 재시작하여 이점이 되는 Activity들도 존재하니 말이다.

## 4. Context
* 앱의 전역 정보에 대한 인터페이스. 리소스 및 시스템 적인 정보에 접근할 수 있다. 현재 앱의 포괄적인 정보를 제공한다.

### Application Context
* Application의 Lifecycle을 따르며, 싱글톤으로 애플리케이션이 살아있는 동안 변경되지 않는다.
* Activity Scope를 벗어난 작업을 할때 필요.

### Activity Context
* Activity의 Lifecycle을 따르며, Activity가 종료될 때 함께 사라진다


