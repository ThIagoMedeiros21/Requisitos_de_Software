package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class PerfilComunitarioVisao implements IObjetoValor {
	private final PerfilComunitarioIdentificador identificador;
	private final UsuarioIdentificador usuario;
	private final Apelido apelido;
	private final Optional<String> cidade;
	private final Optional<String> descricao;
	private final Optional<String> foto;
	private final Optional<String> preferenciasPublicas;
	private final boolean decksFavoritosVisiveis;
	private final List<CartaIdentificador> cartasFavoritas;
	private final boolean ehProprietario;
	private final List<CartaColecaoResumo> cartasColecaoDisponiveis;

	public PerfilComunitarioVisao(PerfilComunitarioIdentificador identificador, UsuarioIdentificador usuario,
			Apelido apelido, Optional<String> cidade, Optional<String> descricao, Optional<String> foto,
			Optional<String> preferenciasPublicas, boolean decksFavoritosVisiveis,
			List<CartaIdentificador> cartasFavoritas, boolean ehProprietario,
			List<CartaColecaoResumo> cartasColecaoDisponiveis) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.apelido = Objects.requireNonNull(apelido);
		this.cidade = Objects.requireNonNull(cidade);
		this.descricao = Objects.requireNonNull(descricao);
		this.foto = Objects.requireNonNull(foto);
		this.preferenciasPublicas = Objects.requireNonNull(preferenciasPublicas);
		this.decksFavoritosVisiveis = decksFavoritosVisiveis;
		this.cartasFavoritas = List.copyOf(Objects.requireNonNull(cartasFavoritas));
		this.ehProprietario = ehProprietario;
		this.cartasColecaoDisponiveis = List.copyOf(Objects.requireNonNull(cartasColecaoDisponiveis));
	}

	public PerfilComunitarioIdentificador getIdentificador() {
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

	public boolean isEhProprietario() {
		return ehProprietario;
	}

	public List<CartaColecaoResumo> getCartasColecaoDisponiveis() {
		return cartasColecaoDisponiveis;
	}
}
