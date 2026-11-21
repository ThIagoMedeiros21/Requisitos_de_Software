package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoExistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class PerfilEAmigosStepDefinitions {
	private final Map<String, PerfilComunitario> perfisPorId = new HashMap<>();
	private final Map<String, PerfilComunitario> perfisPorUsuario = new HashMap<>();
	private final Map<String, Amizade> amizadesPorId = new HashMap<>();
	private final Map<String, Set<String>> cartasPorUsuario = new HashMap<>();

	private final AtomicLong sequenciaPerfil = new AtomicLong(1);
	private final AtomicLong sequenciaAmizade = new AtomicLong(1);

	private final ISegurancaServico segurancaServico = mock(ISegurancaServico.class);
	private final IPerfilComunitarioRepositorio perfilRepositorio = mock(IPerfilComunitarioRepositorio.class);
	private final IAmizadeRepositorio amizadeRepositorio = mock(IAmizadeRepositorio.class);
	private final IColecaoCartaConsulta colecaoCartaConsulta = mock(IColecaoCartaConsulta.class);

	private final PerfilComunitarioServico perfilServico = new PerfilComunitarioServico(segurancaServico,
			perfilRepositorio, colecaoCartaConsulta);
	private final IPerfilComunitarioVisibilidadeProxy perfilProxy = new PerfilComunitarioVisibilidadeProxy(
			perfilServico, colecaoCartaConsulta);
	private final AmizadeServico amizadeServico = new AmizadeServico(segurancaServico, amizadeRepositorio,
			perfilRepositorio);

	private String usuarioAutenticado = "lucas";
	private String ultimoUsuarioAcao = "lucas";
	private PerfilComunitarioVisao visaoConsultada;
	private RuntimeException excecaoCapturada;

	public PerfilEAmigosStepDefinitions() {
		when(segurancaServico.getUsuario()).thenAnswer(chamada -> usuarioAutenticado);
		when(segurancaServico.possuiAutorizacao(Autorizacao.USUARIO.name())).thenReturn(true);

		when(perfilRepositorio.existePorIdentificador(any())).thenAnswer(chamada -> {
			PerfilComunitarioIdentificador id = chamada.getArgument(0);
			return perfisPorId.containsKey(id.toString());
		});

		when(perfilRepositorio.buscarPorIdentificador(any())).thenAnswer(chamada -> {
			PerfilComunitarioIdentificador id = chamada.getArgument(0);
			var perfil = perfisPorId.get(id.toString());
			if (perfil == null) {
				throw new AgregadoInexistenteExcecao(
						PerfilComunitarioServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return perfil;
		});

		when(perfilRepositorio.existePorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return perfisPorUsuario.containsKey(usuario.toString());
		});

		when(perfilRepositorio.buscarPorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			var perfil = perfisPorUsuario.get(usuario.toString());
			if (perfil == null) {
				throw new AgregadoInexistenteExcecao("Não existe perfil comunitário para o usuário %s".formatted(usuario));
			}
			return perfil;
		});

		when(perfilRepositorio.existePorApelido(any(), any())).thenAnswer(chamada -> {
			Apelido apelido = chamada.getArgument(0);
			Optional<PerfilComunitarioIdentificador> ignorado = chamada.getArgument(1);
			return perfisPorId.values().stream()
					.anyMatch(candidato -> candidato.getApelido().toString().equalsIgnoreCase(apelido.toString())
							&& (ignorado.isEmpty() || !candidato.getIdentificador().equals(ignorado)));
		});

		when(perfilRepositorio.incluir(any())).thenAnswer(chamada -> {
			PerfilComunitario perfil = chamada.getArgument(0);
			var id = new PerfilComunitarioIdentificador(String.valueOf(sequenciaPerfil.getAndIncrement()));
			var salvo = new PerfilComunitario(Optional.of(id), perfil.getUsuario(), perfil.getApelido(),
					perfil.getCidade(), perfil.getDescricao(), perfil.getFoto(), perfil.getPreferenciasPublicas(),
					perfil.isDecksFavoritosVisiveis(), perfil.getCartasFavoritas());
			armazenarPerfil(salvo);
			return salvo;
		});

		doAnswer(chamada -> {
			PerfilComunitario perfil = chamada.getArgument(0);
			armazenarPerfil(perfil);
			return null;
		}).when(perfilRepositorio).alterar(any());

		when(colecaoCartaConsulta.usuarioPossuiCarta(any(), any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			CartaIdentificador carta = chamada.getArgument(1);
			return cartasPorUsuario.getOrDefault(usuario.toString(), Set.of()).contains(carta.toString());
		});

		when(colecaoCartaConsulta.listarCartasDisponiveis(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return cartasPorUsuario.getOrDefault(usuario.toString(), Set.of()).stream()
					.map(codigo -> new CartaColecaoResumo(new CartaIdentificador(codigo), codigo, "OP-01")).toList();
		});

		when(amizadeRepositorio.existePorIdentificador(any())).thenAnswer(chamada -> {
			AmizadeIdentificador id = chamada.getArgument(0);
			return amizadesPorId.containsKey(id.toString());
		});

		when(amizadeRepositorio.buscarPorIdentificador(any())).thenAnswer(chamada -> {
			AmizadeIdentificador id = chamada.getArgument(0);
			var amizade = amizadesPorId.get(id.toString());
			if (amizade == null) {
				throw new AgregadoInexistenteExcecao(AmizadeServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return amizade;
		});

		when(amizadeRepositorio.existeRelacaoEntre(any(), any())).thenAnswer(chamada -> {
			UsuarioIdentificador a = chamada.getArgument(0);
			UsuarioIdentificador b = chamada.getArgument(1);
			return buscarRelacao(a, b).isPresent();
		});

		when(amizadeRepositorio.buscarRelacaoEntre(any(), any())).thenAnswer(chamada -> {
			UsuarioIdentificador a = chamada.getArgument(0);
			UsuarioIdentificador b = chamada.getArgument(1);
			return buscarRelacao(a, b).orElseThrow(
					() -> new AgregadoInexistenteExcecao("Não existe relação entre %s e %s".formatted(a, b)));
		});

		when(amizadeRepositorio.listarPorUsuario(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return amizadesPorId.values().stream().filter(amizade -> amizade.envolve(usuario)).toList();
		});

		when(amizadeRepositorio.listarAmizadesAtivas(any())).thenAnswer(chamada -> {
			UsuarioIdentificador usuario = chamada.getArgument(0);
			return amizadesPorId.values().stream().filter(amizade -> amizade.estaAceita() && amizade.envolve(usuario))
					.toList();
		});

		when(amizadeRepositorio.incluir(any())).thenAnswer(chamada -> {
			Amizade amizade = chamada.getArgument(0);
			var id = new AmizadeIdentificador(String.valueOf(sequenciaAmizade.getAndIncrement()));
			var salva = new Amizade(Optional.of(id), amizade.getSolicitante(), amizade.getDestinatario(),
					amizade.getStatus(), amizade.getCriadoEm(), amizade.getAtualizadoEm());
			amizadesPorId.put(id.toString(), salva);
			return salva;
		});

		doAnswer(chamada -> {
			Amizade amizade = chamada.getArgument(0);
			amizadesPorId.put(amizade.getIdentificador().orElseThrow().toString(), amizade);
			return null;
		}).when(amizadeRepositorio).alterar(any());

		doAnswer(chamada -> {
			AmizadeIdentificador id = chamada.getArgument(0);
			amizadesPorId.remove(id.toString());
			return null;
		}).when(amizadeRepositorio).excluir(any());
	}

	private void armazenarPerfil(PerfilComunitario perfil) {
		var id = perfil.getIdentificador().orElseThrow().toString();
		perfisPorId.put(id, perfil);
		perfisPorUsuario.put(perfil.getUsuario().toString(), perfil);
	}

	private Optional<Amizade> buscarRelacao(UsuarioIdentificador a, UsuarioIdentificador b) {
		return amizadesPorId.values().stream().filter(amizade -> amizade.envolve(a) && amizade.envolve(b)).findFirst();
	}

	private void autenticar(String usuario) {
		this.usuarioAutenticado = usuario;
		this.ultimoUsuarioAcao = usuario;
	}

	private void executar(Runnable acao) {
		excecaoCapturada = null;
		try {
			acao.run();
		} catch (RuntimeException excecao) {
			excecaoCapturada = excecao;
		}
	}

	@Dado("que não existe nenhum perfil comunitário cadastrado")
	public void que_nao_existe_nenhum_perfil_comunitario_cadastrado() {
		perfisPorId.clear();
		perfisPorUsuario.clear();
		cartasPorUsuario.clear();
		visaoConsultada = null;
		excecaoCapturada = null;
	}

	@Dado("que não existe nenhuma solicitação ou amizade cadastrada")
	public void que_nao_existe_nenhuma_solicitacao_ou_amizade_cadastrada() {
		amizadesPorId.clear();
		excecaoCapturada = null;
	}

	@Dado("que existe o perfil comunitário do usuário {string} com apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void que_existe_o_perfil_comunitario_do_usuario_com_apelido_cidade_descricao_e_foto(String usuario,
			String apelido, String cidade, String descricao, String foto) {
		var id = new PerfilComunitarioIdentificador(String.valueOf(sequenciaPerfil.getAndIncrement()));
		var perfil = new PerfilComunitario(Optional.of(id), new UsuarioIdentificador(usuario), new Apelido(apelido),
				Optional.of(cidade), Optional.of(descricao), Optional.of(foto), Optional.empty(), true, List.of());
		armazenarPerfil(perfil);
	}

	@Quando("o usuário {string} consulta o seu próprio perfil comunitário")
	public void o_usuario_consulta_o_seu_proprio_perfil_comunitario(String usuario) {
		autenticar(usuario);
		executar(() -> visaoConsultada = perfilProxy.consultarPerfil(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario)));
	}

	@Entao("o perfil comunitário retornado exibe apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_perfil_comunitario_retornado_exibe_apelido_cidade_descricao_e_foto(String apelido, String cidade,
			String descricao, String foto) {
		assertNull(excecaoCapturada);
		assertNotNull(visaoConsultada);
		assertTrue(visaoConsultada.isEhProprietario());
		assertEquals(apelido, visaoConsultada.getApelido().toString());
		assertEquals(Optional.of(cidade), visaoConsultada.getCidade());
		assertEquals(Optional.of(descricao), visaoConsultada.getDescricao());
		assertEquals(Optional.of(foto), visaoConsultada.getFoto());
	}

	@Quando("o usuário {string} consulta o perfil público do usuário {string}")
	public void o_usuario_consulta_o_perfil_publico_do_usuario(String solicitante, String alvo) {
		autenticar(solicitante);
		executar(() -> visaoConsultada = perfilProxy.consultarPerfil(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(alvo)));
	}

	@Entao("o perfil público retornado exibe apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_perfil_publico_retornado_exibe_apelido_cidade_descricao_e_foto(String apelido, String cidade,
			String descricao, String foto) {
		assertNull(excecaoCapturada);
		assertNotNull(visaoConsultada);
		assertFalse(visaoConsultada.isEhProprietario());
		assertEquals(apelido, visaoConsultada.getApelido().toString());
		assertEquals(Optional.of(cidade), visaoConsultada.getCidade());
		assertEquals(Optional.of(descricao), visaoConsultada.getDescricao());
		assertEquals(Optional.of(foto), visaoConsultada.getFoto());
	}

	@Quando("o usuário {string} tenta consultar o perfil público do usuário {string}")
	public void o_usuario_tenta_consultar_o_perfil_publico_do_usuario(String solicitante, String alvo) {
		autenticar(solicitante);
		executar(() -> visaoConsultada = perfilProxy.consultarPerfil(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(alvo)));
	}

	@Entao("a consulta do perfil comunitário é rejeitada porque o perfil {string} não existe")
	public void a_consulta_do_perfil_comunitario_e_rejeitada_porque_o_perfil_nao_existe(String alvo) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, excecaoCapturada);
		assertTrue(excecaoCapturada.getMessage().contains(alvo));
	}

	@Quando("o usuário {string} edita seu perfil comunitário para apelido {string}, cidade {string}, descrição {string}, foto {string} e preferências públicas {string}")
	public void o_usuario_edita_seu_perfil_comunitario_para_apelido_cidade_descricao_foto_e_preferencias_publicas(
			String usuario, String apelido, String cidade, String descricao, String foto, String preferencias) {
		autenticar(usuario);
		executar(() -> {
			var atual = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador(usuario));
			var editado = atual.editarDados(new Apelido(apelido), Optional.of(cidade), Optional.of(descricao),
					Optional.of(foto), Optional.of(preferencias), atual.isDecksFavoritosVisiveis());
			perfilProxy.editarPerfil(new UsuarioIdentificador(usuario), editado);
		});
	}

	@Entao("a edição do perfil comunitário é concluída com sucesso")
	public void a_edicao_do_perfil_comunitario_e_concluida_com_sucesso() {
		assertNull(excecaoCapturada);
	}

	@Entao("o perfil comunitário do usuário {string} passa a ter apelido {string}, cidade {string}, descrição {string}, foto {string} e preferências públicas {string}")
	public void o_perfil_comunitario_do_usuario_passa_a_ter_apelido_cidade_descricao_foto_e_preferencias_publicas(
			String usuario, String apelido, String cidade, String descricao, String foto, String preferencias) {
		var perfil = perfisPorUsuario.get(usuario);
		assertNotNull(perfil);
		assertEquals(apelido, perfil.getApelido().toString());
		assertEquals(Optional.of(cidade), perfil.getCidade());
		assertEquals(Optional.of(descricao), perfil.getDescricao());
		assertEquals(Optional.of(foto), perfil.getFoto());
		assertEquals(Optional.of(preferencias), perfil.getPreferenciasPublicas());
	}

	@Quando("o usuário {string} inicia a edição do perfil com apelido {string} e cancela a alteração")
	public void o_usuario_inicia_a_edicao_do_perfil_com_apelido_e_cancela_a_alteracao(String usuario,
			String apelidoRascunho) {
		autenticar(usuario);
		// Cancelamento em fluxo de formulário não dispara alteração no serviço/proxy
		assertNotNull(apelidoRascunho);
		excecaoCapturada = null;
	}

	@Entao("o perfil comunitário do usuário {string} permanece com apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_perfil_comunitario_do_usuario_permanece_com_apelido_cidade_descricao_e_foto(String usuario,
			String apelido, String cidade, String descricao, String foto) {
		var perfil = perfisPorUsuario.get(usuario);
		assertNotNull(perfil);
		assertEquals(apelido, perfil.getApelido().toString());
		assertEquals(Optional.of(cidade), perfil.getCidade());
		assertEquals(Optional.of(descricao), perfil.getDescricao());
		assertEquals(Optional.of(foto), perfil.getFoto());
	}

	@Quando("o usuário {string} edita seu perfil comunitário para apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_usuario_edita_seu_perfil_comunitario_para_apelido_cidade_descricao_e_foto(String usuario,
			String apelido, String cidade, String descricao, String foto) {
		autenticar(usuario);
		executar(() -> {
			var atual = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador(usuario));
			var editado = atual.editarDados(new Apelido(apelido), Optional.of(cidade), Optional.of(descricao),
					Optional.of(foto), atual.getPreferenciasPublicas(), atual.isDecksFavoritosVisiveis());
			perfilProxy.editarPerfil(new UsuarioIdentificador(usuario), editado);
		});
	}

	@Entao("o perfil comunitário do usuário {string} passa a ter apelido {string} e cidade {string}")
	public void o_perfil_comunitario_do_usuario_passa_a_ter_apelido_e_cidade(String usuario, String apelido,
			String cidade) {
		var perfil = perfisPorUsuario.get(usuario);
		assertNotNull(perfil);
		assertEquals(apelido, perfil.getApelido().toString());
		assertEquals(Optional.of(cidade), perfil.getCidade());
	}

	@Quando("o usuário {string} tenta editar seu perfil comunitário para o apelido {string}")
	public void o_usuario_tenta_editar_seu_perfil_comunitario_para_o_apelido(String usuario, String novoApelido) {
		autenticar(usuario);
		executar(() -> {
			var atual = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador(usuario));
			var editado = atual.editarDados(new Apelido(novoApelido), atual.getCidade(), atual.getDescricao(),
					atual.getFoto(), atual.getPreferenciasPublicas(), atual.isDecksFavoritosVisiveis());
			perfilProxy.editarPerfil(new UsuarioIdentificador(usuario), editado);
		});
	}

	@Entao("a edição do perfil comunitário é rejeitada porque o apelido {string} já está em uso")
	public void a_edicao_do_perfil_comunitario_e_rejeitada_porque_o_apelido_ja_esta_em_uso(String apelido) {
		assertInstanceOf(AgregadoExistenteExcecao.class, excecaoCapturada);
		assertEquals(PerfilComunitarioServico.APELIDO_EXISTENTE_MODELO.formatted(apelido),
				excecaoCapturada.getMessage());
	}

	@Entao("a edição do perfil comunitário é rejeitada porque o apelido é obrigatório")
	public void a_edicao_do_perfil_comunitario_e_rejeitada_porque_o_apelido_e_obrigatorio() {
		assertInstanceOf(IllegalArgumentException.class, excecaoCapturada);
		assertTrue(excecaoCapturada.getMessage().contains("apelido é obrigatório"));
	}

	@Quando("o usuário {string} tenta editar o perfil comunitário do usuário {string} para o apelido {string}")
	public void o_usuario_tenta_editar_o_perfil_comunitario_do_usuario_para_o_apelido(String solicitante, String alvo,
			String apelido) {
		autenticar(solicitante);
		executar(() -> {
			var perfilAlvo = perfilRepositorio.buscarPorUsuario(new UsuarioIdentificador(alvo));
			var editado = perfilAlvo.editarDados(new Apelido(apelido), perfilAlvo.getCidade(), perfilAlvo.getDescricao(),
					perfilAlvo.getFoto(), perfilAlvo.getPreferenciasPublicas(), perfilAlvo.isDecksFavoritosVisiveis());
			perfilProxy.editarPerfil(new UsuarioIdentificador(solicitante), editado);
		});
	}

	@Entao("a edição do perfil comunitário é rejeitada por falta de autorização")
	public void a_edicao_do_perfil_comunitario_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, excecaoCapturada);
	}

	@Dado("que o usuário {string} possui a carta {string} na sua coleção")
	public void que_o_usuario_possui_a_carta_na_sua_colecao(String usuario, String carta) {
		cartasPorUsuario.computeIfAbsent(usuario, chave -> new HashSet<>()).add(carta);
	}

	@Quando("o usuário {string} fixa a carta {string} como destaque do seu perfil")
	public void o_usuario_fixa_a_carta_como_destaque_do_seu_perfil(String usuario, String carta) {
		autenticar(usuario);
		executar(() -> perfilProxy.fixarCartaFavorita(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new CartaIdentificador(carta)));
	}

	@Entao("a carta {string} passa a constar nos destaques de cartas favoritas do perfil de {string}")
	public void a_carta_passa_a_constar_nos_destaques_de_cartas_favoritas_do_perfil_de(String carta, String usuario) {
		assertNull(excecaoCapturada);
		var perfil = perfisPorUsuario.get(usuario);
		assertNotNull(perfil);
		assertTrue(perfil.getCartasFavoritas().contains(new CartaIdentificador(carta)));
	}

	@Dado("que o usuário {string} já possui {int} cartas fixadas nos destaques do seu perfil: {string}, {string}, {string} e {string}")
	public void que_o_usuario_ja_possui_cartas_fixadas_nos_destaques_do_seu_perfil_e(String usuario, Integer quantidade,
			String c1, String c2, String c3, String c4) {
		assertEquals(4, quantidade);
		var cartas = List.of(new CartaIdentificador(c1), new CartaIdentificador(c2), new CartaIdentificador(c3),
				new CartaIdentificador(c4));
		var set = cartasPorUsuario.computeIfAbsent(usuario, chave -> new HashSet<>());
		cartas.forEach(c -> set.add(c.toString()));

		var atual = perfisPorUsuario.get(usuario);
		var atualizado = new PerfilComunitario(atual.getIdentificador(), atual.getUsuario(), atual.getApelido(),
				atual.getCidade(), atual.getDescricao(), atual.getFoto(), atual.getPreferenciasPublicas(),
				atual.isDecksFavoritosVisiveis(), cartas);
		armazenarPerfil(atualizado);
	}

	@Quando("o usuário {string} tenta fixar a carta {string} como destaque do seu perfil")
	public void o_usuario_tenta_fixar_a_carta_como_destaque_do_seu_perfil(String usuario, String carta) {
		autenticar(usuario);
		executar(() -> perfilProxy.fixarCartaFavorita(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new CartaIdentificador(carta)));
	}

	@Entao("a fixação da carta nos destaques é rejeitada porque o limite de {int} destaques foi atingido")
	public void a_fixacao_da_carta_nos_destaques_e_rejeitada_porque_o_limite_de_destaques_foi_atingido(
			Integer limite) {
		assertEquals(PerfilComunitario.MAX_CARTAS_FAVORITAS, limite);
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(PerfilComunitario.LIMITE_DESTAQUES_ATINGIDO, excecaoCapturada.getMessage());
	}

	@Dado("que o usuário {string} já possui a carta {string} fixada nos destaques do seu perfil")
	public void que_o_usuario_ja_possui_a_carta_fixada_nos_destaques_do_seu_perfil(String usuario, String carta) {
		cartasPorUsuario.computeIfAbsent(usuario, chave -> new HashSet<>()).add(carta);
		var atual = perfisPorUsuario.get(usuario);
		armazenarPerfil(atual.fixarCartaFavorita(new CartaIdentificador(carta)));
	}

	@Quando("o usuário {string} tenta fixar a carta {string} novamente nos destaques do seu perfil")
	public void o_usuario_tenta_fixar_a_carta_novamente_nos_destaques_do_seu_perfil(String usuario, String carta) {
		autenticar(usuario);
		executar(() -> perfilProxy.fixarCartaFavorita(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new CartaIdentificador(carta)));
	}

	@Entao("a fixação da carta nos destaques é rejeitada porque a carta {string} já está nos destaques")
	public void a_fixacao_da_carta_nos_destaques_e_rejeitada_porque_a_carta_ja_esta_nos_destaques(String carta) {
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(PerfilComunitario.CARTA_DUPLICADA_DESTAQUE_MODELO.formatted(carta),
				excecaoCapturada.getMessage());
	}

	@Quando("o usuário {string} remove a carta {string} dos destaques do seu perfil")
	public void o_usuario_remove_a_carta_dos_destaques_do_seu_perfil(String usuario, String carta) {
		autenticar(usuario);
		executar(() -> perfilProxy.removerCartaFavorita(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario), new CartaIdentificador(carta)));
	}

	@Quando("fixa a carta {string} como destaque do seu perfil")
	public void fixa_a_carta_como_destaque_do_seu_perfil(String carta) {
		o_usuario_fixa_a_carta_como_destaque_do_seu_perfil(ultimoUsuarioAcao, carta);
	}

	@Entao("os destaques de cartas favoritas do perfil de {string} contêm {string}, {string}, {string} e {string}")
	public void os_destaques_de_cartas_favoritas_do_perfil_de_contem_e(String usuario, String c1, String c2, String c3,
			String c4) {
		assertNull(excecaoCapturada);
		var perfil = perfisPorUsuario.get(usuario);
		assertNotNull(perfil);
		assertEquals(List.of(new CartaIdentificador(c1), new CartaIdentificador(c2), new CartaIdentificador(c3),
				new CartaIdentificador(c4)), perfil.getCartasFavoritas());
	}

	@Quando("o usuário {string} envia uma solicitação de amizade para o usuário {string}")
	public void o_usuario_envia_uma_solicitacao_de_amizade_para_o_usuario(String solicitante, String destinatario) {
		autenticar(solicitante);
		executar(() -> amizadeServico.enviarSolicitacao(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(destinatario)));
	}

	@Entao("a solicitação de amizade de {string} para {string} fica com status pendente")
	public void a_solicitacao_de_amizade_de_para_fica_com_status_pendente(String solicitante, String destinatario) {
		assertNull(excecaoCapturada);
		var relacao = buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario));
		assertTrue(relacao.isPresent());
		assertEquals(StatusAmizade.PENDENTE, relacao.get().getStatus());
	}

	@Quando("o usuário {string} tenta enviar uma solicitação de amizade para o usuário {string}")
	public void o_usuario_tenta_enviar_uma_solicitacao_de_amizade_para_o_usuario(String solicitante,
			String destinatario) {
		autenticar(solicitante);
		executar(() -> amizadeServico.enviarSolicitacao(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(destinatario)));
	}

	@Entao("a solicitação de amizade é rejeitada porque não é possível adicionar a si mesmo como amigo")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_nao_e_possivel_adicionar_a_si_mesmo_como_amigo() {
		assertInstanceOf(IllegalArgumentException.class, excecaoCapturada);
		assertEquals(Amizade.AUTO_SOLICITACAO, excecaoCapturada.getMessage());
	}

	@Entao("a solicitação de amizade é rejeitada porque o usuário {string} não existe")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_o_usuario_nao_existe(String destinatario) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, excecaoCapturada);
		assertEquals(AmizadeServico.DESTINATARIO_INEXISTENTE_MODELO.formatted(destinatario),
				excecaoCapturada.getMessage());
	}

	@Dado("que existe uma solicitação de amizade pendente de {string} para {string}")
	public void que_existe_uma_solicitacao_de_amizade_pendente_de_para(String solicitante, String destinatario) {
		var id = new AmizadeIdentificador(String.valueOf(sequenciaAmizade.getAndIncrement()));
		var amizade = new Amizade(Optional.of(id), new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(destinatario), StatusAmizade.PENDENTE, Instant.now(), Optional.empty());
		amizadesPorId.put(id.toString(), amizade);
	}

	@Entao("a solicitação de amizade é rejeitada porque já existe uma solicitação pendente entre {string} e {string}")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_ja_existe_uma_solicitacao_pendente_entre_e(
			String solicitante, String destinatario) {
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(AmizadeServico.SOLICITACAO_PENDENTE_MODELO.formatted(solicitante, destinatario),
				excecaoCapturada.getMessage());
	}

	@Dado("que {string} e {string} já são amigos")
	public void que_e_ja_sao_amigos(String a, String b) {
		var id = new AmizadeIdentificador(String.valueOf(sequenciaAmizade.getAndIncrement()));
		var agora = Instant.now();
		var amizade = new Amizade(Optional.of(id), new UsuarioIdentificador(a), new UsuarioIdentificador(b),
				StatusAmizade.ACEITA, agora, Optional.of(agora));
		amizadesPorId.put(id.toString(), amizade);
	}

	@Entao("a solicitação de amizade é rejeitada porque {string} e {string} já são amigos")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_e_ja_sao_amigos(String a, String b) {
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(AmizadeServico.AMIZADE_JA_ATIVA_MODELO.formatted(a, b), excecaoCapturada.getMessage());
	}

	@Quando("o usuário {string} aceita a solicitação de amizade enviada por {string}")
	public void o_usuario_aceita_a_solicitacao_de_amizade_enviada_por(String destinatario, String solicitante) {
		autenticar(destinatario);
		executar(() -> {
			var relacao = buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
					.orElseThrow();
			amizadeServico.aceitarSolicitacao(new UsuarioIdentificador(destinatario),
					relacao.getIdentificador().orElseThrow());
		});
	}

	@Entao("{string} e {string} passam a ser amigos bilateralmente")
	public void e_passam_a_ser_amigos_bilateralmente(String a, String b) {
		assertNull(excecaoCapturada);
		var relacao = buscarRelacao(new UsuarioIdentificador(a), new UsuarioIdentificador(b));
		assertTrue(relacao.isPresent());
		assertTrue(relacao.get().estaAceita());
	}

	@Entao("{string} consta na lista de amigos de {string}")
	public void consta_na_lista_de_amigos_de(String amigo, String usuario) {
		var amigos = amizadeServico.listarAmizadesAtivas(new UsuarioIdentificador(usuario)).stream()
				.map(amizade -> amizade.outroUsuario(new UsuarioIdentificador(usuario)).toString()).toList();
		assertTrue(amigos.contains(amigo));
	}

	@Quando("o usuário {string} tenta aceitar a solicitação de amizade de {string} para {string}")
	public void o_usuario_tenta_aceitar_a_solicitacao_de_amizade_de_para(String executor, String solicitante,
			String destinatario) {
		autenticar(executor);
		executar(() -> {
			var relacao = buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
					.orElseThrow();
			amizadeServico.aceitarSolicitacao(new UsuarioIdentificador(executor),
					relacao.getIdentificador().orElseThrow());
		});
	}

	@Entao("a resposta à solicitação de amizade é rejeitada por falta de autorização")
	public void a_resposta_a_solicitacao_de_amizade_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, excecaoCapturada);
	}

	@Quando("o usuário {string} recusa a solicitação de amizade enviada por {string}")
	public void o_usuario_recusa_a_solicitacao_de_amizade_enviada_por(String destinatario, String solicitante) {
		autenticar(destinatario);
		executar(() -> {
			var relacao = buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
					.orElseThrow();
			amizadeServico.recusarSolicitacao(new UsuarioIdentificador(destinatario),
					relacao.getIdentificador().orElseThrow());
		});
	}

	@Entao("a solicitação de amizade de {string} para {string} é encerrada sem criar amizade")
	public void a_solicitacao_de_amizade_de_para_e_encerrada_sem_criar_amizade(String solicitante,
			String destinatario) {
		assertNull(excecaoCapturada);
		assertTrue(buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
				.isEmpty());
	}

	@Entao("{string} e {string} não são amigos")
	public void e_nao_sao_amigos(String a, String b) {
		var amigosA = amizadeServico.listarAmizadesAtivas(new UsuarioIdentificador(a)).stream()
				.map(amizade -> amizade.outroUsuario(new UsuarioIdentificador(a)).toString()).toList();
		assertFalse(amigosA.contains(b));
	}

	@Quando("o usuário {string} cancela a solicitação de amizade enviada para {string}")
	public void o_usuario_cancela_a_solicitacao_de_amizade_enviada_para(String solicitante, String destinatario) {
		autenticar(solicitante);
		executar(() -> {
			var relacao = buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
					.orElseThrow();
			amizadeServico.cancelarSolicitacao(new UsuarioIdentificador(solicitante),
					relacao.getIdentificador().orElseThrow());
		});
	}

	@Entao("a solicitação de amizade pendente de {string} para {string} deixa de existir")
	public void a_solicitacao_de_amizade_pendente_de_para_deixa_de_existir(String solicitante, String destinatario) {
		assertNull(excecaoCapturada);
		assertTrue(buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
				.isEmpty());
	}

	@Quando("o usuário {string} tenta cancelar a solicitação de amizade de {string} para {string}")
	public void o_usuario_tenta_cancelar_a_solicitacao_de_amizade_de_para(String executor, String solicitante,
			String destinatario) {
		autenticar(executor);
		executar(() -> {
			var relacao = buscarRelacao(new UsuarioIdentificador(solicitante), new UsuarioIdentificador(destinatario))
					.orElseThrow();
			amizadeServico.cancelarSolicitacao(new UsuarioIdentificador(executor),
					relacao.getIdentificador().orElseThrow());
		});
	}

	@Entao("o cancelamento da solicitação de amizade é rejeitado por falta de autorização")
	public void o_cancelamento_da_solicitacao_de_amizade_e_rejeitado_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, excecaoCapturada);
	}

	@Quando("o usuário {string} remove {string} da sua lista de amigos")
	public void o_usuario_remove_da_sua_lista_de_amigos(String solicitante, String amigo) {
		autenticar(solicitante);
		executar(() -> amizadeServico.removerAmizadeBilateral(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(amigo)));
	}

	@Entao("a amizade entre {string} e {string} é removida dos dois lados")
	public void a_amizade_entre_e_e_removida_dos_dois_lados(String a, String b) {
		assertNull(excecaoCapturada);
		assertTrue(buscarRelacao(new UsuarioIdentificador(a), new UsuarioIdentificador(b)).isEmpty());
	}

	@Entao("{string} não consta mais na lista de amigos de {string}")
	public void nao_consta_mais_na_lista_de_amigos_de(String amigo, String usuario) {
		var amigos = amizadeServico.listarAmizadesAtivas(new UsuarioIdentificador(usuario)).stream()
				.map(amizade -> amizade.outroUsuario(new UsuarioIdentificador(usuario)).toString()).toList();
		assertFalse(amigos.contains(amigo));
	}

	@Quando("o usuário {string} tenta remover {string} da sua lista de amigos")
	public void o_usuario_tenta_remover_da_sua_lista_de_amigos(String solicitante, String amigo) {
		autenticar(solicitante);
		executar(() -> amizadeServico.removerAmizadeBilateral(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(amigo)));
	}

	@Entao("a remoção da amizade é rejeitada porque {string} e {string} não são amigos")
	public void a_remocao_da_amizade_e_rejeitada_porque_e_nao_sao_amigos(String a, String b) {
		assertInstanceOf(IllegalStateException.class, excecaoCapturada);
		assertEquals(AmizadeServico.NAO_SAO_AMIGOS_MODELO.formatted(a, b), excecaoCapturada.getMessage());
	}
}
