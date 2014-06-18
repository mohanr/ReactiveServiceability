package com.rxjava.jmx;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rx.Subscriber;

public class MetaSpaceObserver {
	
	Logger l = Logger.getLogger("generation.gc");
	
	List<String> al = new ArrayList<>();

	public MetaSpaceObserver(){
		DOMConfigurator.configure("log4j.xml");
	}

    public static void main(String[] args) {
    	MetaSpaceObserver test = new MetaSpaceObserver();
        test.consume();
    }

    private void consume() {
        l.debug("[consume]");

        new MetaSpaceObservable().getMetaSpaceCapacity()
        .subscribe(new Subscriber<Object>() {


			@Override
			public void onCompleted() {
	            l.debug("[onCompleted]");
				
			}

			@Override
			public void onError(Throwable t) {
	            l.debug(getStackTrace(t));
				
			}

			@Override
			public void onNext(Object capacity) {
	            l.debug("onNext[" + capacity + " ]");
				al.add((String) capacity);
			}
        });
    }
    
    private String getStackTrace(Throwable t){
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	t.printStackTrace(pw);
    	return sw.toString();
    	
    }
}
