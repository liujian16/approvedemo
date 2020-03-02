package com.example.demo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InMemeoryCoordinator implements IItemTaskCoordinator ,OrderTaskConfiger{

	public final  Logger log = LoggerFactory.getLogger(InMemeoryCoordinator.class); 
	
	private ConcurrentHashMap<String, AtomicInteger> order_taskcount_map = new ConcurrentHashMap<>();
	
	
	@Override
	public int decrementAndGet(String orderId) {
		AtomicInteger count = order_taskcount_map.get(orderId);
		if(count == null) {
			log.warn("订单:{} 没有任务记录",orderId);
			return 0;
			
		}
		return count.decrementAndGet();
	}

	@Override
	public void setItemTaskCount(String orderId, int taskCount) {
		order_taskcount_map.put(orderId, new AtomicInteger(taskCount));
	}

}
