### 基于客户端模式的最简oauth2服务器

## 1 获取访问令牌
#请求
curl -X POST "http://localhost:8080/oauth/token" --user clientdevops:789
 -d "grant_type=client_credentials&scope=devops"
#响应
{
    "access_token": "9d3109d7-a85f-4f58-9d39-d87e39ff9b46",
    "token_type": "bearer",
    "expires_in": 43199,
    "scope": "read"
}

## 2 携带token访问api资源
#请求
curl -X GET http://localhost:8080/api/userlist -H "authorization: Bearer 9d3109d7-a85f-4f58-9d39-d87e39ff9b46"
#响应
{
    "hight": "172",
    "flag": "client",
    "userage": "27",
    "username": "feifei"
}
