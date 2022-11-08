package qx.app.study;

import java.io.Serializable;

/**
 * @ProjectName: Study
 * @Package: qx.app.study
 * @ClassName: TestBean
 * @Description: java类作用描述
 * @Author: 张耀
 * @CreateDate: 2021/9/9 15:44
 * @UpdateUser: 更新者：
 * @UpdateDate: 2021/9/9 15:44
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */

/**
 * Serializable 序列化
 */
public class TestBean implements Serializable{
    String name;
    int age;

    public TestBean(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public TestBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
