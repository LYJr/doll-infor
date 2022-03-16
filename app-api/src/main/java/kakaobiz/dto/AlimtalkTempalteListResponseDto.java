package kakaobiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlimtalkTempalteListResponseDto {
    TemplateListResponse templateListResponse;

    public List<String> getTemplateList() {
        return templateListResponse.getTemplates().stream().map(Template::getTemplateCode).collect(Collectors.toList());
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TemplateListResponse {
        List<Template> templates;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Template {
        String templateCode;
    }
}
