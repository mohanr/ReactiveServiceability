package com.memory.jmx.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import rx.Subscriber;

import com.memory.probe.Probe;


public class MetaSpaceProbe implements Probe{
	
	Logger l = Logger.getLogger("generation.gc");

	private static final String METASPACE = "Metaspace";
	private Subscriber<? super Long> ob;
	
	public  Optional<MemoryPoolMXBean> getMemoryPool() {
		
	   MBeanServerConnection mbs = connectJMXRemote(9010);
	   Optional<MemoryPoolMXBean> mpx = Optional.empty();
	   Set<ObjectName> pools;
		try {
			pools = mbs.queryNames(new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",name=*"), null);
	
			   Set<MemoryPoolMXBean> memoryBeans = new HashSet<MemoryPoolMXBean>(pools.size());
			   for(ObjectName o: pools) {
					memoryBeans.add(ManagementFactory.newPlatformMXBeanProxy(mbs, o.toString(), MemoryPoolMXBean.class));
			   }
			   for (MemoryPoolMXBean pool : memoryBeans) {
		       		l.debug(pool.getName());
		               if (pool.getName().equals(METASPACE)) {
		            	   mpx =  Optional.ofNullable(pool);
		               }
		   }
		} catch (MalformedObjectNameException | IOException e) {
			getTrace(e);
		}
			return mpx;
	}


	public void setSubscriber(Subscriber<? super Long> ob) {
		this.ob = ob;
		
	}
}
