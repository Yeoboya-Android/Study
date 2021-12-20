
|topic|author  |
|--|--|
| Activity, Fragment LifeCycle and Context | 남현웅 |

**Activity LifeCycle**
 - 
 - 안드로이드의 Activity는 마치 사람처럼 태어나고 죽기까지의 생명주기가 존재한다.
 Activity가 시작되고 완전히 종료되기까지의 주기 안에서 그 액티비티의 상태가 계속하여 바뀌게 된다.
 - 안드로이드 프레임워크에서는 **액티비티의 '상태가 변화'할 때마다 특정 동작을 수행할 수 있도록 여러 콜백 메소드를 제공**한다. 이를 **'Lifecycle Callback Method'** 라고 한다.
 
 ![Activity LifeCycle](https://t1.daumcdn.net/cfile/tistory/26767134590211090F)
 
### 1.  `onCreate()`
- 시스템이 액티비티를 생성할 때 실행되는 콜백 메서드이다. 
 - 이 메소드에는 액티비티 전체 수명 주기 동안 **딱 한 번만 동작되는 초기화 및 시작 로직을 실행**할 수 있다.
 - `onCreate()` 의 동작이 끝났다고 해서 액티비티가 종료되는 것이 아니고, 시스템은 `onStart()` 와 `onResume()` 을 연달아 호출하게 된다.
 ### * Tip
 `onCreate()` 는 `**savedInstanceState` 라는 파라미터를 수신하는데, 이는 액티비티의 이전 상태가 저장**된 `Bundle` 객체에 해당된다.  이를 통해 이전 상태를 복원하여 화면에 표시할 수 있다. 처음 생성된 액티비티인 경우 `null` 을 담고 있다.
 
 ### 2.  `onStart()`
 - 이 메소드가 호출되면 액티비티가 사용자에게 보여지고, Foreground Task로써 사용자와 상호작용할 수 있도록  준비한다.
- `onStart()`  는 매우 빠른 속도로 실행되고, 액티비티가 '**_RESUMED_**' 상태에 진입함과 동시에  `onResume()`  메소드를 호출하게 된다.

### 3.  `onResume()`
 - 포그라운드에 액티비티가 표시되고 앱이 사용자와 상호작용을 할 수 있는 상태가 된다. 
 - 어떤 방해 이벤트 및 인터럽트 (전화가 오거나 화면을 슬립하는 등 이벤트) 가 발생하여 사용자의 포커스가 없어지지 않는 이상, 이제 앱은 '**_RESUMED_**' 상태에 머무르게 된다.
- 방해 이벤트 및 인터럽트가 발생하면 액티비티는 '**_PAUSED_**'상태에 들어가게 되고, 시스템이 'onPause()' 콜백 메소드를 호출하게 된다.

### 4.  `onPause()`
- 사용자가 잠시 액티비티를 떠났을 때 (다른 액티비티에 포커스를 뒀을 때) 호출되는 콜백 메소드이다. 즉, 해당 액티비티가 **포그라운드에 있지 않게 되었다는 것을 의미** 한다.
- 예외적으로 한 화면에 두 앱을 동시에 구동할 수 있는 멀티 윈도우 모드에서는, 멀티 윈도우의 또다른 앱에 포커스를 두어도 해당 액티비티가 포그라운드에 있으면서 `onPause()` 가 호출된다
- 보통 `onPause()` 에서는, 액티비티가 포그라운드에 없을 동안 **계속 실행되어서는 안 되지만 언젠가 다시 시작할 작업을 일시중지하는 작업**을 수행한다.
#### 액티비티가 '**_PAUSED_**' 상태에 진입하게 되는 여러가지 루트들
- 앱이 실행되던 중  **방해 이벤트 및 인터럽트가 발생**한 경우 (전화가 갑자기 오는 경우)
-   **멀티 윈도우 상 다른 앱에 포커스**를 두는 경우
-   권한 요청 다이얼로그

### 5.  `onStop()`

- 액티비티가 사용자에게 더 이상 표시되지 않으면 '**_STOPPED_**' 상태에 진입하고 시스템이  `onStop()`  콜백 메소드를 호출한다. 즉,  새로 시작된 액티비티가 화면 전체를 차지할 경우에 해당된다. 혹은 액티비티의 실행이 완료되어 종료될 시점에  `onStop()`  를 호출할 수도 있다.

- 이 메소드에서는  필요하지 않은 리소스를 해제하거나 조정해야 한다. 예를 들어  애니메이션을 일시중지하거나, GPS 사용 시 배터리를 아끼기 위해  **위치 인식 정확도를 '세밀한 위치' 에서 '대략적인 위치' 로 전환**할 수 있다.
- 만약 사용자가 다시 액티비티로 돌아오게 되면, '**_STOPPED_**' 상태에서 다시 시작되어 `onRestart()` → `onStart()` → `onResume()` 이 연달아 호출되며 '**_RESUMED_**' 상태로 변화하여 다시 포그라운드 액티비티로써의 태스크를 시작하며 사용자와 상호작용을 시작한다.

### * Tip
액티비티가 '**_STOPPED_**' 상태에 들어가더라도 **액티비티 객체는 메모리 안에 머무른다**. 그런데 시스템이 **더 우선순위가 높은 프로세스**를 위해 **메모리를 확보**해야하는 경우, **이 액티비티를 메모리 상에서 죽이게 된다**. 하지만 그럼에도 **Bundle** 에 View 객체 상태를 그대로 저장해두고, 사용자가 이 액티비티로 다시 돌아오게 되면 이를 기반으로 상태를 복원한다. 

### 6.  `onDestroy()`

- 액티비티가  **완전히 소멸되기 전에 이 콜백 메소드가 호출**된다. 아래와 같은 경우 액티비티가 완전히 소멸된다.

1.  `finish()`  가 호출되거나  **사용자가 앱을 종료하여 액티비티가 종료**되는 경우
2.  화면 구성이 변경되어 (기기 회전 등)  **일시적으로 액티비티를 소멸**시키는 경우

- 액티비티가 종료되는 경우  `onDestroy()`  가  **마지막 라이프사이클 콜백 메소드가 된다.**  만약 위의 2번 사유로 인해 호출된거라면, 시스템이 즉시 새롭게 변경된 액티비티 인스턴스를 생성하여  `onCreate()`  를 호출한다.

### * Tip
만약 `onDestroy()` 가 호출되기 까지 **해제되지 않은 리소스가 있다면, 모두 여기서 해제**해줘야 한다. **Memory Leak** (메모리 누수) 의 위험이 있다.
Memory Leak이란 어플리케이션에서 사용하지 않는 객체가 사용중인 어떠한 객체를 참조중이라서 사용되지 않는 객체를 GC(Garbage Collector)가 결국 사용되지 않는 객체의 할당된 메모리를 되찾아올 수 없는 현상이다.


**Fragment LifeCycle**
-
- Activity의 생명주기에 따른 콜백 메서드와 비교해봤을때 생성 시에는 onViewCreated() - onViewStateRestored() 가 추가로 있고, 파괴시에는 onSaveInstanceState() - onDestroyView() 가 추가됨.
- Activity도 마찬가지지만 기본적으로 Lifecycle은 위에서 아래 방향으로 진행된다. 
- Fragment 가 백스택에 최상단으로 올라왔을 경우에는 생명주기가 **CREATED - STARTED - RESUMED** 순으로 진행되고, 반대로 백스택에서 pop 됐을 경우에는 **RESUMED - STARTED - CREATED - DESTROYED** 순으로 진행됩니다.

![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/cDyVCU/btq9CtTEtoA/kpOuUqYRAw8aVmbyKT7jpk/img.png)

### **1. onCreate()**

- Fragment 만 CREATED 가 된 상황이다.

- FragmentManager 에 add 됐을 때 도달하며 onCreate() 콜백함수를 호출한다. 주의할 점은 onCreate() 이전에 onAttach() 가 먼저 호출된다는 것이다. 

- 이 시점에는 아직 Fragment View 가 생성되지 않았기 때문에 Fragment 의 View 와 관련된 작업을 두기에 적절하지 않다.

- onCreate() 콜백 시점에는 Bundle 타입으로 savedInstanceState 파라미터가 함께 제공되는데, 이는 onSaveInstanceState() 콜백 함수에 의에 저장된 Bundle 값이다. 여기서 또 알아야할 부분은 savedInstanceState 파라미터는  **프래그먼트가 처음 생성 됐을 때만 null**  로 넘어오며, onSaveInstanceState() 함수를 재정의하지 않았더라도 그 이후 재생성부터는 non-null 값으로 넘어온다.

### **2. onCreateView(), onViewCreated()**

- onCreate() 이후에는 onCreateView() 와 onViewCreated() 콜백함수가 이어서 호출된다. onCreateView() 의 반환값으로 정상적인 Fragment View 객체를 제공했을 때만 Fragment View 의 Lifecycle 이 생성된다.

- onCreateView() 를 재정의 하여 Fragment View 를 직접 생성하고 inflate 할 수 있지만, LayoutId 를 받는 Fragment 의 생성자를 사용하여 해당 리소스 아이디 값을 통해 onCreateView() 재정의 없이도 Fragment View 를 생성할 수도 있다.

- onCreateView() 를 통해 반환된 View 객체는 onViewCreated() 의 파라미터로 전달되는데, 이 시점부터는 Fragment View 의 Lifecycle 이 INITIALIZED 상태로 업데이트 됐기 때문에  View 의 초기값을 설정해주거나 LiveData 옵저빙, RecyclerView 또는 ViewPager2 에 사용될 Adapter 세팅 등은 onViewCreated() 에서 해주는 것이 적절하다.

### **3. onViewStateRestored()**

- 이거는 처음봤음. ㅎㅎ;

- onViewStateRestored() 함수는 저장해둔 모든 state 값이 Fragment 의 View 계층구조에 복원 됐을 때 호출된다. 따라서 여기서부터는 체크박스 위젯이 현재 체크 되어있는지 등 각 뷰의 상태값을 체크할 수 있다.

- ViewLifecycleOwner 는 이때 INITIALIZED 상태에서 CREATED 상태로 변경됐음을 알리ㄴ다.

### **4. onStart()**

- Fragment 가 사용자에게 보여질 수 있을 때 호출된다. 
- 이는 주로 Fragment 가 attach 되어있는 Activity 의 onStart() 시점과 유사하다. 
- 이 시점부터는 Fragment 의 child FragmentManager 통해 FragmentTransaction 을 안전하게 수행할 수 있다.
- 
### **5. onResume()**

- Fragment 가 보이는 상태에서 모든 Animator 와 Transition 효과가 종료되고, 프래그먼트가 사용자와 상호작용할 수 있을 때 onResume() 콜백이 호출된다. onStart() 와 마찬가지로 주로 Activity 의 onResume() 시점과 유사하다.

### *TIP
Resumed 상태가 됐다는 것은 사용자가 프래그먼트와 상호작용 하기에 적절한 상태가 됐다고 했는데, 이는 반대로  **onResume() 이 호출되지 않은 시점에서는 입력을 시도하거나 포커스를 설정하는 등의 작업을 임의로 하면 안된다는 것을 의미**한다.

### **6. onPause()**

- 사용자가 Fragment 를 떠나기 시작했지만 Fragment 는 여전히 visible 일 때 onPause() 가 호출e된다.

**여기서 눈 여겨 볼 점은 Fragment 와 View 의 Lifecycle 이 PAUSED 가 아닌 STARTED 가 된다는 점이다.**

- 엄밀히 따지면  [Lifecycle](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State)  에 PAUSE 와 STOP 에 해당하는 상태가 없다.

### **7. onStop()**

- Fragment 가 더이상 화면에 보여지지 않게 되면 Fragment 와 View 의 Lifecycle 은 CREATED 상태가 되고, onStop() 콜백 함수가 호출되게 된다. 이 상태는 부모 액티비티나 프래그먼트가 중단됐을 때 뿐만 아니라, 부모 액티비티나 프래그먼트의 상태가 저장될 때도 호출된다.

- Fragment 의 onStop() 의 경우 주의해야할 점이 있는데,  **API 28 버전을 기점으로 onSaveInstanceState() 함수와 onStop() 함수 호출 순서가 달라졌다.**  아래 사진에서 보다시피 API 28 버전부터 onStop() 이 onSaveInstanceState() 함수보다 먼저 호출됨으로써 onStop() 이 FragmentTransaction 을 안전하게 수행할 수 있는 마지막 지점이 되었다.
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/bC4Zkm/btq9DwbxrgQ/Il287fhextuJbiCRZtZde1/img.png)

