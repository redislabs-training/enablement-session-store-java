package com.redis.te.redisjsonsession;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redis.te.redisjsonsession.interfaces.ShoppingCartItem;

@ComponentScan(basePackages = "com.redis.te.redisjsonsession")
public class App 
{
    @Autowired
	private static ApplicationContext context;
	
	public static void main( String[] args )
    {
        System.out.println( "Hello Redis JSON Session!" );
        org.apache.log4j.BasicConfigurator.configure();
        
        context = new ClassPathXmlApplicationContext("classpath:META-INF\\spring\\spring.xml");
        RedisJsonSession session = (RedisJsonSession)context.getBean(RedisJsonSession.class); 
     
        try {
        	// Needs a new session
			String sessionID = session.createSession();
			
			session.shoppingCartItemGetAll();
			
			// Create cart items
			ShoppingCartItem laptop = new ShoppingCartItem("hp-2341", new BigDecimal("1990.99"), 3);
			ShoppingCartItem backpack = new ShoppingCartItem("invicta-jolly", new BigDecimal("70.99"), 1);
			ShoppingCartItem mac = new ShoppingCartItem("MacBook", new BigDecimal("2990.99"), 1);
			
			session.shoppingCartItemAppend(laptop);
			session.shoppingCartItemAppend(backpack);
			session.shoppingCartItemAppend(mac);
			
			// Manage HyperLogLog 
			System.out.println("Manage HyperLogLog");
			session.addVisited("www.redis.com");
			session.addVisited("www.redis.io");
			System.out.println("Items in the HyperLogLog: " + session.countVisited());
			
			// Get session clone
			RedisJsonSession cloneSession = (RedisJsonSession)context.getBean(RedisJsonSession.class);
			cloneSession.loadSession(sessionID);
			
			// Get the entire cart
			for (ShoppingCartItem item : cloneSession.shoppingCartItemGetAll()) {
				System.out.println(item.toJson());
			}
			
			// Now buy 15 Macs
			mac.setQuantity(15);
			session.shoppingCartItemAppend(mac);
			
			// Get single item from cloned cart
			System.out.println("Getting product from cart");
			System.out.println(cloneSession.shoppingCartItemGet("invicta-jolly").toJson());
			
			// Remove item from cloned cart
			System.out.println("Remove product from cart");
			cloneSession.shoppingCartItemRemove("invicta-jolly");
			
			// Push visited url to list
			System.out.println("Push visited url to list");
			cloneSession.historyAppendUrl("www.mortensi.com");
			cloneSession.historyAppendUrl("www.mortensi.com/admin");
			cloneSession.historyAppendUrl("www.mortensi.com/feed");
			
			// Set and Get attributes
			System.out.println("Set and Get attributes");
			cloneSession.setAttribute("Marvel", "Avengers");
			cloneSession.setAttribute("Disney", "Frozen");
			System.out.println(cloneSession.getAttribute("Marvel"));
			System.out.println(cloneSession.getAttribute("Disney"));
			System.out.println(cloneSession.getAttribute("None"));
			
			// Check global expiration
			session.expireAll(3600);
			session.geoSetLocation("34.638,31.79");
			
			RedisJsonSession session2 = (RedisJsonSession)context.getBean(RedisJsonSession.class); 
			RedisJsonSession session3 = (RedisJsonSession)context.getBean(RedisJsonSession.class); 
			RedisJsonSession session4 = (RedisJsonSession)context.getBean(RedisJsonSession.class); 
			session2.createSession();
			session3.createSession();
			session4.createSession();
			session2.geoSetLocation("34.639,31.793");
			session3.geoSetLocation("35.213,31.785");
			session4.geoSetLocation("35.178,31.768");
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
