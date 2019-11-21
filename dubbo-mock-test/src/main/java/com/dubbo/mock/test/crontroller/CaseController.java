/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.dubbo.mock.test.crontroller;

import com.alibaba.fastjson.JSON;
import com.dubbo.mock.test.dto.CaseModelDTO;
import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.model.CaseModel;
import com.dubbo.mock.test.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doe/case")
@Slf4j
public class CaseController {

    @Autowired
    private CaseService caseService;



    @RequestMapping("/doSend")
    public ResultDTO<String> doSend(@NotNull ConnectDTO dto) {

        log.info("DubboController.doSend({})", JSON.toJSONString(dto));

        ResultDTO<String> resultDTO;

        try {

            resultDTO = caseService.send(dto);

        } catch(Exception e) {
            e.printStackTrace();
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }

        return resultDTO;
    }


    @RequestMapping("/doSave")
    public ResultDTO<CaseModel> doSave(@NotNull CaseModelDTO dto) {

        log.info("CaseController.doSave({})", JSON.toJSONString(dto));

        ResultDTO<CaseModel> resultDTO;

        try {

            CaseModel model = new CaseModel();
            BeanUtils.copyProperties(dto, model);
            resultDTO = caseService.save(model);

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, CaseModel.class);
        }

        return resultDTO;
    }

    @RequestMapping("/doList")
    public String doList(CaseModelDTO dto) {

        log.info("CaseController.doList({})", JSON.toJSONString(dto));

        try {

            List<Object> list = caseService.listAll();

            List<CaseModel> ret = list.stream().map(l -> {
                CaseModel model = new CaseModel();
                BeanUtils.copyProperties(l, model);
                return model;
            }).collect(Collectors.toList());

            return JSON.toJSONString(ret);

        } catch(Exception e) {

            return "[]";
        }
    }
}
