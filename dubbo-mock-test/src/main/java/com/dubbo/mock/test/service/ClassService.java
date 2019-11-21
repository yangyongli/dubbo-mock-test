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
import com.dubbo.mock.test.dto.MethodModelDTO;
import com.dubbo.mock.test.dto.ResultDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ClassService {

    /**
     * generate the simple json string of the method parameters.
     *
     * @param dto
     * @return
     */
    ResultDTO<String> generateMethodParamsJsonString(@NotNull MethodModelDTO dto) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * get all methods from the given interface.
     *
     * @param dto
     * @return
     */
    List<MethodModelDTO> listMethods(ConnectDTO dto);

}
