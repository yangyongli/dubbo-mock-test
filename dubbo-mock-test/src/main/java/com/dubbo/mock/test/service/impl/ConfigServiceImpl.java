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
import com.dubbo.mock.test.cache.RedisResolver;
import com.dubbo.mock.test.context.Const;
import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.exception.DubboTestException;
import com.dubbo.mock.test.model.RegistryModel;
import com.dubbo.mock.test.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Joey
 * @date 2018/7/9 19:41
 */
@Service("configService")
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private RedisResolver redisResolver;

    @Value("classpath:registry.json")
    private Resource resource;

    /**
     * list all registry.
     *
     * @return
     */
    @Override
    public List<RegistryModel> listRegistry() {

        List<RegistryModel> ret = new ArrayList<>();
        Set<Object> list = redisResolver.sMembers(Const.DUBBO_MOCK_REGISTRY_KEY);

        if (CollectionUtils.isNotEmpty(list)) {
            ret = list.stream().map(l -> {
                RegistryModel model = new RegistryModel();
                BeanUtils.copyProperties(l, model);
                return model;
            }).collect(Collectors.toList());
        }

        return ret;
    }

    /**
     * add registry.
     *
     * @param model
     * @return
     */
    @Override
    public ResultDTO<RegistryModel> addRegistry(RegistryModel model) {


        if (null == model) {
            throw new DubboTestException("the paramter can't be null.");
        }
        if (StringUtils.isEmpty(model.getRegistryKey())) {
            throw new DubboTestException("the registryKey can not be null.");
        }
        if (StringUtils.isEmpty(model.getRegistryDesc())) {
            throw new DubboTestException("the registryDesc can not be null.");
        }

        boolean flag = redisResolver.sAdd(Const.DUBBO_MOCK_REGISTRY_KEY, model) > 0;
        if (flag) {
            return ResultDTO.createSuccessResult("success to add registry.", RegistryModel.class);
        } else {
            return ResultDTO.createErrorResult("fail to add registry, check whether if duplicate configuration or not.", RegistryModel.class);
        }
    }

    @Override
    public ResultDTO<RegistryModel> delRegistry(RegistryModel model) {

        if (null == model) {
            throw new DubboTestException("the paramter can't be null.");
        }
        if (StringUtils.isEmpty(model.getRegistryKey())) {
            throw new DubboTestException("the registryKey can not be null.");
        }
        if (StringUtils.isEmpty(model.getRegistryDesc())) {
            throw new DubboTestException("the registryDesc can not be null.");
        }

        boolean flag = redisResolver.sRem(Const.DUBBO_MOCK_REGISTRY_KEY, model) > 0;
        if (flag) {
            return ResultDTO.createSuccessResult("success to delete registry.", RegistryModel.class);
        } else {
            return ResultDTO.createErrorResult("fail to delete registry, check whether if the configuration exists or not.", RegistryModel.class);
        }
    }

    @PostConstruct
    public void loadConfig() {

        log.info("ConfigServiceImpl.loadConfig()");

        loadZkConfigFromResource();

    }

    @Override
    public void loadZkConfigFromResource() {

        try {

            BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            StringBuilder sb = new StringBuilder();

            String sTempOneLine;

            while ((sTempOneLine = tBufferedReader.readLine()) != null) {

                sb.append(sTempOneLine);

            }

            List<RegistryModel> list = JSON.parseArray(sb.toString(), RegistryModel.class);
            redisResolver.sAdd(Const.DUBBO_MOCK_REGISTRY_KEY,list.toArray());


        } catch (Exception e) {

            log.error("occur an error when reading zk address configuration.", e);

        }
    }
}
