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
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaPrivacidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.IAberturaPreferidaPrivacidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi.IAberturaPreferidaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;
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
	IAberturaPreferidaPrivacidadeProxy aberturaPreferidaPrivacidadeProxy(AberturaPreferidaServico servico,
			IAberturaPreferidaRepositorio preferidaRepositorio, IAberturaRepositorio aberturaRepositorio,
			IAmizadeRepositorio amizadeRepositorio) {
		return new AberturaPreferidaPrivacidadeProxy(servico, preferidaRepositorio, aberturaRepositorio,
				amizadeRepositorio);
	}

	@Bean
	ApplicationRunner inicializadorPerfisComunidade(IUsuarioContaRepositorio contaRepositorio,
			IPerfilComunitarioRepositorio perfilRepositorio, IDeckFavoritoRepositorio deckFavoritoRepositorio,
			IAberturaRepositorio aberturaRepositorio, IAberturaPreferidaRepositorio aberturaPreferidaRepositorio,
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

			criarAberturasOficiaisSeAusentes(aberturaRepositorio);
			criarAberturaPreferidaSeAusente(aberturaRepositorio, aberturaPreferidaRepositorio, "lucas", 1);
			criarAberturaPreferidaSeAusente(aberturaRepositorio, aberturaPreferidaRepositorio, "ricardo", 1);
			criarAberturaPreferidaSeAusente(aberturaRepositorio, aberturaPreferidaRepositorio, "thiago", 22);
		};
	}

	private static void criarAberturasOficiaisSeAusentes(IAberturaRepositorio aberturaRepositorio) {
		var capaPadrao = "https://pub-92474f7785774e91a790e086dfa6b2ef.r2.dev/anime/large-cover/OnePiece.jpg";
		List<Abertura> iniciais = List.of(
				new Abertura(Optional.empty(), 1, "We Are!", "Hiroshi Kitadani", "1 a 47", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP1.webm", true),
				new Abertura(Optional.empty(), 2, "Believe", "Folder5", "48 a 115", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP2.webm", true),
				new Abertura(Optional.empty(), 3, "Hikari e", "The Babystars", "116 a 168", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP3.webm", true),
				new Abertura(Optional.empty(), 6, "Brand New World", "D-51", "264 a 278", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP6.webm", true),
				new Abertura(Optional.empty(), 15, "We Go!", "Hiroshi Kitadani", "517 a 590", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP15.webm", true),
				new Abertura(Optional.empty(), 20, "Hope", "Namie Amuro", "807 a 855", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP20.webm", true),
				new Abertura(Optional.empty(), 22, "OVER THE TOP", "Hiroshi Kitadani", "892 a 934", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP22.webm", true),
				new Abertura(Optional.empty(), 26, "UUUUUS!", "Hiroshi Kitadani", "1089 a 1122", capaPadrao,
						"https://v.animethemes.moe/OnePiece-OP26.webm", true));
		for (var ab : iniciais) {
			if (!aberturaRepositorio.existePorNumero(ab.getNumeroValor())) {
				aberturaRepositorio.incluir(ab);
			}
		}
	}

	private static void criarAberturaPreferidaSeAusente(IAberturaRepositorio aberturaRepositorio,
			IAberturaPreferidaRepositorio preferidaRepositorio, String usuario, int numeroAbertura) {
		var usuarioId = new UsuarioIdentificador(usuario);
		if (!preferidaRepositorio.existePorUsuario(usuarioId) && aberturaRepositorio.existePorNumero(numeroAbertura)) {
			var ab = aberturaRepositorio.buscarPorNumero(numeroAbertura);
			preferidaRepositorio.incluir(new AberturaPreferida(Optional.empty(), usuarioId,
					ab.getIdentificador().orElseThrow(), true, Instant.now()));
		}
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
