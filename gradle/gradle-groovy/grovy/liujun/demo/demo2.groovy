//groovy 高效的类型
//1,可选的类型
def version = 1
//print version

//2,assert断言，可以任何地方执行
//assert version == 2

//3，括号是可选的
//println(version)
//println version

//4,字符串
def s1 = 'liujun'
def s2 = "myname is ${version}"
def s3 = '''
this
is
my name
'''

//println s1
//println s2
//println s3

//5,集合
def buildTools = ['ant','maven']
buildTools << 'gradle'
assert buildTools.getClass() == ArrayList
assert buildTools.size() == 3
//println buildTools

def map = ['ant':2000,'maven':2004]
map.gradle = 2009
//println map
//println map.ant
//println map['gradle']
//println map.getClass()


//6,闭包
//可用多种方式
def c1 = {
    v ->
        print v
}

def c2 ={
    print 'hello'
}

def method(Closure cluse) {
    cluse('param')
}

def method2(Closure clus2) {
    clus2();
}

method(c1)
method2(c2)