package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.IDeckFavoritoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoExistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class DeckFavoritoServico
		extends AgregadoServicoAbstrato<DeckFavoritoIdentificador, DeckFavorito, DeckFavoritoAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe deck favorito com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe um deck favorito com o identificador %s";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador do deck favorito é gerado na inclusão e não deve ser informado";
	public static final String LIMITE_DECKS_MODELO = "O limite de %d decks favoritos foi atingido";
	public static final String DECK_DUPLICADO_MODELO = "O deck %s já está nos favoritos";
	public static final String DECK_INATIVO_MODELO = "O deck %s está inativo";
	public static final String DECK_INEXISTENTE_MODELO = "O deck %s não existe";
	public static final String DECK_NAO_FAVORITADO_MODELO = "O deck %s não está nos favoritos de %s";
	public static final String OPERACAO_NAO_AUTORIZADA = "Apenas o proprietário pode gerenciar os decks favoritos deste perfil";

	private final IDeckFavoritoRepositorio repositorio;
	private final IPerfilComunitarioRepositorio perfilRepositorio;
	private final PerfilComunitarioServico perfilServico;
	private final ICatalogoDeckProxy catalogoDeckProxy;

	public DeckFavoritoServico(ISegurancaServico segurancaServico, IDeckFavoritoRepositorio repositorio,
			IPerfilComunitarioRepositorio perfilRepositorio, PerfilComunitarioServico perfilServico,
			ICatalogoDeckProxy catalogoDeckProxy) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
		this.perfilServico = Objects.requireNonNull(perfilServico);
		this.catalogoDeckProxy = Objects.requireNonNull(catalogoDeckProxy);
	}

	@Override
	public DeckFavoritoAutorizacoes criarAutorizacoes(Optional<DeckFavoritoIdentificador> identificador) {
		Objects.requireNonNull(identificador);
		var possuiPapelUsuario = getSegurancaServico().possuiAutorizacao(Autorizacao.USUARIO.name());
		if (identificador.isEmpty() || !repositorio.existePorIdentificador(identificador.get())) {
			return new DeckFavoritoAutorizacoes(possuiPapelUsuario, true, false, possuiPapelUsuario,
					possuiPapelUsuario, possuiPapelUsuario, true);
		}
		var favorito = repositorio.buscarPorIdentificador(identificador.get());
		var ehProprietario = possuiPapelUsuario
				&& getSegurancaServico().getUsuario().equals(favorito.getUsuario().toString());
		return new DeckFavoritoAutorizacoes(possuiPapelUsuario, true, false, ehProprietario, ehProprietario,
				ehProprietario, true);
	}

	@Override
	protected void validarInclusao(DeckFavorito agregado) {
		Objects.requireNonNull(agregado);
		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
		exigirProprietario(new UsuarioIdentificador(getSegurancaServico().getUsuario()), agregado.getUsuario());
	}

	@Override
	protected void validarInclusaoAlteracao(DeckFavorito agregado) {
		Objects.requireNonNull(agregado);
		if (agregado.getIdentificador().isEmpty()) {
			validarRegrasInclusao(agregado.getUsuario(), agregado.getDeck());
		}
	}

	public DeckFavorito adicionar(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			DeckIdentificador deck) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		Objects.requireNonNull(deck);

		exigirProprietario(solicitante, alvo);
		validarAutorizacao(Optional.empty(), DeckFavoritoAutorizacoes::getIncluir);
		validarRegrasInclusao(alvo, deck);

		int proximaOrdem = repositorio.contarPorUsuario(alvo) + 1;
		var novo = new DeckFavorito(Optional.empty(), alvo, deck, proximaOrdem, Instant.now());
		return repositorio.incluir(novo);
	}

	public void remover(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, DeckIdentificador deck) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		Objects.requireNonNull(deck);

		exigirProprietario(solicitante, alvo);
		if (!repositorio.existePorUsuarioEDeck(alvo, deck)) {
			throw new AgregadoInexistenteExcecao(DECK_NAO_FAVORITADO_MODELO.formatted(deck, alvo));
		}

		var existente = repositorio.buscarPorUsuarioEDeck(alvo, deck);
		repositorio.excluir(existente.getIdentificador().orElseThrow());

		var restantes = repositorio.listarPorUsuario(alvo);
		for (int i = 0; i < restantes.size(); i++) {
			var atual = restantes.get(i);
			if (atual.getOrdem() != i + 1) {
				repositorio.alterar(atual.comOrdem(i + 1));
			}
		}
	}

	public void ocultarNoPerfilPublico(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		exigirProprietario(solicitante, alvo);
		perfilServico.alterarVisibilidadeDecksFavoritos(alvo, false);
	}

	public void reexibirNoPerfilPublico(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		exigirProprietario(solicitante, alvo);
		perfilServico.alterarVisibilidadeDecksFavoritos(alvo, true);
	}

	public List<DeckFavorito> listarVisiveisNoPerfil(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);

		var perfil = perfilRepositorio.buscarPorUsuario(alvo);
		if (solicitante.equals(alvo) || perfil.isDecksFavoritosVisiveis()) {
			return repositorio.listarPorUsuario(alvo);
		}
		return List.of();
	}

	public List<ComposicaoDeck> pesquisarDecksDisponiveis(String termo) {
		Objects.requireNonNull(termo);
		return catalogoDeckProxy.pesquisarDecksAtivos(termo);
	}

	public ComposicaoDeck obterComposicaoExpandida(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			DeckIdentificador deck) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		Objects.requireNonNull(deck);

		var perfil = perfilRepositorio.buscarPorUsuario(alvo);
		if (!solicitante.equals(alvo) && !perfil.isDecksFavoritosVisiveis()) {
			throw new NaoAutorizadoExcecao("Os decks favoritos deste perfil estão ocultos");
		}
		return catalogoDeckProxy.obterComposicaoDeck(deck);
	}

	private void validarRegrasInclusao(UsuarioIdentificador usuario, DeckIdentificador deck) {
		if (repositorio.existePorUsuarioEDeck(usuario, deck)) {
			throw new AgregadoExistenteExcecao(DECK_DUPLICADO_MODELO.formatted(deck));
		}
		int total = repositorio.contarPorUsuario(usuario);
		Validate.validState(total < DeckFavorito.MAX_DECKS_FAVORITOS, LIMITE_DECKS_MODELO,
				DeckFavorito.MAX_DECKS_FAVORITOS);
		boolean ativo = catalogoDeckProxy.verificarDeckAtivo(deck);
		Validate.validState(ativo, DECK_INATIVO_MODELO, deck);
	}

	private void exigirProprietario(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		if (!solicitante.equals(alvo) || !getSegurancaServico().getUsuario().equals(solicitante.toString())) {
			throw new NaoAutorizadoExcecao(OPERACAO_NAO_AUTORIZADA);
		}
	}
}
