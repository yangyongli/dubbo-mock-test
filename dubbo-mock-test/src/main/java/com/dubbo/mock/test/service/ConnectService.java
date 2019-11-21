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
import com.dubbo.mock.test.dto.UrlModelDTO;
import com.dubbo.mock.test.model.ServiceModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ConnectService {

    /**
     * connect to zk and get all providers.
     *
     * @param conn
     * @return
     */
    List<ServiceModel> connect(@NotNull String conn) throws NoSuchFieldException, IllegalAccessException;

    /**
     * list providers of service.
     *
     * @param connect
     * @return
     */
    List<UrlModelDTO> listProviders(@NotNull ConnectDTO connect) throws NoSuchFieldException, IllegalAccessException;

    /**
     * send request to the real dubbo server.
     *
     * @param dto
     * @return
     */
    ResultDTO<String> send(ConnectDTO dto) throws Exception;
}
