package smartsuite.app.event;

public class CustomSpringEvent{

	private static final long serialVersionUID = 1L;

	public String eventId;
	private final Object data;
	private Object result;
	
	public static CustomSpringEvent toCompleteEvent(String eventId, Object data) {
		return CustomSpringEvent.builder()
				.eventId(eventId)
				.data(data)
				.build();
	}
	
	private CustomSpringEvent(String eventId, Object data) {
   		this.eventId = eventId;
   		this.data = data;
   	}
	
	public Object getData(){
		return this.data;
	}
	
	public void setResult(Object value) {
		this.result = value;
	}
	
	
	public String getEventId() {
		return this.eventId;
	}

	public Object getResult() {
		return this.result;
	}
   	
   	public static  CustomSpringEventBuilder builder() {
   		return new CustomSpringEventBuilder();
   	}
   	
   	public static class CustomSpringEventBuilder {
   		private String eventId;
   		private Object data;
   		
   		private CustomSpringEventBuilder() {}
   		
   		public CustomSpringEventBuilder eventId(String eventId) {
   			this.eventId = eventId;
   			return this;
   		}
   		
   		public CustomSpringEventBuilder data(Object data) {
   			this.data = data;
   			return this;
   		}
   		
   		@java.lang.Override public String toString() {
   			return "CustomSpringEventBuilder(eventId = " + eventId +")";
   		}
   		
   		public CustomSpringEvent build() {
   			return new CustomSpringEvent(eventId, data);
   		}
   	}
}
