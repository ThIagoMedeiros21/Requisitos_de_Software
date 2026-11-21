package com.cesar.tgchub.tcghub.ui.backend.comunidade;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.IPerfilComunitarioVisibilidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioVisibilidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

@Configuration
public class ComunidadeConfiguracao {
	@Bean
	IPerfilComunitarioVisibilidadeProxy perfilComunitarioVisibilidadeProxy(PerfilComunitarioServico servico,
			IColecaoCartaConsulta colecaoCartaConsulta) {
		return new PerfilComunitarioVisibilidadeProxy(servico, colecaoCartaConsulta);
	}

	@Bean
	ApplicationRunner inicializadorPerfisComunidade(IPerfilComunitarioRepositorio perfilRepositorio) {
		return args -> {
			criarSeAusente(perfilRepositorio, "lucas", "LuffyBR", "Recife",
					"Colecionador de One Piece TCG e jogador de torneios locais.",
					List.of(new CartaIdentificador("OP01-001"), new CartaIdentificador("OP01-016")));
			criarSeAusente(perfilRepositorio, "ricardo", "ZoroTC", "Olinda",
					"Foco em decks agressivos vermelhos e trocas presenciais.",
					List.of(new CartaIdentificador("OP01-025")));
			criarSeAusente(perfilRepositorio, "thiago", "SanjiOP", "Recife",
					"Colecionando coleções clássicas OP-01 a OP-05.", List.of());
			criarSeAusente(perfilRepositorio, "u", "ColecionadorU", "Recife",
					"Perfil padrão de testes do TCGHub.", List.of(new CartaIdentificador("OP01-001")));
		};
	}

	private static void criarSeAusente(IPerfilComunitarioRepositorio repositorio, String usuario, String apelido,
			String cidade, String descricao, List<CartaIdentificador> cartas) {
		var usuarioId = new UsuarioIdentificador(usuario);
		if (!repositorio.existePorUsuario(usuarioId)) {
			repositorio.incluir(new PerfilComunitario(Optional.empty(), usuarioId, new Apelido(apelido),
					Optional.of(cidade), Optional.of(descricao), Optional.empty(),
					Optional.of("Trocas presenciais aos fins de semana"), true, cartas));
		}
	}
}
