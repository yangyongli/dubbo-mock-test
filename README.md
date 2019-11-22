# dubbo-mock-test

## 感谢VIPJoey在GitHub上分享代码,原项目(https://github.com/VIPJoey/doe)
- 修改增加依赖功能（增加exclusion标签支持）
- 升级项目中spring boot与duboo包 springboot2.18,dubbo2.7.3
- 修改增加依赖后完成跳回依赖列表，菜单空白BUG
- 增加项目启动时加载jar中class方法（依赖的jar中的）
- 修改自定义的classloader。目的是在项目使用时不重复加载依赖的class
- 修改dubbo接口调用方式，修改成netty4支持的nettyclient调用，
- 修复nettyclient#send与主线程不同步，无法及时注册channelHanlder问题（通过sleep，后续有待优化）
- 修复极简模式telnet调用方式返回结果BUG问题。

以上问题可能是因为修改springboot与dubbo版本引起的，欢迎相互沟通（wo4yyl@163.com）


## 目录结构
- dubbo-mock-test 主项目，实现dubbo接口调试。
- deploy文件夹中，pom.xml为热加载dubbo测试依赖包的示例pom，deploy.sh为linux下启动示便脚本。

## 功能特性
- 简单模式：通过dubbo提供的telnet协议收发数据,端口号为dubbo服务的端口
- 普通模式：使用netty4客户端nettyclient收发数据。
- 用例模式：通过缓存数据，方便下一次操作，依赖普通模式。
- 增加依赖：通过调用maven命令，下载jar包和热加载到系统，主要用来分析接口方法参数，主要作用在普通模式（已过时，请使用【依赖编辑】模块）。
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