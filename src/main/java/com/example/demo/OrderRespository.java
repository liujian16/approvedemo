package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderRespository {

	private List<String> products = new ArrayList<>();
	
	private Random random = new Random();
	
	private int itemsPerOrder;
	public OrderRespository(@Value("${app.productNum}")int productNum , @Value("${app.itemsPerOrder}")int itemsPerOrder) {
		
		for(int i = 0; i < productNum; i++ ) {
			products.add(UUID.randomUUID().toString());
		}
		this.itemsPerOrder = itemsPerOrder;

	}
	public Order queryById(String id) {
		Order order = new Order();
		order.setId(id);

		List<OrderItem> items = new ArrayList<>();
		for(int i = 0; i < itemsPerOrder; i++) {
			OrderItem item = new OrderItem();
			item.setId("Item-" + (i+1));
			item.setProductId(products.get(random.nextInt(products.size())));
			items.add(item);
		}
		order.setItems(items);
		return order;
	}
}
