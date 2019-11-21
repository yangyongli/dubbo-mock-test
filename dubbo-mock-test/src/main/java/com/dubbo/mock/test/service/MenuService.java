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

public interface MenuService {

    /**
     * get url map to the mid.
     *
     * @param mid menuId
     * @return the menu mrl
     */
    String getUrl(Integer mid);

    /**
     * get the menu text.
     * @return
     */
    String getHtml();
}
