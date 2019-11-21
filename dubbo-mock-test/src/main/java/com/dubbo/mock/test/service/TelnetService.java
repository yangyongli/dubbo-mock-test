/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */

package com.dubbo.mock.test.service;

import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.ResultDTO;

import javax.validation.constraints.NotNull;

public interface TelnetService {

    /**
     * send message with telnet client.
     * @param dto
     * @return
     */
    ResultDTO<String> send(@NotNull ConnectDTO dto);
}
