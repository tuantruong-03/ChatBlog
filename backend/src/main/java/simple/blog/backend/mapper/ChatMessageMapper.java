package simple.blog.backend.mapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import simple.blog.backend.dto.response.ChatMessageResponse;
import simple.blog.backend.model.ChatMessage;

public class ChatMessageMapper {
    public static ChatMessageResponse toResponseFromModel(ChatMessage chatMessage) {
        String inputDateString = chatMessage.getTimestamp().toString();
        
        // Define the input formatter to parse the given date string
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("EEE MMM dd HH:mm:ss z yyyy")
                .toFormatter(Locale.ENGLISH);

        // Parse the input date string into a ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(inputDateString, inputFormatter);

        // Define the output formatter to format the ZonedDateTime
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy 'AT' HH:mm", Locale.ENGLISH);

        // Format the ZonedDateTime into the desired string
        String outputDateString = zonedDateTime.format(outputFormatter);
        return ChatMessageResponse.builder()
                                .chatMessageId(chatMessage.getChatMessageId())
                                .content(chatMessage.getContent())
                                .senderId(chatMessage.getSenderId())
                                .timestamp(outputDateString)
                                .build();
    }
}
