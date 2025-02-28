package com.example.stockaccounts.entity;

import com.example.stockaccounts.dto.MagicFormulaDTO;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

//@Entity
public class MagicFormulaStock {
    private String papel;
    private BigDecimal cotacao;
    private BigDecimal pl;
    private BigDecimal EvEbit;
    private BigDecimal roic;
    private BigDecimal liq2meses;

    private int rankEvEbit;
    private int rankRoic;
    private int rankGlobal;

    public MagicFormulaStock(String papel, BigDecimal cotacao, BigDecimal pl, BigDecimal evEbit, BigDecimal roic, BigDecimal liq2meses) {
        this.papel = papel;
        this.cotacao = cotacao;
        this.pl = pl;
        EvEbit = evEbit;
        this.roic = roic;
        this.liq2meses = liq2meses;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public BigDecimal getCotacao() {
        return cotacao;
    }

    public void setCotacao(BigDecimal cotacao) {
        this.cotacao = cotacao;
    }

    public BigDecimal getPl() {
        return pl;
    }

    public void setPl(BigDecimal pl) {
        this.pl = pl;
    }

    public BigDecimal getEvEbit() {
        return EvEbit;
    }

    public void setEvEbit(BigDecimal evEbit) {
        EvEbit = evEbit;
    }

    public BigDecimal getRoic() {
        return roic;
    }

    public void setRoic(BigDecimal roic) {
        this.roic = roic;
    }

    public BigDecimal getLiq2meses() {
        return liq2meses;
    }

    public void setLiq2meses(BigDecimal liq2meses) {
        this.liq2meses = liq2meses;
    }

    public int getRankEvEbit() {
        return rankEvEbit;
    }

    public void setRankEvEbit(int rankEvEbit) {
        this.rankEvEbit = rankEvEbit;
    }

    public int getRankRoic() {
        return rankRoic;
    }

    public void setRankRoic(int rankRoic) {
        this.rankRoic = rankRoic;
    }

    public int getRankGlobal() {
        return rankGlobal;
    }

    public void setRankGlobal(int rankGlobal) {
        this.rankGlobal = rankGlobal;
    }
}
