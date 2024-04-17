package com.web.av1unidade.DAO;

import com.web.av1unidade.Models.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProdutoDAO {
    private final Conexao con;
    private final String INSERT_QUERY = "INSERT INTO Produto (nome, descricao, preco, estoque) VALUES (?, ?, ?, ?)";
    private final String SELECT_ALL_QUERY = "SELECT * FROM Produto";
    private final String SELECT_BY_ID_QUERY = "SELECT * FROM Produto WHERE id = ?";
    private final String UPDATE_STOCK_QUERY = "UPDATE Produto SET estoque = estoque - ? WHERE id=?";

    public ProdutoDAO() {
        con = new Conexao("jdbc:postgresql://localhost:5432/banco_pw", "postgres", "postgres");
    }

    public void cadastrar(Produto produto) {
        try {
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(INSERT_QUERY);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setInt(3, produto.getPreco());
            ps.setInt(4, produto.getEstoque());
            ps.execute();
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na inclusão: " + e.getMessage());
        }
    }

    public ArrayList<Produto> listarProdutos() {
        ArrayList<Produto> produtos = new ArrayList<>();
        try {
            con.conectar();
            Statement st = con.getConexao().createStatement();
            ResultSet rs = st.executeQuery(SELECT_ALL_QUERY);
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getInt("preco"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("estoque")
                );
                produtos.add(produto);
            }
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return produtos;
    }

    public Produto getProdutoById(int produtoId) {
        Produto produto = null;
        try {
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(SELECT_BY_ID_QUERY);
            ps.setInt(1, produtoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                produto = new Produto(
                        rs.getInt("id"),
                        rs.getInt("preco"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("estoque")
                );
            }
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return produto;
    }

    public void decrementarEstoque(int quantidade, int id) {
        try {
            con.conectar();
            PreparedStatement ps = con.getConexao().prepareStatement(UPDATE_STOCK_QUERY);
            ps.setInt(1, quantidade);
            ps.setInt(2, id);
            ps.execute();
            con.desconectar();
        } catch (Exception e) {
            System.out.println("Erro na alteração: " + e.getMessage());
        }
    }

    public boolean estoqueMaiorOuIgualQueQuantidade(int quantidade, int id) {
        try {
            Produto produto = getProdutoById(id);
            return produto != null && produto.getEstoque() >= quantidade;
        } catch (Exception e) {
            System.out.println("Erro na verificação: " + e.getMessage());
        }
        return false;
    }
}
