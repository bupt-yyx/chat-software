# chat-software
## 基于Spring MVC架构开发的一款web在线聊天应用系统  
### 简介
>本项目是基于Spring MVC架构开发的一款web在线聊天应用系统
> * 前端使用html+css+js+thymleaf
> * 后端使用spring boot与websocket
> * 数据库选用mysql
> * 前后端交互采用pull模型，大部分情况由前端主动拉取数据，当有新消息时后端通知前端拉取数据，使用了https,存储密码加盐加密,阻止url暴露敏感信息等手段保证安全性。
### 我的任务
> * 实现了基于websocket协议的聊天功能的实现以及在springboot进行装配
> * 大部分controller文件的编写
### 项目收获
> * 学习了springboot框架
> * 学习了websocket，实现了聊天功能
> * 了解了MVC思想，通过前端进行消息传递，后端进行判断和跳转，通过controller完成交互
