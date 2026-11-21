package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.util.List;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;

public interface IAberturaPreferidaPrivacidadeProxy {
	AberturaPreferidaPerfilVisao consultarAberturaNoPerfil(UsuarioIdentificador solicitante, UsuarioIdentificador alvo);

	boolean amigosPossuemMesmaPreferencia(UsuarioIdentificador solicitante, UsuarioIdentificador amigo);

	List<UsuarioIdentificador> listarAmigosComAbertura(UsuarioIdentificador usuario, AberturaIdentificador abertura);

	int contarAmigosComAbertura(UsuarioIdentificador usuario, AberturaIdentificador abertura);

	List<AberturaParaEscolhaVisao> listarAberturasAtivasParaEscolha(UsuarioIdentificador usuario,
			Optional<String> termo);

	AberturaPreferidaDetalhesVisao consultarDetalhes(UsuarioIdentificador solicitante,
			AberturaIdentificador aberturaId);

	AberturaPreferida escolher(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			AberturaIdentificador aberturaId);

	AberturaPreferida escolherPorNumero(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, int numero);

	AberturaPreferida alterarVisibilidade(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, boolean visivel);
}
