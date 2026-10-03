package com.example.user2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
// 1. Point cut yazılırken dönüş tipi farketmez ise 'yıldız' yazılmalı. Aksi takdirde full paket adı yazılmalı
// Örnek: com.example.user2.dto.CarResponseDto   Örnek2: java.lang.Integer    Örnek3: int vs...
// 2. Aynı şekilde metodların parametreleri Integer olanlarda çalıssın istiyorsak
// * com.example.user2.service.*.*(Integer)) || * com.example.user2.service.*.*(int)) yazılabilir
// 3. @Before, @After, @AfterReturning ve @AfterThrowing anotasyonları loglama, güvenlik için kullanıldığı için returnü olsa bile AOP bunu yok sayar.
// 4. @Around genelde Object döndürür ve Parametre olarak diğerlerinin aksine proceedingJoinPoint alır.
// 5. * com.example.user2.service.*.*(Integer) * com.example.user2.service..*.*(Integer) arasındaki fark ..*.* olan alt paketlere de iner
// ama .* olan, service paketindeki sınıflara bakar ancak alt paketlere inmez.
// 6. genellikle service paketinde kullanılır
// 7. * com.example.user2.service.*.*(*, Integer) * com.example.user2.service.*.*(.., Integer) * var ise integerdan önce tipi ne olursa olsun bir parametre var demektir
// .. var ise integerdan önce parametre hiç olmayabilir veya olabilir de demektir.
// 8. date, BigDecimal, List, Map, ArrayList, Set vs. veri tiplerini tanıyamaybilir o zaman paket yolunu yazabiliriz
// 9. "execution(* com.example.user2.service.*Service+.*)" tüm "Service" içeren class, interfaceleri ve sub classların tüm metodlarını alır.
// 10. public, static, final olan metodlar da alınabilir. "execution(static * com.example.user2.service.*Service+.*)"
// 11. Throwable; exception ve error classlarının parent classıdır.
// 12. class adını döneceksek joinPoint.getSignature().getClass().getSimpleName() MethodSignatureImpl döner. Bu yüzden yerine
//  joinPoint.getTarget().getClass().getSimpleName() kullanmalıyız.
public class AspectService {

    public static Logger logger = LoggerFactory.getLogger(AspectService.class);

    @Before("execution(* com.example.user2.service.*.*(..))")
    public void before(JoinPoint joinPoint) {

        log.info("before method call");
        Object[] args = joinPoint.getArgs();
        log.info("parameters: {}", Arrays.toString(args));
    }

    // ResponseEntity<CarResponseDto> normalde döndüren metodları alamayız çünkü
    // ResponseEntity generic tip olduğu için runtimeda tipi silinir
    // bu yüzden instance of kullanarak yapabiliriz
//    @Before("execution(org.springframework.http.ResponseEntity com.example.user2.service.*.*(..))")
//    public void before2(){
//        log.info("before method call for return type ResponseEntity");
//    }


    @After("execution(* com.example.user2.service.*.*(..))")
    public void after(){
        log.info("after method call");
    }

    @After("execution(java.lang.Integer com.example.user2.service.*.*(Integer)) || execution(* com.example.user2.service.*.*(int))")
    public void after2(){
        log.info("integer parameterized method call");
    }

    // 'pointcut =' yazılabilenler;
    // returning = "result" service classındaki metodlardan dönen değeri yakalar resulta eşitler.
    // Object result sadece @AfterReturningde bulunur.
    @AfterReturning(pointcut = "execution(* com.example.user2.service.*.*(..))", returning = "result")
    public void afterReturnSuccess(JoinPoint joinPoint, Object result){

        log.info("method name: {}", joinPoint.getSignature().getName());
        log.info("class name: {}", joinPoint.getTarget().getClass().getSimpleName());
        log.info("result: {}", result);




//        log.info("method worked successfully");
//        log.info("method name: {}",joinPoint.getSignature().getName());
//        log.info("method defined class path: {}", joinPoint.getSignature().getDeclaringType());

    }

    @AfterThrowing(pointcut = "execution(* com.example.user2.service.*.*(..))", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        log.info("method executed with an exception: {}", ex.getMessage());
        log.info("method name: {}", joinPoint.getSignature().getName());
        log.info("method defined class name: {}", joinPoint.getTarget().getClass().getSimpleName());
        log.info("exception class name: {}",ex.getClass().getSimpleName());





//        log.info("method executed with an error");
//        log.error(ex.getMessage());
//        String name = joinPoint.getSignature().getName();
//        log.info("method name: {}", name);
//        log.info("method defined class path: {}", joinPoint.getSignature().getDeclaringType());
    }

