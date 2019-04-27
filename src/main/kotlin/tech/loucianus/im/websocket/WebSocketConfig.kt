package tech.loucianus.im.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.WebSocketHandlerDecorator

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    var users = HashMap<String, String>()

//    @Autowired private lateinit var template: SimpMessagingTemplate

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
//                    users[session.id] = session.principal!!.name



                    super.afterConnectionEstablished(session)
                }

                @Throws(Exception::class)
                override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
//                    users.remove(session.id)

//                    val user = UserStatus(
//                        name = session.principal!!.name,
//                        id = session.id,
//                        online = false
//                    )

//                    template.convertAndSend("/topic/contacts", user)

                    super.afterConnectionClosed(session, closeStatus)
                }
            }
        }
        super.configureWebSocketTransport(registration)
    }
}