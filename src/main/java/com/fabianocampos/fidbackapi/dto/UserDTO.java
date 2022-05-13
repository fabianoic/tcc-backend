package com.fabianocampos.fidbackapi.dto;

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
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotNull(message = "Preencha o nome")
    private String firstName;

    @NotNull(message = "Preencha o sobrenome")
    private String lastName;

    @NotNull(message = "Preencha o nickname")
    private String nickname;

    @NotNull(message = "Digite um e-mail")
    private String email;

    private String password;

    private String confirmPassword;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;


}