### **8. onDestroyView()**

- 모든 exit animation 과 transition 이 완료되고, Fragment 가 화면으로부터 벗어났을 경우 Fragment View 의 Lifecycle 은 DESTROYED 가 되고 onDestroy() 가 호출된다.

- 이 시점부터는 getViewLifecycleOwnerLiveData() 의 리턴값으로 null 이 반환된다.

### *TIP
**해당 시점에서는 가비지 컬렉터에 의해 수거될 수 있도록 Fragment View 에 대한 모든 참조가 제거되어야 한다.**

### **9. onDestroy()**

- Fragment 가 제거되거나 FragmentManager 가 destroy 됐을 경우, 프래그먼트의 Lifecycle 은 DESTROYED 상태가 되고, onDestroy() 콜백 함수가 호출된다. 해당 지점은 Fragment Lifecycle 의 끝.

- onAttach() 가 onCreate() 이전에 호출 됐던 것처럼 onDetach() 또한 onDestroy() 이후에 호출된다.

**Context**
-
- 안드로이드에서 가장 흔하게 인자로 사용되는 타입은 Context이다. Context를 잘못 사용하면 앱이 비정상 종료되거나 Memory Leak이 발생하기도 한다.

**Context란?**

 - 어플리케이션의 현재 상태를 나타낸다.
