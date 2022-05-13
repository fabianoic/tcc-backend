package com.fabianocampos.fidbackapi.services;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabianocampos.fidbackapi.domain.User;
import com.fabianocampos.fidbackapi.dto.UserDTO;
import com.fabianocampos.fidbackapi.dto.converter.UserConverter;
import com.fabianocampos.fidbackapi.repository.UserRepository;
import com.fabianocampos.fidbackapi.services.exception.LoginOrPasswordInvalidException;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private UserRepository repo;

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado!"));
    }

    public User findByNickname(String nickname) {
        User user = repo.findByNickname(nickname);
        return Optional.ofNullable(user).orElseThrow(() -> new ObjectNotFoundException("Nickname inválido"));
    }

    public User findByEmail(String email) {
        User user = repo.findByEmail(email);
        return Optional.ofNullable(user).orElseThrow(() -> new ObjectNotFoundException("Email inválido"));
    }

    public User createOrUpdate(UserDTO userDTO, Operation operation, String connectedUserEmail) {
        if (operation.equals(Operation.UPDATE)) {
            User user = findByEmail(connectedUserEmail);
            if (!(user.getId() == userDTO.getId())) {
                throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
            }
        }
        validateUser(userDTO);

        User user = userConverter.decode(userDTO, operation);
        return repo.save(user);
    }

    public void delete(Integer id) {
        User user = this.findById(id);
        user.setActive(false);
        repo.save(user);
    }

    public User findByPrincipal(Principal principal) {
        return repo.findByEmail(principal.getName());
    }

    private boolean validateUser(UserDTO userDTO) {
        User userByEmail = repo.findByEmail(userDTO.getEmail());
        User userByNickname = repo.findByNickname(userDTO.getNickname());
        boolean existsUserByEmail = repo.existsByEmail(userDTO.getEmail());
        boolean existsUserByNickname = repo.existsByNickname(userDTO.getNickname());

        if (userDTO.getId() != null) {
            if (userByNickname != null && userByNickname.getId() != userDTO.getId()) {
                throw new ObjectAlreadyExistsException("Nickname indisponivel!");
            }
            if (userByEmail != null && userByEmail.getId() != userDTO.getId()) {
                throw new ObjectAlreadyExistsException("Já existe conta com este e-mail!");
            }
        }

        if (userDTO.getNickname().contains(" ")) {
            throw new IllegalArgumentException("Nickname não deve conter espaço em branco!");
        } else if (userDTO.getPassword() != null && userDTO.getConfirmPassword() != null && !userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new LoginOrPasswordInvalidException("A senha e a confirmação de senha não são iguais!");
        } else if (userDTO.getId() == null && existsUserByEmail) {
            throw new ObjectAlreadyExistsException("Já existe conta com este e-mail!");
        } else if (userDTO.getId() == null && existsUserByNickname) {
            throw new ObjectAlreadyExistsException("Nickname indisponivel!");
        }

        return true;
    }
}