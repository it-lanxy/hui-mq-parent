package com.dianping.hui.context.bean;

/**
 * @author: lanxinyu@meituan.com  2018-11-26 8:21 PM
 * @Description:
 */
public enum ActionType {
    /**
     * mafka client action
     */
    ON_LINE_SUCCESS, ON_LINE_FAIL, OFF_LINE_SUCCESS, OFF_LINE_FAIL,
    /**
     * rpc client action
     */
    CONNECTION_SUCCESS, CONNECTION_FAIL
}
