package com.rxjava.jmx;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import com.memory.jmx.probe.YoungGenSpaceProbe;
import com.memory.probe.Probe;

public class YoungGenPeriodicObservable implements Probe{

	Logger l = Logger.getLogger("generation.gc");

	private YoungGenSpaceProbe ygsp = new YoungGenSpaceProbe();

	ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);

	Scheduler youngGenSched = Schedulers.from(ses);

	/**
     * Non-blocking method that uses a thread to fetch the value and
     * callback via `onNext()` when done.
	 * @param  
     */
    public Observable<YoungGenOccupancy> getYoungGenCapacity() {
    	
    	Optional<MemoryPoolMXBean> memoryPool = ygsp.getMemoryPool();
    	
    	MemoryUsage mu = null;
    	
        if(memoryPool.isPresent()){
        	MemoryPoolMXBean mpmx = memoryPool.get();
            mu = mpmx.getUsage();
        }

    	Observable<YoungGenOccupancy> o = null;
    	try {
				o =  Observable.create(create( mu));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				l.debug(getTrace(e));
			}
			return o;
            
    }
    
    OnSubscribe<YoungGenOccupancy> create(MemoryUsage mu) throws InterruptedException {

        return s -> {
            Worker w = youngGenSched.createWorker();
			w.schedulePeriodically(new Action0() {

			    @Override
			    public void call() {
			        
			        s.onNext(new YoungGenOccupancy(mu.getUsed(),mu.getMax()));
			        s.onCompleted();
			    }

			},  25, 1, TimeUnit.SECONDS);
            };
     }

}
