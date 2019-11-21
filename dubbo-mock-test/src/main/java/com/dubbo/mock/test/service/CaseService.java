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
import com.dubbo.mock.test.model.CaseModel;

import java.util.List;


public interface CaseService {

    /**
     * save the case.
     *
     * @param model
     * @return
     */
    ResultDTO<CaseModel> save(CaseModel model);

    /**
     * list all case.
     *
     * @return
     */
    List<Object> listAll();

    ResultDTO<String> send(ConnectDTO dto) throws Exception;
}
