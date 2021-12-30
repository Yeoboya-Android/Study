# LifeCycleObserver
===========================

# Activity가 새로운 Activity를 생성시 LifeCycle과정
    - Main::OnPause -> Sub::OnCreate -> Sub::OnStart -> Sub::OnResume -> Main::OnStop
    - SubActivity가 완전히 생성된 다음에 Main Activity는 OnStop이벤트가 호출된다.
    
# 최상위 Activity 제거시 LifeCycle과정
    - Sub::OnPause -> Main::OnRestart -> Main::OnStart -> Main::OnResume -> Sub::OnStop -> Sub::OnDestroy
    - MainActivity(하위)가 완전히 화면에 보이게 된 후에 SubActivity(상위)가 제거된다.
    
# Activity 위에 Fragment가 띄워져 있는 상태에서 Activity 제거시 LifeCycle과정
    - Fragment::onStop -> Main::onStop -> Fragment::onDestroy -> Main::onDestroy
    - 상위에 보이는 프라그먼트의 이벤트가 호출 되고, Fragment를 감싸는 Activity의 LifeCycle이벤트가 호출된다.
    
# 올바른 라이프사이클 처리.
    - LifeCycle 인식 컴포넌트를 통하여 LifeCycle 상태 변경에 따라 작업을 수행한다.
      이러한 컴포넌트를 사용하면 Activity의 라이프사이클 이벤트에 따라서 동작해야 하는 부분을 각 클래스별로 라이프사이클을 감지하여 해당 처리를 할 수 있다.
      클래스의 독립성적인 측면에서 볼때 각 클래스에서 라이프사이클을 캣치하여 처리하는것이 옳다.
      또한 Activity의 onResume, onStop등에서 한꺼번에 처리해야 한다면 코드의 복잡성이 증가 할 것이다.
      
# 예제
    - 예제는 GPS정보를 얻어오는 예제이다.
      예제는 두가지 문제점을 처리 하는 방법에 대하여 코딩하였다.
      1. 예제에서 GPS정보를 얻어오는 동안 해당Fragment가 사라졌다면?
      2. GPS가 활성화 되지 않았다면 GPS설정 Activity로 이동 후 유저가 활성화 시킨 다음에 다시 예제App으로 돌아오는 경우 그다음 처리는 어떻게?
      
      이렇게 두가지 관점에서 작성한 예제이다.
      따라서 GPS요청 버튼을 누르면 Fragment는 바로 삭제되고, 3초 후에 GPS데이터를 불러오도록 하였다.
      
    ```
    class GpsHelper(val context : Context, val lifecycle: Lifecycle) : LifecycleObserver{
        public fun init(){
            m_googleApiClient = GoogleApiClient.Builder(context)
                                               .addApi(LocationServices.API)
                                               .addConnectionCallbacks(m_gpsChecker)
                                               .addOnConnectionFailedListener(m_gpsChecker)
                                               .build()
           
            lifecycle.addObserver(this)
        }
    }
    ```
    
    * GpsHelper를 생성시 lifecycle를 생성자에서 받는다. 그리고 GpsHelper를 LifecycleObserver상속 받게 하였다.
      그리고 초기화 lifecycle.addObserver(this)를 호출하여 GpsHelper 클래스가 라이프사이클를 감지 하도록 하였다.
      
    * @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) 어노테이션 사용
    ```
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public fun onResume(){
            if(false == m_isCheckingGps) return
        
            m_isCheckingGps = false
            doGps(false)
        }
    ```
    
      @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) 어노테이션을 사용하여 라이프사이클이 onResume 상태가 될때 호출되게 하였다.
      이곳에서 GPS설정이 활성화 되지 않아 GPS설정 Activity를 호출 후, 다시 테스트앱으로 복귀 했을때
      GPS설정을 다시 체크하고 우리의 위치 좌표를 얻어 올 수 있도록 하였다.
      
      
    * GPS를 얻어온 다음에 현재 해당 Fragment가 Forground상태인지 체크한다.
      체크 후 활성화 되어 있을때만 값을 콜백으로 넘겨준다.
      
      ```
      private fun onComplete(_location : Location, _isSuccess : Boolean){
          if(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)){
              m_googleApiClient.disconnect()
              m_callbackFunc?.invoke(_location.latitude, _location.longitude, _isSuccess)
          }
          else{
              m_googleApiClient.disconnect()
          }
      }
      ```     
    
# Application이 활성화 되어 있는지 체크방법
    - 가끔 앱 자체가 활성화 되어 있는지 체크해야 할 때가 있을 것이다.
      앱이 Forground에 있는지, Background에 있는지 체크 해야 할 때 필요할것이다.
      
      앱 프로세스의 라이프 사이클을 관리하려면 ProcessLifecycleOwner를 참고해야 한다.
      ```
      class MainApplication : MultiDexApplication()
      {
          override fun onCreate() {
              super.onCreate()
      
              ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
          }
      }
      ``` 
      
      어플리케이션 자체의 라이프사이클을 감지하게 설정 하였다.
              
       