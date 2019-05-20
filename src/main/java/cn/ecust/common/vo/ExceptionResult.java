package cn.ecust.common.vo;


import cn.ecust.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * @Auther: SWM
 * @Date: 2019/4/13 15:58
 * @Description: 
 */
@Data
public class ExceptionResult {
    private int status;
    private String msg;
    private Long timestamp;
    public ExceptionResult(ExceptionEnum em){
        this.status=em.getCode();
        this.msg=em.getMsg();
        this.timestamp=System.currentTimeMillis();
    }
}
