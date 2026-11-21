package com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class PerfilComunitarioProjecao implements IProjecao<PerfilComunitario> {
	private final PerfilComunitarioIdentificador identificador;
	private final UsuarioIdentificador usuario;
	private final Apelido apelido;
	private final Optional<String> cidade;
	private final Optional<String> foto;
	private final boolean decksFavoritosVisiveis;

	public PerfilComunitarioProjecao(PerfilComunitarioIdentificador identificador, UsuarioIdentificador usuario,
			Apelido apelido, Optional<String> cidade, Optional<String> foto, boolean decksFavoritosVisiveis) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.apelido = Objects.requireNonNull(apelido);
		this.cidade = Objects.requireNonNull(cidade);
		this.foto = Objects.requireNonNull(foto);
		this.decksFavoritosVisiveis = decksFavoritosVisiveis;
	}

	public PerfilComunitarioProjecao(Long identificador, String usuario, String apelido, @Nullable String cidade,
			@Nullable String foto, boolean decksFavoritosVisiveis) {
		this(new PerfilComunitarioIdentificador(identificador.toString()), new UsuarioIdentificador(usuario),
				new Apelido(apelido), Optional.ofNullable(cidade), Optional.ofNullable(foto), decksFavoritosVisiveis);
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

	public Optional<String> getFoto() {
		return foto;
	}

	public boolean isDecksFavoritosVisiveis() {
		return decksFavoritosVisiveis;
	}
}
