package cn.pomit.jpamapper.core.domain.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Sort {
	List<Order> orders;
	
	public Sort(){}
	
	public Sort(List<Order> orders) {
		super();
		this.orders = orders;
	}
	
	public Sort(Order order) {
		super();
		this.orders = new ArrayList<>();
		orders.add(order);
	}
	
	public Sort(Order... orders) {
		super();
		this.orders = Arrays.asList(orders);
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public String toSort(){
		StringBuilder sql = new StringBuilder();
		sql.append(" ");
		if(orders != null && orders.size() > 0){
			for(Order order : orders){
				sql.append(order.property);
				sql.append(" ");
				sql.append(order.direction.name());
				sql.append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
		}
		
		return sql.toString().trim();
	}

	public static class Order{
		private Direction direction;
		private String property;
		
		public Order(Direction direction, String property) {
			this.direction = direction;
			this.property = property;
		}

		public static Order asc(String property) {
			return new Order(Direction.ASC, property);
		}

		public static Order desc(String property) {
			return new Order(Direction.DESC, property);
		}

		public Direction getDirection() {
			return direction;
		}

		public String getProperty() {
			return property;
		}

		public void setDirection(Direction direction) {
			this.direction = direction;
		}

		public void setProperty(String property) {
			this.property = property;
		}
		
	}
	
	public static enum Direction {
		ASC, DESC;

		public boolean isAscending() {
			return this.equals(ASC);
		}

		public boolean isDescending() {
			return this.equals(DESC);
		}

		public static Direction fromString(String value) {

			try {
				return Direction.valueOf(value.toUpperCase(Locale.US));
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format(
						"Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
			}
		}
	}
}
