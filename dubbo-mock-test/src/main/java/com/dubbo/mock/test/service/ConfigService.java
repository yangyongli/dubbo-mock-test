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

import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.model.RegistryModel;

import java.util.List;

public interface ConfigService {

    /**
     * list all registry.
     *
     * @return
     */
    List<RegistryModel> listRegistry();

    /**
     * add registry.
     *
     * @return
     */
    ResultDTO<RegistryModel> addRegistry(RegistryModel model);

    /**
     * load zk config.
     */
    void loadZkConfigFromResource();

    /**
     * delete registry.
     *
     * @param model
     * @return
     */
    ResultDTO<RegistryModel> delRegistry(RegistryModel model);
}
