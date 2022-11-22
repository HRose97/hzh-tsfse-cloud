package com.hzh.test.mybatis.except;

import com.hzh.test.mybatis.pojo.vo.Messager;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class CustomException extends  RuntimeException {

    protected String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ResponseBody
    @ExceptionHandler(value=DataAccessException.class)
    public Messager dataAccessErrorHandler(DataAccessException e){
        System.out.println("data access error handler");
        Messager messager = new Messager();
        messager.setStrMsg("SQL异常");
        return messager;
    }

}
