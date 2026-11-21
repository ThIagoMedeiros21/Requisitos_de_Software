package com.cesar.tgchub.tcghub.infraestrutura.comunidade.conta;

import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.IUsuarioContaRepositorio;
import com.cesar.tgchub.tcghub.dominio.UsuarioConta;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

public class UsuarioContaRepositorio implements IUsuarioContaRepositorio {
	private final IUsuarioContaRepositorioJpa repositorioJpa;

	public UsuarioContaRepositorio(IUsuarioContaRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	public boolean existePorUsuarioIgnoreCase(String usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.existePorUsuarioIgnoreCase(usuario.trim());
	}

	@Override
	public boolean existePorEmailIgnoreCase(String email) {
		Objects.requireNonNull(email);
		return repositorioJpa.existePorEmailIgnoreCase(email.trim());
	}

	@Override
	public Optional<UsuarioConta> buscarPorUsuarioOuEmailIgnoreCase(String identificador) {
		Objects.requireNonNull(identificador);
		return repositorioJpa.buscarPorUsuarioOuEmailIgnoreCase(identificador.trim())
				.map(UsuarioContaRepositorio::paraDominio);
	}

	@Override
	public UsuarioConta salvar(UsuarioConta conta) {
		Objects.requireNonNull(conta);
		var jpa = new UsuarioContaJpa(conta.getUsuario().toString().toLowerCase(), conta.getEmail().toLowerCase(),
				conta.getSenhaHash(), conta.getAutorizacao().name(), conta.getCriadoEm());
		return paraDominio(repositorioJpa.save(jpa));
	}

	private static UsuarioConta paraDominio(UsuarioContaJpa jpa) {
		return new UsuarioConta(new UsuarioIdentificador(jpa.getUsuario()), jpa.getEmail(), jpa.getSenhaHash(),
				Autorizacao.valueOf(jpa.getAutorizacao()), jpa.getCriadoEm());
	}
}
