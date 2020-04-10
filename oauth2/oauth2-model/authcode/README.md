###操作方式


###1，获取授权码

##通过配制的用户名和密码登录security.user.name
##浏览器请求
http://localhost:8080/oauth/authorize?client_id=liujunapp&redirect_uri=http://localhost:9002/callback&response_type=code&scope=read
#响应可获取授权码
http://localhost:9001/callback?code=ORuBtm

##2，获取访问令牌
#请求
curl -X POST 
--user liujunapp:123456 http://localhost:8080/oauth/token 
-H "content-type: application/x-www-form-urlencoded" 
-d "code=8uYpdo&grant_type=authorization_code&
redirect_uri=http%3A%2F%2Flocalh ost%3A9001%2Fcallback&scope=read_userinfo"

#响应1
{
    "access_token": "c10f2467-e966-40d4-8617-df6676b9952c",
    "token_type": "bearer",
    "expires_in": 43199,
    "scope": "read_userinfo"
}
#响应2，在添加了支持刷新令牌后返回
{
    "access_token": "7d06fe06-18b8-4fd2-af42-29848af3d559",
    "token_type": "bearer",
    "refresh_token": "c6caacbb-7989-4b43-a260-fcf1841f9c4c",
    "expires_in": 43199,
    "scope": "read"
}

##3携带令牌访问资源
#请求
curl -X GET http://localhost:8080/data/getDataInfo -H "authorization: Bearer c10f2467-e966-40d4-8617-df6676b9952c"
#响应
{
    "hight": "172",
    "userage": "27",
    "username": "feifei"
}


##4刷新令牌
#1,添加刷新令牌模式
 .authorizedGrantTypes("authorization_code","refresh_token")
#2,刷新令牌
#请求
curl -X POST 
--user liujunapp:123456 http://localhost:8080/oauth/token 
-H "content-type: application/x-www-form-urlencoded" 
-d "grant_type=refresh_token&refresh_token=c6caacbb-7989-4b43-a260-fcf1841f9c4c
&client_id=liujunapp&client_secret=123456&redirect_uri=http://localhost:9002/callback&scope=read"
                        
#响应
{
    "access_token": "5b3ca651-af59-451e-acf1-9d11cf6e6eaa",
    "token_type": "bearer",
    "refresh_token": "c6caacbb-7989-4b43-a260-fcf1841f9c4c",
    "expires_in": 43199,
    "scope": "read"
}

