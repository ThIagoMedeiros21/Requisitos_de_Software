package com.cesar.tgchub.tcghub.ui.backend.comunidade;

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
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

@SpringBootTest
class ComunidadeControladoresTest {
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private IPerfilComunitarioRepositorio perfilRepositorio;

	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	void autenticacaoRetornaAutorizacoesDePerfilEAmizade() throws Exception {
		mockMvc.perform(get("/backend/autenticacao")).andExpect(status().isUnauthorized());

		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("lucas"))
				.andExpect(jsonPath("$.perfilComunitario.buscar").value(true))
				.andExpect(jsonPath("$.perfilComunitario.alterar").value(true))
				.andExpect(jsonPath("$.amizade.incluir").value(true))
				.andExpect(jsonPath("$.amizade.remover").value(true));

		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("lucas@tcghub.com", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("lucas"));
	}

	@Test
	void cadastroCriaContaRealEPerfilComunitarioAutomaticamenteERejeitaDuplicidades() throws Exception {
		mockMvc.perform(post("/backend/autenticacao/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "usuario": "nami",
								  "apelido": "NamiNavigator",
								  "email": "nami@tcghub.com",
								  "senha": "berri123",
								  "cidade": "Cocoyasi",
								  "descricao": "Navegadora dos Chapéus de Palha."
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("nami"))
				.andExpect(jsonPath("$.apelido").value("NamiNavigator"))
				.andExpect(jsonPath("$.email").value("nami@tcghub.com"))
				.andExpect(jsonPath("$.perfilId").isNotEmpty());

		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("nami", "berri123")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("nami"));

		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("nami@tcghub.com", "berri123")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("nami"));

		mockMvc.perform(get("/backend/comunidade/perfil-comunitario/visualizacao")
						.with(httpBasic("nami", "berri123")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("nami"))
				.andExpect(jsonPath("$.apelido").value("NamiNavigator"))
				.andExpect(jsonPath("$.cidade").value("Cocoyasi"))
				.andExpect(jsonPath("$.ehProprietario").value(true))
				.andExpect(jsonPath("$.cartasColecaoDisponiveis[0].card_set_id").value("OP01-001"))
				.andExpect(jsonPath("$.cartasColecaoDisponiveis[0].card_name").value("Roronoa Zoro"))
				.andExpect(jsonPath("$.cartasColecaoDisponiveis[0].set_name").value("Romance Dawn"))
				.andExpect(jsonPath("$.cartasColecaoDisponiveis[0].card_image")
						.value("https://optcgapi.com/media/static/Card_Images/OP01-001.jpg"));

		mockMvc.perform(post("/backend/autenticacao/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"usuario":"NAMI","apelido":"OutraNami","email":"outra@tcghub.com","senha":"1234"}
								"""))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensagem").isNotEmpty());

		mockMvc.perform(post("/backend/autenticacao/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"usuario":"robin","apelido":"NicoRobin","email":"NAMI@tcghub.com","senha":"1234"}
								"""))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensagem").isNotEmpty());

		mockMvc.perform(post("/backend/autenticacao/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"usuario":"robin","apelido":"luffybr","email":"robin@tcghub.com","senha":"1234"}
								"""))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.mensagem").isNotEmpty());
	}

	@Test
	void perfilComunitarioAplicaProxyDeVisibilidadeEValidacoes() throws Exception {
		var perfilLucasId = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador("lucas")).getIdentificador()
				.orElseThrow().toString();

		mockMvc.perform(get("/backend/comunidade/perfil-comunitario/visualizacao").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("lucas"))
				.andExpect(jsonPath("$.apelido").value("LuffyBR"))
				.andExpect(jsonPath("$.ehProprietario").value(true))
				.andExpect(jsonPath("$.statusRelacao").value("PROPRIO"))
				.andExpect(jsonPath("$.cartasFavoritas[0].card_set_id").value("OP01-001"))
				.andExpect(jsonPath("$.cartasFavoritas[0].card_image")
						.value("https://optcgapi.com/media/static/Card_Images/OP01-001.jpg"))
				.andExpect(jsonPath("$.cartasColecaoDisponiveis").isNotEmpty());

		mockMvc.perform(get("/backend/comunidade/perfil-comunitario/{id}/visualizacao", perfilLucasId)
						.with(httpBasic("ricardo", "ricardo")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.usuario").value("lucas"))
				.andExpect(jsonPath("$.ehProprietario").value(false))
				.andExpect(jsonPath("$.cartasColecaoDisponiveis").isEmpty());

		mockMvc.perform(post("/backend/comunidade/perfil-comunitario/{id}/alterar", perfilLucasId)
						.with(httpBasic("ricardo", "ricardo")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"apelido":"Hackeado","cidade":"Recife","descricao":"x","foto":null,"preferenciasPublicas":null,"decksFavoritosVisiveis":true}
								"""))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/backend/comunidade/perfil-comunitario/{id}/alterar", perfilLucasId)
						.with(httpBasic("lucas", "lucas")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"apelido":"zorotc","cidade":"Recife","descricao":"x","foto":null,"preferenciasPublicas":null,"decksFavoritosVisiveis":true}
								"""))
				.andExpect(status().isConflict());

		mockMvc.perform(post("/backend/comunidade/perfil-comunitario/{id}/fixar-destaque", perfilLucasId)
						.with(httpBasic("lucas", "lucas")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"cartaId":"OP05-119"}
								"""))
				.andExpect(status().isOk());

		mockMvc.perform(post("/backend/comunidade/perfil-comunitario/{id}/remover-destaque", perfilLucasId)
						.with(httpBasic("lucas", "lucas")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"cartaId":"OP05-119"}
								"""))
				.andExpect(status().isOk());
	}

	@Test
	void amizadeExecutaFluxoCompletoComAutorizacaoERemocaoBilateral() throws Exception {
		mockMvc.perform(get("/backend/comunidade/amizade/inclusao?termo=Zoro").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].usuario").value("ricardo"))
				.andExpect(jsonPath("$[0].statusRelacao").value("NENHUMA"));

		mockMvc.perform(post("/backend/comunidade/amizade/incluir").with(httpBasic("lucas", "lucas")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"destinatario":"ricardo"}
								"""))
				.andExpect(status().isOk());

		var pesquisaLucas = mockMvc
				.perform(get("/backend/comunidade/amizade/pesquisa").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.enviadas[0].usuario").value("ricardo"))
				.andReturn().getResponse().getContentAsString();
		var amizadeId = com.fasterxml.jackson.databind.json.JsonMapper.builder().build().readTree(pesquisaLucas)
				.get("enviadas").get(0).get("amizadeId").asText();

		mockMvc.perform(post("/backend/comunidade/amizade/{id}/aceitar", amizadeId)
						.with(httpBasic("thiago", "thiago")).with(csrf()))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/backend/comunidade/amizade/{id}/aceitar", amizadeId)
						.with(httpBasic("ricardo", "ricardo")).with(csrf()))
				.andExpect(status().isOk());

		mockMvc.perform(get("/backend/comunidade/amizade/pesquisa").with(httpBasic("ricardo", "ricardo")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.amigos[0].usuario").value("lucas"));

		mockMvc.perform(post("/backend/comunidade/amizade/{id}/remover", amizadeId)
						.with(httpBasic("lucas", "lucas")).with(csrf()))
				.andExpect(status().isOk());

		mockMvc.perform(get("/backend/comunidade/amizade/pesquisa").with(httpBasic("ricardo", "ricardo")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.amigos").isEmpty());
	}
}
