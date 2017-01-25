package com.example.helper;


import com.example.exception.InvtAppException;

public class DefaultExceptionHandler implements ExceptionHandler {

	@Override
	public void handleException(Exception rootCause) {
		rootCause.printStackTrace();
		throw new InvtAppException(rootCause);
	}
}
