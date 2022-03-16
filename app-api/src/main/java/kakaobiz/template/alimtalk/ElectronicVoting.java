package kakaobiz.template.alimtalk;

import com.nhn.aproject.appapi.common.kakaobiz.common.AlimtalkBaseTempalte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 전자 투표 = electronic_vote_001
@Getter
public class ElectronicVoting extends AlimtalkBaseTempalte {
    private SubstitutionParam param;

    @Builder
    public ElectronicVoting(String title, String complex, String dongHo, String phone, String recipientNo) {
        super(recipientNo);
        this.param = SubstitutionParam.builder()
                .title(title)
                .complex(complex)
                .dongHo(dongHo)
                .phone(phone)
                .build();
    }

    @Override
    protected Object getTemplateParameter() {
        return param;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SubstitutionParam {
        private String title;
        private String complex;
        private String dongHo;
        private String phone;
    }
}

