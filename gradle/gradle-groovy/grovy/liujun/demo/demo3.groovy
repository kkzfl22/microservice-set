//构建脚本 中默认都有个project实例
//在project中有个project中有个apply方法，参数是plugin:'java'
apply plugin:'java'

//设置project实例中的version中的版本号
version = '0.0.1'

//闭包参数调用mavenCentral()
repositories{
    mavenCentral()
}

//闭包以一个属性做为参数
dependencies{
    compile:'commons-codec:commons-codec:1.6'
}