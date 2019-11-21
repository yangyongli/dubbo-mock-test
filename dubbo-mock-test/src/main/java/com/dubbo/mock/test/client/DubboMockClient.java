/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.client;

import com.dubbo.mock.test.handler.SendReceiveHandler;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.transport.netty4.NettyClient;



public class DubboMockClient extends NettyClient {

    public DubboMockClient(URL url) throws RemotingException {
        super(url, new SendReceiveHandler());
    }

    @Override
    public void doConnect()  {
        try {
            super.doConnect();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
