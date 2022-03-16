package kakaobiz.enums;

import lombok.Getter;

@Getter
public enum AlimtalkTemplateCodeEnum {

    ElectronicVoting("전자 투표", "electronic_vote_001");
//    TEST("실패 테스트", "실패 테스트를 위해 존재");

    public String templateName;
    public String templateCode;

    AlimtalkTemplateCodeEnum(String name, String code) {
        this.templateName = name;
        this.templateCode = code;
    }
}