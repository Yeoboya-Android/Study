# 안드로이드 Thread
========================================================

- Thread 클래스를 사용하여 새로운 스레드를 생성하고 실행하는 방법은 크게 두 가지가 있다.
  1. Thread 클래스를 상속(extends)한 서브클래스(subclass)를 만든 다음, Thread클래스의 run() 메서드를 오버라이드(override)하는 것
  2. Runnable 인터페이스를 구현(implements)한 클래스를 선언한 다음, run() 메서드를 작성하는 것
  
# Thread클래스 상속
  - Thread 클래스를 상속(extends)한 클래스를 만들고 run() 메서드를 오버라이드(override)한 다음, 클래스 인스턴스를 생성하고 start() 메서드를 호출하는 것.
    
# Runnable 인터페이스 구현(implements)
  - Runnable 인터페이스를 구현(implements)하는 클래스를 선언하고 run() 메서드를 구현한 다음, 클래스 인스턴스를 Thread 클래스 인스턴스의 생성자에 전달하고 Thread 클래스 인스턴스의 start() 메서드를 호출하면 된다.
  
# Thread vs Runnable
  - 왜 쓰레드 구현을 2개의 방법으로 했을까?
    객체지향 프로그래밍에서 클래스를 상속한다는 것은, 부모 클래스의 특징을 물려받아 재사용하는 것을 기본으로, 부모의 기능을 재정의 하거나, 새로운 기능을 추가하여 클래스를 확장하는 것을 의미한다.
    그리고 이 내용을 달리보자면, 클래스의 기능을 재정의 하거나 확장할 필요가 없다면, 굳이 클래스를 상속하지 않아도 된다는 말이 된다. 즉 기존 클래스를 그대로 사용하면 되는 것이다.
    
    스레드를 실행하기 위해서는 Thread 클래스가 제공하는 기능을 사용해야 한다. 그리고 스레드로 실행될 메서드도 재정의해야 한다.
    그래서 Thread 클래스를 상속하고 스레드 실행 메서드인 run() 메서드를 오버라이드한 클래스를 만든 것이다.
    
    그런데 단지, run() 메서드 하나를 위해 Thread 클래스를 상속해야 할까?
    Thread 클래스의 다른 모든 기능들은 그대로 재사용하는데, 무조건 오버라이드(override)되어야 하는 run() 메서드만을 위해서?
    Thread 클래스를 상속하지 않고, run() 메서드 코드만을 작성해서 Thread의 start() 메서드로 전달할 수 있으면 좋을때도 있을것이다.
    게다가, 프로그램 설계에 따라, run() 메서드를 구현할 클래스가 반드시 Thread가 아닌 다른 클래스를 상속해야 한다면, 다중상속이 허용되지 않는 자바에서 Thread 클래스를 상속하여 스레드를 만드는 것이 불가능해진다.
    
    이런 문제들을 해결하기 위해 개발자가 선택할 수 있는 방법은 무엇일까? 스레드 생성 시 반드시 구현해야 할 run() 메서드를 Thread 클래스와 분리하고 그 구현을 강제하는 것,
    그리고 추상화되어 있는 메서드를 클래스에서 구현(implements)하도록 만듦으로써, 다중상속이 불가능한 자바에서 그와 유사한 효과를 낼 수 있도록 만드는 방법.
    바로, 인터페이스를 사용하는 것이다. 그리고 그 인터페이스가 Runnable 인터페이스이다.
     
    정리하자면, 두 가지 방법 중 어떤 것을 선택할 것인지는 Thread 클래스 기능의 확장 여부에 따라 결정된다.
    단순히 run() 메서드만을 구현하는 경우라면 Runnable 인터페이스를 구현하고,
    Thread 클래스가 제공하는 기능을 오버라이드하거나 확장해야 한다면 Thread 클래스를 상속하는 방법을 선택할 수 있다.
    
![Alt text](./images/thread_runnable.png)    

  - Thread상속으로 구현
    ````````
    class TimerThread : Thread
    {
        constructor() : super()
        constructor(_runnable : Runnable): super(_runnable)
    
        override fun run() {
            for (i in 0..10){
                try{
                    Thread.sleep(1000)
                    Log.i("aaaa", "${i}")
                }
                catch (_e : InterruptedException){
                    Log.i("aaaa", "${_e.message}")
                }
            }
        }
    }
    
    val timerThread = TimerThread()
    timerThread.start()
    ````````
    
  - Runnable로 구현
    ````````
    val timerThread2 = TimerThread(Runnable {
                for (i in 0..10){
                    try{
                        Thread.sleep(1000)
                        Log.i("aaaa", "${i}")
                    }
                    catch (_e : InterruptedException){
                        Log.i("aaaa", "${_e.message}")
                    }
                }
            })
    timerThread2.start()
    ````````
    
