---
topic: Fragment LifeCycle And Context
description: FragmentLifeCycle
author: 김동현
---
# FragmentLifeCycle
```mBinding.lifecycleOwner = this```

```mBinding.lifecycleOwner = viewLifecycleOwner```

프래그먼트에서 데이터바인딩의 라이프사이클을 지정할 때 Fragment Lifecycle을 지정해야할까 아니면 ViewLifeCycle을 지정해야 할까?

## ViewLifecycleOwner ? LifecycleOwner
![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/Fragment_LifeCycle.png?raw=true)

Fragment's view 는 사용자가 다른 뷰로 이동하면 destroy 되지만, fragment 그 자체는 destroy 되지 않습니다. 이는 곧 fragment 에 두 개의 lifecycle 을 만들게 되는데, 위 그림처럼 fragment 의 lifecycle 과 fragment's view 의 lifecycle 입니다.



![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/FragmentBackStack.png?raw=true)


- Fragment AA가 Fragment BB에 의해 replace 되고 transaction이 backstack에 add 될 때,  Fragment AA의 lifecycle의 상태는 onDestroyView 다.

- User가 Fragment BB에서 back key를 눌렀을 때, Fragment AA는 onCreateView() -> onViewCreated() 순서를 거친다.- Fragment AA는 결코 destroy 되지 않기 때문에, 이전 observer 역시 remove 되지 않았다. 결국,  onCreateView()가 매번 call 될 때, 새로운 observer 가 등록되고 이전 observer 역시 남아있는 상태이다.


![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/FragmentOnCreateView_this.png?raw=true)

![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/FragmentOnCreateView_viewLifeCycle.png?raw=true)

위는 빨간줄과 상관없이 빌드는 되지만 , onDestroy()를 거치지 않은 Fragment의 Lifecycle이 죽지 않은 상태로 onCreateView()에서 새로운 Observer들이 호출되어 중복되는 현상이 발생될 수 있습니다.

따라서 옵저버 등록시 fragment 의 lifecycleowner 를 등록하게 되면 문제가 생깁니다. 반드시 fragment's view 의 lifecycleowner 인 viewLifecycleOwner 를 등록해주어야 합니다.


즉,
-> user 가 fragment 를 벗어나면 fragment view 는 파괴되지만 fragment 자체는 파괴되지 않는다.  
-> LiveData사용시 fragment 의 lifecycle 을 참조하면 fragment view 를 업데이트할때 버그가 발생할 수도 있다


## DataBinding과 함께 사용할 때

결국
이때도, fragment 의 경우에는 viewLifecycleOwner 로 지정

![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/DataBinding.png?raw=true)

Fragment는 Fragment View보다 긴 생명주기를 가지며, 일반적으로 UI를 업데이트용으로는 Fragment View Lifecycle 이 적절합니다. 그리고, Fragment View Lifecycle 도입으로 LiveData/observe에서 사용하는 Observer 중복 호출 문제도 해결할 수 있습니다. Fragment 사용 시 데이터 갱신에 대한 Lifecycle을 Fragment Lifecycle보다 Fragment View Lifecycle이 올바르다고 언급하고 있습니다.

![onCreate](https://github.com/net772/Study/blob/main/1.%20Activity,%20Fragment%20LifeCycle%20And%20Context/donghyun/preview/Binding_lifecycleOwner.png?raw=true)
