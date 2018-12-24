package com.dianping.hui.context.backup;

import com.dianping.hui.bean.ConnectionConfigEntity;
import com.dianping.hui.context.factory.IConfigManager;
import com.dianping.hui.context.support.IDisasterTolerance;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: lanxinyu@meituan.com  2018-11-29 2:45 PM
 * @Description: 备份数据的切面
 * 目前禁用，启用的时候加上下面注解
 * @Aspect
 * @Component
 *
 */
public class BackupAdvice {

    private IConfigManager backupConfigManager;
    private Class clazz;

    @Autowired public BackupAdvice(IDisasterTolerance iConfigContext) {
        this.backupConfigManager = iConfigContext.getBackupConfigManager();
        if(this.backupConfigManager.getClass().getSuperclass().equals(Object.class)) {
            this.clazz = this.backupConfigManager.getClass();
        } else {
            this.clazz = this.backupConfigManager.getClass().getSuperclass();
        }
    }

    @Pointcut("execution(* com.dianping.hui.manager.*ConfigManager.findAll())")
    public void findAll(){}

    @Pointcut("execution(* com.dianping.hui.manager.*ConfigManager.save(*))")
    public void save(){}

    @Pointcut("execution(* com.dianping.hui.manager.*ConfigManager.saveAll(*))")
    public void saveAll(){}

    @Pointcut("execution(* com.dianping.hui.manager.*ConfigManager.update(*))")
    public void update(){}

    @Pointcut("execution(* com.dianping.hui.manager.*ConfigManager.delete(*))")
    public void delete(){}

    @AfterReturning(value = "findAll()", returning = "list")
    public void findAllBackup(JoinPoint jp ,List<ConnectionConfigEntity> list) {
        if(isArrange(jp))
            backupConfigManager.saveAll(list);
    }

    @AfterReturning(value = "save()", returning = "isSucc")
    public void saveBackup(JoinPoint jp ,Boolean isSucc) {
        if(isArrange(jp) && isSucc)
            backupConfigManager.save((ConnectionConfigEntity) jp.getArgs()[0]);
    }

    @AfterReturning(value = "saveAll()", returning = "isSucc")
    public void saveAllBackup(JoinPoint jp ,Boolean isSucc) {
        if(isArrange(jp) && isSucc)
            backupConfigManager.saveAll((List<ConnectionConfigEntity>) jp.getArgs()[0]);
    }

    @AfterReturning(value = "update()", returning = "isSucc")
    public void updateBackup(JoinPoint jp ,Boolean isSucc) {
        if(isArrange(jp) && isSucc)
            backupConfigManager.update((ConnectionConfigEntity) jp.getArgs()[0]);
    }

    @AfterReturning(value = "delete()", returning = "isSucc")
    public void deleteBackup(JoinPoint jp ,Boolean isSucc) {
        if(isArrange(jp) && isSucc)
            backupConfigManager.delete((Long) jp.getArgs()[0]);
    }


    private boolean isArrange(JoinPoint jp) {
        return !jp.getSourceLocation().getWithinType().equals(clazz);
    }

}
