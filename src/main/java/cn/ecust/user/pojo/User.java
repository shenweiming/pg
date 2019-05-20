package cn.ecust.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: SWM
 * @Date: 2019/4/20 14:06
 * @Description: 
 */
@Entity()
@Table(name = "tb_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="username")
    private String username;// 用户名
    @Column(name="password")
    @JsonIgnore
    private String password;// 密码
    @Column(name="mail")
    private String mail;// 邮箱
    @Column(name="created")
    private Date created;// 创建时间
    @Column(name="salt")
    @JsonIgnore
    private String salt;// 密码的盐值

}
