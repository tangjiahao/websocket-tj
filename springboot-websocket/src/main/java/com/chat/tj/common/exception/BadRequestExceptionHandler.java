package com.chat.tj.common.exception;

import com.chat.tj.chat.model.vo.ResponseVo;
import com.chat.tj.common.constant.ResponseEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * 自定义异常处理信息
 *
 * @author tangjing
 * @date 2020/11/11 15:37
 */
@Slf4j
@ControllerAdvice
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

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    private String maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.debug(e.getClass().getName(), e);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("文件上传错误").append(":").append("文件上传的大小不能超过50MB");
        log.info(stringBuilder.toString().trim());
        return stringBuilder.toString().trim();
    }

}
