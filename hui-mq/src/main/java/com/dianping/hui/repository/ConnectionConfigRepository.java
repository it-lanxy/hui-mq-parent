package com.dianping.hui.repository;

import com.dianping.hui.bean.ConnectionConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: lanxinyu@meituan.com  2018-11-21 4:51 PM
 * @Description:
 */
@Repository
public interface ConnectionConfigRepository extends JpaRepository<ConnectionConfigEntity, Long> {

}
