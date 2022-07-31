package com.example.phanquyen.api.restcontroller;


import com.example.phanquyen.api.exception.EntityNotFoundDeletedException;
import com.example.phanquyen.api.responsemodel.CustomizeResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(EntityNotFoundDeletedException.class)
    public ResponseEntity<CustomizeResponseModel> handleEntityNotFoundDeleted(EntityNotFoundDeletedException exc){
        CustomizeResponseModel customizeResponseModel = new CustomizeResponseModel();
        customizeResponseModel.setData(null);
        customizeResponseModel.setMsg(exc.getMessage());
        return new ResponseEntity<>(customizeResponseModel, HttpStatus.INTERNAL_SERVER_ERROR); // server error = lỗi 500
    }
}
