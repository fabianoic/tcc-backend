package com.fabianocampos.fidbackapi.dto.converter;

import com.fabianocampos.fidbackapi.domain.Card;
import com.fabianocampos.fidbackapi.domain.Category;
import com.fabianocampos.fidbackapi.dto.CardDTO;
import com.fabianocampos.fidbackapi.dto.CategoryDTO;
import com.fabianocampos.fidbackapi.dto.Converter;
import com.fabianocampos.fidbackapi.dto.ReportDTO;
import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;
import com.fabianocampos.fidbackapi.repository.CardRepository;
import com.fabianocampos.fidbackapi.repository.CategoryRepository;
import com.fabianocampos.fidbackapi.services.CardService;
import com.fabianocampos.fidbackapi.services.LabelService;
import com.fabianocampos.fidbackapi.services.ReportService;
import com.fabianocampos.fidbackapi.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class CardConverter implements Converter<Card, CardDTO> {

    @Autowired
    private CardService cardService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LabelService labelService;

    @Override
    public CardDTO encode(Card card) {
        CardDTO cardDTO = CardDTO.builder().id(card.getId()).timeSpent(card.getTimeSpent()).reportId(card.getReport().getId()).labelId(card.getLabel().getId()).content(card.getReport().getTitle()).build();
        cardDTO.setLabels(new ArrayList<>(Arrays.asList("#7159c1")));
        cardDTO.setUser("https://www.w3schools.com/howto/img_avatar.png");
        return cardDTO;
    }

    @Override
    public Card decode(CardDTO cardDTO, Operation operation) {
        Card card = null;
        if (operation.equals(Operation.FIND)) {
            return cardService.findById(cardDTO.getId());
        } else if (operation.equals(Operation.UPDATE)) {
            card = cardService.findById(cardDTO.getId());
        } else if (operation.equals(Operation.CREATE)) {
            card = new Card();
            card.setReport(reportService.findById(cardDTO.getReportId()));
        }
        card.setLabel(labelService.findById(cardDTO.getLabelId()));
        card.setTimeSpent(cardDTO.getTimeSpent());
        return card;
    }
}
