package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.IDTO;
import at.sporty.team1.shared.enums.MessageType;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public class MessageDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _recipientId;
    private Integer _senderId;
    private MessageType _messageType;
    private String _messageSubject;
    private String _messageContent;

    public Integer getSenderId() {
        return _senderId;
    }

    public MessageDTO setSenderId(Integer senderId) {
        _senderId = senderId;
        return this;
    }

    public Integer getRecipientId() {
        return _recipientId;
    }

    public MessageDTO setRecipientId(Integer recipientId) {
        _recipientId = recipientId;
        return this;
    }

    public MessageType getMessageType() {
        return _messageType;
    }

    public MessageDTO setMessageType(MessageType messageType) {
        _messageType = messageType;
        return this;
    }

    public String getMessageSubject() {
        return _messageSubject;
    }

    public MessageDTO setMessageSubject(String messageSubject) {
        _messageSubject = messageSubject;
        return this;
    }

    public String getMessageContent() {
        return _messageContent;
    }

    public MessageDTO setMessageContent(String messageContent) {
        _messageContent = messageContent;
        return this;
    }
}
