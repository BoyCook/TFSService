package org.cccs.tfs.web;

/**
 * User: boycook
 * Date: 15/02/2011
 * Time: 20:13
 */
public class ExceptionResponse {

    private int errorCode;
    private String errorMessage;

    public ExceptionResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
