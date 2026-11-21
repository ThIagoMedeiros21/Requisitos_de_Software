package com.cesar.tgchub.tcghub.ui.backend.comunidade.deckfavorito;

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
class DeckFavoritoControladorTest {
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
	void autenticacaoExpoeAutorizacoesDeDeckFavorito() throws Exception {
		mockMvc.perform(get("/backend/autenticacao").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.deckFavorito.incluir").value(true))
				.andExpect(jsonPath("$.deckFavorito.buscar").value(true))
				.andExpect(jsonPath("$.deckFavorito.excluir").value(true))
				.andExpect(jsonPath("$.deckFavorito.ocultar").value(true))
				.andExpect(jsonPath("$.deckFavorito.reexibir").value(true))
				.andExpect(jsonPath("$.deckFavorito.visualizarComposicao").value(true));
	}

	@Test
	void listaCatalogoEComposicaoBandaiDosDecksFavoritos() throws Exception {
		mockMvc.perform(get("/backend/comunidade/deck-favorito/pesquisa").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.total").value(2))
				.andExpect(jsonPath("$.limite").value(4))
				.andExpect(jsonPath("$.decksFavoritosVisiveis").value(true))
				.andExpect(jsonPath("$.ehProprietario").value(true))
				.andExpect(jsonPath("$.decks[0].deckId").value("ST01"))
				.andExpect(jsonPath("$.decks[0].liderCodigo").value("ST01-001"))
				.andExpect(jsonPath("$.decks[0].totalCartasMainDeck").value(50))
				.andExpect(jsonPath("$.decks[1].deckId").value("OP01-ZORO"));

		mockMvc.perform(get("/backend/comunidade/deck-favorito/catalogo?cor=Purple").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].jaFavoritado").value(false));

		mockMvc.perform(get("/backend/comunidade/deck-favorito/ST01/composicao").with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.deckId").value("ST01"))
				.andExpect(jsonPath("$.liderCodigo").value("ST01-001"))
				.andExpect(jsonPath("$.liderImagem").value("https://optcgapi.com/media/static/Card_Images/ST01-001.jpg"))
				.andExpect(jsonPath("$.totalCartasMainDeck").value(50))
				.andExpect(jsonPath("$.totalCartasComLider").value(51))
				.andExpect(jsonPath("$.conformidadeBandai").value(true))
				.andExpect(jsonPath("$.totaisPorTipo.Leader").value(1))
				.andExpect(jsonPath("$.totaisPorTipo.Character").value(40))
				.andExpect(jsonPath("$.totaisPorTipo.Event").value(8))
				.andExpect(jsonPath("$.totaisPorTipo.Stage").value(2))
				.andExpect(jsonPath("$.decklistBandai").isNotEmpty())
				.andExpect(jsonPath("$.itens[0].card_set_id").value("ST01-001"))
				.andExpect(jsonPath("$.itens[0].card_image")
						.value("https://optcgapi.com/media/static/Card_Images/ST01-001.jpg"));
	}

	@Test
	void aplicaRegrasDeLimiteDuplicidadeInativoEVisibilidadeNoPerfilPublico() throws Exception {
		var perfilRicardoId = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador("ricardo"))
				.getIdentificador().orElseThrow().toString();

		// Adicionar 4 decks para ricardo
		for (var deckId : new String[] { "ST01", "ST10", "ST04", "OP01-ZORO" }) {
			mockMvc.perform(post("/backend/comunidade/deck-favorito/incluir")
							.with(httpBasic("ricardo", "ricardo")).with(csrf())
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"deckId\":\"" + deckId + "\"}"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.deckId").value(deckId));
		}

		// Duplicado -> 409
		mockMvc.perform(post("/backend/comunidade/deck-favorito/incluir")
						.with(httpBasic("ricardo", "ricardo")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"deckId\":\"ST01\"}"))
				.andExpect(status().isConflict());

		// 5o deck excede limite -> 422 / 400 (IllegalStateException)
		mockMvc.perform(post("/backend/comunidade/deck-favorito/incluir")
						.with(httpBasic("ricardo", "ricardo")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"deckId\":\"OP01-CROC\"}"))
				.andExpect(status().is4xxClientError());

		// Remover um deck para testar inativo e inexistente
		mockMvc.perform(post("/backend/comunidade/deck-favorito/OP01-ZORO/excluir")
						.with(httpBasic("ricardo", "ricardo")).with(csrf()))
				.andExpect(status().isOk());

		// Deck inativo -> 4xx
		mockMvc.perform(post("/backend/comunidade/deck-favorito/incluir")
						.with(httpBasic("ricardo", "ricardo")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"deckId\":\"ST09-INATIVO\"}"))
				.andExpect(status().is4xxClientError());

		// Deck inexistente -> 404
		mockMvc.perform(post("/backend/comunidade/deck-favorito/incluir")
						.with(httpBasic("ricardo", "ricardo")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"deckId\":\"DECK-NAO-EXISTE\"}"))
				.andExpect(status().isNotFound());

		// Ocultar no perfil público
		mockMvc.perform(post("/backend/comunidade/deck-favorito/ocultar")
						.with(httpBasic("ricardo", "ricardo")).with(csrf()))
				.andExpect(status().isOk());

		// Visitante (lucas) vê lista vazia
		mockMvc.perform(get("/backend/comunidade/deck-favorito/perfil/{id}", perfilRicardoId)
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.decksFavoritosVisiveis").value(false))
				.andExpect(jsonPath("$.ehProprietario").value(false))
				.andExpect(jsonPath("$.total").value(0))
				.andExpect(jsonPath("$.decks").isEmpty());

		// Visitante tentando abrir composição do perfil oculto -> 403
		mockMvc.perform(get("/backend/comunidade/deck-favorito/ST01/composicao?perfilId={id}", perfilRicardoId)
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isForbidden());

		// Proprietário (ricardo) continua vendo seus decks
		mockMvc.perform(get("/backend/comunidade/deck-favorito/pesquisa").with(httpBasic("ricardo", "ricardo")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.decksFavoritosVisiveis").value(false))
				.andExpect(jsonPath("$.ehProprietario").value(true))
				.andExpect(jsonPath("$.total").value(3));

		// Reexibir no perfil público
		mockMvc.perform(post("/backend/comunidade/deck-favorito/reexibir")
						.with(httpBasic("ricardo", "ricardo")).with(csrf()))
				.andExpect(status().isOk());

		// Visitante volta a ver os decks
		mockMvc.perform(get("/backend/comunidade/deck-favorito/perfil/{id}", perfilRicardoId)
						.with(httpBasic("lucas", "lucas")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.decksFavoritosVisiveis").value(true))
				.andExpect(jsonPath("$.total").value(3));
	}
}
