package kakaobiz.common;

import com.nhn.aproject.appapi.common.kakaobiz.enums.AlimtalkTemplateCodeEnum;
import kakaobiz.template.AlimtalkRecipients;
import kakaobiz.template.alimtalk.ElectronicVoting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlimtalkParametersMaker<T extends AlimtalkBaseTempalte> {
    List<AlimtalkBaseTempalte> tempaltes = new ArrayList<>();
    AlimtalkTemplateCodeEnum templateCode;

    private AlimtalkParametersMaker(AlimtalkTemplateCodeEnum templateCode) {
        this.templateCode = templateCode;
    }

    //이 부분이 추가됨.
    public static AlimtalkParametersMaker<ElectronicVoting> ofElectronicVoting() {
        return new AlimtalkParametersMaker<>(AlimtalkTemplateCodeEnum.ElectronicVoting);
    }

    public void addTemplates(T tempalte) {
        tempaltes.add(tempalte);
    }

    public AlimtalkTemplateCodeEnum getTemplateCode() {
        return templateCode;
    }

    public List<AlimtalkRecipients> getAlimtalkRecipients() {
        return tempaltes.stream()
                .map(AlimtalkBaseTempalte::getAlimtalkRecipients)
                .collect(Collectors.toList());
    }
}