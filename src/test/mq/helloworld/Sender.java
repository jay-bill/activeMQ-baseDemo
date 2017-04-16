package test.mq.helloworld;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	public static void main(String[] args) throws JMSException {
		// TODO Auto-generated method stub
		//第一步，建立ConnectionFactory工厂对象，需填入用户、密码、地址
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"tcp://0.0.0.0:61616");
		//第二步，通过连接工厂创建一个连接，并开启连接
		Connection connection = connectionFactory .createConnection();
		connection.start();
		//第三步，通过Connection创建session对象，确定是否启用事务，设置签收模式
		//如果使用事务提交的话，最后需要session.commit();
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);				
		//第四步，通过session创建Destination对象。并指定队列名称。
		Destination destination = session.createQueue("queue1");
		//第五步，创建生产者或消费者对象
		MessageProducer messageProducer = session.createProducer(null);
		//第六步，设置持久化与否的特性
//		messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		for(int i=0;i<5;i++){
			//第七步，创建消息对象并发送到ActiveMQ上
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("我是消息内容");			
			//第八步，发送消息
			messageProducer.send(destination, textMessage, DeliveryMode.PERSISTENT, i, 1000*60*60);			
			System.out.println("生产者生产消息"+i+":"+textMessage.getText());
		}
		if(connection!=null)
			connection.close();
	}
}
