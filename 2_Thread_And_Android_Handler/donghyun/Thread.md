## 1. Thread

**1.1 안드로이드에서의 스레드**

스레드란 쉽게 말해 여러작업을 같이 하기 위한 기능이라고 생각하면 됩니다. 예를 들어 음악을 들으면서 SNS를 할 수 있는 것처럼 말입니다.  
  
어플리케이션은 성능향상을 위해 멀티 스레드를 많이 사용하지만, UI를 업데이트 할 때는 단일 스레드 모델이 적용됩니다. 멀티 스레드로 UI를 업데이트 하면 동일한 UI자원을 사용할 때 교착 상태, 경합 상태 등 여러 문제가 발생할 수 있기 때문입니다. 따라서 **UI업데이트는 메인스레드**에서만 **허용**됩니다.

**1.2 Thread, 왜 사용할까?**

메인스레드 만으로 구현하게 된다면, 사용자는 해당 작업이 끝날 때까지 멈춰있는 화면을 보고만 있어야합니다. 오랜 시간동안 UI관련 작업이 처리되지 못하면 결국 ANR 에러가 발생되게 됩니다. 그러면 어플리케이션은 정지가 됩니다.

안드로이드 3.0 버전부터 통신 클래스 내 전송이나 수신과 관련된 메소드는 메인 스레드에서 사용하지 못하도록 의도적으로 막아버렸습니다. EX) 메인 스레드에서 소켓클래스의 conntect() 메소드를 호출하여 'NetworkOnMainThreadException' 이 발생.

위와같은 네트워크 통신기능이나 데이터 검색, 빈번하게 사용하는 파일의 로드과 같이 시간이 걸리는 작업들은 사용자가 기다리지 않도록 하기 위하여 여분의 스레드를 사용합니다. (멀티스레드)

**1.3** **Thread VS. Runnable**

Thread와 Runnable 은 몇가지 차이점이 있습니다.  
Thread는 상속(Extends)을 받는 것이며 Runnable은 인터페이스로서 구현하는 것이 큰 차이점입니다.
![](https://blog.kakaocdn.net/dn/xs4D9/btrpJRRuebX/NlyMk0uHD2AmlghoEkD01K/img.png)

위와 같이 생성된 스레드는 start() 메서드를 호출하여 실행합니다.

![](https://blog.kakaocdn.net/dn/wNDYD/btrpu2GZEgG/Gg1DYoLznP857ABeAypx10/img.png)

Runnable 인터페이스를 이용한 예시로는

![](https://blog.kakaocdn.net/dn/sbau8/btrpDZW43Im/NoO8UYOLr3km1RFKURAMe0/img.png)

Thread 클래스를 사용하는 경우와 동일하지만 스레드를 실행시키는 방법은 객체 자체를 Thread 클래스의 생성자로 전달하여 실행시켜야 합니다.

![](https://blog.kakaocdn.net/dn/RRDe5/btrpJRYgezc/Wh8CN4Q5z5IXnqRwnCV2L1/img.png)

  
**Thread는 재사용 불가능, Runnable 가능**  
스레드는 일회용입니다. 따라서 한 번 사용한 스레드는 재사용할 수 없습니다. 재사용시 'IllegalThreadStateException' 이 발생하게 됩니다. 즉, 하나의 스레드에 대해 start()가 한 번만 호출될 수 있습니다. 하지만, Runnable로 구현한 경우 재사용 가능합니다.

----------

### **2. Handler**

**2.1 Handler란?**

Handler는 Looper로부터 받은 Message를 실행, 처리하거나 다른 스레드로부터 메시지를 받아서 Message Queue에 넣는 역할을 하는 스레드 간의 통신 장치입니다.

**2.2 사용목적**

핸들러는 일반적으로 UI갱신을 위해 사용됩니다.

1. 메소드가 단일 스레드 모델일 때 , Thread-Safe로 만들기 위해  아래의 코드를 실행시키면 'CalledFromWrongThreadException' 이라는 예외가 발생합니다. 이것은 뷰 클래스에서 제공하는 메소드(여기서는 setText()) 를 메인스레드(UI 스레드) 가 아닌 서브 스레드에서 실행 시켰기 때문입니다.  

```
public class TaeiimThread extends Thread {
    @Override
    public void run() {
        super.run();
        textView.setText("****");  // Error!!
    }
}
```

이러한 문제를 해결하기 위해 자바는 동기화(Synchronized)라는 기능을 제공하지만,  **안드로이드는 핸들러**를 제공합니다.

또, 스레드에서 네트워크 등의 작업들을 하는 도중에 UI를 업데이트 할 때 핸들러를 사용합니다.

#### **Looper와 Handler의 작동 원리**

메인 스레드는 내부적으로 Looper을 가지고 있고 그 속에 Message Queue가 존재한다. 이때 Message Queue는 자기 자신 혹은 다른 스레드로 부터 받은 Message이나 Runnable 객체를 순서대로 꺼내 Handler가 처리하도록 전달한다.

이때 Handler를 Looper에게 받은 Message를 순서대로 처리하게 된다.

![](https://blog.kakaocdn.net/dn/R2tcD/btrpIxTkLKz/KLSadpk7lfcmjhrhd1w5u1/img.png)


**정리,,,**
### **메인스레드 (UI 스레드)**
-   어플리케이션 실행 시 시스템에서 main()을 실행하여 만들어지는 프로세스에 자동 생성되는 스레드
-   UI를 그리고 갱신할 수 있는 유일한 스레드이기 때문에 UI 스레드라고 불린다

### **작업 스레드**
-   시간이 걸리는 작업을 메인 스레드가 아닌 별도의 스레드에서 처리해준다
-   하지만 UI 갱신은 메인 스레드에서만 가능하므로 스레드 통신을 통해 작업 스레드의 내용을 메인스레드로 전달하여 UI 작업을 완료한다.

### **Handler**
-   Message를 처리하거나 다른 스레드로부터 Message 객체를 받아 Message Queue에 저장하는 통신장치
-   Handler 객체는 하나의 스레드와 해당 스레드의 Message Queue에 종속되며 생성 시 자동으로 연결된다
-   Message 전달 시 수신 스레드에 속한 핸들러의 post() 또는 sendMessage() 메소드를 호출
-   받은 Message 처리 방식은 handleMessage() 메소드로 구현