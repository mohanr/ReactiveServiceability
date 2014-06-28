package com.memory.probe;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ArrayBlockingQueue;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

import com.rxjava.jmx.YoungGenOccupancy;

public interface Probe {
	
	Logger l = Logger.getLogger("generation.gc");
	
	ArrayBlockingQueue<YoungGenOccupancy> abq = new ArrayBlockingQueue<>(100);

	 
	default MBeanServerConnection connectJMXRemote(int port){
	      JMXServiceURL url;
	      MBeanServerConnection remote = null;
	  	try {
	  		url = new JMXServiceURL(
	  		            "service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmxrmi");
	  		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
	  		remote = jmxc.getMBeanServerConnection();
	  		l.debug("Remote JMX connection is successful");
	  	} catch (IOException e) {
	  		l.debug(getStackTrace(e));
	  	}
        return remote;
	}
	
	default String getTrace(Throwable t){
		return getStackTrace(t);
	}
	
    static String getStackTrace(Throwable t){
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	t.printStackTrace(pw);
    	return sw.toString();
    	
    }


}
