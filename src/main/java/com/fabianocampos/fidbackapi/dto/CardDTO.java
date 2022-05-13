package com.fabianocampos.fidbackapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer timeSpent;

    private Integer reportId;

    private Integer labelId;

    private String content;

    private List<String> labels = new ArrayList<>(Arrays.asList("#7159c1"));

    private String user = "https://www.w3schools.com/howto/img_avatar.png";
}
