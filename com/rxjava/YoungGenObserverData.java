package com.rxjava;

import org.apache.log4j.Logger;

import rx.Subscriber;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.PSYoungGen;
import sun.jvm.hotspot.gc_implementation.parallelScavenge.ParallelScavengeHeap;
import sun.jvm.hotspot.gc_implementation.shared.MutableSpace;
import sun.jvm.hotspot.gc_interface.CollectedHeap;
import sun.jvm.hotspot.memory.Universe;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;

public class YoungGenObserverData extends Tool{

	Logger l = Logger.getLogger("generation.gc");
	
	private  long youngCapacity;

	private Subscriber<? super Long> ob;

	public long getYoungCapacity() {
		return youngCapacity;
	}

	public  void  capture(String[] pid){
	    this.execute(pid);
	}
	
	public void stopCapture(){
		this.stop();
	}

    @Override
    public void run() {
       VM vm = VM.getVM();
       Universe universe = vm.getUniverse();
        CollectedHeap heap = universe.heap();
        if (heap instanceof ParallelScavengeHeap) {
            ParallelScavengeHeap psHeap = (ParallelScavengeHeap) heap;
            PSYoungGen y = psHeap.youngGen();
            MutableSpace youngObjSpace = y.edenSpace();
            youngCapacity = youngObjSpace.capacity();
            l.debug(youngCapacity);
		    ob.onNext(getYoungCapacity());
			ob.onCompleted();
        }
    }

	public void setSubscriber(Subscriber<? super Long> ob) {
			this.ob = ob;
	}

}