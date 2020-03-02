package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class OrderItemTaskBuilder {

	@Autowired
	private IItemTaskCoordinator taskCoordinator;
	
	@Autowired
	private Meter meter;
	
	@Value("${app.productLockTime}")
	private int productLockTime;
	
	
	public  List<OrderItemTask> fromOrder(Order order) {
		return order.getItems().stream().map( item->
				new OrderItemTask(order.getId(), item.getId(), item.getProductId(), taskCoordinator,meter, productLockTime)
			).collect(Collectors.toList());
		
	} 
	
}
