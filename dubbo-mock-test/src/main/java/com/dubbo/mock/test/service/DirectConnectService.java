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

public interface DirectConnectService {

    /**
     * send request to the real dubbo server.
     *
     * @param dto
     * @return
     */
    ResultDTO<String> send(ConnectDTO dto) throws Exception;
}
