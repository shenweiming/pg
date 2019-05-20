package cn.ecust.user.service;

import cn.ecust.PgApplication;
import cn.ecust.user.dao.UserRepository;
import cn.ecust.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Auther: SWM
 * @Date: 2019/5/14 11:55
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes =PgApplication.class)
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void findByUsername() {

        List<User> all = userRepository.findAll();
        for (User user:all
             ) {
            System.out.println(user);
        }
        return;
    }
    @Test
    public void testFindByName(){
        String name="2501236550@qq.com";
        User byUsername = this.userRepository.findByMail(name);
        System.out.println(byUsername);
    }
}