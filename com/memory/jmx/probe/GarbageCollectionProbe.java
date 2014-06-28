package com.memory.jmx.probe;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.memory.probe.Probe;


public class GarbageCollectionProbe implements Probe{
	
	Logger l = Logger.getLogger("collection.gc");

	public  Optional<GarbageCollectorMXBean> getMemoryPool() {
		
		connectJMXRemote(9010);
 	
	   Optional<GarbageCollectorMXBean> mpx = Optional.empty();
	   List<GarbageCollectorMXBean> pools = ManagementFactory.getGarbageCollectorMXBeans();
       l.debug("GC [" + pools.size() + "]");
	           for (GarbageCollectorMXBean bean : pools) {
	       		l.debug("GC " + bean.getName());
	       		l.debug("GC [" + bean.getCollectionCount() + "]");
	       		l.debug("GC [" + bean.getCollectionTime()  + "]");
	           }
			return mpx;
	}	

}
