public class ProjectVersion {

    /**
     * 大版本号
     */
    private int major

    /**
     * 小版本号
     */
    private int minor
    public ProjectVersion(int major, int minor) {
        this.major = major
        this.minor = minor
    }
    public int getMajor() {
        major
    }
    public void setMajor(int major) {
        this.major = major
    }
}

ProjectVersion projectversion = new ProjectVersion(8,10)
println projectversion.major
projectversion.setMajor(11)
println projectversion.major
ProjectVersion v2 = null
println projectversion == v2

//与java不同
//1，默认public
//2,不用写get与set方法，
//3,不用写;号
//4.在外部可直接访问私有
//5,使用== 比较不会有NullPointException
