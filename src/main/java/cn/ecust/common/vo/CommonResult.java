package cn.ecust.common.vo;

import cn.ecust.common.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: SWM
 * @Date: 2019/5/16 22:59
 * @Description: 返回的实体
 */
@Data
@AllArgsConstructor
public class CommonResult<T>{

    private HttpStatus status;

    private String date;

    private T data;

    private int code;

    private String msg;

    public CommonResult(ResponseEnum responseEnum) {
        this.code= responseEnum.getCode();
        this.msg= responseEnum.getMsg();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        this.date=df.format(new Date());
    }
    public CommonResult(ResponseEnum responseEnum,HttpStatus status) {
        this.status=status;
        this.code= responseEnum.getCode();
        this.msg= responseEnum.getMsg();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        this.date=df.format(new Date());
    }
    public CommonResult(ResponseEnum responseEnum,HttpStatus status,T data) {
        this.status=status;
        this.code= responseEnum.getCode();
        this.msg= responseEnum.getMsg();
        this.data=data;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        this.date=df.format(new Date());
    }

}
