package com.rxjava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class YoungGenObserver {

	Logger l = Logger.getLogger("generation.gc");


	private YoungGenObserverData tool = new YoungGenObserverData();
	
	ExecutorService es = Executors.newFixedThreadPool(4);

	/**
     * Non-blocking method that uses a thread to fetch the value and
     * callback via `onNext()` when done.
     */
    public Observable<Long> getYoungGenCapacity(final  String[] pid) {
    	
        //spawn thread or async IO to fetch data
            return Observable.create(new OnSubscribe<Long>() {

                @Override
                public void call(Subscriber<? super Long> ob) {
                    // do work on separate thread
    	                
                	tool.setSubscriber(ob);
   	                Future<Void> captureYoungCapacity =
    	                es.submit(  new Callable<Void>(){
    	                @Override
    	                public Void call()
    	                {
        	                tool.capture(pid);
                    		tool.stopCapture();
                    		return null;
    	                }
    	                } );
        	            es.shutdown();
                }

            });
            

    }
}
