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
import com.dubbo.mock.test.dto.ConnectDTO;
import com.dubbo.mock.test.dto.MethodModelDTO;
import com.dubbo.mock.test.dto.ResultDTO;
import com.dubbo.mock.test.dto.UrlModelDTO;
import com.dubbo.mock.test.model.ServiceModel;
import com.dubbo.mock.test.service.ClassService;
import com.dubbo.mock.test.service.ConnectService;
import com.dubbo.mock.test.service.DirectConnectService;
import com.dubbo.mock.test.service.TelnetService;
import com.dubbo.mock.test.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/doe/dubbo")
@Slf4j
public class DubboController {

    @Autowired
    private ConnectService connectService;

    @Autowired
    private ClassService classService;

    @Autowired
    private TelnetService telnetService;

    @Autowired
    private DirectConnectService directConnectService;

    @RequestMapping("/doSendWithTelnet")
    public ResultDTO<String> doSendWithTelnet(@NotNull ConnectDTO dto) {

        log.info("DubboController.doSendWithTelnet({})", JSON.toJSONString(dto));

        ResultDTO<String> resultDTO;

        try {

            resultDTO = telnetService.send(dto);

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }

        return resultDTO;
    }

    @RequestMapping("/doSend")
    public ResultDTO<String> doSend(@NotNull ConnectDTO dto) {

        log.info("DubboController.doSend({})", JSON.toJSONString(dto));

        ResultDTO<String> resultDTO;

        try {

            resultDTO = connectService.send(dto);

        } catch(Exception e) {
            e.printStackTrace();
            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }

        return resultDTO;
    }

    @RequestMapping("/doListParams")
    public ResultDTO<String> doListParams(@NotNull MethodModelDTO dto) {

        log.info("DubboController.doListParams({})", JSON.toJSONString(dto));

        ResultDTO<String> resultDTO;

        try {

            resultDTO = classService.generateMethodParamsJsonString(dto);

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }


        return resultDTO;
    }

    @RequestMapping("/doListMethods")
    public ResultDTO<Object> doListMethods(@NotNull ConnectDTO dto) {

        log.info("DubboController.doListMethods({})", dto.getProviderKey());

        ResultDTO<Object> resultDTO = new ResultDTO<>();

        try {

            List<MethodModelDTO> models = classService.listMethods(dto);
            if (CollectionUtils.isEmpty(models)) {

                resultDTO = ResultDTO.createErrorResult(StringUtil.format("no methods for {}.",
                        dto.getServiceName()), Object.class);

            } else {

                log.info("methods: {}", JSON.toJSONString(models));
                resultDTO.setData(models);
                resultDTO.setSuccess(true);

            }

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, Object.class);
            resultDTO.setMsg("occur an error when get methods : " + resultDTO.getMsg());
        }

        return resultDTO;
    }

    @RequestMapping("/doListProviders")
    public ResultDTO<Object> doListProviders(@NotNull ConnectDTO dto) {

        log.info("DubboController.doListProviders({} {} {})", dto.getServiceName(), dto.getVersion(), dto.getGroup());

        ResultDTO<Object> resultDTO = new ResultDTO<>();

        try {


            List<UrlModelDTO> models = connectService.listProviders(dto);
            if (CollectionUtils.isEmpty(models)) {

                resultDTO = ResultDTO.createErrorResult(StringUtil.format("no provider for {} in this zookeeper registry.",
                        dto.getServiceName()), Object.class);

            } else {

                log.info("providers: {}", JSON.toJSONString(models));
                resultDTO.setData(models);
                resultDTO.setSuccess(true);

            }

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, Object.class);
            resultDTO.setMsg("occur an error when get provider : " + resultDTO.getMsg());
        }

        return resultDTO;
    }

    @RequestMapping("/doConnect")
    public ResultDTO<Object> doConnect(@NotNull String conn) {

        log.debug("DubboController.doConnect({})", conn);

        ResultDTO<Object> resultDTO = new ResultDTO<>();

        try {

            List<ServiceModel> models = connectService.connect(conn);
            if (CollectionUtils.isEmpty(models)) {

                resultDTO = ResultDTO.createErrorResult("no provider for this this zookeeper registry.", Object.class);

            } else {

                resultDTO.setData(models);
                resultDTO.setSuccess(true);

            }

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, Object.class);
        }

        return resultDTO;

    }

    @RequestMapping("/doDirectSend")
    public ResultDTO<String> doDirectSend(@NotNull ConnectDTO dto) {

        log.info("DubboController.doDirectSend({})", JSON.toJSONString(dto));

        ResultDTO<String> resultDTO;

        try {

            resultDTO = directConnectService.send(dto);

        } catch(Exception e) {

            resultDTO = ResultDTO.createExceptionResult(e, String.class);
        }

        return resultDTO;
    }

}
