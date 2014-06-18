package com.memory.jmx.probe;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

import rx.Subscriber;


public class MetaSpaceProbe {
	
	Logger l = Logger.getLogger("generation.gc");

	private static final String METASPACE = "Metaspace";
	private Subscriber<? super Long> ob;
	
	public  Optional<MemoryPoolMXBean> getMemoryPool() {
		
      JMXServiceURL url;
	try {
		url = new JMXServiceURL(
		            "service:jmx:rmi:///jndi/rmi://localhost:9010/jmxrmi");
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		l.debug("Remote JMX connection is successful");
	} catch (IOException e) {
		l.debug(getStackTrace(e));
	}
	
	
	   Optional<MemoryPoolMXBean> mpx = Optional.empty();
	   List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
	           for (MemoryPoolMXBean pool : pools) {
	       		l.debug(pool.getName());
	               if (pool.getName().equals(METASPACE)) {
	            	   mpx =  Optional.ofNullable(pool);
	               }
	           }
			return mpx;
	}


	public void setSubscriber(Subscriber<? super Long> ob) {
		this.ob = ob;
		
	}
	
    private String getStackTrace(Throwable t){
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	t.printStackTrace(pw);
    	return sw.toString();
    	
    }
	

}
