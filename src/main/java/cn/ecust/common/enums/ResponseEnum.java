package cn.ecust.common.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: SWM
 * @Date: 2019/5/16 23:23
 * @Description:
 */
@Getter
@NoArgsConstructor
public enum ResponseEnum {

    USER_DATA_NOT_EXIST(0,"可以使用"),
    USER_DATA_EXIST(1,"信息已存在"),
    ERROR_CODE(1,"验证码错误"),
    REFISTER_SUCCESS(0,"注册成功"),
    LOGIN_ERROR(1,"用户名或密码错误"),
    LOGIN_SUCCESS(0,"登陆成功"),
    CHANGEPASSWORD_SUCCESS(0,"修改密码成功"),
    CHANGEPASSWORD_ERROR(0,"修改密码失败"),
    ;
    private int code;

    private String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
