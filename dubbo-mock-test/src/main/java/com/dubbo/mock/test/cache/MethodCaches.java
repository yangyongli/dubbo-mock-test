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

import com.dubbo.mock.test.dto.MethodModelDTO;
import com.dubbo.mock.test.util.MD5Util;
import com.dubbo.mock.test.util.StringUtil;
import com.dubbo.mock.test.model.MethodModel;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCaches {

    private final static Map<String, MethodModel> map = new ConcurrentHashMap<>();

    /**
     * cache the method object so we can get them next time quickly.
     *
     * @param interfaceName
     * @param methods
     * @return
     */
    public static List<MethodModelDTO> cache(final String interfaceName, Method[] methods) {

        List<MethodModelDTO> ret = new ArrayList<>();

        Arrays.stream(methods).forEach(m -> {

            String key = generateMethodKey(m, interfaceName);

            MethodModel model = new MethodModel(key, m);

            ret.add(new MethodModelDTO(model));

            map.putIfAbsent(key, model); // add to cache

        });


        return ret;
    }

    private static String generateMethodKey(Method method, String interfaceName) {
        return StringUtil.format("{}#{}", interfaceName, MD5Util.encrypt(method.toGenericString()));
    }

    public static MethodModel get(@NotNull String key) {
        return map.get(key);
    }
}
