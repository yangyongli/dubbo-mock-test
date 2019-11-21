/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.dto;

import java.util.concurrent.atomic.AtomicLong;


public class BaseDTO {

    private static final AtomicLong counter = new AtomicLong();

    private final String requestId;

    public BaseDTO() {

        this.requestId = String.valueOf(counter.getAndAdd(1));
    }

    public String getRequestId() {
        return requestId;
    }
}
