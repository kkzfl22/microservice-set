###用户名密码模式最简服务器

##1,获取访问令牌
#请求
curl -X POST --user liujunapp:123456 http://localhost:8080/oauth/token 
-H "accept: application/json" -H "content-type: application/x-www-form-urlencoded"
 -d "grant_type=password&username=bobo&password=xyz&scope=read"
 
#响应
{
    "access_token": "6a6558b7-5c52-48c7-ba35-4a4a14d6824a",
    "token_type": "bearer",
    "expires_in": 43143,
    "scope": "read"
}

##2,访问资源
#请求
curl -X GET http://localhost:8080/data/getDataInfo -H "authorization: Bearer 6a6558b7-5c52-48c7-ba35-4a4a14d6824a"
#响应
{
    "hight": "172",
    "flag": "password",
    "userage": "27",
    "username": "feifei"
}


