# Singleton pattern ( 싱글톤 패턴 )

> `Singleton pattern`은 applciation 런타임시 하나의 instance만 생성하는 패턴을 말한다. 

<img src="../img/singleton_pattern_uml_diagram.jpg" width="250" height="500" />

    Single Thread에서 사용되는 경우에는 문제가 되지 않지만 Multi Thread 환경에서 Singleton 객체에 접근 시 초기화 관련하여 문제가 발생한다.

## Thread safe 생성 기법
> 보통 Singleton 객체를 얻는 static 메서드는 getInstance()로 작명하는 게 일반적이다.

### 1. DCL ( Double Checked Locking )
> 일명 DCL이라고 불리는 이 기법은 현재 broken 이디엄이고 사용을 권고하지 않지만 이러한 기법이 있었다는 것만 알고 넘어 가자.
```java
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

메서드에 `synchronized`를 빼면서 동기화 오버헤드를 줄여보고자 하는 의도로 설계된 이디엄이다. 
`instance`가 null 인지 체크하고 null 일 경우 동기화 블럭에 진입하게 된다. 
그래서 최초 객체가 생성된 이후로는 동기화 블럭에 진입하지 않기 때문에 효율적인 이디엄이라고 생각할 수 있다. 
하지만 아주 안 좋은 케이스로 정상 동작하지 않을 수 있다. 그래서 `broken` 이디엄이라고 하는 것이다.
      
Thread A와 Thread B가 있다고 하자. 
Thread A가 instance의 생성을 완료하기 전에 메모리 공간에 할당이 가능하기 때문에 Thread B가 할당된 것을 보고 instance를 사용하려고 하나 생성과정이 모두 끝난 상태가 아니기 때문에 오동작할 수 있다는 것이다. 
물론 이러할 확률은 적겠지만 혹시 모를 문제를 생각하여 쓰지 않는 것이 좋다.
      
### 2. Enum
> 간단하게 Class가 아닌 Enum으로 정의하는 것으로 Java에 지대한 공헌을 한 Joshua Bloch가 언급한 이디엄이다.

```java
    public enum Singleton {
        INSTANCE;
    }
```

`Enum`은 인스턴스가 여러 개 생기지 않도록 확실하게 보장해주고 복잡한 `직렬화`나 `리플렉션` 상황에서도 직렬화가 자동으로 지원된다는 이점이 있다. 
하지만 Enum에도 한계는 있다. 보통 Android 개발을 하게 될 경우 보통 Singleton의 초기화 과정에 Context라는 의존성이 끼어들 가능성이 높다. 
`Enum`의 초기화는 컴파일 타임에 결정이 되므로 매번 메서드 등을 호출할 때 `Context` 정보를 넘겨야 하는 비효율적인 상황이 발생할 수 있다. 
결론은 `Enum`은 효율적인 이디엄이지만 상황에 따라 사용이 어려울 수도 있다는 점이다.

### 3. LazyHolder
> synchronized도 필요 없고 Java 버전도 상관없고 성능도 뛰어나 현재까지 가장 완벽하다고 평가받는 이디엄이다.

```java
    public class Singleton {
        private Singleton() {}
        public static Singleton getInstance() {
            return LazyHolder.INSTANCE;
        }
        
        private static class LazyHolder {
            private static final Singleton INSTANCE = new Singleton();
        }
    }
```

객체가 필요할 때로 초기화를 미루는 것으로 `Lazy Initialization`이라고도 한다. 
`Singleton` 클래스에는 `LazyHolder` 클래스의 변수가 없기 때문에 `Singleton` 클래스 로딩 시 `LazyHolder` 클래스를 초기화하지 않는다. 
`LazyHolder` 클래스는 `Singleton` 클래스의 `getInstance()` 메서드에서 `LazyHolder.INSTANCE`를 참조하는 순간 Class가 로딩되며 초기화가 진행된다. 
Class를 로딩하고 초기화하는 시점은 thread-safe를 보장하기 때문에 `volatile`이나 `synchronized` 같은 키워드가 없어도 thread-safe 하면서 성능도 보장하는 아주 훌륭한 이디엄이라고 할 수 있다.

현재까지 `LazyHolder`에 대해서 문제점은 나타나지 않고 있다. 
혹시나 Multi Thread 환경에서 Singleton을 고려하고 있다면 LazyHolder 기법을 이용하자.

## 참고 자료
* https://medium.com/@joongwon/multi-thread-%ED%99%98%EA%B2%BD%EC%97%90%EC%84%9C%EC%9D%98-%EC%98%AC%EB%B0%94%EB%A5%B8-singleton-578d9511fd42