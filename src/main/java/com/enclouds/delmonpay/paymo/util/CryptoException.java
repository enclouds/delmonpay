package com.enclouds.delmonpay.paymo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;

@SuppressWarnings("serial")
public class CryptoException extends NestedRuntimeException {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoException.class);

    public CryptoException(String msg) {
        super(msg);
        LOG.error(msg);
    }

    public CryptoException(String msg, Throwable cause) {
        super(msg, cause);
        LOG.error(msg);
    }

}
