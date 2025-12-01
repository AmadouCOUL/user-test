package fr.miage.m2.usertest.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* fr.miage.m2.usertest.controller..*(..))")
    public Object journaliserAppelController(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.currentTimeMillis();
        System.out.println("INPUT: ");

        for (Object arg : pjp.getArgs()) {
            afficherChamps(arg);
        }

        Object result = pjp.proceed();

        System.out.println("OUTPUT:");
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;
            System.out.println("  - status = " + response.getStatusCode());
            afficherChamps(response.getBody());
        } else {
            System.out.println("  - " + result);
        }

        System.out.println("TIME: " + (System.currentTimeMillis() - start) + "ms\n");

        return result;
    }

    private void afficherChamps(Object obj) {
        if (obj == null) {
            System.out.println(" null");
            return;
        }

        if (obj instanceof String || obj instanceof Number) {
            System.out.println("    " + obj);
            return;
        }

        if (obj instanceof Collection) {
            int index = 0;
            for (Object item : (Collection<?>) obj) {
                System.out.println("  - élément " + index + ":");
                afficherChamps(item);
                index++;
            }
            return;
        }

        for (Field f : obj.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
            f.setAccessible(true);
            try {
                System.out.println("    " + f.getName() + " = " + f.get(obj));
            } catch (Exception e) {
                System.out.println("    " + f.getName() + " = <inaccessible>");
            }
        }
    }

}