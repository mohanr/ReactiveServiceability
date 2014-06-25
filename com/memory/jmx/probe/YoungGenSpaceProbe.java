package com.memory.jmx.probe;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import java.util.Optional;

import com.memory.probe.Probe;


public class YoungGenSpaceProbe implements Probe{
	
	private static final String PS_EDEN_SPACE = "PS Eden Space";
	
	public  Optional<MemoryPoolMXBean> getMemoryPool() {
		
		connectJMXRemote(9010);
 	
	   Optional<MemoryPoolMXBean> mpx = Optional.empty();
	   List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
	           for (MemoryPoolMXBean pool : pools) {
	       		l.debug(pool.getName());
	               if (pool.getName().equals(PS_EDEN_SPACE)) {
	            	   mpx =  Optional.ofNullable(pool);
	               }
	           }
			return mpx;
	}	

}
