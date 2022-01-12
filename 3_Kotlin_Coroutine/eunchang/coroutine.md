# Coroutine 
코루틴은 비동기적인 작업을 쉽게 처리할 수 있게 도와주는 설계 패턴이다. 
기존 비동기 프로그래밍에서 네트워크 작업 등 장기 실행 작업을 위해 새로운 스레드를 생성해서 작업했다면, 
코루틴에서는 새로운 스레드가 아닌 기존 스레드에서 여러 코루틴을 실행하여 각각의 작업을 실행한다.
  
스레드를 차단하지 않고 정지의 개념으로 비동기 작업을 수행하여 메모리 등의 자원적인 부분에서 보다 높은 효율을 낼 수 있다.
또한 RxJava와 같은 다른 비동기 프로그래밍 방식보다 간단한 코드로 구현할 수 있어 초기 진입 장벽이 낮다고 한다.

하지만 아직 메모리 효율이 좋고 코드를 작성하기 쉽다는 부분에서 피부에 크게 와닿는 부분이 없어,
다른 비동기 프로그래밍 방식과 비교하여 코루틴에서 얻는 이점을 중심으로 학습 할 계획이다.
우선 개념과 사용법을 확실히 알아두어야 할 것.


## Scope 
코루틴이 실행되는 범위의 개념으로 범위를 지정해두고 해당 범위 내에서만 해당 작업을 수행하도록 한다.
GlobalScope의 경우엔 앱의 생명주기에 관계가 있기 때문에 앱 시작부터 앱 종료까지 동작하는 코루틴에 용이하다.
GlobalScope는 딱 그러할 필요가 있는 경우에만 사용하고, 
되도록이면 CoroutineScope를 사용하여 개발자가 의도할 수 있는 범위 내에서만 사용하는 걸 추천한다고 한다.
LifecycleScope나 ViewModelScope을 사용하면 해당 Lifecycle이 끝나거나 ViewModel이 제거될 때 실행중이던 작업이 자동으로 취소된다.
올바른 Scope를 사용하지 않는다면 취소해야할 작업들이 제대로 취소되지 않아 메모리 릭으로 이어질 수 있다.

## Dispatchers
Dispatcher를 사용하여 코루틴이 실행되는 스레드를 지정할 수 있다.
안드로이드 디벨로퍼 문서에서는 다음과같이 Dispatchers를 안내하고 있다.
>Dispatchers.Main - 이 디스패처를 사용하여 기본 Android 스레드에서 코루틴을 실행합니다. 이 디스패처는 UI와 상호작용하고 빠른 작업을 실행하기 위해서만 사용해야 합니다. 예를 들어 suspend 함수를 호출하고 Android UI 프레임워크 작업을 실행하며 LiveData 객체를 업데이트합니다.
 Dispatchers.IO - 이 디스패처는 기본 스레드 외부에서 디스크 또는 네트워크 I/O를 실행하도록 최적화되어 있습니다. 예를 들어 회의실 구성요소를 사용하고 파일에서 읽거나 파일에 쓰며 네트워크 작업을 실행합니다.
 Dispatchers.Default - 이 디스패처는 CPU를 많이 사용하는 작업을 기본 스레드 외부에서 실행하도록 최적화되어 있습니다. 예를 들어 목록을 정렬하고 JSON을 파싱합니다.
```
CoroutineScope(Dispatchers.IO).launch {
    work1()
    ...
}
```

## CoroutineBuilder
코루틴이 실행될 Scope와 실행시킬 Dispatchers를 지정하여 CoroutineBuilder를 이용하여 시작할 수 있다.
주요 CoroutineBuilder launch, async, withContext 가 있다.
launch는 값을 반환받을 필요가 없는 작업을 수행하고, async는 await() 함수로 작업이 완료되는 것을 기다렸다가 결과값을 반환한다는 차이가 있다.

## Job, Deferred
launch를 사용해 코루틴을 시작할 때 코루틴 작업이 Job 인스턴스로 반환되어 이를 제어할 수 있다.
async는 Deferred 인스턴스로 반환이 된다.

```
    val myJob = viewModelScope.launch(Dispatchers.Default) {
        work1()
        work2()
        ...
    }

    myJob.cancel()
```

```
val myDeferred = CoroutineScope(Dispatchers.Main).async {
    ...
}

val value = myDeferred.await() 

```

## 테스트

```

private fun coroutineTest() {
    Log.d("qwe123", "coroutineTest() start")
    viewModelScope.launch(Dispatchers.Default) {
        test1()
        test2()
    }
    Log.d("qwe123", "coroutineTest() end")
}

private suspend fun test1() {
    delay(2000)
    Log.i("qwe123", "test1()")
}

private suspend fun test2() {
    delay(2000)
    Log.i("qwe123", "test2()")
}

// 로그
23.602(시간): coroutineTest() start
23.623: coroutineTest() end
25.627: test1()
27.629: test2()

```