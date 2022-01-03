---
topic: 스레드와 핸들러
author: 오혜미
---

# 스레드(Thread)
- 동시 작업을 할려면 필요한 하나의 작업 단위.
- UI부분에 접근하는 것은 스레드 2개가 동시에 접근을 할 수 없음.
- 메인스레드만 UI에 접근 할 수 있고 메인 외 다른 스레드들은 별도의 제어를 통해 UI를 다뤄야함

### 메인 스레드

- 화면의 UI를 그리는 처리를 담당.
- 안드로이드 UI 툴킷의 구성 요소와 상호작용하고, UI 이벤트를 사용자에게 응답하는 스레드

### 백그라운드 스레드

- 시간을 특정할 수 없는 작업을 백그라운드 스레드에서 처리하는 것을 권장.  
  
Thread 객체
```
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)

	var thread = WorkerThread()
	thread.start()
}

class WorkerThread : Thread() {
	override fun run() {
		//스레드가 처리할 로직
	}
}
```
  
Runnable 인터페이스  
다중 상속을 허용하지 않는 코틀린 언어의 특성상 상속 관계에 있는 클래스도 구현할 수 있음.
```
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)

	var thread = Thread(WorkerRunnable()) // runnable객체를 thread의 생성자로 전달.
	thread.start()
}

class WorkerRunnable : Runnable {
	override fun run() {
		//스레드가 처리할 로직
	}
}
```

람다식으로 Runnable 익명객체 구현
```
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)

	Thread {
		//스레드가 처리할 로직
	}.start()
}
```
코틀린에서 제공하는 Thread() 구현
```
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)

	thread(start=true){
		//스레드가 처리할 로직
	}
}
```

# 핸들러(Handler)
- 각각의 스레드 안에 만들어질 수 있고 다른 스레드에서 요청하는 정보를 순서대로 실행시켜 줄 수 있음.
- 리소스에 대한 동시 접근의 문제를 해결해줌. (핸들러를 이용해 스레드에서 동적으로 UI에 접근할 수 있도록함.)
- 다른 스레드에서 요청하는 정보를 메세지 큐를 통해 순서대로 실행시켜 줌.
- 메인스레드와 백그라운드스레드 및 스레드 간의 통신을 위해 핸들러와 루퍼(Looper) 제공.
- 루퍼
    - 자신의 큐에 쌓인 메시지를 핸들러에 전달.
    - 여러 개의 백그라운드에서 큐에 메시지를 입력하면, 입력된 순서대로 하니씩 꺼내 핸들러에 전달.
    ```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                // 핸들러에서 메시지를 받아 처리할 로직.
            }
        }

        button.setOnClickListener {
            thread(start=true){
                //스레드가 처리할 로직
                handler?.sendEmptyMessage(0)
            }
        }
    }
    ```