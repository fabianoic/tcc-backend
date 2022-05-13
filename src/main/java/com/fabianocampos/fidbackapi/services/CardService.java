package com.fabianocampos.fidbackapi.services;

import com.fabianocampos.fidbackapi.domain.*;
import com.fabianocampos.fidbackapi.domain.enums.UserType;
import com.fabianocampos.fidbackapi.domain.log.LogCard;
import com.fabianocampos.fidbackapi.dto.CardDTO;
import com.fabianocampos.fidbackapi.dto.converter.CardConverter;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CardRepository;
import com.fabianocampos.fidbackapi.repository.LogCardRepository;
import com.fabianocampos.fidbackapi.repository.TagRepository;
import com.fabianocampos.fidbackapi.repository.UserProjectRepository;
import com.fabianocampos.fidbackapi.services.exception.ObjectAlreadyExistsException;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import com.fabianocampos.fidbackapi.services.exception.PermissionInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CardService {

    @Autowired
    private CardRepository repo;

    @Autowired
    private CardConverter cardConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private LogCardRepository logCardRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    public Card findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Card não encontrado!"));
    }

    public Card findById(Integer id, String connectedUserEmail) {
        Card card = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Card não encontrado!"));
        validate(card, connectedUserEmail);

        return card;
    }

    public Card createOrUpdate(CardDTO cardDTO, Operation operation, String connectedUserEmail) {
        Card card = cardConverter.decode(cardDTO, operation);
        validate(card, connectedUserEmail);

        LogCard logCard = LogCard.builder().user(userService.findByEmail(connectedUserEmail)).createdAt(new Date()).descrition("").card(null).build();

        if (operation.equals(Operation.CREATE)) {
            if (repo.existsByReportId(cardDTO.getReportId())) {
                throw new ObjectAlreadyExistsException("Já existe card associado a este report.");
            }
            logCard.setDescrition("Card criado.");
        } else {
            logCard.setDescrition("Card alterado.");
        }

        card = repo.save(card);
        logCard.setCard(card);

        logCardRepository.save(logCard);

        return card;
    }

    private void validate(Card card, String connectedUserEmail) {
        User user = userService.findByEmail(connectedUserEmail);
        Report report = reportService.findById(card.getReport().getId());
        Project project = projectService.findById(report.getProject().getId());
        User isUserParticipant = projectService.findParticipantById(project.getId(), user.getId(), connectedUserEmail);

        if (isUserParticipant == null || userProjectRepository.existsByIdProjectIdAndIdUserIdAndUserType(project.getId(), user.getId(), UserType.USER)) {
            throw new PermissionInvalidException("Usuário não tem permissão para esta ação.");
        }
    }

    public void joinOrRemoveTagToCard(Integer cardId, Integer tagId, String connectedUserEmail, Operation operation) {
        Card card = findById(cardId);
        Tag tag = tagService.findById(tagId);
        projectService.connectedUserHasDevPermission(card.getReport().getProject().getId(), connectedUserEmail);

        if (operation.equals(Operation.DELETE)) {
            card.getTags().remove(tag);
            tag.getCards().remove(card);
        } else if (operation.equals(Operation.CREATE)) {
            card.getTags().add(tag);
            tag.getCards().add(card);
        }

        tagRepository.save(tag);
        repo.save(card);
    }

}
