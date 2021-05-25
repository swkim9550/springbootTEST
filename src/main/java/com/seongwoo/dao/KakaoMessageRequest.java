package com.seongwoo.dao;

import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
public class KakaoMessageRequest {
    MultiValueMap<String, Object> template_object = new LinkedMultiValueMap<String, Object>();
    private String object_type;
    private String text;
    private String link;

    public MultiValueMap<String, Object> getTemplate_object() {
        return template_object;
    }

    public void setTemplate_object(MultiValueMap<String, Object> template_object) {
        template_object.add("object_type",object_type);
        template_object.add("text",text);
        template_object.add("link",link);

        this.template_object = template_object;
    }
}
