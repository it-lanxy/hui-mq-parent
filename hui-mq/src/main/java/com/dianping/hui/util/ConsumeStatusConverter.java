package com.dianping.hui.util;

import com.meituan.mafka.client.consumer.ConsumeStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lanxinyu@meituan.com  2018-11-23 5:26 PM
 * @Description:
 */
@Slf4j
public class ConsumeStatusConverter {

    public static ConsumeStatus convert(int code) {
        switch (code) {
            case 0: return ConsumeStatus.CONSUME_SUCCESS;
            case 1: return ConsumeStatus.RECONSUME_LATER;
            case 2: return ConsumeStatus.CONSUME_FAILURE;
            default:
                log.error("known result ", code);
                return ConsumeStatus.RECONSUME_LATER;
        }
    }
}
