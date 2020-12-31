package com.chat.tj.common.exception;

import com.chat.tj.common.constant.ResponseEnums;
import com.chat.tj.model.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * 自定义异常处理信息
 *
 * @author tangjing
 * @date 2020/11/11 15:37
 */
@Slf4j
@RestControllerAdvice
public class BadRequestExceptionHandler {
    /**
     * 参数校验失败产生的异常处理
     *
     * @param e 参数检验异常抛出的异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseVo<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.debug(e.getClass().getName(), e);
        StringBuilder stringBuilder = new StringBuilder();
        //只提示首个校验失败的参数属性
        FieldError errInfo = (FieldError) e.getBindingResult().getAllErrors().get(0);
        stringBuilder.append(errInfo.getField()).append(":").append(errInfo.getDefaultMessage());
        return new ResponseVo<>().error(ResponseEnums.PARAM_WARNING.getCode(), stringBuilder.toString().trim());
    }

}
