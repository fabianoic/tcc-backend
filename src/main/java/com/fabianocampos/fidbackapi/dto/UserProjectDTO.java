package com.fabianocampos.fidbackapi.dto;

import com.fabianocampos.fidbackapi.domain.UserProjectPK;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer projectId;

    @NotNull(message = "Preencha o id do usuário")
    private Integer userId;

    @NotNull(message = "Preencha o tipo de usuário")
    private Integer userTypeId;

}
