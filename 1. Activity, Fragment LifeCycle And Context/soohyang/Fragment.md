# Fragment

생성일시: December 18, 2021 7:27 PM
채널: Note, study
카테고리: android

## Fragment

액티비티의 생명주기를 따르는 뷰

## Fragment의 생명주기

기본적으로 액티비티와 동일하며, fragment를 위한 함수가 추가된 구조

backStack : fragment가 화면에 안보이는 순가 제거하지않고 저장 후 다시 이용할 수 있는 기능

backStack의 설정 여부에 따라 Fragment의 생명주기가 다르게 동작

backStack을 사용하지 않을때, fragment를 다른 fragment로 대체하면 onDetach까지 호출되고 fragment의 생명주기 종료

backStack을 사용할 때, 다른 fragment로 대체되도 onDestoryView 까지 호출되었다가 뒤로가기를 누르면 onCreateView부터 다시 호출됨