    // metoda gelen parametreleri loglamak
    @Before("execution(* com.example.user2.service.*.*(..))")
    public void logArgs(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        log.info("method args: {}", Arrays.toString(args));
    }

    // metodların hangi sınıftan geldiği loglamak
    @Before("execution(* com.example.user2.service.*.*(..))")
    public void logTargets(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();
        log.info("method defined in this class: {}", target.getClass().getName());
    }

    // date, BigDecimal, List, Map, ArrayList, Set vs. veri tiplerini tanıyamaybilir o zaman paket yolunu yazabiliriz
    @Before("execution(* com.example.user2.service.*.*(java.util.Date)) || " +
            "execution(* com.example.user2.service.*.*(java.math.BigDecimal))")
    public void logging(JoinPoint joinPoint){
        log.info("method logged in class for return types Date and BigDecimal: {}", joinPoint.getSignature().getName());

    }

    @Before("execution(* com.example.user2.service.*.*(java.util.List)) ||" +
            "execution(* com.example.user2.service.*.*(java.util.Map))")
    public void logging2(JoinPoint joinPoint){

    }

    @Before("execution(* com.example.user2.service.*.*(java.util.ArrayList) ) ||" +
            "execution(* com.example.user2.service.*.*(java.util.Set) )")
    public void logging3(JoinPoint joinPoint){

    }

    @Before("execution(* com.example.user2.service.*.*(Object[]) ) ||" +
            "execution(* com.example.user2.service.*.*(int[]) )")
    public void logging4(JoinPoint joinPoint){

    }

    @Before("execution(* com.example.user2.service.*.*( org.springframework.data.domain.Pageable)) ||" +
            "execution(* com.example.user2.service.*.*( com.example.user2.dto.CarRequestDto) ) ||" +
            "execution(* com.example.user2.service.*.*( org.springframework.data.jpa.domain.Specification))")
    public void logging5(JoinPoint joinPoint){
    }

    // parametresiz metodlar için çalışır.
    @After("execution(* com.example.user2.service.*.*())")
    public void after3(){}

    // get ile başlayan parametresi String olan metodlar için çalışır
    @After("execution(* com.example.user2.service.*.get*(String))")
    public void after4(){}

    // birden fazla parametre yazılabilir veya bir parametre yazılır farklı parametreler devamında varsa ve bizim için fark etmiyosa .. konur.
    @After("execution(* com.example.user2.service.*.get*(String,Integer,Boolean)) ||" +
           "execution( org.springframework.data.domain.Pageable com.example.user2.service.*.*(Double, ..))")
    public void after5(){}

    // ikinci parametresi String olan sadece iki parametreli metod
    @After("execution(* com.example.user2.service.*.*(*, String))")
    public void after6(){}

    // find ile başlayan ve son parametresi Boolean olan çok parametreli metod
    @After("execution(* com.example.user2.service.*.find*(.., Boolean))")
    public void after7(){}

    // find ile başlayıp or içeren classın sonu Service ile biten metodlar
    @After("execution(* com.example.user2.service.*Service.find*Or*(..))")
    public void after10(){}

    // sadece public olan metodları almak istiyorsak
    @After("execution(public static * com.example.user2.service.*.find*(.., Boolean))")
    public void after8(){}

    // create veya update içeren metodlar
    @After("execution(* com.example.user2.service.*.*Create*(..)) ||" +
            "execution(* com.example.user2.service.*.create*(..)) ||" +
            "execution(* com.example.user2.service.*.*Update*(..)) ||"  +
            "execution(* com.example.user2.service.*.update*(..))")
    public void after9(){}


    @Around("execution(* com.example.user2.service.*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // metod çalışmadan öncesi
        log.info("method hasn't been called yet");
        Long startTime = System.currentTimeMillis();

        //metodu çalıştırır
        try {
            Object proceed = proceedingJoinPoint.proceed();

            //metod çalıştıktan sonrası
            Long endTime = System.currentTimeMillis();
            Long timeElapsed = endTime - startTime;

            log.info("method has been called");
            log.info("method executed in {} ms", timeElapsed);

            return proceed;
        }
        catch (Throwable e) {
            log.error("error message: {}",e.getMessage());
            log.info("error path: {}", proceedingJoinPoint.getTarget().getClass().getName());
            throw e;
        }


    }









}
