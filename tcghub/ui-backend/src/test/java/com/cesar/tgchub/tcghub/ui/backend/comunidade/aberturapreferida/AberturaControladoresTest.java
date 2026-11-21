package com.cesar.tgchub.tcghub.ui.backend.comunidade.aberturapreferida;

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

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

@SpringBootTest
class AberturaControladoresTest {
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private IAberturaRepositorio aberturaRepositorio;

	@Autowired
	private IPerfilComunitarioRepositorio perfilRepositorio;

	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	void autenticacaoExpoeAutorizacoesDeAberturaEAberturaPreferida() throws Exception {
		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.abertura.incluir").value(false))
				.andExpect(jsonPath("$.abertura.buscar").value(true))
				.andExpect(jsonPath("$.aberturaPreferida.escolher").value(true))
				.andExpect(jsonPath("$.aberturaPreferida.ocultar").value(true))
				.andExpect(jsonPath("$.aberturaPreferida.reexibir").value(true));

		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("a", "a")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.abertura.incluir").value(true))
				.andExpect(jsonPath("$.abertura.inativar").value(true))
				.andExpect(jsonPath("$.abertura.excluir").value(true));
	}

	@Test
	void gestaoAdministrativaDeAberturasAplicaAutorizacaoUnicidadeERN006() throws Exception {
		var op1Id = aberturaRepositorio.buscarPorNumero(1).getIdentificador().orElseThrow().toString();

		// Pesquisa administrativa
		mockMvc.perform(get("/backend/adm/abertura/pesquisa").with(httpBasic("a", "a")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(8))
				.andExpect(jsonPath("$.ativas").value(8))
				.andExpect(jsonPath("$.itens[0].numero").value(1))
				.andExpect(jsonPath("$.itens[0].quantidadePreferencias").value(2));

		// Usuário comum não pode cadastrar nem excluir
		mockMvc.perform(post("/backend/adm/abertura/incluir")
						.with(httpBasic("lucas", "lucas")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"numero":99,"nome":"Teste","artista":"Art","episodios":"1","imagem":"img.jpg","video":"vid.webm","ativa":true}
								"""))
				.andExpect(status().isForbidden());

		// Admin não pode cadastrar número duplicado (409)
		mockMvc.perform(post("/backend/adm/abertura/incluir")
						.with(httpBasic("a", "a")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"numero":1,"nome":"Dup","artista":"Art","episodios":"1","imagem":"img.jpg","video":"vid.webm","ativa":true}
								"""))
				.andExpect(status().isConflict());

		// Admin não pode excluir abertura já escolhida (RN006)
		mockMvc.perform(post("/backend/adm/abertura/{id}/excluir", op1Id)
						.with(httpBasic("a", "a")).with(csrf()))
				.andExpect(status().is4xxClientError());

		// Admin cria, edita, inativa, reativa e exclui abertura sem escolhas
		var criadaJson = mockMvc.perform(post("/backend/adm/abertura/incluir")
						.with(httpBasic("a", "a")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"numero":99,"nome":"Dreamin' On","artista":"Da-iCE","episodios":"935 a 999","imagem":"https://api.animethemes.moe/img/99.jpg","video":"https://v.animethemes.moe/OP23.webm","ativa":true}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numero").value(99))
				.andReturn().getResponse().getContentAsString();
		var id99 = com.fasterxml.jackson.databind.json.JsonMapper.builder().build().readTree(criadaJson).get("id")
				.asText();

		mockMvc.perform(post("/backend/adm/abertura/{id}/inativar", id99).with(httpBasic("a", "a")).with(csrf()))
				.andExpect(status().isOk());
		mockMvc.perform(post("/backend/adm/abertura/{id}/reativar", id99).with(httpBasic("a", "a")).with(csrf()))
				.andExpect(status().isOk());
		mockMvc.perform(post("/backend/adm/abertura/{id}/excluir", id99).with(httpBasic("a", "a")).with(csrf()))
				.andExpect(status().isOk());
	}

	@Test
	void comunidadeAberturaPreferidaAplicaRankingProxyDePrivacidadeEEscolha() throws Exception {
		var perfilThiagoId = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador("thiago"))
				.getIdentificador().orElseThrow().toString();
		var op1Id = aberturaRepositorio.buscarPorNumero(1).getIdentificador().orElseThrow().toString();
		var op2Id = aberturaRepositorio.buscarPorNumero(2).getIdentificador().orElseThrow().toString();

		// Pesquisa comunidade + Ranking Top 5
		mockMvc.perform(get("/backend/comunidade/abertura-preferida/pesquisa").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.minhaPreferida.numero").value(1))
				.andExpect(jsonPath("$.aberturasAtivas.length()").value(8))
				.andExpect(jsonPath("$.ranking.totalEscolhas").value(3))
				.andExpect(jsonPath("$.ranking.itens[0].numero").value(1))
				.andExpect(jsonPath("$.ranking.itens[0].quantidadeEscolhas").value(2));

		// Detalhes da abertura #1
		mockMvc.perform(get("/backend/comunidade/abertura-preferida/{id}/visualizacao", op1Id)
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numero").value(1))
				.andExpect(jsonPath("$.ehMinhaPreferida").value(true))
				.andExpect(jsonPath("$.posicaoRanking").value(1));

		// Thiago substitui abertura para #2 e oculta do perfil público
		mockMvc.perform(post("/backend/comunidade/abertura-preferida/escolher")
						.with(httpBasic("thiago", "thiago")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"aberturaId\":\"" + op2Id + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numero").value(2));

		mockMvc.perform(post("/backend/comunidade/abertura-preferida/ocultar")
						.with(httpBasic("thiago", "thiago")).with(csrf()))
				.andExpect(status().isOk());

		// Visitante (lucas) não vê abertura oculta de thiago
		mockMvc.perform(get("/backend/comunidade/abertura-preferida/perfil/{id}", perfilThiagoId)
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.possuiPreferidaVisivel").value(false))
				.andExpect(jsonPath("$.abertura").isEmpty())
				.andExpect(jsonPath("$.mesmaPreferenciaDoVisitante").value(false));

		// Ranking continua contabilizando a escolha oculta (total=3)
		mockMvc.perform(get("/backend/comunidade/abertura-preferida/ranking").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalEscolhas").value(3));

		// Thiago reexibe
		mockMvc.perform(post("/backend/comunidade/abertura-preferida/reexibir")
						.with(httpBasic("thiago", "thiago")).with(csrf()))
				.andExpect(status().isOk());

		mockMvc.perform(get("/backend/comunidade/abertura-preferida/perfil/{id}", perfilThiagoId)
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.possuiPreferidaVisivel").value(true))
				.andExpect(jsonPath("$.abertura.numero").value(2));
	}
}
