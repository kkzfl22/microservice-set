###apollo配制是携程开源的一款配制中心产品
##客户端接入配制
#1，配制appid
META-INF/app.properties文件中配制
app.id=应用的id
在0.7版本后，可通过system properties文件中配制
-Dapp.id=应用的id


##2,配制metaserver地址
#启动参数-Ddev_meta=http://someip:8080
# 或者classpath中单独一份app-env.properties文件中配制(推荐）
local.meta=http://localhost:8080
dev.meta=http://dev.com
fat.meta=http://fat.com
uat.meta=http://ua.com
pro.meta=http://dev.com


##3，运行环境设置
# 启动参数-Denv=YOUR-env注意key小写
# 配制文件(推荐)
mac/linux /opt/settings/server.properties文件中配制
windows c:/opt/settings/server.properties
# 格式env=DEV/FAT/UAT/PRO/local
# 本地开发模式local