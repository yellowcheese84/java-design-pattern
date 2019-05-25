# Singleton pattern ( 싱글톤 패턴 )

> `Singleton pattern`은 applciation 런타임시 하나의 instance만 생성하는 패턴을 말한다. 

<img src="../img/singleton_pattern_uml_diagram.jpg" width="250" height="500" />

    * Single Thread에서 사용되는 경우에는 문제가 되지 않지만 Multi Thread 환경에서 Singleton 객체에 접근 시 초기화 관련하여 문제가 발생한다.

## Thread safe 생성 기법
> 보통 Singleton 객체를 얻는 static 메서드는 getInstance()로 작명하는 게 일반적이다.

* DCL ( Double Checked Locking )
> 일명 DCL이라고 불리는 이 기법은 현재 broken 이디엄이고 사용을 권고하지 않지만 이러한 기법이 있었다는 것만 알고 넘어 가자.
```
    public class Singleton {
        private volatile static Singleton instance;
        private Singleton() {}
        
        public static Singleton getInstance() {
            if (instance == null) {
                synchronized(Singleton.class) {
                    if(instance == null) {
                        instance = new Singleton(); 
                    }                
                }
            }
            
            return instance;
        }
    }
```
    * 메서드에 synchronized를 빼면서 동기화 오버헤드를 줄여보고자 하는 의도로 설계된 이디엄이다. 
      instance가 null 인지 체크하고 null 일 경우 동기화 블럭에 진입하게 된다. 그래서 최초 객체가 생성된 이후로는 동기화 블럭에 진입하지 않기 때문에 효율적인 이디엄이라고 생각할 수 있다. 하지만 아주 안 좋은 케이스로 정상 동작하지 않을 수 있다. 그래서 broken 이디엄이라고 하는 것이다.
      
    *  Thread A와 Thread B가 있다고 하자. Thread A가 instance의 생성을 완료하기 전에 메모리 공간에 할당이 가능하기 때문에 Thread B가 할당된 것을 보고 instance를 사용하려고 하나 생성과정이 모두 끝난 상태가 아니기 때문에 오동작할 수 있다는 것이다. 
      물론 이러할 확률은 적겠지만 혹시 모를 문제를 생각하여 쓰지 않는 것이 좋다.
      
* Enum

* LazyHolder

