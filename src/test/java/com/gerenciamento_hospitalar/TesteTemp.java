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

    private List<String> stringToList(String string) {
        return List.of(string.split(","));
    }

//    private String ListToString(List<String> lista) {
//
//    }

    @Test
    void teste () {

       String string = "dor de ouvido, sudorese";

       List<String> lista = List.of(string.split(","));

        System.out.println(string);
        System.out.println(lista);
        System.out.println(new StringBuilder(lista.toArray().toString()).toString());
        String teste = "";
        for (String s : lista) {
            teste = teste + s + ",";
        }

        String.join(",", lista);
        System.out.println(String.join(",", lista));


    }
}
