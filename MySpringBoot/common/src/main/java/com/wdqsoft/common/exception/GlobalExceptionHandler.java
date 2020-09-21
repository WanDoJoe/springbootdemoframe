package com.wdqsoft.common.exception;

import com.wdqsoft.common.controller._BaseController;
import com.wdqsoft.common.lang.Result;
import com.wdqsoft.common.lang.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * 处理全局异常类
 *
 如果出现 #0052：你无法访问移动办公！ 错误  则ip被禁
 */
@Slf4j
@RestController
@RestControllerAdvice(basePackages = {"com.sinosoft","com.sinosoft.common.jwt"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends _BaseController {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result handler_INTERNAL_SERVER_ERROR(Exception e){
        log.error("GlobalExceptionHandler-----运行异常！！！--------");
        log.error("-------Exception-----运行异常！！！---\n"+e.getLocalizedMessage());
        e.printStackTrace();
        return Result.fail(ResultCode.INTERNAL_SERVER_ERROR,e.getLocalizedMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Object handler(ShiroException e){
        log.error("GlobalExceptionHandler-----运行异常！！！--------");
        log.error("-------ShiroException-----运行异常！！！---\n"+e.getLocalizedMessage());
        e.printStackTrace();

        String msg="";

        if(e.getLocalizedMessage().contains("The current Subject is not authenticated")){
            msg="无权访问！";
        }else {
            msg=e.getMessage();
        }
        return decodeResponseDate(
                Result.fail(ResultCode.ShiroException_CODE,msg)
        );

    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object handler(MethodArgumentNotValidException e){
        log.error("GlobalExceptionHandler-----运行异常！！！--------");
        log.error("-------MethodArgumentNotValidException-bvgfhg----运行异常！！！---\n"+e.getLocalizedMessage());
        e.printStackTrace();

        BindingResult bindingResult=e.getBindingResult();
        ObjectError objectError=bindingResult.getAllErrors().stream().findFirst().get();

        return decodeResponseDate(
                Result.fail(ResultCode.MethodArgumentNotValidException_CODE,objectError.getDefaultMessage())
        );

    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalAccessException.class)
    public Object handler(IllegalAccessException e){
        log.error("GlobalExceptionHandler-----Assert！！！--------");
        log.error("-------IllegalAccessException-----运行异常！！！---\n"+e.getLocalizedMessage());
        e.printStackTrace();
        return decodeResponseDate(
                Result.fail(ResultCode.IllegalAccessException_CODE,e.getMessage())
        );
    }


//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Object handler(RuntimeException e){
        log.error("GlobalExceptionHandler-----运行异常！！！--------");
        log.error("-------RuntimeException-----运行异常！！！---\n"+e.getLocalizedMessage());
        e.printStackTrace();

        return decodeResponseDate(
                Result.fail(ResultCode.RuntimeException_CODE,e.getMessage())
        );
    }

//    @Override
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
//
//        log.error("统一异常处理捕获了异常，请求地址："+httpServletRequest.getRequestURL(),e);
//        ModelAndView modelAndView=new ModelAndView();
//        Map<String ,String > stringStringMap=new HashMap<>();
//        stringStringMap.put("error",e.getMessage());
//        modelAndView.addAllObjects(stringStringMap);
//        return modelAndView;
//    }
}
