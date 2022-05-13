package com.fabianocampos.fidbackapi.dto;

import com.fabianocampos.fidbackapi.domain.User;
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
public class ProjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotNull(message = "Preencha o nome do projeto")
    private String name;

    @NotNull(message = "Preencha a descrição do projeto")
    private String description;

    @NotNull(message = "Preencha a visibilidade do projeto")
    private boolean visibility;

    private Integer projectOwnerId;

    @JsonFormat(pattern="dd/MM/yyyy")
    private Date createdAt;

}
