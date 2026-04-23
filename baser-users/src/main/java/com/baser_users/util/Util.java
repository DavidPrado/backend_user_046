package com.baser_users.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class Util {

    public static LocalDate convertStringToLocalDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            throw new IllegalArgumentException("A data de nascimento é obrigatória.");
        }

        // DateTimeFormatter é thread-safe e mais performático que o SimpleDateFormat
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Retorna diretamente o LocalDate
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            log.error("Data inválida recebida: {}", dateString);
            throw new IllegalArgumentException("O formato da data deve ser dd/MM/yyyy");
        }
    }
}
