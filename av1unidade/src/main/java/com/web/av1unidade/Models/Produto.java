package com.web.av1unidade.Models;

public class Produto {
    private int id;
    private int preco;
    private String nome;
    private String descricao;
    private int estoque;

    public Produto(int id, int preco, String nome, String descricao, int estoque) {
        this.id = id;
        this.preco = preco;
        this.nome = nome;
        this.descricao = descricao;
        this.estoque = estoque;
    }

    public Produto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEstoque() {
        return estoque;
    }

    public void incrementaEstoque() {
        this.estoque++;
    }

    public void diminuiEstoque() {
        this.estoque--;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}
