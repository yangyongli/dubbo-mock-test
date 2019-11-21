/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.handler;

import com.alibaba.fastjson.JSON;
import com.dubbo.mock.test.context.ResponseDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.remoting.exchange.Response;

@Slf4j
public class SendReceiveHandler implements ChannelHandler {


    @Override
    public void connected(Channel channel) throws RemotingException {
        log.info("SendReceiveHandler.connected");
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        log.info(channel.getUrl().toFullString());
        log.info("SendReceiveHandler.disconnected");
    }

    @Override
    public void sent(Channel channel, Object message) throws RemotingException {

        log.info("SendReceiveHandler.sent");

        if (message instanceof Request) {

            Request req = (Request) message;
            ResponseDispatcher.getDispatcher().register(req);
            log.info("message instanceof Request.");
        }
    }

    @Override
    public void received(Channel channel, Object message) throws RemotingException{

        log.info("SendReceiveHandler.received({})", JSON.toJSONString(message));

        if (message instanceof Response) {

            Response res = (Response) message;

            if (res.getStatus() == Response.OK) {
                try {
                    log.info("走这里", JSON.toJSONString(message));
                     ResponseDispatcher.getDispatcher().dispatch(res);

//                    if (res.getResult() instanceof AppResponse) {
//                        ResponseDispatcher.getDispatcher().dispatch(res);
//                    }

                } catch (Exception e) {
                    log.error("callback invoke error .result:" + res.getResult() + ",url:" + channel.getUrl(), e);
                }
            } else if (res.getStatus() == Response.CLIENT_TIMEOUT || res.getStatus() == Response.SERVER_TIMEOUT) {
                try {
                    TimeoutException te = new TimeoutException(res.getStatus() == Response.SERVER_TIMEOUT, channel, res.getErrorMessage());
//                    callbackCopy.caught(te);
                } catch (Exception e) {
                    log.error("callback invoke error ,url:" + channel.getUrl(), e);
                }
            } else {
                try {
                    RuntimeException re = new RuntimeException(res.getErrorMessage());
//                    callbackCopy.caught(re);
                } catch (Exception e) {
                    log.error("callback invoke error ,url:" + channel.getUrl(), e);
                }
            }
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        log.error("SendReceiveHandler.caught", exception);
    }

}
