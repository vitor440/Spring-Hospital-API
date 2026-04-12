package com.gerenciamento_hospitalar.integrationtests.pageContent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;

import java.util.List;

public class MedicoPageContent {

    @JsonProperty("content")
    private List<MedicoResponse> content;

    public MedicoPageContent() {
    }

    public List<MedicoResponse> getContent() {
        return content;
    }

    public void setContent(List<MedicoResponse> content) {
        this.content = content;
    }
}
