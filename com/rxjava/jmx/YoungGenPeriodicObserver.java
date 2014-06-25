package com.rxjava.jmx;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;

import com.memory.probe.Probe;

public class YoungGenPeriodicObserver implements Probe{
	
	YoungGenOccupancy occupancy = new YoungGenOccupancy();

	public YoungGenPeriodicObserver(){
		//DOMConfigurator.configure("log4j.xml");
	}

    public static void main(String[] args) {
    	YoungGenPeriodicObserver test = new YoungGenPeriodicObserver();
        test.consume();
    }

    private void consume() {
        l.debug("[consume]");

        new YoungGenPeriodicObservable().getYoungGenCapacity()
        .subscribe(new Subscriber<YoungGenOccupancy>() {


			@Override
			public void onCompleted() {
	            l.debug("[onCompleted]");
				
			}

			@Override
			public void onError(Throwable t) {
	            l.debug(getTrace(t));
				
			}

			@Override
			public void onNext(YoungGenOccupancy occupancy) {
	            l.debug("onNext[" + occupancy + " ]");
	            try {
					abq.offer(occupancy, 150, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					l.debug(getTrace(e));
				}
			}
        });
    }
    
    public YoungGenOccupancy getOccupancy(){
    	
    	YoungGenOccupancy ygo = null;
    	
    	try {
    		ygo = abq.poll(150, TimeUnit.MILLISECONDS);
			l.debug("There are [" + abq.size() + "] elements in the queue");
		} catch (InterruptedException e) {
			l.debug(getTrace(e));
		}
    	return ygo;
    }
}
