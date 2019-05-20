package cn.ecust.user.controller;

import cn.ecust.common.enums.ExceptionEnum;
import cn.ecust.common.enums.ResponseEnum;
import cn.ecust.common.exception.LyException;
import cn.ecust.common.vo.CommonResult;
import cn.ecust.user.pojo.User;
import cn.ecust.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: SWM
 * @Date: 2019/4/20 14:12
 * @Description: 
 */
@Slf4j
@RestController
@Api(description = "用户接口")
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserService userService;
    /**
     *
     * 功能描述:进行用户输入数据校验，type 1为用户名，type 2为邮箱
     * @param:
     * @return:
     * @auther: SWM
     * @date: 2019/4/20 14:36
     */
    @ApiOperation(value = "用户信息校验" ,  notes="用户信息校验")
    @GetMapping("user/check/{data}/{type}")
    public CommonResult<Void> checkUserData(@ApiParam(name = "data", value = "校验的数据", required = true) @PathVariable("data") String data,@ApiParam(name = "type", value = "数据类型，1为用户名，2为邮箱", required = true) @PathVariable(value = "type") Integer type) {
        Boolean boo = this.userService.checkData(data, type);
        if (boo) {
            return new CommonResult<>(ResponseEnum.USER_DATA_NOT_EXIST,HttpStatus.OK);
        }
        return new CommonResult<>(ResponseEnum.USER_DATA_EXIST,HttpStatus.BAD_REQUEST);
    }

   /** @PostMapping("user/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
        userService.sendVerifyCode(phone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    } **/
    @ApiOperation(value = "发送验证码接口" ,  notes="发送验证码接口")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "mail", value = "邮箱", required = true, dataType = "String",paramType = "query")
            })
    @PostMapping("user/verifyMail")
    public ResponseEntity<Void> sendVerifyMailCode(@RequestParam("mail") String mail) {
        userService.sendVerifyMailCode(mail);
        return new ResponseEntity<>(HttpStatus.CREATED);
}
    //注册接口
    @ApiOperation(value = "注册接口" ,  notes="校验验证码注册")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "user", value = "用户信息", required = true, dataType = "User"),
                    @ApiImplicitParam(name = "code", value = "验证码", required = true,dataType = "String",paramType = "query")
            })
    @PostMapping("user/register")
    public CommonResult<Void> register(User user, @RequestParam("code") String code) {
        if(code==null){
            log.info("验证码不存在");
            return new CommonResult<Void>(ResponseEnum.ERROR_CODE,HttpStatus.BAD_REQUEST);
        }
        Boolean boo = this.userService.register(user, code);
        if (!boo) {
            log.info("注册失败" );
            return new CommonResult<Void>(ResponseEnum.ERROR_CODE,HttpStatus.BAD_REQUEST);
        }
        return new CommonResult<Void>(ResponseEnum.REFISTER_SUCCESS,HttpStatus.OK);

    }
    @ApiOperation(value = "用户登录接口" ,  notes="用户登录接口")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",paramType = "query"),
                    @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String",paramType = "query"),
                    @ApiImplicitParam(name = "typeOfName", value = "用户名类型", required = true, dataType = "String",paramType = "query")
            })
    //username 可能是用户名或邮箱，根据typeOfName确定
    @PostMapping("user/login")
    public CommonResult<Void> queryUser(@RequestParam("username")String username, @RequestParam("password")String password, @RequestParam("typeOfName") String typeOfName,
                                                  HttpServletRequest request, HttpServletResponse response){
        if("email".equals(typeOfName)){
            String nameByMail = this.userService.findNameByMail(username);
            if(nameByMail==null){
                return new CommonResult<Void>(ResponseEnum.LOGIN_ERROR,HttpStatus.BAD_REQUEST);
            }
            username=nameByMail;
        }
        User user = this.userService.queryUser(username,password);
        if (user == null){
            log.info("用户名或密码错误");
            return new CommonResult<Void>(ResponseEnum.LOGIN_ERROR,HttpStatus.BAD_REQUEST);
        }
        Cookie cookie= new Cookie("username", username);
        cookie.setPath("/");
        response.setCharacterEncoding("UTF-8");
        response.addCookie(cookie);
        return new CommonResult<Void>(ResponseEnum.LOGIN_SUCCESS,HttpStatus.OK);
    }
    @GetMapping("auth/verify")
    public CommonResult<User> verify(@CookieValue("username") String username){
        if(StringUtils.isBlank(username)){
            throw new LyException(ExceptionEnum.UNAUTHORIZED);
        }
        //如果cookie存在，则直接包装成User返回
        User user = new User();
        user.setUsername(username);
        return new CommonResult<User>(ResponseEnum.CHANGEPASSWORD_ERROR,HttpStatus.BAD_REQUEST,user);

    }
    @ApiOperation(value = "密码修改" ,  notes="密码修改接口")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",paramType = "query"),
                    @ApiImplicitParam(name = "oldPassword", value = "原密码", required = true, dataType = "String",paramType = "query"),
                    @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String",paramType = "query")

            })
    @PostMapping("user/changePassword")
    public CommonResult<Void> changePassword(@RequestParam("username")String username,@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword){
        boolean b = this.userService.changePassword(username, oldPassword, newPassword);
        if (b){
            return new CommonResult<Void>(ResponseEnum.CHANGEPASSWORD_SUCCESS,HttpStatus.OK);
        }
        return new CommonResult<Void>(ResponseEnum.CHANGEPASSWORD_ERROR,HttpStatus.BAD_REQUEST);
    }
    }

