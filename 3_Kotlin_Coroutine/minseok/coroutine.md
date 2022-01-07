---
topic: Kotlin Coroutine
author: 김민석
---

# Coroutine

[[원문 보러가기]](https://ko.wikipedia.org/wiki/%EC%BD%94%EB%A3%A8%ED%8B%B4) 코루틴은 1958년도에 설계되었으며, 최초로 어셈블리어에 적용되었던 동시성 프로그래밍 기법이다.
이 말은 즉슨 코틀린 뿐만아니라 이미 여러 언어에서 코루틴을 사용하고 있으며 심지어 JavaScript에서도 제공되고있다.

각 언어마다 철학과 목적는 같지만 내부적으로는 프레임워크와 구현체가 다르기 때문에 조금씩 틀린다는 **학교의 점심**

# 안드로이드 공식문서 정의

Android의 Kotlin 코루틴

- 코루틴은 `비동기적`으로 실행되는 코드를 간소화하기 위해 Android에서 사용할 수 있는 동시 실행 설계 패턴이며, 코루틴은 Kotlin 1.3 버전에 추가되었으며 다른 언어에서 확립된 개념을 기반으로 되어있다.

기능
  - 경량 스레드: 코틀린 코루틴은 실행 중인 스레드를 `blocking` 하지 않고 코틀린의 `suspend` 키워드를 이용하여 단일 스레드에서 많은 코루틴을 실행할 수 있으며, `suspend` 는 많은 동시 작업을 지원하면서도 `blocking` 작업보다 메모리를 절약한다.
  - 메모리 누수 감소: [구조화된 동시 실행](https://kotlinlang.org/docs/coroutines-basics.html#structured-concurrency)을 사용하여 범위 내에서 작업을 실행한다.
  - 기본적으로 제공되는 취소: 실행중인 코루틴을 `cancel()` 요청할 수 있다.
  - Jetpack: 이미 많은 Android 라이브러리들이 코루틴을 지원하며, 구글의 공식 라이브러리(일명 AAC)에서도 적극 지원하고있다.

> 왜 경량스레드 일까?
>
> 아래 코드를 봐보자.
> ``` 
> CoroutineScope(Dispatchers.Main).launch {
>   val a = launch {
>      // 아무런 변화없이 그냥 일반적인 함수 호출
>   }
>   val b = launch(Dispatchers.IO) {
>      // 워커스레드에서 작동하기에 환경을 새로 구성하거나 변경사항이 생김
>   }
> }
> ```
> b는 부모 코루틴의 CoroutineContext는 같지만 Dispatcher에 변경이 생겨서 워커 스레드로 생성 및 변경이 생긴다. 때문에 Dispatcher가 변경될때 생기는 리소스가 발생한다.
>
> a의 경우는 부모의 CoroutineContext와 Dispatcher를 모두 그대로 받아서 사용했기 때문에 일반적인 함수 호출과 동일하게 수행된다. 코루틴이 경량 스레드인 이유는 바로 이 때문이다.

# 언제 써야할까?

> 코루틴을 공부하기전에 `non-blocking'과 'blocking'의 차이점과 동시성 프로그래밍에 대해서 숙지를 해놓으면 좋다.

- 뷰의 애니메이션, I/O 작업, 네트워크 통신, CPU 연산작업, JSON 파싱등에서 사용하며
이는 Dispatcher라는 기능을 사용해 스케줄러마다 목적이 다르다.

## Kotlin 코루틴을 사용하는 이유

> [코틀린 동시성 프로그래밍 책 中] 스레드를 만들고 관리하는 일은 여러 프로그래밍 언어에서 동시성 코드를 작성할 때 가장 어려운 부분 중 하나다. 언제 스레드를 만들 것인가를 아는 것 못지 않게 얼마나 많은 스레드를 만드는지를 아는 것도 중요하다. 또한 I/O 작업 전용 스레드와 CPU 바운드 작업을 처리하는 스레드가 있어야 하는데, 스레드를 통신/동기화하는 것은 그 자체로 어려운 일이다. 코틀린은 동시성 코드를 쉽게 구현할 수 있는 고급 함수와 기본형을 제공한다.

- 낮은 진입장벽
  - RxJava가 초반 진입장벽이 높은것에 비해 Kotlin 코루틴은 훨씬 접근하기 쉽다.
- 가독성
  - 쉽게말해 Callback Hell에서 벗어나는걸 뛰어넘어서 RxJava로 작성한 코드보다 간단하다.
- 본문 초반에 나와 있듯이 구글에서도 권장하고있다.

# Kotlin 코루틴 권장 사항 및 기초

`CoroutineContext`
- 코루틴이 돌아가는 환경이며, 안드로이드 컴포넌트의 `Context`처럼 생각할 수도 있다.

`Dispatchers`
- Main: 메인 스레드에서 동작하며 LiveData의 업데이트 및 UI와 상호작용을 위한 디스패처
- Default: CPU 연산처리에 적합하며 리스트 정렬, JSON 파싱처리 등에 쓰인다.
- IO: DB, Disk 처리 및 네트워크 작업에 적합

`CoroutineScope`
- `CoroutineContext`만 가지고 있는 인터페이스
  - ```
    interface CoroutineScope {
      val coroutineContext: CoroutineContext
    }
    ```
- 실제 구현체는 `ContextScope`이며 `internal` 접근 제한자로 내부 모듈에서만 참조 가능하다.
  - ```
    internal class ContextScope(context: CoroutineContext) : CoroutineScope {
      override val coroutineContext: CoroutineContext = context
      override fun toString(): String = "CoroutineScope(coroutineContext=$coroutineContext)"
    }
    ```
- `CoroutineScope()` 함수는 `ContextScope`를 생성하고 `CoroutineScope`로 반환한다.
  - ```
    fun CoroutineScope(context: CoroutineContext): CoroutineScope =
      ContextScope(if (context[Job] != null) context else context + Job())
    ```
  - `CoroutineScope()` 함수는 `camelCase`가 아닌 `PascalCase` 표기법이기 때문에 클래스로 오해하지말자
- 이외의 `scope builder`
  - coroutineScope()
  - mainScope()
  - withContext()
  - lifecycleScope()
  - viewModelScope()
  - 등등..

  > 공식 구글 Developer에서는 GlobalScope의 사용을 제한하고 있다.
  >
  > 해당 스코프는 object 클래스(싱글톤)이며 앱의 생명주기를 따르기에 GlobalScope의 사용이 남발할 경우 관리하기 힘든 상황이 생길 수 있다.(메모리 누수 발생 및 앱이 종료 되어야 작업이 종료되는 최악의 상황)

  `Coroutine Builder`
  - 대표적으로 `launch, async`가 있으며 이 외에 `actor, produce, broadcast`가 있다.(현재 Android에서는 사용하는걸 못봄)
    - launch: `Job`을 반환하며 람다로 결과를 통지 받는다.
    - async<T>: `Deferred<T>`를 반환받으며 `.await()` 을 호출하면 래핑된 값을 반환한다.