# Context

생성일시: December 18, 2021 8:08 PM
채널: Note, study
카테고리: android

## Context

애플리케이션 환경의 전역 정보를 제공하는 인터페이스. 이를 이용해 어플리케이션 리소스, 클래스, 동작 등에 접근할 수 있다.

## 우리는 액티비티는 어떻게 context에 접근 할 수 있을까?

activity는 Context추상 클래스(android.content.Context)를 상속 받은 클래스이다. 따라서 activity 클래스에서 context에 접근 할 수 있다.

## 왜 Context가 필요할까?

Context는 어플리케이션과 관련된 정보에 접근하고자 하거나 어플리케이션과 연관된 시스템 레벨의 함수를 호출하고자 할 때 사용. 그런데 안드로이드에서는 시스템이 아닌 AcitivityManagerService라는 다른 어플리케이션이 해당 정보를 관리함

따라서 다른 일반적인 플랫폼과는 달리, 안드로이드에서는 어플리케이션과 관련된 정보에 접근하고자 할 때는 AcitivityManagerService를 통해서 접근 가능

정보를 얻고자 하는 어플리케이션이 어떤 어플리케이션인지에 관한 키 값이 필요

즉, 안드로이드 플랫폼 관점에서 Context는 다음과 같은 두 가지 역할을 수행하기 때문에 꼭 필요하다.

- 자신이 어떤 어플리케이션을 나타내고 있는지 알려주는 ID 역할
- ActivityManagerService에 접근할 수 있도록 하는 통로 역할

```csharp
//c#의 application의 경우
//Get an Application Name.
 String applicationName = System.AppDomain.CurrentDomain.FriendlyName;

 //Start a new process(application)
 System.Diagnostics.Process.Start("test.exe");
```

```java
//안드로이드의 경우
//Get an application name
String applicationName = this.getPackageName();

//Start a new activity(application)
this.startActivity(new Intent(this, Test.class));
```

## Application Context와 Activity Context

- **Application Context**
    - 싱글톤 객체로 어플리케이션의 생명주기를 따른다. 즉, 앱의 시작 시 부터 종료 시 까지 살아있다.
- **Activity Context**
    - Activity 자체가 Context를 상속하고 있기 때문에 Activity 인스턴스 자체가 Context 역할을 한다.그렇기 때문에 Activity Context는 액티비티 생명주기를 따른다.
    

### **Context Type**

- **Application** - 싱글톤으로 어플리케이션 객체가 생성될 때 함께 Application Context가 생성되기 때문에 Application Context는 동일한 앱 안에서 항상 동일한 인스턴스를 반환한다.
- **Activity / Service** - 액티비티나 서비스가 생성될 때마다 각자의 Context 인스턴스가 생성된다.
- **Broadcast Receiver** - 자기 자신이 Context는 아니다. 리시버가 브로드캐스트 처리를 할 때마다 Context를 onReceive()의 인자로 전달 받아서 사용한다. 전달 받은 Context의 생명주기를 따르기 때문에 액티비티 컨텍스트로 브로드캐스트 실행 시 액티비티가 종료되면 브로드캐스트도 함께 종료된다.
- **Content Provider** - 자기 자신이 Context는 아니다. 동일한 응용 프로그램에 대해 호출 시 동일한 싱글톤 Context 객체를 반환하고, 서로 다른 응용 프로그램에 대해 호출 시 다른Context 객체를 반환한다.

## Context사용할 때 주의

Context의 생명주기가 다르기 때문에 **Activity와 분리된 작업에 Activity Context를 사용하면 예상치 못한 동작이 발생**할 수 있다. 반대로 **Activity에 종속된 작업을 Application Context를 이용하면 Activity가 종료되어도 Context를 이용한 작업이 종료되지 않아 memory leak이 발생**할 수도 있다. 그렇기 때문에 Context를 사용할 때 각 Context의 특징과 Context가 필요한 작업의 특성을 고려해 Context를 알맞게 선택해 사용해야 한다.

## context변수의 바인딩 식에서 활용

xml 바인딩 식에서 context를 직접 사용해도 문제가 없으며, 심지어 context를 내장하고 있기때문에 별도로 variable을 선언하지 않아도 된다.

```xml
<TextView android:text="@{model.showToast(context)}"/>

...

<Button android:onClick="@{()->((AppCompatActivity)context).finish())}"/>

...

<import type="com.example.test1_databinding.R"/>
<TextView android:text="@{context.getResources().getString(R.string.hello)}"/>

전부 가능
```

참고 : 

깡샘의 안드로이드 프로그래밍
Head First Android Development

[https://arabiannight.tistory.com/284](https://arabiannight.tistory.com/284)

[https://s2choco.tistory.com/10](https://s2choco.tistory.com/10)
