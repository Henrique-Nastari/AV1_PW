package com.web.av1unidade.Controllers;

import com.web.av1unidade.DAO.ProdutoDAO;
import com.web.av1unidade.Models.Produto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CarrinhoController {

    @GetMapping("/addCarrinho")
    public void addCarrinho(@RequestParam int produtoId, @RequestParam String comando, HttpServletRequest request, HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession(true);

        final Produto p = ProdutoDAO.getProdutoById(produtoId);

        final Integer clienteId = (Integer) session.getAttribute("clienteId");

        Map<Integer, Integer> carrinho = (Map<Integer, Integer>) session.getAttribute("carrinho_" + clienteId);
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }

        if ("add".equals(comando)) {
            p.diminuiEstoque();
            carrinho.put(produtoId, carrinho.getOrDefault(produtoId, 0) + 1);
        } else if ("remove".equals(comando)) {
            p.incrementaEstoque();
            if (carrinho.containsKey(produtoId)) {
                int quantidade = carrinho.get(produtoId);
                if (quantidade > 1) {
                    carrinho.put(produtoId, quantidade - 1);
                } else {
                    carrinho.remove(produtoId);
                }
            }
        }

        session.setAttribute("carrinho_" + clienteId, carrinho);

        salvarCarrinhoNosCookies(clienteId, carrinho, response);

        response.sendRedirect("listaProdutos.html");
    }

    private void salvarCarrinhoNosCookies(Integer clienteId, Map<Integer, Integer> carrinho, HttpServletResponse response) {
        final StringBuilder carrinhoString = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            carrinhoString.append(entry.getKey()).append(":").append(entry.getValue()).append("_");
        }
        if (carrinhoString.length() > 0) {
            carrinhoString.deleteCharAt(carrinhoString.length() - 1);
        }
        final Cookie carrinhoCookie = new Cookie("carrinho_" + clienteId, carrinhoString.toString());
        carrinhoCookie.setMaxAge(48 * 60 * 60);
        response.addCookie(carrinhoCookie);
    }

    public Map<Integer, Integer> getCarrinhoFromCookies(Integer clienteId, HttpServletRequest request) {
        final Map<Integer, Integer> carrinho = new HashMap<>();
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("carrinho_" + clienteId).equals(cookie.getName())) {
                    final String cookieValue = cookie.getValue();
                    if (!cookieValue.isEmpty()) {
                        final String[] itens = cookieValue.split("_");
                        for (String item : itens) {
                            final String[] produtoInfo = item.split(":");
                            if (produtoInfo.length >= 2) {
                                try {
                                    final int produtoId = Integer.parseInt(produtoInfo[0]);
                                    final int quantidade = Integer.parseInt(produtoInfo[1]);
                                    carrinho.put(produtoId, quantidade);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        return carrinho;
    }

    @GetMapping("/finalizarCompra")
    public void finalizarCompra( HttpServletRequest request, HttpServletResponse response) throws IOException {
        final HttpSession session = request.getSession();
        final Integer clienteId = (Integer) session.getAttribute("clienteId");
        final CarrinhoController carrinhoController = new CarrinhoController();


        final Map<Integer, Integer> carrinho = carrinhoController.getCarrinhoFromCookies(clienteId, request);


        final ProdutoDAO p = new ProdutoDAO();
        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            final int productId = entry.getKey();
            final int quantity = entry.getValue();

            final Produto produto = ProdutoDAO.getProdutoById(productId);
            p.decrementarEstoque(quantity, productId);
        }


        session.removeAttribute("carrinho_" + clienteId);


        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (("carrinho_" + clienteId).equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        response.sendRedirect("/listaProdutosCliente");
    }
}
