package kakaobiz.dto;

import kakaobiz.template.AlimtalkRecipients;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlimtalkSubstitutionSendRequestDto {
    String senderKey;
    String templateCode;
    List<AlimtalkRecipients> recipientList;
}
