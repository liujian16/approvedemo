package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;


public class OrderItemTask implements Runnable {

	public final  Logger log = LoggerFactory.getLogger(OrderItemTask.class); 
	
	private String orderId;
	private String orderItemId;
	private String productId;
	private IItemTaskCoordinator taskCordinator;
	private Meter meter;
	private int productLockTime;
	
	
	public OrderItemTask(String orderId, String orderItemId, String productId, IItemTaskCoordinator taskCordinator,Meter meter
			,int productLockTime) {
		super();
		this.orderId = orderId;
		this.orderItemId = orderItemId;
		this.productId = productId;
		this.taskCordinator = taskCordinator;
		this.meter = meter;
		this.productLockTime = productLockTime;
	}

	@Override
	public void run() {
		StopWatch sw = new StopWatch();
		sw.start();
		meter.threadTaskStart(Thread.currentThread().getName());
		log.info("处理 订单：{}, 明细：{}, 商品: {} 开始....", orderId, orderItemId, productId);
		synchronized (productId) {
			try {
				Thread.sleep(productLockTime);
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		sw.stop();
		log.info("处理 订单：{}, 明细：{}, 商品: {} 结束，耗时 {} ms", orderId, orderItemId, productId, sw.getTotalTimeMillis());

		
		if(taskCordinator.decrementAndGet(orderId)==0) {
			meter.orderFinished(orderId);
			log.info("订单:{} 处理完毕", orderId);
		}
		meter.threadTaskEnd(Thread.currentThread().getName());
		
	}

	
	
	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getOrderItemId() {
		return orderItemId;
	}


	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	
}
