
const appList = [{
  appId: 'ruoyi',
  appType: 'basic-platform',
  title: '若依管理系统',
  link: 'http://doc.ruoyi.vip/',
  desc: 'RuoYi 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring Security、MyBatis、Jwt、Vue），内置模块如：部门管理、角色用户、菜单及按钮授权、数据权限、系统参数、日志管理、代码生成等。在线定时任务配置；支持集群，支持多数据源，支持分布式事务。。',
  rules: [
    { type: 'unique', value: 'basic-platform', msg: '基础平台只可选择一个，请先将应用列表中的其他基础平台移除！' }
  ]
}, {
  appId: 'yudao',
  appType: 'basic-platform',
  title: '芋道管理系统',
  link: 'https://www.iocoder.cn/',
  desc: '芋道，以开发者为中心，打造中国第一流的快速开发平台，全部开源，个人与企业可 100% 免费使用。多种内置功能：系统功能、工作流程、支付系统、商城系统、基础设施等。',
  rules: [
    { type: 'unique', value: 'basic-platform', msg: '基础平台只可选择一个，请先将应用列表中的其他基础平台移除！' },
    { type: 'serverMode',  value: ['single'], msg: '服务端请选择单应用！'},
    { type: 'webFramework',  value: ['vue2'], msg: '请选择Vue2前端框架！'},
    { type: 'webUI',  value: ['element'], msg: '请选择Element UI前端组件！'},
  ]
// }, {
//   appId: 'jeecg',
//   appType: 'basic-platform',
//   title: 'JEECG BOOT 低代码开发平台',
//   link: 'http://www.jeecg.com/',
//   desc: 'JeecgBoot 是一款基于代码生成器的低代码平台！前后端分离架构 SpringBoot2.x，SpringCloud，Ant Design&Vue，Mybatis-plus，Shiro，JWT，支持微服务。强大的代码生成器让前后端代码一键生成，实现低代码开发! JeecgBoot 引领新的低代码开发模式(OnlineCoding-> 代码生成器-> 手工MERGE)， 帮助解决Java项目70%的重复工作，让开发更多关注业务。既能快速提高效率，节省研发成本，同时又不失灵活性！',
//   rules: [
//     { type: 'unique', value: 'basic-platform', msg: '基础平台只可选择一个，请先将应用列表中的其他基础平台移除！' },
//     { type: 'webFramework',  value: ['vue2'], msg: '请选择Vue2前端框架！'},
//     { type: 'webUI',  value: ['element'], msg: '请选择Element UI前端组件！'},
//   ]
// }, {
//   appId: 'spring-blade',
//   appType: 'basic-platform',
//   title: 'BladeX企业级开发平台',
//   link: 'https://gitee.com/smallc/SpringBlade/',
//   desc: 'SpringBlade 是一个由商业级项目升级优化而来的微服务架构，采用Spring Boot 2.6 、Spring Cloud 2021 等核心技术构建，完全遵循阿里巴巴编码规范。提供基于React和Vue的两个前端框架用于快速搭建企业级的SaaS多租户微服务平台。',
//   rules: [
//     { type: 'inUse', inUse: 0, msg: '功能开发中，敬请期待！' }
//   ]
}];


export default {
  namespaced: true,
  state: {
    config: {
      projectTitle: 'Fastbuild管理平台',
      projectName: 'fastbuild',
      webFramework: 'vue2',
      webUI: 'element',
      mobileFramework: 'uniapp',
      serverMode: 'single',
      packagePrefix: 'com.fastbuild',
      database: 'mysql',
      databaseVersion: '5.6',
      appList: []
    },
    appList: appList
  },
  mutations: {
    setAppList (state, appList) {
      state.appList = appList;
    },
    setConfig (state, config) {
      state.config = config;
    },
  },
  getters: {
  },
  actions: {
  }
}