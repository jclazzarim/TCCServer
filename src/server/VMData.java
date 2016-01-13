/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author server
 */
public class VMData implements Serializable {

    private String domain;
    private String cpuLivre;
    private String memUsada;
    private String memLivre;

    public String getCpuLivre() {
        return cpuLivre;
    }

    public void setCpuLivre(String cpuLivre) {
        this.cpuLivre = cpuLivre;
    }

    public String getMemUsada() {
        return memUsada;
    }

    public void setMemUsada(String memUsada) {
        this.memUsada = memUsada;
    }

    public String getMemLivre() {
        return memLivre;
    }

    public void setMemLivre(String memLivre) {
        this.memLivre = memLivre;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dominio: ");
        sb.append(getDomain());
        sb.append(" | CPU Livre: ");
        sb.append(getCpuLivre());
        sb.append(" | Memo Usada: ");
        sb.append(getMemUsada());
        sb.append(" | Memo Livre: ");
        sb.append(getMemLivre());

        return sb.toString();
    }

}
