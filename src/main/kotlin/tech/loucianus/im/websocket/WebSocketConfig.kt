package tech.loucianus.im.websocket

import com.alibaba.fastjson.JSONObject
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.WebSocketHandlerDecorator

@Configuration
//此注解表示开启WebSocket支持。通过此注解开启使用STOMP协议来传输基于代理（message broker）的消息。
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    companion object {
        private val log = LogFactory.getLog(this::class.java)
    }

    var users = JSONObject()

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setUserDestinationPrefix("/user")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS()
    }

    override fun configureWebSocketTransport(registration: WebSocketTransportRegistration) {
        registration.addDecoratorFactory { handler ->
            object : WebSocketHandlerDecorator(handler) {
                @Throws(Exception::class)
                override fun afterConnectionEstablished(session: WebSocketSession) {
                    users[session.id] = session.principal!!.name
//                    val user = User(
//                        name = session.principal!!.name,
//                        id = session.id,
//                        online = true
//                    )

//                    template!!.convertAndSend("/topic/userlist", JSON.toJSON(user))
                    if (log.isInfoEnabled) {
                        log.info("sessionId::${users[session.id]}")
                        log.info("principal::${session.principal!!.name}")
                        log.info("连接成功 ${session.id}")
                    }
                    super.afterConnectionEstablished(session)
                }

                @Throws(Exception::class)
                override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
                    users.remove(session.id)
//                    val user = User(
//                        name = session.principal!!.name,
//                        id = session.id,
//                        online = false
//                    )
//                    template!!.convertAndSend("/topic/userlist", JSON.toJSON(user))
                    if (log.isInfoEnabled) log.info("连接成功 ${session.id}")
                    super.afterConnectionClosed(session, closeStatus)
                }
            }
        }
        super.configureWebSocketTransport(registration)
    }

//    override fun registerStompEndpoints(stompEndpointRegistry: StompEndpointRegistry) {
//        //注册一个名为/endpointChat的节点，并指定使用SockJS协议。
//        stompEndpointRegistry.addEndpoint("/webChat").withSockJS()
//    }
//
//    //配置消息代理（Message Broker），可以理解为信息传输的通道
//    override fun configureMessageBroker(messageBrokerRegistry: MessageBrokerRegistry) {
//        //点对点式应增加一个/queue的消息代理。相应的如果是广播室模式可以设置为"/topic"
//        messageBrokerRegistry.enableSimpleBroker("/queue")
//    }
}