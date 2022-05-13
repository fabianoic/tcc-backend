package com.fabianocampos.fidbackapi.resources;

import com.fabianocampos.fidbackapi.dto.CardDTO;
import com.fabianocampos.fidbackapi.dto.converter.CardConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/cards")
public class CardResource {

    @Autowired
    private CardService CardService;

    @Autowired
    private CardConverter cardConverter;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CardDTO> findById(@PathVariable Integer id, Principal principal) {
        return ResponseEntity.ok(cardConverter.encode(CardService.findById(id, principal.getName())));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CardDTO> create(@Validated @RequestBody CardDTO CardDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardConverter.encode(CardService.createOrUpdate(CardDto, Operation.CREATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CardDTO> update(@Validated @RequestBody CardDTO CardDto, @PathVariable Integer id, Principal principal) {
        CardDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(cardConverter.encode(CardService.createOrUpdate(CardDto, Operation.UPDATE, principal.getName())));
    }

    @RequestMapping(value = "/{id}/tags/{tagId}", method = RequestMethod.POST)
    public ResponseEntity<Void> joinTagToCard(@PathVariable Integer id, @PathVariable Integer tagId, Principal principal) {
        CardService.joinOrRemoveTagToCard(id, tagId, principal.getName(), Operation.CREATE);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/tags/{tagId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeTagToCard(@PathVariable Integer id, @PathVariable Integer tagId, Principal principal) {
        CardService.joinOrRemoveTagToCard(id, tagId, principal.getName(), Operation.DELETE);
        return ResponseEntity.noContent().build();
    }

}
