# Activity

생성일시: December 18, 2021 6:28 PM
채널: Note, study
카테고리: android

## Activity 생명주기

활성 상태(activity running) : 액티비티가 화면을 점유, 사용자 이벤트 처리가 진행

화면을 출력해주어야 한다 (setContentView) // 에러발생X

setContentView 반복실행 가능 // 마지막 내용이 화면에 표시

addContentView // 이미 실행된 화면 위에 다른 화면을 출력

일시 정지 상태(pause): 액티비티가 일시적으로 사용이 불가능한 상태

다른 액티비티가 **화면 전체를 가리지않고 실행(반투명 or 다이얼로그형태 등)**된 상태

화면에 보이지만 포커스를 잃은 상태

대부분 onPause 까지 호출

비활성 상태(stop): 다른 액티비티로 화면이 완벽하게 가려진 상태

onPause, onStop 까지 호출

화면을 가렸던 액티비티가 사라지면(뒤로가기 등) 다시 활성화 상태로 전환

onRestart → onStart → onResume 호출

onPause 함수 호출시 작업 권장

- 애니메이션이나 CPU 소비를 야기할 수 있는 지속적 작업 정지
- 서버 네트워킹 등 불필요한 동작 정지

## Activity 상태 저장

화면 회전 : onPause → onStop → onDestory // 자동 재시작 // onCreate → onStart → onResume

최초 실행시 네트워크, 사용자 이벤트로 인한 데이터 변경에 유실 발생

위와같이 액티비티 종료될 때 사라지는 데이터를 유지하기 위한 함수

onSaveInstanceState(outState Bundle) / onRestoreInstanceState(savedInstanceState Bundle)
: 액티비티가 다시 시작될 때 무조건 호출 됨

onSaveInstanceState(outState Bundle, outPersistentState PersistableBundle) / onRestoreInstanceState(savedInstanceState Bundle, persistentState PersistableBundle)
: 저장된 데이터가 없으면 호출되지 않음

EditText는 스스로 화면 회전에 의한 데이터 유실 복원 제공

## 태스크 관리

앱이 실행되면 프로세스가 실행되는데 이때 태스크(스택)의 정보를 활용

태스크 : 사용자 관점을 고려한 프로그램의 실행단위

ex ) 일반적인 경우 앱A를 실행하고 앱B를 실행하면, 앱A Task와 앱B Task가 실행된다.

그러나 앱A에서 intent로 앱B를 실행한 경우 사용자의 관점에서 하나의 앱만 실행하였으므로 앱A Task에 앱A와 앱B의 Activity 정보가 유지 됨

AndroidManifest.xml 파일설정, intent flag 두가지 방법으로 태스크 관리 가능

## AndroidManifest.xml 파일설정

standard : 인텐트가 발생하면 매번 액티비티를 생성하고 태스크 목록에 반복해서 추가

singleTop : 실행하려는 액티비티가 태스크의 최상단에 있으면 다시 생성하지 않음

객체 생성없이 화면 전환 방법 : **onNewIntent**

다시 activity를 생성하지 않고 위 함수만 다시 호출한다.

singleTask : 액티비티 실행시 새로운 태스크 목록을 만듦.

최근 실행한 앱 목록에 두개의 앱 정보가 올라감

같은 앱 내에서 발생한 인텐트에 의해 실행 불가, 다른 앱과 연동한 경우에만 적용가능

singleInstance : 액티비티 실행시 새로운 태스크 목록을 만들고, 해당 태스크를 혼자 점유

이후 수행되는 컴포넌트들은 별도의 태스크에 저장됨 

## Intent Flag

FLAG_ACTIVITY_NEW_DOCUMENT : 태스크 목록 리셋

FLAG_ACTIVITY_CLEAR_TOP : 실행되는 액티비티의 상단 태스크 정보 삭제

FLAG_ACTIVITY_NEW_TASK : 새로운 태스크 목록을 만들어 액티비티 실행 (singleTask)

FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS : 최근 실행한 앱 목록에 새로운 태스크의 앱이 나오지 않게 설정

FLAG_ACTIVITY_FORWARD_RESULT : 인텐트 실행 결과를 이전 액티비티에 전달

FLAG_ACTIVITY_NO_HISTORY : 태스크 목록에 액티비티 저장하지 않음

FLAG_ACTIVITY_REORDER_TO_FRONT : 액티비티를 태스크 목록 최상위로 실행

FLAG_ACTIVITY_SINGLE_TOP : 액티비티가 태스크 목록 최상위에 있을 때 다시 생성하지 않음