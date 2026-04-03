package com.ohgiraffers.team3backendadmin.common.exception;

public class OCSAWeightConfigNotFoundException extends BusinessException {

    public OCSAWeightConfigNotFoundException() {
        super(ErrorCode.OCSA_WEIGHT_CONFIG_NOT_FOUND);
    }

    public OCSAWeightConfigNotFoundException(String message) {
        super(ErrorCode.OCSA_WEIGHT_CONFIG_NOT_FOUND, message);
    }
}
