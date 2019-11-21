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

import com.alibaba.dubbo.common.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dubbo.mock.test.cache.MethodCaches;
import com.dubbo.mock.test.cache.RedisResolver;
import com.dubbo.mock.test.cache.UrlCaches;
import com.dubbo.mock.test.client.DubboMockClient;
import com.dubbo.mock.test.context.Const;
import com.dubbo.mock.test.context.DubboMockClassLoader;
import com.dubbo.mock.test.context.ResponseDispatcher;
import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.exception.DubboTestException;
import com.dubbo.mock.test.model.CaseModel;
import com.dubbo.mock.test.service.CaseService;
import com.dubbo.mock.test.util.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.rpc.RpcInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@Service("caseService")
@Slf4j
public class CaseServiceImpl implements CaseService {

    @Autowired
    private RedisResolver redisResolver;

    private static final AtomicLong counter = new AtomicLong();

    /**
     * save the case.
     *
     * @param model
     * @return
     */
    @Override
    public ResultDTO<CaseModel> save(@NotNull CaseModel model) {

        if (StringUtils.isEmpty(model.getProviderKey())) {
            throw new DubboTestException("获取不到提供者！");
        }
        if (StringUtils.isEmpty(model.getMethodKey())) {
            throw new DubboTestException("获取不到方法！");
        }

        model.setAddress(UrlCaches.get(model.getProviderKey()).getUrl().getAddress());
        model.setInterfaceName(UrlCaches.get(model.getProviderKey()).getUrl().getParameter(Constants.INTERFACE_KEY ));
        model.setMethodText(MethodCaches.get(model.getMethodKey()).getMethodText());
        model.setCaseId(counter.getAndAdd(1));
        model.setInsertTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        redisResolver.rPush(Const.DUBBO_MOCK_CASE_KEY, model);

        //保存case时将getProviderKey与url 保存到redis中，目的是使重新启动项目直接通过case页面访问。
        redisResolver.set(model.getProviderKey(),UrlCaches.get(model.getProviderKey()).getUrl().toFullString());


        // TODO
        // save to db.

        return ResultDTO.createSuccessResult("SUCCESS", model, CaseModel.class);
    }

    /**
     * list all case.
     *
     * @return
     */
    @Override
    public List<Object> listAll() {

        List<Object> list = redisResolver.lGet(Const.DUBBO_MOCK_CASE_KEY, 0, -1);

        if (CollectionUtils.isEmpty(list)) {

            // TODO
            // get from db and put them to cache.

        }

        return list;
    }

    @Override
    public ResultDTO<String> send(@NotNull ConnectDTO dto) throws Exception {

        log.info("begin to send {} .", JSON.toJSONString(dto));

        // get provider url
        URL url = URL.valueOf((String)redisResolver.get(dto.getProviderKey()));
        // get method
       // MethodModel methodModel = MethodCaches.get(dto.getMethodKey());

        // parse parameter
        Class c = DubboMockClassLoader.getInstance().loadClass(dto.getInterfaceName());

        String methodName = dto.getMethodName().split("\\(")[0];
        Method method = null;
        Method[]  methods = c.getDeclaredMethods();
        for (Method m: methods) {
            if(methodName.equals(m.getName())){
                method = m;
            }
        }

        Object[] params = ParamUtil.parseJson(dto.getJson(), method);

        url = url.addParameter(org.apache.dubbo.remoting.Constants.CODEC_KEY, "dubbo"); // 非常重要，必须要设置编码器协议类型

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
