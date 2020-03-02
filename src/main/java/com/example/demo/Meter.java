package com.example.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

/**
 * @author liujian
 *
 */
@Component
public class Meter {
	
	private AtomicLong startTime = new AtomicLong(0);
	
	private AtomicLong endTime = new AtomicLong(0);
	
	private ConcurrentHashMap<String, AtomicLong> threadTaskStartTime= new ConcurrentHashMap<String, AtomicLong>();
	private ConcurrentHashMap<String, AtomicLong> threadTaskEndTime= new ConcurrentHashMap<String, AtomicLong>();
	
	private ConcurrentHashMap<String, AtomicInteger> threadTaskCount= new ConcurrentHashMap<String, AtomicInteger>();
	
	
	private AtomicLong orderCount = new AtomicLong(0);
	
	
	public AtomicLong getOrderCount() {
		return orderCount;
	}

	public void orderFinished(String id) {
		orderCount.incrementAndGet();
	}

	/**
	 * @return 每个线程的执行的任务数
	 */
	public Map<String, Integer> getTaskLoad(){
		HashMap<String, Integer> result = new HashMap<>();
 		threadTaskCount.forEach((k,v)->result.put(k, v.get()));
 		return result;
	}
	
	public void taskAssgined(String tid) {
		threadTaskCount.putIfAbsent(tid, new AtomicInteger(0));
		threadTaskCount.get(tid).incrementAndGet();
		
	}
	
	public void threadTaskStart(String tid) {
		long now = System.currentTimeMillis();
		threadTaskStartTime.putIfAbsent(tid, new AtomicLong(now));
		startTime.compareAndSet(0, now);
	}

	public void threadTaskEnd(String tid) {
		long now = System.currentTimeMillis();
		threadTaskEndTime.put(tid, new AtomicLong(now));
		endTime.set(now);
//		taskAssgined(tid);
	}
	
	public String getStartTime() {
		return new Date(startTime.get()).toString();
	}

	public String getEndTime() {
		return new Date(endTime.get()).toString();
	}

	public void reset() {
		threadTaskStartTime.clear();
		threadTaskEndTime.clear();
		threadTaskCount.clear();
		startTime.set(0);
		endTime.set(0);
	}

	public long getDuration() {
		return endTime.get() - startTime.get();
	}
	
	
	
}
