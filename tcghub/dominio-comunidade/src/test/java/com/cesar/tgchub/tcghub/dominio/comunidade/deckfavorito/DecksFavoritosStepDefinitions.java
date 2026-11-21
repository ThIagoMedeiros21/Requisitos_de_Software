package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.ICatalogoDeckConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.IDeckFavoritoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoExistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class DecksFavoritosStepDefinitions {
	private final Map<String, PerfilComunitario> perfisPorId = new HashMap<>();
	private final Map<String, PerfilComunitario> perfisPorUsuario = new HashMap<>();
	private final Map<String, DeckFavorito> favoritosPorId = new HashMap<>();
	private final Map<String, ComposicaoDeck> catalogoDecks = new HashMap<>();

	private final AtomicLong seqPerfil = new AtomicLong(1);
	private final AtomicLong seqFavorito = new AtomicLong(1);

	private final ISegurancaServico segurancaServico = mock(ISegurancaServico.class);
	private final IPerfilComunitarioRepositorio perfilRepositorio = mock(IPerfilComunitarioRepositorio.class);
	private final IColecaoCartaConsulta colecaoCartaConsulta = mock(IColecaoCartaConsulta.class);
	private final IDeckFavoritoRepositorio deckFavoritoRepositorio = mock(IDeckFavoritoRepositorio.class);
	private final ICatalogoDeckConsulta catalogoDeckConsulta = mock(ICatalogoDeckConsulta.class);

	private final PerfilComunitarioServico perfilServico = new PerfilComunitarioServico(segurancaServico,
			perfilRepositorio, colecaoCartaConsulta);
	private final ICatalogoDeckProxy catalogoDeckProxy = new CatalogoDeckProxy(catalogoDeckConsulta);
	private final DeckFavoritoServico deckFavoritoServico = new DeckFavoritoServico(segurancaServico,
			deckFavoritoRepositorio, perfilRepositorio, perfilServico, catalogoDeckProxy);

	private String usuarioAutenticado = "lucas";
	private String ultimoUsuarioAcao = "lucas";
	private boolean catalogoIndisponivel = false;
	private List<ComposicaoDeck> resultadoPesquisa = List.of();
	private List<DeckFavorito> resultadoConsultaPerfil = List.of();
	private ComposicaoDeck composicaoConsultada;
	private RuntimeException excecaoCapturada;

	public DecksFavoritosStepDefinitions() {
		when(segurancaServico.getUsuario()).thenAnswer(chamada -> usuarioAutenticado);
		when(segurancaServico.possuiAutorizacao(Autorizacao.USUARIO.name())).thenReturn(true);

		when(perfilRepositorio.existePorIdentificador(any())).thenAnswer(chamada -> {
			PerfilComunitarioIdentificador id = chamada.getArgument(0);
			return perfisPorId.containsKey(id.toString());
		});

		when(perfilRepositorio.buscarPorIdentificador(any())).thenAnswer(chamada -> {
			PerfilComunitarioIdentificador id = chamada.getArgument(0);
			var perfil = perfisPorId.get(id.toString());
			if (perfil == null) {
				throw new AgregadoInexistenteExcecao(
						PerfilComunitarioServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return perfil;
		});

		when(perfilRepositorio.existePorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return perfisPorUsuario.containsKey(usuario.toString());
		});

		when(perfilRepositorio.buscarPorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			var perfil = perfisPorUsuario.get(usuario.toString());
			if (perfil == null) {
				throw new AgregadoInexistenteExcecao("Não existe perfil comunitário para o usuário %s".formatted(usuario));
			}
			return perfil;
		});

		doAnswer(chamada -> {
			PerfilComunitario perfil = chamada.getArgument(0);
			armazenarPerfil(perfil);
			return null;
		}).when(perfilRepositorio).alterar(any());

		when(deckFavoritoRepositorio.existePorIdentificador(any())).thenAnswer(chamada -> {
			DeckFavoritoIdentificador id = chamada.getArgument(0);
			return favoritosPorId.containsKey(id.toString());
		});

		when(deckFavoritoRepositorio.buscarPorIdentificador(any())).thenAnswer(chamada -> {
			DeckFavoritoIdentificador id = chamada.getArgument(0);
			var fav = favoritosPorId.get(id.toString());
			if (fav == null) {
				throw new AgregadoInexistenteExcecao(
						DeckFavoritoServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return fav;
		});

		when(deckFavoritoRepositorio.contarPorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return (int) favoritosPorId.values().stream().filter(f -> f.getUsuario().equals(usuario)).count();
		});

		when(deckFavoritoRepositorio.existePorUsuarioEDeck(any(), any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			DeckIdentificador deck = chamada.getArgument(1);
			return favoritosPorId.values().stream()
					.anyMatch(f -> f.getUsuario().equals(usuario) && f.getDeck().equals(deck));
		});

		when(deckFavoritoRepositorio.buscarPorUsuarioEDeck(any(), any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			DeckIdentificador deck = chamada.getArgument(1);
			return favoritosPorId.values().stream()
					.filter(f -> f.getUsuario().equals(usuario) && f.getDeck().equals(deck)).findFirst()
					.orElseThrow(() -> new AgregadoInexistenteExcecao(
							DeckFavoritoServico.DECK_NAO_FAVORITADO_MODELO.formatted(deck, usuario)));
		});

		when(deckFavoritoRepositorio.listarPorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return favoritosPorId.values().stream().filter(f -> f.getUsuario().equals(usuario))
					.sorted(Comparator.comparingInt(DeckFavorito::getOrdem)).toList();
		});

		when(deckFavoritoRepositorio.incluir(any())).thenAnswer(chamada -> {
			DeckFavorito fav = chamada.getArgument(0);
			var id = new DeckFavoritoIdentificador(String.valueOf(seqFavorito.getAndIncrement()));
			var salvo = new DeckFavorito(Optional.of(id), fav.getUsuario(), fav.getDeck(), fav.getOrdem(),
					fav.getAdicionadoEm());
			favoritosPorId.put(id.toString(), salvo);
			return salvo;
		});

		doAnswer(chamada -> {
			DeckFavorito fav = chamada.getArgument(0);
			favoritosPorId.put(fav.getIdentificador().orElseThrow().toString(), fav);
			return null;
		}).when(deckFavoritoRepositorio).alterar(any());

		doAnswer(chamada -> {
			DeckFavoritoIdentificador id = chamada.getArgument(0);
			favoritosPorId.remove(id.toString());
			return null;
		}).when(deckFavoritoRepositorio).excluir(any());

		when(catalogoDeckConsulta.pesquisarAtivos(any())).thenAnswer(chamada -> {
			if (catalogoIndisponivel) {
				throw new RuntimeException("Falha de conexão com Catálogo");
			}
			return catalogoDecks.values().stream().filter(ComposicaoDeck::isAtivo).toList();
		});

		when(catalogoDeckConsulta.buscarPorIdentificador(any())).thenAnswer(chamada -> {
			if (catalogoIndisponivel) {
				throw new RuntimeException("Falha de conexão com Catálogo");
			}
			DeckIdentificador deck = chamada.getArgument(0);
			return Optional.ofNullable(catalogoDecks.get(deck.toString()));
		});
	}

	private void armazenarPerfil(PerfilComunitario perfil) {
		var id = perfil.getIdentificador().orElseThrow().toString();
		perfisPorId.put(id, perfil);
		perfisPorUsuario.put(perfil.getUsuario().toString(), perfil);
	}

	private void autenticar(String usuario) {
		this.usuarioAutenticado = usuario;
		this.ultimoUsuarioAcao = usuario;
	}

	private void executar(Runnable acao) {
		excecaoCapturada = null;
		try {
			acao.run();
		} catch (RuntimeException excecao) {
			excecaoCapturada = excecao;
		}
	}

	private void registrarDeck(String deckId, String nome, String lider, boolean ativo) {
		catalogoDecks.put(deckId,
				new ComposicaoDeck(new DeckIdentificador(deckId), nome, lider, "Red", ativo, List.of()));
	}

	private void favoritarDireto(String usuario, String deckId) {
		int ordem = (int) favoritosPorId.values().stream()
				.filter(f -> f.getUsuario().toString().equals(usuario)).count() + 1;
		var id = new DeckFavoritoIdentificador(String.valueOf(seqFavorito.getAndIncrement()));
		favoritosPorId.put(id.toString(), new DeckFavorito(Optional.of(id), new UsuarioIdentificador(usuario),
				new DeckIdentificador(deckId), ordem, Instant.now()));
	}

	private static String extrairCodigoCarta(String rotulo) {
		int abre = rotulo.lastIndexOf('(');
		int fecha = rotulo.lastIndexOf(')');
		if (abre >= 0 && fecha > abre + 1) {
			return rotulo.substring(abre + 1, fecha).trim();
		}
		return rotulo.trim();
	}

	@Dado("que existe o perfil comunitário do usuário {string} com apelido {string}")
	public void que_existe_o_perfil_comunitario_do_usuario_com_apelido(String usuario, String apelido) {
		var id = new PerfilComunitarioIdentificador(String.valueOf(seqPerfil.getAndIncrement()));
		var perfil = new PerfilComunitario(Optional.of(id), new UsuarioIdentificador(usuario), new Apelido(apelido),
				Optional.of("Recife"), Optional.empty(), Optional.empty(), Optional.empty(), true, List.of());
		armazenarPerfil(perfil);
	}

	@Dado("que o usuário {string} não possui decks favoritos cadastrados")
	public void que_o_usuario_nao_possui_decks_favoritos_cadastrados(String usuario) {
		favoritosPorId.values().removeIf(f -> f.getUsuario().toString().equals(usuario));
		catalogoIndisponivel = false;
		excecaoCapturada = null;
	}

	@Dado("que existe o deck ativo {string} de nome {string} com líder {string}")
	public void que_existe_o_deck_ativo_de_nome_com_lider(String deckId, String nome, String lider) {
		registrarDeck(deckId, nome, lider, true);
	}

	@Quando("o usuário {string} adiciona o deck {string} aos seus decks favoritos")
	public void o_usuario_adiciona_o_deck_aos_seus_decks_favoritos(String usuario, String deckId) {
		autenticar(usuario);
		executar(() -> deckFavoritoServico.adicionar(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new DeckIdentificador(deckId)));
	}

	@Entao("o deck {string} é adicionado aos decks favoritos de {string} com sucesso")
	public void o_deck_e_adicionado_aos_decks_favoritos_de_com_sucesso(String deckId, String usuario) {
		assertNull(excecaoCapturada);
		assertTrue(deckFavoritoRepositorio.existePorUsuarioEDeck(new UsuarioIdentificador(usuario),
				new DeckIdentificador(deckId)));
	}

	@Entao("a lista de decks favoritos de {string} contém {int} deck")
	public void a_lista_de_decks_favoritos_de_contem_deck(String usuario, Integer quantidade) {
		assertEquals(quantidade, deckFavoritoRepositorio.contarPorUsuario(new UsuarioIdentificador(usuario)));
	}

	@Quando("o usuário {string} pesquisa decks disponíveis pelo termo {string}")
	public void o_usuario_pesquisa_decks_disponiveis_pelo_termo(String usuario, String termo) {
		autenticar(usuario);
		executar(() -> resultadoPesquisa = deckFavoritoServico.pesquisarDecksDisponiveis(termo));
	}

	@Entao("o resultado da pesquisa de decks contém o deck {string} de nome {string}")
	public void o_resultado_da_pesquisa_de_decks_contem_o_deck_de_nome(String deckId, String nome) {
		assertNull(excecaoCapturada);
		assertTrue(resultadoPesquisa.stream()
				.anyMatch(d -> d.getDeck().toString().equals(deckId) && d.getNomeDeck().equals(nome)));
	}

	@Dado("que existem os decks ativos {string}, {string}, {string} e {string}")
	public void que_existem_os_decks_ativos_e(String d1, String d2, String d3, String d4) {
		registrarDeck(d1, "Deck " + d1, "OP01-001", true);
		registrarDeck(d2, "Deck " + d2, "OP01-002", true);
		registrarDeck(d3, "Deck " + d3, "OP01-003", true);
		registrarDeck(d4, "Deck " + d4, "OP01-004", true);
	}

	@Dado("que o usuário {string} já possui os decks favoritos {string}, {string} e {string}")
	public void que_o_usuario_ja_possui_os_decks_favoritos_e(String usuario, String d1, String d2, String d3) {
		favoritarDireto(usuario, d1);
		favoritarDireto(usuario, d2);
		favoritarDireto(usuario, d3);
	}

	@Entao("a lista de decks favoritos de {string} contém {int} decks")
	public void a_lista_de_decks_favoritos_de_contem_decks(String usuario, Integer quantidade) {
		assertEquals(quantidade, deckFavoritoRepositorio.contarPorUsuario(new UsuarioIdentificador(usuario)));
	}

	@Dado("que existem os decks ativos {string}, {string}, {string}, {string} e {string}")
	public void que_existem_os_decks_ativos_e_2(String d1, String d2, String d3, String d4, String d5) {
		que_existem_os_decks_ativos_e(d1, d2, d3, d4);
		registrarDeck(d5, "Deck " + d5, "OP01-005", true);
	}

	@Dado("que o usuário {string} já possui {int} decks favoritos: {string}, {string}, {string} e {string}")
	public void que_o_usuario_ja_possui_decks_favoritos_e(String usuario, Integer quantidade, String d1, String d2,
			String d3, String d4) {
		assertEquals(4, quantidade);
		favoritarDireto(usuario, d1);
		favoritarDireto(usuario, d2);
		favoritarDireto(usuario, d3);
		favoritarDireto(usuario, d4);
	}

	@Quando("o usuário {string} tenta adicionar o deck {string} aos seus decks favoritos")
	public void o_usuario_tenta_adicionar_o_deck_aos_seus_decks_favoritos(String usuario, String deckId) {
		autenticar(usuario);
		executar(() -> deckFavoritoServico.adicionar(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new DeckIdentificador(deckId)));
	}

	@Entao("a inclusão do deck favorito é rejeitada porque o limite de {int} decks favoritos foi atingido")
	public void a_inclusao_do_deck_favorito_e_rejeitada_porque_o_limite_de_decks_favoritos_foi_atingido(
			Integer limite) {
		assertEquals(DeckFavorito.MAX_DECKS_FAVORITOS, limite);
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(DeckFavoritoServico.LIMITE_DECKS_MODELO.formatted(limite), excecaoCapturada.getMessage());
	}

	@Dado("que o usuário {string} já possui o deck {string} em seus decks favoritos")
	public void que_o_usuario_ja_possui_o_deck_em_seus_decks_favoritos(String usuario, String deckId) {
		favoritarDireto(usuario, deckId);
	}

	@Quando("o usuário {string} tenta adicionar o deck {string} novamente aos seus decks favoritos")
	public void o_usuario_tenta_adicionar_o_deck_novamente_aos_seus_decks_favoritos(String usuario, String deckId) {
		o_usuario_tenta_adicionar_o_deck_aos_seus_decks_favoritos(usuario, deckId);
	}

	@Entao("a inclusão do deck favorito é rejeitada porque o deck {string} já está nos favoritos")
	public void a_inclusao_do_deck_favorito_e_rejeitada_porque_o_deck_ja_esta_nos_favoritos(String deckId) {
		assertInstanceOf(AgregadoExistenteExcecao.class, excecaoCapturada);
		assertEquals(DeckFavoritoServico.DECK_DUPLICADO_MODELO.formatted(deckId), excecaoCapturada.getMessage());
	}

	@Dado("que existe o deck inativo {string} de nome {string} com líder {string}")
	public void que_existe_o_deck_inativo_de_nome_com_lider(String deckId, String nome, String lider) {
		registrarDeck(deckId, nome, lider, false);
	}

	@Entao("a inclusão do deck favorito é rejeitada porque o deck {string} está inativo")
	public void a_inclusao_do_deck_favorito_e_rejeitada_porque_o_deck_esta_inativo(String deckId) {
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(DeckFavoritoServico.DECK_INATIVO_MODELO.formatted(deckId), excecaoCapturada.getMessage());
	}

	@Entao("a inclusão do deck favorito é rejeitada porque o deck {string} não existe")
	public void a_inclusao_do_deck_favorito_e_rejeitada_porque_o_deck_nao_existe(String deckId) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, excecaoCapturada);
		assertEquals(DeckFavoritoServico.DECK_INEXISTENTE_MODELO.formatted(deckId), excecaoCapturada.getMessage());
	}

	@Quando("o usuário {string} remove o deck {string} dos seus decks favoritos")
	public void o_usuario_remove_o_deck_dos_seus_decks_favoritos(String usuario, String deckId) {
		autenticar(usuario);
		executar(() -> deckFavoritoServico.remover(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new DeckIdentificador(deckId)));
	}

	@Quando("adiciona o deck {string} aos seus decks favoritos")
	public void adiciona_o_deck_aos_seus_decks_favoritos(String deckId) {
		o_usuario_adiciona_o_deck_aos_seus_decks_favoritos(ultimoUsuarioAcao, deckId);
	}

	@Entao("a lista de decks favoritos de {string} contém os decks {string}, {string}, {string} e {string}")
	public void a_lista_de_decks_favoritos_de_contem_os_decks_e(String usuario, String d1, String d2, String d3,
			String d4) {
		assertNull(excecaoCapturada);
		var ids = deckFavoritoRepositorio.listarPorUsuario(new UsuarioIdentificador(usuario)).stream()
				.map(f -> f.getDeck().toString()).toList();
		assertEquals(List.of(d1, d2, d3, d4), ids);
	}

	@Quando("o usuário {string} tenta remover o deck {string} dos seus decks favoritos")
	public void o_usuario_tenta_remover_o_deck_dos_seus_decks_favoritos(String usuario, String deckId) {
		o_usuario_remove_o_deck_dos_seus_decks_favoritos(usuario, deckId);
	}

	@Entao("a remoção do deck favorito é rejeitada porque o deck {string} não está nos favoritos de {string}")
	public void a_remocao_do_deck_favorito_e_rejeitada_porque_o_deck_nao_esta_nos_favoritos_de(String deckId,
			String usuario) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, excecaoCapturada);
		assertEquals(DeckFavoritoServico.DECK_NAO_FAVORITADO_MODELO.formatted(deckId, usuario),
				excecaoCapturada.getMessage());
	}

	@Dado("que a interface do Catálogo fornece para o deck {string} o líder {string} e as cartas {string} quantidade {int} preço {string} e {string} quantidade {int} preço {string}")
	public void que_a_interface_do_catalogo_fornece_para_o_deck_o_lider_e_as_cartas_quantidade_preco_e_quantidade_preco(
			String deckId, String lider, String carta1, Integer qtd1, String preco1, String carta2, Integer qtd2,
			String preco2) {
		var existente = catalogoDecks.get(deckId);
		var nomeDeck = existente != null ? existente.getNomeDeck() : "Deck " + deckId;
		var item1 = new ItemComposicaoDeck(new CartaIdentificador(extrairCodigoCarta(carta1)), carta1, qtd1,
				new BigDecimal(preco1));
		var item2 = new ItemComposicaoDeck(new CartaIdentificador(extrairCodigoCarta(carta2)), carta2, qtd2,
				new BigDecimal(preco2));
		catalogoDecks.put(deckId,
				new ComposicaoDeck(new DeckIdentificador(deckId), nomeDeck, lider, "Red", true, List.of(item1, item2)));
	}

	@Quando("o usuário {string} visualiza a composição expandida do deck favorito {string}")
	public void o_usuario_visualiza_a_composicao_expandida_do_deck_favorito(String usuario, String deckId) {
		autenticar(usuario);
		executar(() -> composicaoConsultada = deckFavoritoServico.obterComposicaoExpandida(
				new UsuarioIdentificador(usuario), new UsuarioIdentificador(usuario), new DeckIdentificador(deckId)));
	}

	@Entao("a composição exibida traz os dados consultados via interface do Catálogo com líder {string}, carta {string} quantidade {int} preço {string} e carta {string} quantidade {int} preço {string}")
	public void a_composicao_exibida_traz_os_dados_consultados_via_interface_do_catalogo_com_lider_carta_quantidade_preco_e_carta_quantidade_preco(
			String lider, String carta1, Integer qtd1, String preco1, String carta2, Integer qtd2, String preco2) {
		assertNull(excecaoCapturada);
		assertNotNull(composicaoConsultada);
		assertEquals(lider, composicaoConsultada.getLider());
		assertEquals(2, composicaoConsultada.getItens().size());
		var i1 = composicaoConsultada.getItens().get(0);
		assertEquals(carta1, i1.getNomeCarta());
		assertEquals(qtd1, i1.getQuantidade());
		assertEquals(0, new BigDecimal(preco1).compareTo(i1.getPrecoUnitario()));
		var i2 = composicaoConsultada.getItens().get(1);
		assertEquals(carta2, i2.getNomeCarta());
		assertEquals(qtd2, i2.getQuantidade());
		assertEquals(0, new BigDecimal(preco2).compareTo(i2.getPrecoUnitario()));
	}

	@Dado("que a interface do Catálogo está temporariamente indisponível")
	public void que_a_interface_do_catalogo_esta_temporariamente_indisponivel() {
		catalogoIndisponivel = true;
	}

	@Quando("o usuário {string} tenta visualizar a composição expandida do deck favorito {string}")
	public void o_usuario_tenta_visualizar_a_composicao_expandida_do_deck_favorito(String usuario, String deckId) {
		o_usuario_visualiza_a_composicao_expandida_do_deck_favorito(usuario, deckId);
	}

	@Entao("a consulta da composição do deck favorito é rejeitada porque o serviço de Catálogo está indisponível")
	public void a_consulta_da_composicao_do_deck_favorito_e_rejeitada_porque_o_servico_de_catalogo_esta_indisponivel() {
		assertInstanceOf(CatalogoIndisponivelExcecao.class, excecaoCapturada);
	}

	@Quando("o deck {string} é marcado como inativo")
	public void o_deck_e_marcado_como_inativo(String deckId) {
		var atual = catalogoDecks.get(deckId);
		assertNotNull(atual);
		catalogoDecks.put(deckId, atual.comStatusAtivo(false));
	}

	@Entao("a inclusão do deck favorito para {string} é rejeitada porque o deck {string} está inativo")
	public void a_inclusao_do_deck_favorito_para_e_rejeitada_porque_o_deck_esta_inativo(String usuario, String deckId) {
		assertNotNull(usuario);
		a_inclusao_do_deck_favorito_e_rejeitada_porque_o_deck_esta_inativo(deckId);
	}

	@Quando("o usuário {string} oculta seus decks favoritos do perfil público")
	public void o_usuario_oculta_seus_decks_favoritos_do_perfil_publico(String usuario) {
		autenticar(usuario);
		executar(() -> deckFavoritoServico.ocultarNoPerfilPublico(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario)));
	}

	@Quando("o usuário {string} consulta os decks favoritos no perfil público de {string}")
	public void o_usuario_consulta_os_decks_favoritos_no_perfil_publico_de(String solicitante, String alvo) {
		autenticar(solicitante);
		executar(() -> resultadoConsultaPerfil = deckFavoritoServico
				.listarVisiveisNoPerfil(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(alvo)));
	}

	@Entao("nenhum deck favorito é exibido para o visitante {string}")
	public void nenhum_deck_favorito_e_exibido_para_o_visitante(String visitante) {
		assertNull(excecaoCapturada);
		assertNotNull(visitante);
		assertTrue(resultadoConsultaPerfil.isEmpty());
	}

	@Quando("o usuário {string} consulta os próprios decks favoritos")
	public void o_usuario_consulta_os_proprios_decks_favoritos(String usuario) {
		o_usuario_consulta_os_decks_favoritos_no_perfil_publico_de(usuario, usuario);
	}

	@Entao("o usuário {string} continua visualizando o deck favorito {string}")
	public void o_usuario_continua_visualizando_o_deck_favorito(String usuario, String deckId) {
		assertNull(excecaoCapturada);
		assertNotNull(usuario);
		assertTrue(resultadoConsultaPerfil.stream().anyMatch(f -> f.getDeck().toString().equals(deckId)));
	}

	@Dado("que o usuário {string} já possui o deck {string} em seus decks favoritos com exibição oculta no perfil público")
	public void que_o_usuario_ja_possui_o_deck_em_seus_decks_favoritos_com_exibicao_oculta_no_perfil_publico(
			String usuario, String deckId) {
		favoritarDireto(usuario, deckId);
		var perfil = perfisPorUsuario.get(usuario);
		assertNotNull(perfil);
		armazenarPerfil(perfil.alterarVisibilidadeDecksFavoritos(false));
	}

	@Quando("o usuário {string} reexibe seus decks favoritos no perfil público")
	public void o_usuario_reexibe_seus_decks_favoritos_no_perfil_publico(String usuario) {
		autenticar(usuario);
		executar(() -> deckFavoritoServico.reexibirNoPerfilPublico(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario)));
	}

	@Entao("o visitante {string} visualiza o deck favorito {string} no perfil de {string}")
	public void o_visitante_visualiza_o_deck_favorito_no_perfil_de(String visitante, String deckId, String dono) {
		assertNull(excecaoCapturada);
		assertNotNull(visitante);
		assertNotNull(dono);
		assertTrue(resultadoConsultaPerfil.stream().anyMatch(f -> f.getDeck().toString().equals(deckId)));
	}

	@Quando("o usuário {string} tenta adicionar o deck {string} aos decks favoritos do perfil de {string}")
	public void o_usuario_tenta_adicionar_o_deck_aos_decks_favoritos_do_perfil_de(String solicitante, String deckId,
			String alvo) {
		autenticar(solicitante);
		executar(() -> deckFavoritoServico.adicionar(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(alvo), new DeckIdentificador(deckId)));
	}

	@Entao("a operação em decks favoritos é rejeitada por falta de autorização")
	public void a_operacao_em_decks_favoritos_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, excecaoCapturada);
	}
}