- 액티비티와 어플리케이션의 정보를 얻기 위해 사용할 수 있다.
- 리소스, 데이터베이스 , shared preference 등에 접근하기 위해 사용할 수 있다.
- 액티비티와 어플리케이션 클래스는 Context 클래스를 확장한 클래스이다.

Context는 크게 Application Context, Activity Context로 나뉜다.

**Application Context**

- Application Context는 싱글턴 인스턴스이며, 액티비티에서  getApplicationContext()를 통해 접근할 수 있다. 이 Context는 Application Lifecycle에 묶여있으며, 현재 Context가 종료된 이후에도 Context가 필요한 작업이나 Activity Scope를 벗어난 Context가 필요한 작업에 적합하다.

**Activity Context**
- Activity Context는 activity 내에서 유효한 Context이다. 이 Context는 Activity Lifecycle과 연결되어 있다. Activity Context는 Activity와 함께 소멸해야 하는 경우에 사용한다. 예를 들어, Activity와 Lifecycle이 같은 오브젝트를 생성해야 할 때 Activity Context를 사용할 수 있다. 

**앱의 계층구조**
![enter image description here](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https://blog.kakaocdn.net/dn/WVBRX/btqWxfzYqKQ/IzwkE5gnxovo8rKrv7z02K/img.jpg)
-   Application Context는 MyApplication, MainActivity1, MainActivity2 모두에서 사용할 수 있다.
-   MainActivity1의 Context는 MainActivity1에서만 사용할 수 있다.
-   MainActivity2의 Context는 MainActivity2에서만 사용할 수 있다.

### *Tip
- 그때 그때 상황에 맞는 Context를 사용해야 메모리 누수가 발생하는 것을 방지할 수 있다.
- 싱글톤의 경우에는 항상 Applicacion Context를 전달하는게 맞다.
- 언제든 Activity를 사용할 때, Toast, Dialog 등의 UI operation에서 Context가 필요하다면 이때 Activity Context를 사용해야 한다.

### _getApplicationContext()_를 쓰면 안 되는 경우
-   Application Context는 Activity Context가 제공하는 기능 전체를 제공하지 않는다. 특히 GUI와 관련된 Context 조작은 실패할 확률이 높다.
-   Application Context가 사용자 호출로 생성된, clean up 되지 않은 객체를 가지고 있다면 메모리 누수가 발생할 수 있다. Activity 객체는 가비지 콜렉션이 가능하지만 Application 오브젝트는 프로세스가 살아있는 동안 남아있다.

테스트 커밋용

