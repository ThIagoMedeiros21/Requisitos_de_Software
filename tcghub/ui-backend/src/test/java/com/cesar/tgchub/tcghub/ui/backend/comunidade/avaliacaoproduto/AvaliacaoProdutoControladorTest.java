package com.cesar.tgchub.tcghub.ui.backend.comunidade.avaliacaoproduto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AvaliacaoProdutoControladorTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	void autenticacaoExpoeAutorizacoesDeAvaliacaoProdutoECatalogoRetornaResumo() throws Exception {
		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.avaliacaoProduto.avaliar").value(true))
				.andExpect(jsonPath("$.avaliacaoProduto.buscar").value(true));

		mockMvc.perform(get("/backend/comunidade/avaliacao-produto/catalogo?termo=OP05-119")
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].produtoId").value("OP05-119"))
				.andExpect(jsonPath("$[0].media").value(4.5))
				.andExpect(jsonPath("$[0].quantidadeValida").value(2))
				.andExpect(jsonPath("$[0].podeAvaliar").value(true));

		mockMvc.perform(get("/backend/comunidade/avaliacao-produto/OP01-006/pesquisa")
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resumo.media").value(0.0))
				.andExpect(jsonPath("$.resumo.quantidadeValida").value(0))
				.andExpect(jsonPath("$.podeAvaliar").value(false));
	}

	@Test
	void aplicaElegibilidadeProxyValidacaoNotaAutorizacaoEReembolsoRN005() throws Exception {
		// Thiago tem apenas PED-300 reembolsado para OP05-119 -> inelegível
		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/OP05-119/avaliar")
						.with(httpBasic("thiago", "thiago")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nota":5,"comentario":"Tentando avaliar reembolsada"}
								"""))
				.andExpect(status().is4xxClientError());

		// Ricardo tentando alterar avaliação de lucas -> 403
		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/OP05-119/avaliar")
						.with(httpBasic("ricardo", "ricardo")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nota":1,"comentario":"Indevido","autorUsuario":"lucas"}
								"""))
				.andExpect(status().isForbidden());

		// Nota fora da faixa 1..5 -> 422 (IllegalArgumentException no D5)
		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/OP01-016/avaliar")
						.with(httpBasic("lucas", "lucas")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nota":6,"comentario":"Nota inválida"}
								"""))
				.andExpect(status().is4xxClientError());

		// Thiago registra compra paga para OP01-047, avalia, reavalia sem duplicar, e depois reembolsa (RN005)
		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/compras/registrar")
						.with(httpBasic("thiago", "thiago")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"pedidoId":"PED-900","produtoId":"OP01-047","paga":true}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.elegivel").value(true));

		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/OP01-047/avaliar")
						.with(httpBasic("thiago", "thiago")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nota":3,"comentario":"Boa carta"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.selo").value("compra verificada"));

		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/OP01-047/avaliar")
						.with(httpBasic("thiago", "thiago")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nota":5,"comentario":"Atualizada para 5"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nota").value(5));

		mockMvc.perform(get("/backend/comunidade/avaliacao-produto/OP01-047/pesquisa")
						.with(httpBasic("thiago", "thiago")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.resumo.media").value(5.0))
				.andExpect(jsonPath("$.resumo.quantidadeValida").value(1));

		// Reembolso do único pedido válido invalida a avaliação e recalcula para 0.0 / 0
		mockMvc.perform(post("/backend/comunidade/avaliacao-produto/compras/PED-900/reembolsar")
						.with(httpBasic("thiago", "thiago")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"produtoId":"OP01-047"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.media").value(0.0))
				.andExpect(jsonPath("$.quantidadeValida").value(0));
	}
}
