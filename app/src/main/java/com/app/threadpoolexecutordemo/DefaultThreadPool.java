package com.app.threadpoolexecutordemo;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DefaultThreadPool Created on 2016/7/20-11:16
 * Description:线程池封装
 * Created by DongHao
 */
public class DefaultThreadPool {
    //阻塞任务队列最大数量
    private static final int BLOCKING_QUEUE_SIZE=4;
    //最大线程数
    private static final int THREAD_POOL_MAX_SIZE=3;
    //核心线程数
    private static final int THREAD_POOL_SIZE=2;

    private static ArrayBlockingQueue<Runnable> blockingQueue=new ArrayBlockingQueue<Runnable>(BLOCKING_QUEUE_SIZE);

    private DefaultThreadPool() {
        super();
    }

    private static DefaultThreadPool instance=null;

    private static AbstractExecutorService pool=new ThreadPoolExecutor(THREAD_POOL_SIZE,THREAD_POOL_MAX_SIZE,
            15L, TimeUnit.SECONDS,blockingQueue,new ThreadPoolExecutor.DiscardOldestPolicy());

    public static synchronized DefaultThreadPool getInstance(){
        if(instance==null){
            instance=new DefaultThreadPool();
        }
        return instance;
    }

    /**
     * 移除队列中所有任务
     */
    public static void removeAllTask(){
        DefaultThreadPool.blockingQueue.clear();
    }

    /**
     * 移除等待队列中指定任务
     * @param object
     */
    public static void removeTaskFromQueue(final Object object){
        DefaultThreadPool.blockingQueue.remove(object);
    }

    /**
     * 关闭，等待任务执行完毕，不再接收新任务
     */
    public static void shutDown(){
        if (DefaultThreadPool.pool!=null){
            DefaultThreadPool.pool.shutdown();
        }
    }

    /**
     * 立刻关闭，并挂起所有正在执行的任务，不再接收新任务
     */
    public static void shutDownNow(){
        if (DefaultThreadPool.pool!=null){
            DefaultThreadPool.pool.shutdownNow();
            try {
                //设置超时极短，强制关闭所有任务
                DefaultThreadPool.pool.awaitTermination(1,TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行任务
     * @param r
     */
    public static void execute(final Runnable r){
        if (r!=null){
            try {
                DefaultThreadPool.pool.execute(r);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
