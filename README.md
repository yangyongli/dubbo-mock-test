# dubbo-mock-test

## 目录结构
- dubbo-mock-test 主项目，实现dubbo接口调试。
- deploy文件夹中，pom.xml为热加载dubbo测试依赖包的示例pom，deploy.sh为linux下启动示便脚本。

## 功能特性
- 简单模式：通过dubbo提供的telnet协议收发数据,端口号为dubbo服务的端口
- 普通模式：使用netty4客户端nettyclient收发数据。
- 用例模式：通过缓存数据，方便下一次操作，依赖普通模式。
- 增加依赖：通过调用maven命令，下载jar包和热加载到系统(支持exclusion)，主要用来分析接口方法参数，主要作用在普通模式（已过时，请使用【依赖编辑】模块）。
- 依赖列表：通过分析pom文件，展示已经加载的jar包。
- 依赖编辑：可以直接编辑pom文件，新增修改依赖jar。
- 注册中心：可以添加或删除zookeeper注册中心。
- 系统配置：可以清空jar或者重新加载jar。

## 开发使用
- jdk1.8
- spring boot 2.18
- maven 3.6
- dubbo 2.7.3
- springboot 整合 thymeleaf
- springboot 整合 logback
- netty4的nettyclient

## 配置文件说明
- application.yml中的有配置环境的绝对目录，使用时请配置好当前所使用的目录，目的存放dubbo接口依赖的pom.xml，jar包
- redis配置成相应的地址。
- 端口配置成相当的端口

## 访问说明
- 项目启动后，访问HomeController中的index入口