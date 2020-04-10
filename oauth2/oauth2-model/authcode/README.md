操作方式

1，获取授权码
通过配制的用户名和密码登录security.user.name
浏览器请求
http://localhost:8080/oauth/authorize?client_id=clientapp&
redirect_uri=http://localhost:9001/callback&response_type=code&scope=read_userinfo

可获取授权码
http://localhost:9001/callback?code=ORuBtm

2，获取访问令牌
curl -X POST 
--user liujunapp:123456 http://localhost:8080/oauth/token 
-H "content-type: application/x-www-form-urlencoded" 
-d "code=8uYpdo&grant_type=authorization_code&
redirect_uri=http%3A%2F%2Flocalh ost%3A9001%2Fcallback&scope=read_userinfo"

响应
{
    "access_token": "c10f2467-e966-40d4-8617-df6676b9952c",
    "token_type": "bearer",
    "expires_in": 43199,
    "scope": "read_userinfo"
}

携带令牌访问资源
curl -X GET http://localhost:8080/data/getDataInfo -H "authorization: Bearer c10f2467-e966-40d4-8617-df6676b9952c"
{
    "hight": "172",
    "userage": "27",
    "username": "feifei"
}