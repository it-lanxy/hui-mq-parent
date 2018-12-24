package com.dianping.hui.context.bean;

import lombok.Data;

import java.util.Set;

/**
 * @author: lanxinyu@meituan.com  2018-11-26 8:18 PM
 * @Description:
 */
@Data
public class ConnectionStatusMark {
    private Long id;
    private Set<Long> ids;
    private Long timestamp;
    private ActionType action;
}
