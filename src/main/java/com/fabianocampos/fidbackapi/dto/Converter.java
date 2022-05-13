package com.fabianocampos.fidbackapi.dto;

import com.fabianocampos.fidbackapi.dto.converter.enums.Operation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Converter<I, O> {

    O encode(I input);

    default List<O> encode(Collection<I> input) {
        return input.stream()
                .map(this::encode)
                .collect(Collectors.toList());
    }

    I decode(O input, Operation operation) throws Exception;
}
