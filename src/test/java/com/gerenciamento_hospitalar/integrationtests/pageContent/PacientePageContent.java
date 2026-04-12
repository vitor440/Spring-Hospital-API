package com.gerenciamento_hospitalar.integrationtests.pageContent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;

import java.io.Serializable;
import java.util.List;

public class PacientePageContent implements Serializable {

    @JsonProperty("content")
    private List<PacienteResponse> content;

    public PacientePageContent() {

    }

    public List<PacienteResponse> getContent() {
        return content;
    }

    public void setContent(List<PacienteResponse> content) {
        this.content = content;
    }
}
