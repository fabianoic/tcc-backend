package com.fabianocampos.fidbackapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer fromList;

    private Integer toList;

    private Integer from;

    private Integer to;

}
