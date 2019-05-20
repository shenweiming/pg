package cn.ecust.user.dao;


import cn.ecust.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * @Auther: SWM
 * @Date: 2019/4/20 14:10
 * @Description:
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByMail(String mail);
}
