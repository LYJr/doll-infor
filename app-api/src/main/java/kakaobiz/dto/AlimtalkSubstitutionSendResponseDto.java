package kakaobiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AlimtalkSubstitutionSendResponseDto {
    private Header header;
    private Message message;

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Header {
        Integer resultCode;
        String resultMessage;
        Boolean isSuccessful;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class Message {
        String requestId;
        String senderGroupingKey;
        List<SendResult> sendResults;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class SendResult {
        Long recipientSeq;
        String recipientNo;
        Integer resultCode;
        String resultMessage;
        String recipientGroupingKey;
    }
}
