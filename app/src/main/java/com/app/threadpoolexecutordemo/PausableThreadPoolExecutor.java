package com.app.threadpoolexecutordemo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * PausableThreadPoolExecutor Created on 2016/7/13-17:26
 * Description:具有暂时功能的线程池
 * Created by DongHao
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
    private boolean isPaused;
    private ReentrantLock pauseLock=new ReentrantLock();//重入锁
    private Condition unPauesd=pauseLock.newCondition();

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);//线程准备执行任务
        pauseLock.lock();//拿到锁
        try {
            while (isPaused){
                unPauesd.await();//线程沉睡等待signal唤醒，
            }
        }catch (Exception e){
           t.interrupt();
        }finally {
            pauseLock.unlock();//释放锁
        }
    }

    public void paush(){
        pauseLock.lock();//拿到锁
       try {
           isPaused=true;
        } catch (Exception e){

        }finally{
           pauseLock.unlock();//释放锁
        }
    }

    public void resume(){
        pauseLock.lock();//拿到锁
        try {
           isPaused=false;
            unPauesd.signalAll();//调用signal方法，这时还没有唤醒沉睡线程
         } catch (Exception e){

         }finally{
            pauseLock.unlock();//释放锁，沉睡线程被唤醒，恢复执行
         }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //线程执行完
    }

    @Override
    protected void terminated() {
        super.terminated();
        //线程池结束
    }

    /**
 * ReentrantLock和Condition：
 * 1. 线程1调用reentrantLock.lock时，线程被加入到AQS的等待队列中。

 2. 线程1调用await方法被调用时，该线程从AQS中移除，对应操作是锁的释放。

 3. 接着马上被加入到Condition的等待队列中，意味着该线程需要signal信号。

 4. 线程2，因为线程1释放锁的关系，被唤醒，并判断可以获取锁，于是线程2获取锁，并被加入到AQS的等待队列中。

 5.  线程2调用signal方法，这个时候Condition的等待队列中只有线程1一个节点，于是它被取出来，并被加入到AQS的等待队列中。  注意，这个时候，线程1 并没有被唤醒。

 6. signal方法执行完毕，线程2调用reentrantLock.unLock()方法，释放锁。这个时候因为AQS中只有线程1，于是，AQS释放锁后按从头到尾的顺序唤醒线程时，线程1被唤醒，于是线程1回复执行。

 7. 直到释放所整个过程执行完毕。
  */
}


