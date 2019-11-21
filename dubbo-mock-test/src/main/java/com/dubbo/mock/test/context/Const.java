/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.context;

/**
 * @author Joey
 * @date 2018/6/17 18:38
 */
public class Const {

    /**
     * download task key.
     */
    public static final String DUBBO_MOCK_DOWNLOAD_JAR_TASK = "dubbo:download:jar:task";
    /**
     * when the task was running.
     */
    public static final int RUNNING_FlAG = 1;
    /**
     * when the task has completed.
     */
    public static final int COMPLETE_FLAG = 2;
    /**
     * download task real time message key.
     */
    public static final String DUBBO_MOCK_DOWNLOAD_JAR_MESSAGE = "dubbo:download:jar:msg:{}";

    /**
     * the project cache namespace.
     */
    public static final String DUBBO_MOCK_CACHE_PREFIX = "dubbo:cache";
    /**
     * use case key.
     */
    public static final String DUBBO_MOCK_CASE_KEY = "dubbo:case";
    /**
     * all config of zk address key.
     */
    public static final String DUBBO_MOCK_REGISTRY_KEY = "dubbo:registry:list";
}
