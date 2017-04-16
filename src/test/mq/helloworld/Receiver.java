package test.mq.helloworld;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {
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
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		//第四步，通过session创建Destination对象。并指定队列名称。
		Destination destination = session.createQueue("queue1");
		//第五步，创建生产者或消费者对象
		MessageConsumer messageCustomer = session.createConsumer(destination);
		
		while(true){
			TextMessage message = (TextMessage) messageCustomer.receive();
			if(message==null)
				break;
			System.out.println(message.getText());
			message.acknowledge();
		}
		//关闭连接
		if(connection!=null)
			connection.close();
	}
}
