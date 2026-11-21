package com.cesar.tgchub.tcghub.dominio;

import java.util.Optional;

public interface IUsuarioContaRepositorio {
	boolean existePorUsuarioIgnoreCase(String usuario);

	boolean existePorEmailIgnoreCase(String email);

	Optional<UsuarioConta> buscarPorUsuarioOuEmailIgnoreCase(String identificador);

	UsuarioConta salvar(UsuarioConta conta);
}
