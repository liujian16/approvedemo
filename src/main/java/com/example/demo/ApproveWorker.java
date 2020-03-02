package com.example.demo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public  class  ApproveWorker {
	
	
	private boolean hashLoadBalance;
	
	
	
	private final AtomicInteger total = new AtomicInteger(0); 
	
	
	private List<ExecutorService> executors;
	
	@Autowired
	private Meter statics;
	
	
	public ApproveWorker(@Value("${app.approveworker.queuenum}")int queueNum, 
			@Value("${app.approveworker.threadPerQueue}")int threadPerQueue, 
			@Value("${app.approveworker.hashLoadBalance}")boolean hashLoadBalance) {
		super();
		
		this.hashLoadBalance = hashLoadBalance;
		
		CopyOnWriteArrayList<ExecutorService> es = new CopyOnWriteArrayList<>();
		for(int i = 0; i < queueNum; i++) {
			  es.add(new ThreadPoolExecutor(threadPerQueue, threadPerQueue,
                     0L, TimeUnit.MILLISECONDS,
                     new LinkedBlockingQueue<Runnable>()));
		}
		this.executors = es;
	}





	public void execute(Runnable task, String hash) {
		
		int t = total.incrementAndGet();
		int seq = 0;
		if(hashLoadBalance ) {
			int nounce = t / 1000;
			
			String digest = DigestUtils.md5DigestAsHex((hash+nounce).getBytes());
			
			int b = Integer.parseInt(digest.substring(0,6), 16);
			
			
			seq = Math.abs(b % executors.size());
		}else {
			 seq = t % executors.size();
		}
		statics.taskAssgined(seq+"");
		executors.get(seq).execute(task);
		
	}
	
}
