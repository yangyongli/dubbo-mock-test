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

import com.alibaba.dubbo.common.Constants;
import com.dubbo.mock.test.cache.MethodCaches;
import com.dubbo.mock.test.cache.UrlCaches;
import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.MethodModelDTO;
import com.dubbo.mock.test.exception.DubboTestException;
import com.dubbo.mock.test.model.ServiceModel;
import com.dubbo.mock.test.model.UrlModel;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.registry.zookeeper.ZookeeperRegistry;
import org.apache.dubbo.remoting.zookeeper.ZookeeperClient;
import org.apache.dubbo.remoting.zookeeper.curator.CuratorZookeeperTransporter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public class CuratorHandler {

    private final String protocol;
    private final String host;
    private final int port;
    private ZookeeperClient zkClient;
    private ZookeeperRegistry registry;
    private String root = "/dubbo";

    public CuratorHandler(String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public void doConnect() throws NoSuchFieldException, IllegalAccessException {

        CuratorZookeeperTransporter zookeeperTransporter = new CuratorZookeeperTransporter();
        URL url = new URL(protocol, host, port);

        registry = new ZookeeperRegistry(url, zookeeperTransporter);

        Field field = registry.getClass().getDeclaredField("zkClient");
        field.setAccessible(true);
        zkClient = (ZookeeperClient) field.get(registry);

    }

    public List<ServiceModel> getInterfaces() {
        List<ServiceModel> ret = new ArrayList<>();
        List<String> list = zkClient.getChildren(root);
        for (int i = 0; i < list.size(); i++) {
            ServiceModel model = new ServiceModel();
            model.setServiceName(list.get(i));
            ret.add(model);
        }

        return ret;
    }

    public List<UrlModel> getProviders(ConnectDTO dto) {

        if (null == dto) {
            throw new DubboTestException("dto can't be null.");
        }
        if (StringUtils.isEmpty(dto.getServiceName())) {
            throw new DubboTestException("service name can't be null.");
        }

        Map<String, String> map = new HashMap<>();
        map.put(Constants.INTERFACE_KEY, dto.getServiceName());

        if (StringUtils.isNotEmpty(dto.getVersion())) {
            map.put(Constants.VERSION_KEY, dto.getVersion());
        }
        if (StringUtils.isNotEmpty(dto.getGroup())) {
            map.put(Constants.GROUP_KEY, dto.getGroup());
        }

        URL url = new URL(protocol, host, port, map);
        List<URL> list = registry.lookup(url);

        return UrlCaches.cache(dto.getServiceName(), list);
    }

    public List<MethodModelDTO> getMethods(String interfaceName) throws ClassNotFoundException {

        Class<?> clazz = Class.forName(interfaceName);
        Method[] methods = clazz.getMethods();

        return MethodCaches.cache(interfaceName, methods); // 缓存一份，方便下次调用

    }

    public void close() {
        registry.destroy();
    }

    public boolean isAvailable() {
        return registry.isAvailable();
    }
}
