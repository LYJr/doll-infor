package kakaobiz.common;

import com.nhn.aproject.appapi.common.dooray.DoorayAlarmUrlConfiguration;
import com.nhn.aproject.appapi.common.dooray.DoorayMessageManager;
import com.nhn.aproject.appapi.common.dooray.DoorayTopicEnum;
import com.nhn.aproject.appapi.common.kakaobiz.dto.AlimtalkSubstitutionSendRequestDto;
import com.nhn.aproject.appapi.common.kakaobiz.dto.AlimtalkSubstitutionSendResponseDto;
import com.nhn.aproject.appapi.common.kakaobiz.dto.AlimtalkTempalteListResponseDto;
import com.nhn.aproject.appapi.common.kakaobiz.enums.AlimtalkTemplateCodeEnum;
import kakaobiz.template.AlimtalkRecipients;
import com.nhn.aproject.appapi.infrastructure.client.dooray.data.DoorayMessageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlimtalkClient {

    @Value("${kakao-biz-alimtalk.base-url}")
    String baseUrl;

    @Value("${kakao-biz-alimtalk.secret-key}")
    String secretKey;

    @Value("${kakao-biz-alimtalk.send-key}")
    String sendKey;

    @Value("${kakao-biz-alimtalk.detail-url.substitution-send}")
    String substitutionSendUrl;

    @Value("${kakao-biz-alimtalk.detail-url.templates-search}")
    String templateSearchUrl;

    HttpClient httpClient = HttpClient
            .create()
            .wiretap(true);


    WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();


    private final DoorayAlarmUrlConfiguration doorayAlarmUrlConfiguration;
    private final DoorayMessageManager doorayMessageManager;

    private List<String> onEmailErrorUrls;

    @PostConstruct
    public void init() {
        onEmailErrorUrls = doorayAlarmUrlConfiguration.getTopicUrls(DoorayTopicEnum.ERROR);
        isTempalte();
    }

    public void isTempalte() {
        List<String> searchTemplateList =  searchTempalteList().getTemplateList();

        for (AlimtalkTemplateCodeEnum templateCode : AlimtalkTemplateCodeEnum.values()) {
            if(!searchTemplateList.contains(templateCode.getTemplateCode())) {
                String messageBody = String.format("(%s)[%s] 이 카카오 비즈 알람톡 템플릿은 유효하지 않습니다.",
                        templateCode.getTemplateName()
                        ,templateCode.getTemplateCode());
                doorayMessageManager.sendMessages(onEmailErrorUrls, DoorayMessageData.of(DoorayTopicEnum.ERROR, messageBody));
            }
        }
    }

    private void substitutionSendErrorMessage(AlimtalkTemplateCodeEnum templateCode, AlimtalkSubstitutionSendResponseDto responseDto) {
        for (AlimtalkSubstitutionSendResponseDto.SendResult response : responseDto.getMessage().getSendResults()) {
            if (response.getResultCode() != 0) {
                String messageBody = String.format("(%s)[%s] 카카오 비즈 알람톡 발송 실패\n(code : %s)[ message: %s ]\n요청 Id: %s [ recipientSeq: %s ]",
                        templateCode.getTemplateCode()
                        , templateCode.getTemplateName()
                        , response.getResultCode()
                        , response.getResultMessage()
                        , responseDto.getMessage().getRequestId()
                        , response.getRecipientSeq());
                doorayMessageManager.sendMessages(onEmailErrorUrls, DoorayMessageData.of(DoorayTopicEnum.ERROR, messageBody));
            }
        }
    }

    public AlimtalkSubstitutionSendResponseDto substitutionSend(AlimtalkParametersMaker parameters) {
        AlimtalkSubstitutionSendResponseDto responseDto = webClient.mutate()
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri(uriBuilder ->
                        uriBuilder.path(substitutionSendUrl).build()
                )
                .header("X-Secret-Key", secretKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createSubstitutionSendParameter(parameters))
                .retrieve()
                .onStatus(status -> status.is4xxClientError()
                                || status.is5xxServerError()
                        , clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .map(RuntimeException::new))
                .bodyToMono(AlimtalkSubstitutionSendResponseDto.class)
                .block();

        substitutionSendErrorMessage(parameters.getTemplateCode(), responseDto);
        return responseDto;
    }

    private AlimtalkSubstitutionSendRequestDto createSubstitutionSendParameter(AlimtalkParametersMaker parameters) {
        List<AlimtalkRecipients> recipients = parameters.getAlimtalkRecipients();
        String templateCode = parameters.getTemplateCode().getTemplateCode();
        return new AlimtalkSubstitutionSendRequestDto(sendKey, templateCode, recipients);
    }

    /**
     * 카카오 비즈 알람톡 템플릿을 검증하기 위해 카카오 비즈니스 템플릿 List api를 호출
     * TSC03 = 승인 받은 템플릿 코드
     * @return
     */
    public AlimtalkTempalteListResponseDto searchTempalteList() {
         return webClient.mutate()
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(templateSearchUrl)
                        .queryParam("templateStatus", "TSC03")
                        .build())
                 .header("X-Secret-Key", secretKey)
                 .retrieve()
                .bodyToMono(AlimtalkTempalteListResponseDto.class)
                .block();
    }
}
