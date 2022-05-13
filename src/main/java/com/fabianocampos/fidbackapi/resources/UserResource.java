package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.UserDTO;
import com.fabianocampos.fidbackapi.dto.converter.UserConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userConverter.encode(userService.findAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(userConverter.encode(userService.findById(id)));
    }

    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userConverter.encode(userService.findByEmail(email)));
    }

    @RequestMapping(value = "/nickname/{nickname}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findByNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(userConverter.encode(userService.findByNickname(nickname)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDTO> create(@Validated @RequestBody UserDTO userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userConverter.encode(userService.createOrUpdate(userDto, Operation.CREATE, null)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> update(@Validated @RequestBody UserDTO userDto, @PathVariable Integer id, Principal principal) {
        userDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(userConverter.encode(userService.createOrUpdate(userDto, Operation.UPDATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
