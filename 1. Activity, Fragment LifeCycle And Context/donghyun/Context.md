---
topic: Activity, Fragment LifeCycle And Context
description: Context
author: 김동현
---

# Context

아래는 Android 공식 문서에 나와 있는 Context에 대한 설명입니다.

![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/ContextDescription.png?raw=true)

Context란 단어 그대로 맥락(Context)을 의미 즉, 현재의 상태를 나타낸다,

- Application의 현재 상태를 Context를 통해 표현한다.
- Activity 그리고 Application의 정보를 Context를 통해 얻을 수 있다.
- Context를 활용하여 Resource, Database, SharedPrefernces 등의 시스템 자원을 얻을 수 있다.
- Application과 Activity 클래스 둘다 Context를 확장한 서브클래스다.


## 안드로이드에서 Context 2가지

Application Context는 하나의 애플리케이션이 실행되어 종료될 때까지 동일한 객체인 반면,

Activity Context는 액티비티가 onDestroy() 된 경우 사라질 수 있는 객체이다. 이를 참고하여 목적에 맞도록 알맞은 종류의 context를 참조해야 한다.

![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/Context.png?raw=true)


MyApplication이나 MainActivity나 다 같은 Context지만 Scope가 상이하고, Activity의 경우 Application Context를 참조할 수 있다.

 
## 어떤 Context를 언제 사용해야 할까?
Application을 확장한 MyApplication과 여러 Activity 클래스들이 있다고 가정하자. 또한 앱에서 데이터베이스를 관장하는 AppDatabase라는 클래스가 싱글톤으로 존재한다고 가정하자. AppDatabase는 아마 초기화시 Context를 필요로 할것이다. 이 때 어떤 Context를 참조해야 할까?

 

정답은 바로 Application Context다. 왜냐면 Activity Context를 전달 한다고 가정했을 때, Activity는 생명주기에 따라 어느시점에 분명히 소멸(Destryed)되지만, AppDatabase는 싱글톤이므로 해당 Activity Context를 지속적으로 참조하고 있게되어 메모리 누수를 일으킨다. 그러므로 싱글턴의 경우에는 애플리케이션 컨택스트를 사용합니다.

 

또한, Application Context는 Activity Context가 지원하는 모든 것을 지원하지 않기 때문에, GUI와 관련된 모든것에 대해서 Application Context는 정상적으로 동작하지 않을 수 있다. 그렇기 때문에 무조건 Application Context를 사용하는 것은 옳지 않다. 예를 들면 Dialog와 같은 객체는 Activity Context를 필요로 한다.

단, Toast는 applicationContext를 이용해서 가능하다. GUI 작업중 유일하게 가능한 작업입니다.


정리하면,,,

Toast 메시지, Dialog 등과 같이 UI의 조작이 필요한 경우에는 Activity Context를 사용해야 합니다. 만약 불필요하게 Application Context를 사용한다면 메모리 누수가 발생할 것 입니다. Application Context는 사용할 객체가 Application에서 초기화되거나 Singleton인 경우에 사용합니다. 
