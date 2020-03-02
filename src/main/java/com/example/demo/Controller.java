package com.example.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	Logger log  = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	OrderRespository repository;
	
	@Autowired
	ApproveWorker worker;
	
	@Autowired
	OrderTaskConfiger taskConfiger;
	
	@Autowired
	IItemTaskCoordinator taskCoordinator;
	
	@Autowired
	Meter statics;
	
	
	@Autowired
	OrderItemTaskBuilder taskBuilder;
	
	@GetMapping("/approve")
	public void approve(@RequestParam String orderId) {
		Order order = repository.queryById(orderId);
		taskConfiger.setItemTaskCount(orderId, order.getItems().size());
		
		taskBuilder.fromOrder(order).forEach(task->{
			worker.execute(task, task.getProductId());
		});
	}
	
	@GetMapping("/createIds")
	public void createOrderIds() {
		try(BufferedWriter writer = Files.newBufferedWriter(new File("/Users/liujian/Downloads/ids.csv").toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)){
			for(int i =1 ; i <= 8000;i++) {
				writer.append(i+"");
				writer.newLine();
			}
			writer.flush();
		}catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		
		
	}
	
	@GetMapping("/report")
	public Map<String,Object> report(){
		Map<String,Object> report = new HashMap<String, Object>();
		report.put("order",statics.getOrderCount());
		report.put("duration", statics.getDuration());
		report.put("loads", statics.getTaskLoad());
		return report;
		
	}
	
	@GetMapping("/reset")
	public void reset() {
		statics.reset();
	} 
	
	
	
	
}
