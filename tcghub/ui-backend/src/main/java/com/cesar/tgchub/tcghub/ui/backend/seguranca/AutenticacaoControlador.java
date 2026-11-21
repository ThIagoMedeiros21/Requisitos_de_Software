package com.cesar.tgchub.tcghub.ui.backend.seguranca;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.IUsuarioContaRepositorio;
import com.cesar.tgchub.tcghub.dominio.UsuarioConta;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoExistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.ui.Backend;
import dev.sauloaraujo.d5.framework.ui.seguranca.AutenticacaoControladorAbstrato;

@RestController
@RequestMapping(Backend.PREFIXO + "autenticacao")
public class AutenticacaoControlador extends AutenticacaoControladorAbstrato {
	private static final Pattern PADRAO_USUARIO = Pattern.compile("^[a-zA-Z0-9._-]{3,50}$");
	private static final Pattern PADRAO_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	private static final String DESCRICAO_PADRAO = "Colecionador de One Piece TCG no TCGHub.";
	private static final String PREFERENCIAS_PADRAO = "Trocas e negociações na comunidade TCGHub";

	private final PerfilComunitarioServico perfilComunitarioServico;
	private final AmizadeServico amizadeServico;
	private final DeckFavoritoServico deckFavoritoServico;
	private final IUsuarioContaRepositorio contaRepositorio;
	private final IPerfilComunitarioRepositorio perfilRepositorio;
	private final PasswordEncoder codificadorSenha;

	public AutenticacaoControlador(ISegurancaServico segurancaServico,
			PerfilComunitarioServico perfilComunitarioServico, AmizadeServico amizadeServico,
			DeckFavoritoServico deckFavoritoServico, IUsuarioContaRepositorio contaRepositorio,
			IPerfilComunitarioRepositorio perfilRepositorio, PasswordEncoder codificadorSenha) {
		super(segurancaServico);
		this.perfilComunitarioServico = Objects.requireNonNull(perfilComunitarioServico);
		this.amizadeServico = Objects.requireNonNull(amizadeServico);
		this.deckFavoritoServico = Objects.requireNonNull(deckFavoritoServico);
		this.contaRepositorio = Objects.requireNonNull(contaRepositorio);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
		this.codificadorSenha = Objects.requireNonNull(codificadorSenha);
	}

	@Override
	protected AutenticacaoRecurso recurso() {
		var perfilComunitario = perfilComunitarioServico.criarAutorizacoes(Optional.empty());
		var amizade = amizadeServico.criarAutorizacoes(Optional.empty());
		var deckFavorito = deckFavoritoServico.criarAutorizacoes(Optional.empty());
		return new AutenticacaoRecurso(segurancaServico.getUsuario(), perfilComunitario, amizade, deckFavorito);
	}

	@PostMapping("cadastro")
	public CadastroContaRecurso cadastrar(@RequestBody CadastroContaRequisicao requisicao) {
		Objects.requireNonNull(requisicao);

		var usuarioBruto = Objects.requireNonNullElse(requisicao.usuario(), "").trim();
		Validate.notBlank(usuarioBruto, "O nome de usuário é obrigatório");
		Validate.isTrue(PADRAO_USUARIO.matcher(usuarioBruto).matches(),
				"O usuário deve ter entre 3 e 50 caracteres (letras, números, ponto, hífen ou sublinhado)");
		var usuarioNormalizado = usuarioBruto.toLowerCase();

		var apelidoBruto = Objects.requireNonNullElse(requisicao.apelido(), "").trim();
		var apelido = new Apelido(apelidoBruto);

		var emailBruto = Objects.requireNonNullElse(requisicao.email(), "").trim().toLowerCase();
		Validate.notBlank(emailBruto, "O e-mail é obrigatório");
		Validate.isTrue(emailBruto.length() <= UsuarioConta.TAMANHO_EMAIL && PADRAO_EMAIL.matcher(emailBruto).matches(),
				"Informe um endereço de e-mail válido");

		var senhaBruta = Objects.requireNonNullElse(requisicao.senha(), "");
		Validate.isTrue(senhaBruta.trim().length() >= 4, "A senha deve ter pelo menos 4 caracteres");

		var usuarioId = new UsuarioIdentificador(usuarioNormalizado);
		if (contaRepositorio.existePorUsuarioIgnoreCase(usuarioNormalizado)
				|| perfilRepositorio.existePorUsuario(usuarioId)) {
			throw new AgregadoExistenteExcecao("O usuário %s já está cadastrado".formatted(usuarioNormalizado));
		}
		if (contaRepositorio.existePorEmailIgnoreCase(emailBruto)) {
			throw new AgregadoExistenteExcecao("O e-mail %s já está cadastrado".formatted(emailBruto));
		}
		if (perfilRepositorio.existePorApelido(apelido, Optional.empty())) {
			throw new AgregadoExistenteExcecao(PerfilComunitarioServico.APELIDO_EXISTENTE_MODELO.formatted(apelido));
		}

		var hash = Objects.requireNonNull(codificadorSenha.encode(senhaBruta));
		contaRepositorio.salvar(new UsuarioConta(usuarioId, emailBruto, hash, Autorizacao.USUARIO, Instant.now()));

		var descricao = Optional.ofNullable(requisicao.descricao()).map(String::trim).filter(v -> !v.isEmpty())
				.orElse(DESCRICAO_PADRAO);
		var perfilCriado = perfilRepositorio.incluir(new PerfilComunitario(Optional.empty(), usuarioId, apelido,
				Optional.ofNullable(requisicao.cidade()), Optional.of(descricao), Optional.empty(),
				Optional.of(PREFERENCIAS_PADRAO), true, List.of()));

		return new CadastroContaRecurso(usuarioNormalizado, apelido.toString(), emailBruto,
				perfilCriado.getIdentificador().orElseThrow().toString());
	}

	public record CadastroContaRequisicao(@Nullable String usuario, @Nullable String apelido, @Nullable String email,
			@Nullable String senha, @Nullable String cidade, @Nullable String descricao) {
	}

	public record CadastroContaRecurso(String usuario, String apelido, String email, String perfilId) {
	}
}
