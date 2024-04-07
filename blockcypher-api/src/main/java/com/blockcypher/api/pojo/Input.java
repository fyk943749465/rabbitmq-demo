package com.blockcypher.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Input {

    @JsonProperty("prev_hash")
    private String prevHash;

    @JsonProperty("output_index")
    private Integer outputIndex;

    private String script;

    @JsonProperty("output_value")
    private Long outputValue;

    private Long sequence;

    private String[] addresses;

    @JsonProperty("script_type")
    private String scriptType;

    private Integer age;
}
