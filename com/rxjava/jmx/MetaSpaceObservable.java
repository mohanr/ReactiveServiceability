package com.rxjava.jmx;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Subscriber;

import com.memory.jmx.probe.MetaSpaceProbe;

public class MetaSpaceObservable {

	Logger l = Logger.getLogger("generation.gc");


	private MetaSpaceProbe msp = new MetaSpaceProbe();
	
	ExecutorService es = Executors.newFixedThreadPool(4);



	/**
     * Non-blocking method that uses a thread to fetch the value and
     * callback via `onNext()` when done.
     */
    public Observable<Object> getMetaSpaceCapacity() {
   	
    	Observable<Long> init = null;
    	Observable<Long> committed = null;
        Observable<Long> max = null;
        Observable<Long> used = null;
    	
            Future<Optional<MemoryPoolMXBean>> memoryPool =
                es.submit(  new Callable<Optional<MemoryPoolMXBean>>(){
                @Override
                public Optional<MemoryPoolMXBean> call()
                {
                	return msp.getMemoryPool();
                }
                } );
	            es.shutdown();
	            
	        Optional<MemoryPoolMXBean> mpmxbOpt = null;
			try {
				mpmxbOpt = memoryPool.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				l.debug(getStackTrace(e));
			}
	        if(mpmxbOpt.isPresent()){
	        	MemoryPoolMXBean mpmx = mpmxbOpt.get();
	        	MemoryUsage mu = mpmx.getUsage();
	        	init = Observable.from(mu.getInit());
	            committed = Observable.from(mu.getCommitted());
	            used = Observable.from(mu.getUsed());
	            max = Observable.from(mu.getMax());
	        }

    return Observable.merge(Observable.zip(init, committed, used, max,(i,c,u,m) -> {
        List<Long> results = Arrays.asList(i,c,u,m);
        return Observable.create((Subscriber<? super String> subscriber) -> {
            for(Long lv : results){
        		l.debug(String.valueOf(lv.longValue()));
            	subscriber.onNext(String.valueOf(lv.longValue()));
            }
            subscriber.onCompleted();
        });
    }));

    }

    private String getStackTrace(Throwable t){
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	t.printStackTrace(pw);
    	return sw.toString();
    	
    }

}
