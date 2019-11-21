/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.cache;

import com.dubbo.mock.test.util.ParamUtil;
import com.dubbo.mock.test.util.StringUtil;
import com.dubbo.mock.test.exception.DubboTestException;
import com.dubbo.mock.test.handler.CuratorHandler;
import com.dubbo.mock.test.model.PointModel;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CuratorCaches {

    private final static Map<String, CuratorHandler> map = new ConcurrentHashMap<>();

    public static CuratorHandler getHandler(@NotNull String conn) throws NoSuchFieldException, IllegalAccessException {

        CuratorHandler client = map.get(conn);

        if (null == client) {


            try {
                // split host and port
                PointModel model = ParamUtil.parsePointModel(conn);

                client = new CuratorHandler("zookeeper", model.getIp(), model.getPort());
                // connect to zk
                client.doConnect();
                // async connecting, so we should wait a few second.
                Thread.sleep(1000);
                if (client.isAvailable()) {
                    // cache client for reuse
                    map.putIfAbsent(conn, client);
                } else {
                    client.close();
                }

            } catch(Exception e) {
                throw new DubboTestException(StringUtil.format("can't connect to {}, {}", conn, e.getMessage()));
            }
        }

        return client;
    }
}
