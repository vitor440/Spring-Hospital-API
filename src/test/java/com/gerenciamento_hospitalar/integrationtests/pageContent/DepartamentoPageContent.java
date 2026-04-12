package com.gerenciamento_hospitalar.integrationtests.pageContent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;

import java.util.List;

public class DepartamentoPageContent {

    @JsonProperty("content")
    private List<DepartamentoResponse> content;

    public DepartamentoPageContent() {
    }

    public List<DepartamentoResponse> getContent() {
        return content;
    }

    public void setContent(List<DepartamentoResponse> content) {
        this.content = content;
    }
}
