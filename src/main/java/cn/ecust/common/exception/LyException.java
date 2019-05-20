package cn.ecust.common.exception;

import cn.ecust.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: SWM
 * @Date: 2019/4/13 15:45
 * @Description: 
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
