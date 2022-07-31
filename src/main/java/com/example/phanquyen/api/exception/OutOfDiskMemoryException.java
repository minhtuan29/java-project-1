package com.example.phanquyen.api.exception;

public class OutOfDiskMemoryException extends Exception{
    OutOfDiskMemoryException(String msg){
        super(msg);
    }
}
