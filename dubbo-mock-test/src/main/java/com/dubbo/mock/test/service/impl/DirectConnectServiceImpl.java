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
import com.dubbo.mock.test.client.DubboMockClient;
import com.dubbo.mock.test.context.DubboMockClassLoader;
import com.dubbo.mock.test.context.ResponseDispatcher;
import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.model.PointModel;
import com.dubbo.mock.test.service.DirectConnectService;
import com.dubbo.mock.test.util.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.Constants;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.rpc.RpcInvocation;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Joey
 * @date 2018/6/18 17:10
 */
@Service("directConnectService")
@Slf4j
public class DirectConnectServiceImpl implements DirectConnectService {

    @Override
    public ResultDTO<String> send(@NotNull ConnectDTO dto) throws Exception {

        log.info("DirectConnectService begin to send {} .", JSON.toJSONString(dto));

        PointModel model = ParamUtil.parsePointModel(dto.getConn());

        // get provider url
        //organize url parameter
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("interface", dto.getServiceName());
        URL url = new URL("dubbo", model.getIp(), model.getPort(), urlParams);
        // get method
        Class c = DubboMockClassLoader.getInstance().loadClass(dto.getServiceName());

        String methodName = dto.getMethodName().split("\\(")[0];
        Method method = null;
        Method[]  methods = c.getDeclaredMethods();
        for (Method m: methods) {
            if(methodName.equals(m.getName())){
                method = m;
            }
        }
        // parse parameter
        Object[] params = ParamUtil.parseJson(dto.getJson(), method);

        url = url.addParameter(Constants.CODEC_KEY, "dubbo"); // 非常重要，必须要设置编码器协议类型
        DubboMockClient client = new DubboMockClient(url);
        client.doConnect();

        // set the path variables
        Map<String, String> map = ParamUtil.getAttachmentFromUrl(url);

        // create request.
        Request req = new Request();
        req.setVersion("2.0.0");
        req.setTwoWay(true);
        req.setData(new RpcInvocation(method, params, map));

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
}
