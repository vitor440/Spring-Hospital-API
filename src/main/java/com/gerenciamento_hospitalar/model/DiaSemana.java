package com.gerenciamento_hospitalar.model;

public enum DiaSemana {
    SEGUNDA(1),
    TERCA(2),
    QUARTA(3),
    QUINTA(4),
    SEXTA(5),
    SABADO(6),
    DOMINGO(7);

    private int value;

    DiaSemana(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static DiaSemana getDiaSemana(int value) {
        switch (value){

            case 1 -> {
                return DiaSemana.SEGUNDA;
            }
            case 2 -> {
                return DiaSemana.TERCA;
            }
            case 3 -> {
                return DiaSemana.QUARTA;
            }
            case 4 -> {
                return DiaSemana.QUINTA;
            }
            case 5 -> {
                return DiaSemana.SEXTA;
            }
            case 6 -> {
                return DiaSemana.SABADO;
            }
            case 7 -> {
                return DiaSemana.DOMINGO;
            }
            default -> {
                return  null;
            }
        }
    }

}
