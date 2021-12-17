# Android Lifecycle

## 1.1. Activity

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


## 1.2. Fragment

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