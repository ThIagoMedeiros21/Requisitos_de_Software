package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class PerfilComunitario implements IAgregado<PerfilComunitarioIdentificador> {
	public static final int MAX_CARTAS_FAVORITAS = 4;
	public static final int TAMANHO_CIDADE = 100;
	public static final int TAMANHO_DESCRICAO = 500;
	public static final int TAMANHO_FOTO = 255;
	public static final int TAMANHO_PREFERENCIAS_PUBLICAS = 255;

	public static final String LIMITE_DESTAQUES_ATINGIDO = "O limite de 4 destaques foi atingido";
	public static final String CARTA_DUPLICADA_DESTAQUE_MODELO = "A carta %s já está nos destaques";
	public static final String CARTA_DESTAQUE_INEXISTENTE_MODELO = "A carta %s não está nos destaques";

	private final Optional<PerfilComunitarioIdentificador> identificador;
	private final UsuarioIdentificador usuario;
	private final Apelido apelido;
	private final Optional<String> cidade;
	private final Optional<String> descricao;
	private final Optional<String> foto;
	private final Optional<String> preferenciasPublicas;
	private final boolean decksFavoritosVisiveis;
	private final List<CartaIdentificador> cartasFavoritas;

	public PerfilComunitario(Optional<PerfilComunitarioIdentificador> identificador, UsuarioIdentificador usuario,
			Apelido apelido, Optional<String> cidade, Optional<String> descricao, Optional<String> foto,
			Optional<String> preferenciasPublicas, boolean decksFavoritosVisiveis,
			List<CartaIdentificador> cartasFavoritas) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.apelido = Objects.requireNonNull(apelido);
		this.cidade = validarTextoOpcional(cidade, TAMANHO_CIDADE, "cidade");
		this.descricao = validarTextoOpcional(descricao, TAMANHO_DESCRICAO, "descrição");
		this.foto = validarTextoOpcional(foto, TAMANHO_FOTO, "foto");
		this.preferenciasPublicas = validarTextoOpcional(preferenciasPublicas, TAMANHO_PREFERENCIAS_PUBLICAS,
				"preferências públicas");
		this.decksFavoritosVisiveis = decksFavoritosVisiveis;

		Objects.requireNonNull(cartasFavoritas);
		this.cartasFavoritas = List.copyOf(cartasFavoritas);
		Validate.isTrue(this.cartasFavoritas.size() <= MAX_CARTAS_FAVORITAS, LIMITE_DESTAQUES_ATINGIDO);
		Validate.isTrue(Set.copyOf(this.cartasFavoritas).size() == this.cartasFavoritas.size(),
				"Não é permitido repetir cartas nos destaques");
	}

	private static Optional<String> validarTextoOpcional(Optional<String> texto, int tamanhoMaximo, String campo) {
		Objects.requireNonNull(texto);
		return texto.map(String::trim).filter(valor -> !valor.isEmpty()).map(valor -> {
			Validate.isTrue(valor.length() <= tamanhoMaximo, "O campo %s deve ter no máximo %d caracteres", campo,
					tamanhoMaximo);
			return valor;
		});
	}

	@Override
	public Optional<PerfilComunitarioIdentificador> getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public Apelido getApelido() {
		return apelido;
	}

	public Optional<String> getCidade() {
		return cidade;
	}

	public Optional<String> getDescricao() {
		return descricao;
	}

	public Optional<String> getFoto() {
		return foto;
	}

	public Optional<String> getPreferenciasPublicas() {
		return preferenciasPublicas;
	}

	public boolean isDecksFavoritosVisiveis() {
		return decksFavoritosVisiveis;
	}

	public List<CartaIdentificador> getCartasFavoritas() {
		return cartasFavoritas;
	}

	public PerfilComunitario editarDados(Apelido novoApelido, Optional<String> novaCidade, Optional<String> novaDescricao,
			Optional<String> novaFoto, Optional<String> novasPreferenciasPublicas, boolean novosDecksFavoritosVisiveis) {
		return new PerfilComunitario(identificador, usuario, novoApelido, novaCidade, novaDescricao, novaFoto,
				novasPreferenciasPublicas, novosDecksFavoritosVisiveis, cartasFavoritas);
	}

	public PerfilComunitario fixarCartaFavorita(CartaIdentificador carta) {
		Objects.requireNonNull(carta);
		Validate.validState(!cartasFavoritas.contains(carta), CARTA_DUPLICADA_DESTAQUE_MODELO, carta);
		Validate.validState(cartasFavoritas.size() < MAX_CARTAS_FAVORITAS, LIMITE_DESTAQUES_ATINGIDO);

		var novasCartas = new ArrayList<CartaIdentificador>(cartasFavoritas);
		novasCartas.add(carta);
		return new PerfilComunitario(identificador, usuario, apelido, cidade, descricao, foto, preferenciasPublicas,
				decksFavoritosVisiveis, novasCartas);
	}

	public PerfilComunitario removerCartaFavorita(CartaIdentificador carta) {
		Objects.requireNonNull(carta);
		if (!cartasFavoritas.contains(carta)) {
			throw new AgregadoInexistenteExcecao(CARTA_DESTAQUE_INEXISTENTE_MODELO.formatted(carta));
		}

		var novasCartas = new ArrayList<CartaIdentificador>(cartasFavoritas);
		novasCartas.remove(carta);
		return new PerfilComunitario(identificador, usuario, apelido, cidade, descricao, foto, preferenciasPublicas,
				decksFavoritosVisiveis, novasCartas);
	}

	public PerfilComunitario alterarVisibilidadeDecksFavoritos(boolean visivel) {
		return new PerfilComunitario(identificador, usuario, apelido, cidade, descricao, foto, preferenciasPublicas,
				visivel, cartasFavoritas);
	}
}
