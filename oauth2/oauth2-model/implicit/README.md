### 简化模式操作
##1，请求获取令牌
# 请求，使用浏览器
http://localhost:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://localhost:9001/callback&response_type=token&scope=read_userinfo&state=abc
#响应
http://localhost:9002/callback#access_token=f56d93cd-9bfc-4d89-9c32-4afabae146fb&token_type=bearer&state=abc&expires_in=120

##2，访问资源
#请求
curl -X GET http://localhost:8080/data/getDataInfo -H "authorization: Bearer f56d93cd-9bfc-4d89-9c32-4afabae146fb"
#响应
{
    "hight": "172",
    "flag": "implicit",
    "userage": "27",
    "username": "feifei"
}