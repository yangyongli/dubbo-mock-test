/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.util;


import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
public class JsonFileUtil {

    /**
     * 从JSON文件流中读取列表.
     */
    public static <T> List<T> readList(InputStream inputStream, Class<T> clazz) throws IOException {

        BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();

        String sTempOneLine;

        while ((sTempOneLine = tBufferedReader.readLine()) != null) {

            sb.append(sTempOneLine);

        }

        return JSON.parseArray(sb.toString(), clazz);

    }

    /**
     * 从JSON文件流中读取对象.
     */
    public static <T> T readObject(InputStream inputStream, Class<T> clazz) throws IOException {

        BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder sb = new StringBuilder();

        String sTempOneLine;

        while ((sTempOneLine = tBufferedReader.readLine()) != null) {

            sb.append(sTempOneLine);

        }

        return JSON.parseObject(sb.toString(), clazz);

    }

}
