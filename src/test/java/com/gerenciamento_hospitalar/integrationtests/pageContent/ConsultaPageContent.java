package com.gerenciamento_hospitalar.integrationtests.pageContent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;

import java.util.List;

public class ConsultaPageContent {

    @JsonProperty("content")
    private List<ConsultaResponse> content;

    public ConsultaPageContent() {
    }

    public List<ConsultaResponse> getContent() {
        return content;
    }

    public void setContent(List<ConsultaResponse> content) {
        this.content = content;
    }
}
