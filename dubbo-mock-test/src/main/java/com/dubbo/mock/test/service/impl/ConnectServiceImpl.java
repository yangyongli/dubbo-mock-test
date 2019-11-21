/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dubbo.mock.test.cache.CuratorCaches;
import com.dubbo.mock.test.cache.MethodCaches;
import com.dubbo.mock.test.cache.UrlCaches;
import com.dubbo.mock.test.client.DubboMockClient;
import com.dubbo.mock.test.context.ResponseDispatcher;
import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.dto.UrlModelDTO;
import com.dubbo.mock.test.exception.DubboTestException;
import com.dubbo.mock.test.handler.CuratorHandler;
import com.dubbo.mock.test.model.MethodModel;
import com.dubbo.mock.test.model.ServiceModel;
import com.dubbo.mock.test.model.UrlModel;
import com.dubbo.mock.test.service.ConnectService;
import com.dubbo.mock.test.util.ParamUtil;
import com.dubbo.mock.test.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.Constants;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.rpc.RpcInvocation;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Joey
 * @date 2018/6/18 17:10
 */
@Service("connectService")
@Slf4j
public class ConnectServiceImpl implements ConnectService {

    @Override
    public ResultDTO<String> send(@NotNull ConnectDTO dto) throws Exception {

        log.info("begin to send {} .", JSON.toJSONString(dto));

        // get provider url
        URL url = UrlCaches.get(dto.getProviderKey()).getUrl();
        // get method
        MethodModel methodModel = MethodCaches.get(dto.getMethodKey());
        // parse parameter
        Object[] params = ParamUtil.parseJson(dto.getJson(), methodModel.getMethod());


        url = url.addParameter(Constants.CODEC_KEY, "dubbo"); // 非常重要，必须要设置编码器协议类型



        DubboMockClient client = new DubboMockClient(url);
        client.doConnect();

        // set the path variables
        Map<String, String> map = ParamUtil.getAttachmentFromUrl(url);

        // create request.
        Request req = new Request();
        req.setVersion("2.0.0");
        req.setTwoWay(true);
        req.setData(new RpcInvocation(methodModel.getMethod(), params, map));

        client.send(req);

        //sleep一定时间，让client有时间发送数据，并注册future,否则getFuture取不到，主线程太快
        Thread.sleep(5000);

        int timeout = (0 == dto.getTimeout()) ? 10 : dto.getTimeout(); // send timeout
        CompletableFuture<Object> future = ResponseDispatcher.getDispatcher().getFuture(req);
        Object result = future.get(timeout, TimeUnit.SECONDS);
        ResponseDispatcher.getDispatcher().removeFuture(req);

        return ResultDTO.createSuccessResult("SUCCESS",
                JSON.toJSONString(result, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat),
                String.class);
    }

    @Override
    public List<UrlModelDTO> listProviders(@NotNull ConnectDTO connect) throws NoSuchFieldException, IllegalAccessException {

        // get client
        CuratorHandler client = CuratorCaches.getHandler(connect.getConn());

        if (null == client) {
            throw new DubboTestException("the cache is validate, please reconnect to zk againt.");
        }

        List<UrlModel> providers = client.getProviders(connect);

        // throw fast json error if you don't convert simple pojo
        // I have no idea why the UrlModel object will throw stack over flow exception.
        List<UrlModelDTO> ret = new ArrayList<>();
        providers.forEach(p -> {
            UrlModelDTO m = new UrlModelDTO();
            m.setKey(p.getKey());
            m.setHost(p.getUrl().getHost());
            m.setPort(p.getUrl().getPort());

            ret.add(m);
        });

        return ret;

    }

    /**
     * connect to zk and get all providers.
     *
     * @param conn
     * @return
     */
    @Override
    public List<ServiceModel> connect(@NotNull String conn) throws NoSuchFieldException, IllegalAccessException {

        // get client
        CuratorHandler client = CuratorCaches.getHandler(conn);

        if (!client.isAvailable()) {
            throw new DubboTestException(StringUtil.format("can't connect to {}", conn));
        }

        // get providers
        List<ServiceModel> list = client.getInterfaces();


        return list;
    }
}
