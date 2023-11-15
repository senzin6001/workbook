package com.example.demo.login.aspect;

//import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect{
	@Around("execution(* **..*.*Controller.*(..))")
	public Object startLog(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("メソッド開始： "+jp.getSignature());
		
		try {
			
			Object result = jp.proceed();
			
			System.out.println("メソッド終了： "+jp.getSignature());
			
			return result;
			
		}catch(Exception e) {
			
			System.out.println("メソッド異常終了： "+jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}
/*	
	@After("execution(* **..*.*Controller.*(..))")
	public void endLog(JoinPoint jp) {
		System.out.println("メソッド終了： "+jp.getSignature());
	}
*/
	@Around("execution(* *..*.*UserDao*.*(..))")
	public Object daolog(ProceedingJoinPoint jp) throws Throwable{
		System.out.println("メソッド開始： "+jp.getSignature());
		try {
			Object result = jp.proceed();
			System.out.println("メソッド終了： "+jp.getSignature());
			return result;
		} catch (Exception e) {
			System.out.println("メソッド異常終了： "+jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}
}