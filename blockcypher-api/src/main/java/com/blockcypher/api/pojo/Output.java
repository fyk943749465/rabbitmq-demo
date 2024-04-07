package com.blockcypher.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Output {

    private Long value;

    private String script;


    private String[] addresses;

    @JsonProperty("script_type")
    private String scriptType;
}
