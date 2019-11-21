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

import lombok.Data;


@Data
public class PomDTO extends BaseDTO {

    /**
     * the pom xml content.
     */
    private String pom;

    /**
     * the path of pom file.
     */
    private String path;

}
