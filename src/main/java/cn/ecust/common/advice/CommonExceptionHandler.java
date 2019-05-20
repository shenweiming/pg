package cn.ecust.common.advice;

import cn.ecust.common.enums.ExceptionEnum;
import cn.ecust.common.exception.LyException;
import cn.ecust.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Auther: SWM
 * @Date: 2019/4/13 15:27
 * @Description: 
 */
@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e){
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }
}
/*
* 通用异常实现流程
* 一 自定义一个异常，异常的参数是一个枚举类型
* 二 自定义一个返回对象类，有一个构造函数可以传递自定义的枚举类型
* */