package com.practice.boardpractice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@TestComponent
public class FormDataEncoder {

    private final ObjectMapper mapper;

    public FormDataEncoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    // URI 쿼리스트링을 URI 형식에 맞게 인코딩 해줌.
    public String encode(Object obj) {
        // ObjectMapper란. Class의 필드나 프로퍼티들을 다른 객체로 변형 시켜주는 역할을 하는 것.
        // 여기서는 Object 형식의 클래스의 field들을 Map의 형태로 반환.
        Map<String, String> fieldMap = mapper.convertValue(obj, new TypeReference<>() {});

        // MultiValueMap : 스프링에서 제공하는 인터페이스.
        // 보통 Map에는 키값이 중복으로 저장될 수 없지만, MultiValueMap에는 하나의 키값으로 여러번 데이터를 넣으면,
        // 해당 키값의 value에 List 형태로 저장이 된다.
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.setAll(fieldMap);

        return UriComponentsBuilder.newInstance()
                .queryParams(valueMap)
                .encode()
                .build()
                .getQuery();
    }
}
