# Fragment와 Fragment View 생명주기
## 1. 프래그먼트와 프래그먼트 뷰 생명주기
![fragmentLifecycle](./preview/fragmentLifecycle.jpg)
### onCreate()
- 프래그먼트가 생성될 때 단 한번만 호출
- **데이터와 같은 리소스 초기화 작업**이 적합
- 뷰는 생성되지 않았으므로 뷰 관련 작업에 적합하지 X
### onCreateView()
- 뷰가 생성되어 매개변수로 사용 가능한 시점
- 프래그먼트가 백스택에서 되돌아오는 지점
### onViewCreated()
- onCreateView()에서 inflate 한 view를 매개변수로 받는다.
- 뷰가 완전히 생성된 시점


프래그먼트의 생성과 프래그먼트 뷰의 생성은 생성 되는 생명주기 콜백 메서드부터 다르다.

### Fragment 생명주기 : onAttach() ~ onDetach()
### FragmentView 생명주기 : onCreateView() ~ onDestroyView()

- 프래그먼트 **뷰**의 생명주기가 프래그먼트 생명주기 보다 짧다.
- 또한 프래그먼트는 백스택에 저장된 경우, replace 등으로 프래그먼트가 종료되어도 onDestroyView() 까지만 가고 onDestroy()까지 가지는 않는다.
- 그 상태에서 다시 프래그먼트로 돌아올 경우 onCreateView() 부터 호출된다.
- 프래그먼트 뷰의 생명주기를 벗어서 viewLifecycleOwner를 호출하면 뷰가 없다는 오류가 난다.

## 2. lifecycleOwner가 아닌, viewLifecycleOwner를 사용해야 할 때 : LiveData
- 프래그먼트에서는 라이브데이터에 옵저버를 연결할 경우 옵저버의 lifecycle을 프래그먼트 라이프사이클(lifecycleOwner)이 아닌, 뷰의 라이프사이클(viewLifecycleOwner)로 설정해야 한다.
- 프래그먼트 라이프사이클(lifecycleOwner)을 설정할 경우 onDestroyView() 까지만 거친 죽지 않은 프래그먼트가 남아있는 채로 onCreateView()에서 새로운 observer가 호출되어 중복되는 현상이 발생하기 때문








