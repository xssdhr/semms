package com.xssdhr.common.exception;

import com.xssdhr.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author xssdhr
 * @4/3/2023 下午11:31
 * @Company 南京点子跳跃传媒有限公司
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public R handler(RuntimeException e){
         log.error("运行时异常：---------｛｝"+e.getMessage());
         return R.error(e.getMessage());
    }
}
