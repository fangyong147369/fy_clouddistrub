package com.mi.hundsun.oxchains.base.core.config;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.LogUtil;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * json数据类controller 基础实现
 *
 * @param <PK> 对象主键类型
 * @param <PO> 对象
 */
@Slf4j
public class GenericController<PK, PO extends GenericPo<PK>> {

    /**
     * 操作成功
     */
    protected <T> ResultEntity<T> ok() {
        return result(null, "success", ResultEntity.SUCCESS);
    }

    /**
     * 操作成功
     *
     * @param po 返回数据
     */
    protected <T> ResultEntity<T> ok(T po) {
        return result(po, "操作成功", ResultEntity.SUCCESS);
    }

    /**
     * 操作成功
     *
     * @param po 返回数据
     */
    protected <T> ResultEntity<T> ok(T po, String msg) {
        return result(po, msg, ResultEntity.SUCCESS);
    }

    /**
     * 操作失败
     */
    protected <T> ResultEntity<T> fail(String msg) {
        return result(null, msg, ResultEntity.FAIL);
    }

    /**
     * 操作失败
     */
    protected <T> ResultEntity<T> fail() {
        return result(null, "操作失败", ResultEntity.FAIL);
    }

    /**
     * 返回消息记录
     *
     * @param resultData 返回实体(可空)
     * @param msg        返回消息
     * @param code       成功/错误
     */
    protected <T> ResultEntity<T> result(T resultData, String msg, int code) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setData(resultData);
        resultEntity.setCode(code);
        resultEntity.setMessage(msg);
        return resultEntity;
    }

    protected <T> ResultEntity<T> result(String msg, int code) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setData(null);
        resultEntity.setCode(code);
        resultEntity.setMessage(msg);
        return resultEntity;
    }

    /**
     * 返回消息记录
     *
     * @param resultData 返回实体
     */
    protected <T> ResultEntity<T> result(ResultEntity resultData) {
        if (resultData.getCode() == ResultEntity.SUCCESS) {
            return ok();
        }
        return fail(resultData.getMessage());
    }

    /**
     * 全局异常处理
     */
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResultEntity exception(Exception e) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        resultEntity.setMessage("系统繁忙");
        if (e instanceof BussinessException) {
            LogUtil.error(e, GenericController.class);
            if (((BussinessException) e).getCode() == BussinessException.Code.E401.code) {
                resultEntity.setCode(BussinessException.Code.E401.code);
            } else if (((BussinessException) e).getCode() == BussinessException.Code.E402.code) {
                resultEntity.setCode(BussinessException.Code.E402.code);
            } else if (((BussinessException) e).getCode() == BussinessException.Code.E404.code) {
                resultEntity.setCode(BussinessException.Code.E404.code);
            }
            resultEntity.setMessage(e.getMessage());
        } else if (e instanceof MySQLIntegrityConstraintViolationException) {
            log.warn(e.getMessage());
        } else {
            e.printStackTrace();
        }

        return resultEntity;
    }


    public static HttpServletRequest getRequest() {
        final RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        final ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }

    public static HttpServletResponse getResponse() {
        final RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        final ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getResponse();
    }
}
