package com.web.av1unidade.Models;

import java.util.ArrayList;

public class Carrinho {
    private ArrayList<Produto> produtos;

    public Carrinho(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public Produto getProduto(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    public void removeProduto(int id) {
        Produto produto = getProduto(id);
        if (produto != null) {
            produtos.remove(produto);
        }
    }

    public void addProduto(Produto produto) {
        produtos.add(produto);
    }
}
