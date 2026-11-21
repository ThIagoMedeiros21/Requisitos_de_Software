package com.cesar.tgchub.tcghub.ui.backend.comunidade;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.IUsuarioContaRepositorio;
import com.cesar.tgchub.tcghub.dominio.UsuarioConta;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.CatalogoDeckProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ICatalogoDeckProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.ICatalogoDeckConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.IDeckFavoritoRepositorio;
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
	ICatalogoDeckProxy catalogoDeckProxy(ICatalogoDeckConsulta catalogoDeckConsulta) {
		return new CatalogoDeckProxy(catalogoDeckConsulta);
	}

	@Bean
	ApplicationRunner inicializadorPerfisComunidade(IUsuarioContaRepositorio contaRepositorio,
			IPerfilComunitarioRepositorio perfilRepositorio, IDeckFavoritoRepositorio deckFavoritoRepositorio,
			PasswordEncoder codificadorSenha) {
		return args -> {
			criarContaEPerfilSeAusente(contaRepositorio, perfilRepositorio, codificadorSenha, "lucas",
					"lucas@tcghub.com", "lucas", Autorizacao.USUARIO, "LuffyBR", "Recife",
					"Colecionador de One Piece TCG e jogador de torneios locais.",
					List.of(new CartaIdentificador("OP01-001"), new CartaIdentificador("OP01-016")));
			criarDecksFavoritosIniciaisSeAusentes(deckFavoritoRepositorio, "lucas",
					List.of("ST01", "OP01-ZORO"));
			criarContaEPerfilSeAusente(contaRepositorio, perfilRepositorio, codificadorSenha, "ricardo",
					"ricardo@tcghub.com", "ricardo", Autorizacao.USUARIO, "ZoroTC", "Olinda",
					"Foco em decks agressivos vermelhos e trocas presenciais.",
					List.of(new CartaIdentificador("OP01-025")));
			criarContaEPerfilSeAusente(contaRepositorio, perfilRepositorio, codificadorSenha, "thiago",
					"thiago@tcghub.com", "thiago", Autorizacao.USUARIO, "SanjiOP", "Recife",
					"Colecionando coleções clássicas OP-01 a OP-05.", List.of());
			criarContaEPerfilSeAusente(contaRepositorio, perfilRepositorio, codificadorSenha, "u", "u@tcghub.com", "u",
					Autorizacao.USUARIO, "ColecionadorU", "Recife", "Perfil padrão de testes do TCGHub.",
					List.of(new CartaIdentificador("OP01-001")));
			criarContaEPerfilSeAusente(contaRepositorio, perfilRepositorio, codificadorSenha, "a", "admin@tcghub.com",
					"a", Autorizacao.ADMINISTRADOR, "AdminTCGHub", "Recife",
					"Conta administrativa oficial do TCGHub.", List.of());
		};
	}

	private static void criarDecksFavoritosIniciaisSeAusentes(IDeckFavoritoRepositorio deckFavoritoRepositorio,
			String usuario, List<String> decks) {
		var usuarioId = new UsuarioIdentificador(usuario);
		if (deckFavoritoRepositorio.contarPorUsuario(usuarioId) == 0) {
			for (int i = 0; i < decks.size(); i++) {
				deckFavoritoRepositorio.incluir(new DeckFavorito(Optional.empty(), usuarioId,
						new DeckIdentificador(decks.get(i)), i + 1, Instant.now()));
			}
		}
	}

	private static void criarContaEPerfilSeAusente(IUsuarioContaRepositorio contaRepositorio,
			IPerfilComunitarioRepositorio perfilRepositorio, PasswordEncoder codificadorSenha, String usuario,
			String email, String senha, Autorizacao autorizacao, String apelido, String cidade, String descricao,
			List<CartaIdentificador> cartas) {
		var usuarioId = new UsuarioIdentificador(usuario);
		if (!contaRepositorio.existePorUsuarioIgnoreCase(usuario)) {
			var hash = Objects.requireNonNull(codificadorSenha.encode(senha));
			contaRepositorio.salvar(new UsuarioConta(usuarioId, email, hash, autorizacao, Instant.now()));
		}
		if (!perfilRepositorio.existePorUsuario(usuarioId)) {
			perfilRepositorio.incluir(new PerfilComunitario(Optional.empty(), usuarioId, new Apelido(apelido),
					Optional.of(cidade), Optional.of(descricao), Optional.empty(),
					Optional.of("Trocas presenciais aos fins de semana"), true, cartas));
		}
	}
}
