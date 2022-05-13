package com.fabianocampos.fidbackapi.domain;

import com.fabianocampos.fidbackapi.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProject implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserProjectPK id;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	@NotNull(message = "É obrigatório selecionar o tipo de usuário")
	private UserType userType;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

}
