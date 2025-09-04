package com.sky.context;
/**
 * 基础上下文类，用于存储和获取当前线程中的用户ID
 * 基于ThreadLocal实现实现线程内的数据共享
 */
public class BaseContext {
    //创建ThreadLocal对象，用于存储Long类型的用户ID，每个线程拥有独立的副本
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    //设置当前线程中的用户ID，将用户ID存入当前线程的ThreadLocal副本中
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
    //获取当前线程中的用户ID，从当前线程的ThreadLocal副本中获取用户ID
    public static Long getCurrentId() {
        return threadLocal.get();
    }
    //移除当前线程中的用户ID，防止线程池复用导致的ThreadLocal数据残留问题，清除当前线程的ThreadLocal副本中的用户ID
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
