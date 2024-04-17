package com.web.av1unidade.Controllers;

import com.web.av1unidade.DAO.ClienteDAO;
import com.web.av1unidade.DAO.LojistaDAO;
import com.web.av1unidade.Models.Cliente;
import com.web.av1unidade.Models.Lojista;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class AutenticacaoController {

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String email = request.getParameter("email");
        final String senha = request.getParameter("senha");

        final ClienteDAO cDAO = new ClienteDAO();
        final LojistaDAO lDAO = new LojistaDAO();
        final Cliente c = new Cliente(email, senha);
        final Lojista l = new Lojista(email, senha);

        if(cDAO.procurar(c)){
            final int clienteId = cDAO.selecionarId(c);
            final HttpSession session = request.getSession(true);
            session.setAttribute("logado", true);
            setClienteIdNaSessao(request, clienteId);
            response.sendRedirect("listaProdutos.html");
        } else if(lDAO.procurar(l)) {
            final HttpSession session = request.getSession(true);
            session.setAttribute("logado", true);

            response.sendRedirect("exibirProdutos.html");
        } else {
            response.sendRedirect("index.html?msg=Login falhou");
        }
    }

    @RequestMapping(value = "/doLogout", method = RequestMethod.GET)
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession();
        session.setAttribute("logado", false);
        limparClienteIdNaSessao(request);
        session.invalidate();
        response.sendRedirect("index.html?msg=Usuario deslogado");
    }

    private void setClienteIdNaSessao(HttpServletRequest request, final int clienteId) {
        final HttpSession session = request.getSession();
        session.setAttribute("clienteId", clienteId);
    }

    private void limparClienteIdNaSessao(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        session.removeAttribute("clienteId");
    }
}