# Thread간의 통신
  - UI의 업데이트는 Main Thread에서 한다고 이야기 했다.
    그러면 Worker Thread에서 작업 내용을 Main Thread에 전달하여 결과를 표현해야 한다면?
    Worker Thread에서 Main Thread로 메시지를 보내야 한다. 어떻게?
    
  - 핸들러(Handler)
    핸들러를 사용하여 Main Thread로 메세지를 보낸다.
    앞서 Handler는 메세지를 보내거나, 수신된 메세지를 처리하는 역할을 한다고 했다.
    핸들러는 Main Thread와 Worker Thread의 메시지 처리 흐름에서, 메시지 전달과 처리를 위해 개발자가 접근할 수 있는 창구 역할을 수행한다.
    
    메세지 수신부
    ````````
    mHandler = Handler(Looper.getMainLooper()){
                Log.i("bbbb", "${it.arg1}")
                mBinding.timer.text = it.arg1.toString()
                true
            }
    ````````
    
    메세지 송신부
    
  - 메세지(Message)
    스레드 통신에서 핸들러를 사용하여 데이터를 보내기 위해서는, 데이터 종류를 식별할 수 있는 식별자와 실질적인 데이터를 저장한 객체, 그리고 추가 정보를 전달할 객체가 필요하다.
    즉, 전달할 데이터를 한 곳에 저장하는 역할을 하는 클래스가 필요한데요, 이 역할을 하는 클래스가 바로 Message 클래스이다.
    
    ````````
    val message = Message().apply {
                            what = 1
                            arg1 = i
                        }
    mHandler.sendMessage(message)
    ````````
    
  - 메세지큐(Message Queue)
    이렇게 전달할 데이터를 메세지로 만들고 이 메세지를 메세지 큐(message queue)에 넣어둔다.
    메세지큐는 FIFO방식이므로 가장 먼저 들어온 데이터부터 처리하게 된다.
    
  - 루퍼(Looper)
    메세지큐는 전달할 메세지를 관리하는 클래스 일 뿐 실제적인 메세지 처리를 위한 핸들러를 실행 시키지 않는다.
    메시지 큐로부터 메시지를 꺼내온 다음, 해당 메시지와 연결된 핸들러를 호출하는 역할은 루퍼(Looper)가 담당한다.
    
    안드로이드 앱의 메인 스레드에는 Looper 객체를 사용하여 메시지 루프를 실행하는 코드가 이미 구현되어 있고, 해당 루프 안에서 메시지 큐의 메시지를 꺼내어 처리하도록 만들어져 있다.
    따라서 메인 스레드에서 메시지 루프와 관련된 코드를 개발자가 추가적으로 작성할 필요는 없다.
    우리가 할 일은, 메인 스레드로 전달할 Message 객체를 구성하고, 스레드의 메시지 큐에 연결된 핸들러(Handler)를 통해 해당 메시지를 보내기만 하면 된다.
    
    핸들러는 생성과 동시에, 코드가 실행된 스레드에 연결(bind)된다.
    Handler 클래스 생성자에서 현재 스레드의 루퍼(Looper) 및 메시지 큐(MessageQueue)에 대한 참조를 가지게 되는 것인데, 이후 단계에서 메시지를 보낼 때 이 참조를 사용하여 메시지 큐에 메시지를 넣었다.
    
    앞서 ActivityThread에 대해서 이야기 하였다.
    ActivityThread의 main() 함수에는 안드로이드 프레임워크 상에서 앱의 동작에 필요한 여러 준비 동작을 수행하는데,
    그 중 가장 중요한 과정이 바로 메인 UI 스레드(Main UI Thread)를 실행하는 것이다. 그리고 런처(Launcher)로 지정된 액티비티를 찾아서 실행한다.
    이 곳 소스를 보면 루퍼를 준비하고, 루퍼를 시작 하는 부분이 있다.
    
    ````````
    public static void main(String[] args) {
            Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "ActivityThreadMain");
            // Install selective syscall interception
            AndroidOs.install();
            // CloseGuard defaults to true and can be quite spammy.  We
            // disable it here, but selectively enable it later (via
            // StrictMode) on debug builds, but using DropBox, not logs.
            CloseGuard.setEnabled(false);
            Environment.initForCurrentUser();
            // Make sure TrustedCertificateStore looks in the right place for CA certificates
            final File configDir = Environment.getUserConfigDirectory(UserHandle.myUserId());
            TrustedCertificateStore.setDefaultUserDirectory(configDir);
            // Call per-process mainline module initialization.
            initializeMainlineModules();
            Process.setArgV0("<pre-initialized>");
            Looper.prepareMainLooper();
            // Find the value for {@link #PROC_START_SEQ_IDENT} if provided on the command line.
            // It will be in the format "seq=114"
            long startSeq = 0;
            if (args != null) {
                for (int i = args.length - 1; i >= 0; --i) {
                    if (args[i] != null && args[i].startsWith(PROC_START_SEQ_IDENT)) {
                        startSeq = Long.parseLong(
                                args[i].substring(PROC_START_SEQ_IDENT.length()));
                    }
                }
            }
            ActivityThread thread = new ActivityThread();
            thread.attach(false, startSeq);
            if (sMainThreadHandler == null) {
                sMainThreadHandler = thread.getHandler();
            }
            if (false) {
                Looper.myLooper().setMessageLogging(new
                        LogPrinter(Log.DEBUG, "ActivityThread"));
            }
            // End of event ActivityThreadMain.
            Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
            Looper.loop();
            throw new RuntimeException("Main thread loop unexpectedly exited");
        }
    ````````
    즉 메인스레드 루퍼에서 UI Message를 처리한다.
    Looper.loop() 메서드에 무한 반복문이 있기 때문에 main() 메서드는 프로세스가 종료될 때까지 끝나지 않는다.

# Thread당 한개의 핸들러만 필요한가?
  - 아니다. 핸들러는 여러 개 만들 수 있다.
    경우에 따라서는 하나의 핸들러에서 모든 메시지를 처리하는 것 보다, 메시지 종류 및 기능에 따라 여러 개의 핸들러로 나누어서 처리하는 것이 더 낳을수도 있다.
    
    
    
    
