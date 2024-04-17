package com.web.av1unidade.Controllers;

import com.web.av1unidade.DAO.ClienteDAO;
import com.web.av1unidade.Models.Cliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class CadastroController {

    @RequestMapping(value = "/doCadastro", method = RequestMethod.POST)
    public void doCadastro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String nome = request.getParameter("nome");
        final String email = request.getParameter("email");
        final String senha = request.getParameter("senha");

        final ClienteDAO cDAO = new ClienteDAO();
        final Cliente c = new Cliente(nome, email, senha);

        try {
            cDAO.cadastrar(c);
            response.sendRedirect("cadastro.html?msg=Cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendRedirect("cadastro.html?msg=Erro ao cadastrar");
        }
    }
}
