# Context
- 애플리케이션 환경에 대한 글로벌 정보를 갖는 인터페이스
→  애플리케이션 환경에 대한 글로벌 정보 ?
애플리케이션의 resource(getPackaget(), getResource())나 안드로이드 시스템에서 제공하는 API(startActivity(), startService(), getSystemService())에 대한 정보를 뜻함
- 애플리케이션의 현재 상태를 나타낸다.
- 액티비티와 애플리케이션의 정보를 얻기 위해 사용할 수 있다.
- 리소스, 데이터베이스, shared preference 등에 접근하기 위해 사용할 수 있다.
- 액티비티와 애플리케이션 클래스는 Context 클래스를 확장한 클래스이다.

# Context가 사용되는 곳
- 일반적으로 뷰(Toast, Adapter, Inflaters), 액티비티 실행(Intents), 시스템 서비스 접근(SharedPreferences, ContentProviders)등에 사용

# Context는 크게 두 종류로 나뉜다!
1. Application Context
- 싱글턴 인스턴스
- 액티비티에서 getApplicationContext()를 통해 접근 간으
- 라이프사이클에 묶여있어 현재 Context가 종료된 이후에도 Context가 필요한 작업이나 액티비티 스코프를 벗어난 Context가 필요한 작업에 적합

- getApplicationContext()를 쓰면 안 되는 경우가 있다!!
	- Application Context는 Activity Context가 제공하는 기능 전체를 제공하지 않는다. 특히 GUI와 관련된 Context 조작은 실패할 확률이 높다. 예를 들어 Dialog를 띄우는 GUI 작업은 불가능 하다.(단 Toast는 GUI 작업 중 유일하게 가능)
	- Application Context가 사용자 호출로 생성된, clean up 되지 않은 객체를 가지고 있다면 메모리 누수가 발생할 수 있다.

2. Activity Context
- activity 내에서 유효한 Context
- 액티비티 라이프사이클과 연결되어 있다.
- 액티비티와 함께 소멸해야 하는 경우에 사용한다.

# 언제, 어떤 Context를 사용해야 할까?
1. Application Context
- 애플리케이션 내에 싱글톤 객체를 만드려고 하는데 이 객체가 Context를 필요로 할 때, Application Context를 사용하면 된다. 만약 Activity Context를 넘겨주게 되면 액티비티에 대한 참조를 메모리에 남겨두며 GC(Garbage Collected)가 진행되지 않아 메모리 누수가 발생할 것이다.
- 애플리케이션 전역에서 사용할 어떤 라이브러리를 MainActivity 에서 초기화 할 때, Application Context를 사용해야 한다. 만약 Activity Context를 넘겨주게 되면, MainActivity에 대한 참조가 메모리 상에서 GC 되지 않아 메모리 누수가 발생할 수 있다.
-> 오랫동안 지속되거나 앱 전역(싱글톤 등)에서 사용될 경우에 사용!

2. Activity Context
- 액티비티와 라이프사이클이 같은 오브젝트를 생성해야 할 때
- Toast, Dialog 등의 UI operation에서 Context가 필요할 때

# Context UseCase
![ex_screenshot](./images/context_usecase.png)

## getContext() vs requireContext()
- getContext()는 Context가 호스트에 붙어있지 않을 때 null을 반환한다
- requireContext()는 getContext()에서 반환된 context가 null인 경우 IllegalStateException이 발생한다.
- Fragment가 Activity에 attach 되지 않은 경우 등의 예외가 발생할 수 있으므로 Fragment.getContext()가 항상 NonNull인 것은 아니다. 따라서 requireContext()를 통해 Context가 Null이 아님을 보장할 수 있다.

## 참조문헌 References
developer.android.com/reference/android/content/Context
blog.mindorks.com/understanding-context-in-android-application-330913e32514
https://roomedia.tistory.com/entry/Android-Context%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C
https://velog.io/@haero_kim/Android-Context-%EB%84%88-%EB%8C%80%EC%B2%B4-%EB%AD%90%EC%95%BC
https://4z7l.github.io/2020/11/22/android-getcontext-requirecontext.html
https://developer.android.com/kotlin/common-patterns?hl=ko#android
https://parkho79.tistory.com/172
https://velog.io/@hanna2100/%EB%B2%88%EC%97%AD-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C%EC%9D%98-Context%EC%99%80-%EB%A9%94%EB%AA%A8%EB%A6%AC%EB%88%84%EC%88%98
https://sodocumentation.net/android/topic/2687/memory-leaks