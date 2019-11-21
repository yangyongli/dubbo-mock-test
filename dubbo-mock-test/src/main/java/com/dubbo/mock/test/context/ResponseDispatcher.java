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

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.remoting.exchange.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ResponseDispatcher {

    private Map<Long, CompletableFuture> futures = new ConcurrentHashMap<>();

    private ResponseDispatcher() {

    }

    @SuppressWarnings("uncheck")
    public CompletableFuture<Object> getFuture(Request req) {
        return futures.get(req.getId());
    }

    public void register(Request req) {
        log.info("-----注册CompletableFuture----");
        log.info("-----注册req.getId---->"+req.getId());
        CompletableFuture future = new CompletableFuture();
        futures.put(req.getId(), future);
    }

    public void dispatch(Response res) {
        log.info("-----将返回结果放入future中");
        log.info("-----注册res.getId---->"+res.getId());
        CompletableFuture future = futures.get(res.getId());
        if (null == future) {
            log.info("-----future为null---->");
            throw new RuntimeException();
        }
        log.info("-----future写入值---->");
        future.complete(res.getResult());
    }

    public CompletableFuture removeFuture(Request req) {
        return futures.remove(req.getId());
    }

    static class ResponseDispatcherHolder {
        static final ResponseDispatcher instance = new ResponseDispatcher();
    }

    public static ResponseDispatcher getDispatcher() {
        return ResponseDispatcherHolder.instance;
    }
}
