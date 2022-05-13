package com.fabianocampos.fidbackapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotNull(message = "Preencha o campo titulo")
    private String title;

    @NotNull(message = "Preencha o campo descrição")
    private String description;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    private Integer status;

    private String statusDescription;

    private Integer categoryId;

    private Integer projectId;

    private Integer userId;
}
