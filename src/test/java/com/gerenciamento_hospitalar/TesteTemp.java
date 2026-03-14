package com.gerenciamento_hospitalar;

import com.gerenciamento_hospitalar.model.Consulta;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TesteTemp {

    @Test
    void teste () {

       LocalDate data = LocalDate.of(2026, 3, 16);

        System.out.println(data.getDayOfWeek().getValue());
    }
}
