# Process & Thread

Thread 는 Process 보다 작은 단위의 실행 인스턴스로만 알고 있는데, 메모리 영역도 조금 다릅니다.

![](https://blog.kakaocdn.net/dn/kZPnP/btrp8iuXlkF/t4dnF3iBRXQkPSUjjcX14K/img.png)

Process 는 독립된 메모리 영역(Heap)을 할당받고 각 Thread도 독립된 메모리 영역(Stack)을 할당받습니다.

Thread 는 본질적으로 Process 내에 속해있기 때문에 Head 메모리 영역은 해당 Process 에 속한 모든 Thread 들이 공유할 수 있습니다.

Program 에 대한 Process 가 생성되면 Heap 영역과 하나의 Thread 와 하나의 Stack 영역을 갖게되고, **Thread 가 추가될때마다 그 수만큼의 Stack 이 추가됩니다.** Thread 가 100 개라면 전체 메모리에 100 개의 Stask 이 생성되는 것입니다.

## Thread

**- Context Switching**

![](https://blog.kakaocdn.net/dn/bP6N2h/btrqiqMrWsX/SPxRTwylq07kmAVhaEk941/img.png)

위 그림에서 작업들은 모두 Thread 단위이며 Thread A에서 작업 1을 수행중에 작업 2가 필요할때 비동기로 호출하게 됩니다. 작업 1은 진행중이던 작업을 멈추고(Blocked) 작업 2는 Thread B 에서 수행되며 이때 CPU 가 연산을 위해 바라보는 메모리 영역을 Thread A 에서 Thread B 로 전환하는 Context Switching 이 일어납니다.

작업 2가 완료되었을때 해당 결과값을 작업 1에 반환하게 되고, 동시에 수행할 작업 3과 작업 4는 각각 Thread C 와 Thread D 에 할당됩니다.

  

## Coroutine

![](https://blog.kakaocdn.net/dn/KG1Qu/btrp66hWQRd/CCbNbPKZjpObYrQrpIcxnk/img.png)

하나의 Thread 에서 다수의 Coroutine Object 들을 수행할 수도 있습니다. 위 그림에 따라 **작업 1과 작업 2의 전환에 있어 단일 Thread A 위에서 Coroutine Object 객체들만 교체함으로써 이뤄지기 때문에 OS 레벨의 Context Switching 은 필요없습니다.** 한 Thread 에 다수의 Coroutine 을 수행할 수 있음과 Context Switching 이 필요없기 떄문에 Coroutine 을 **Lightweight Thread** 로도 부릅니다.

  

다만 위 그림의 Thread A 와 Thread C 의 예처럼 다수의 스레드가 동시에 수행된다면 Concurrency 보장을 위해 두 Threads 간 Context Switching 은 수행되어야합니다. 따라서 Coroutine 을 사용할때에는 No Context Switching 이라는 장점을 최대한 활용하기 위해 다수의 Thread 를 사용하는 것보다 **단일 Thread 에서 여러 Coroutine Object 들을 실행하는 것이 좋습니다.**

  

--> **Coroutine 은 Thread 의 대안이 아니라 기존의 Thread 를 더 잘게 쪼개어 사용하기위한 개념**

Coroutine으로  작업의 단위를 Thread 가 아닌 Object 로 축소하면서 작업의 전환 및 다수 작업 수행에 굳이 다수의 Thread 를 필요로 하지 않게됩니다.

----------

## Flow

  
Flow는 콜드 비동기 스트림이다.

-> Flow{ } 빌더의 코드블럭은 플로우가  수집(Collect)되기 전까지 실행되지 않는다는 것을 의미

  
기존의 코루틴에서 채널이라는게 있는데 그건 핫 비동기 스트림이다.  
보통 코루틴은 RX와 비교가 많이 되는데 RX와 비교했을 때 콜드 스트림에 해당하는게 코루틴에 없었어서 완전히 대체하기 어려웠는데 Flow API로 인해 RX에서 코루틴을 사용하게 될 수있는 길이 된 것 같다.  
참고 -  [https://www.youtube.com/watch?v=D8rUDoYCZlo](https://www.youtube.com/watch?v=D8rUDoYCZlo)

  
이러한 Flow는 suspend function을 보완하는데 예를 들어 비동기 동작의 결과로 suspend function이 하나의 결과물을 반환한다면 flow를 이용하면 여러 개의 결과를 원하는 형식으로 받을 수 있다.

  

### 특징

비동기이며 코루틴에서만 동작 가능한 것은  suspend function  과 동일하다.

다른 점은 함수 앞에 suspend  를 붙이지 않아도 된다.

  

Coroutine의 Flow는 데이터 스트림이며, 코루틴 상에서 리액티브 프로그래밍 지원 하기 위한 구성요소이다.

  

### 리액티브 프로그래밍이란?

리액티브 프로그래밍이란 데이터가 변경 될 때 이벤트를 발생시켜서 데이터를 계속해서 전달하도록 하는 프로그래밍 방식

  

Coroutine Flow는 코루틴 상에서 리액티브 프로그래밍을 지원하기 위해 만들어진 구현체로 데이터 스트림을 구현하기 위해서는 Flow를 사용해야 한다,

![](https://blog.kakaocdn.net/dn/cmhc4v/btrqnkefqrr/NDUkLPFfdopSqGKYMNDSd1/img.png)

데이터 스트림은 아래 세가지로 구성

- Producer(생산자)

- Intermediary(중간 연산자)

- Consumer(소비자)

  

### - Producer(생산자)

![](https://blog.kakaocdn.net/dn/mG2zs/btrqnj7uhjY/7ZUN9wuWiyvLurJag6R8nK/img.png)

생산자는 데이터를 발행하는 역할

Flow에서의 Producer는 flow{ } 블록 내부에서의 emit()을 통해 데이터를 생성

  

안드로이드 상에서 생산자가 가져오는 데이터의 대표적인 DataSource

1. 서버의 데이터로 보통 REST API

2. 휴대폰 상의 DB(Local DataSource)

private fun getNumbers(): Flow<Int> = flow { 
	for (i in 1..100) { 
    	emit(i) 
        println("Emit $i") // 1 ~ 100 까지 방출
    } 
    emitAll((101 .. 200).asFlow()) // 101 ~ 200 까지 방출 
}

가장 간단한 방법으로 flow{ }를 통해서 생성이 가능

데이터 전달을 위해서 emit 함수를 사용

###   

### - Intermediary(중간 연산자)

![](https://blog.kakaocdn.net/dn/zjKTI/btrqnkL5PZC/zPhhX1kyoZvz75N8KVkB9k/img.png)

생산자가 데이터를 생성했으면 중간 연산자는 생성된 데이터를 수정하는 역할

예를 들어 A라는 객체로 이루어진 데이터를 발행했는데 B라는 객체 데이터가 필요한 경우 Flow에서 지원하는 중간 연산자를 이용해 A객체를 B객체로 바꿀 수 있다.

  

대표적인 중간 연산자는 map(데이터 변형), filter(데이터 필터링), onEach(모든 데이터마다 연산 수행) 등의 중간 연산자가 있다.

### - Consumer(소비자)

![](https://blog.kakaocdn.net/dn/tsNpL/btrqbE6HHNQ/QSqbE43I9gaAqn4bKYjfDk/img.png)

중간 연산자를 통해 수정된 데이터를 소비자로 전달한다. Flow에서는 collect를 이용해 전달된 데이터를 소비할 수 있다.

안드로이드 상에서 데이터의 소비자는 보통 UI 구성요소이다. UI는 데이터를 소비하여 데이터에 맞게 UI를 그려낸다.

  

### Flow 디스패처 분리 사용

flow의 context를 변경하는 유일한 방법은 업스트림 context를 변경하는 flowOn 연산자입니다.

fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) 
        log("Emitting $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default)

fun main() = runBlocking<Unit> {
    simple().collect { value ->
        log("Collected $value") 
    } 
}

결과

[DefaultDispatcher-worker-1] Emitting 1  
[main] Collected 1  
[DefaultDispatcher-worker-1] Emitting 2  
[main] Collected 2  
[DefaultDispatcher-worker-1] Emitting 3  
[main] Collected 3

  

  

참고

[https://www.youtube.com/watch?v=D8rUDoYCZlo](https://www.youtube.com/watch?v=D8rUDoYCZlo)[](https://www.youtube.com/watch?v=D8rUDoYCZlo)

[https://kotlinworld.com/175?category=973477](https://kotlinworld.com/175?category=973477)

[https://jaejong.tistory.com/67](https://jaejong.tistory.com/67)  - 플로우 연산

[https://timewizhan.tistory.com/entry/Coroutine-Flow-1%EB%B6%80﻿](https://timewizhan.tistory.com/entry/Coroutine-Flow-1%EB%B6%80