package com.memory.probe;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rx.Observer;
import rx.Subscriber;

import com.rxjava.YoungGenObserver;

public class YoungGenProbe {
	
	Logger l = Logger.getLogger("generation.gc");

	private Long youngCapacity;

	public YoungGenProbe(){
		DOMConfigurator.configure("log4j.xml");
	}

    public static void main(String[] args) {
        YoungGenProbe test = new YoungGenProbe();
        test.consume(args);
    }

    private void consume(String[] args) {
        l.debug("[consume]");

        new YoungGenObserver().
        getYoungGenCapacity(args)
        .subscribe(new Subscriber<Long>() {


			@Override
			public void onCompleted() {
	            l.debug("[onCompleted]");
				
			}

			@Override
			public void onError(Throwable arg0) {
	            l.debug("[onError]");
				
			}

			@Override
			public void onNext(Long youngCapacity) {
	            l.debug("onNext[" + youngCapacity + " ]");
	            YoungGenProbe.this.setYoungCapacity(youngCapacity);
				
			}
        });
    }

	public long getYoungCapacity() {
		return youngCapacity;
	}

	public void setYoungCapacity(Long youngCapacity) {
		this.youngCapacity = youngCapacity;
	}

}
