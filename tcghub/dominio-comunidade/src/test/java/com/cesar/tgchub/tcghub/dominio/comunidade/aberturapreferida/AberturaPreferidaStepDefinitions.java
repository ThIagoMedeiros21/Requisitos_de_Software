package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi.IAberturaPreferidaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AberturaPreferidaStepDefinitions {
	private final Map<AberturaIdentificador, Abertura> bancoAberturas = new LinkedHashMap<>();
	private final Map<AberturaPreferidaIdentificador, AberturaPreferida> bancoPreferidas = new LinkedHashMap<>();
	private final Map<String, Amizade> bancoAmizades = new LinkedHashMap<>();
	private final AtomicInteger seqAbertura = new AtomicInteger(0);
	private final AtomicInteger seqPreferida = new AtomicInteger(0);
	private final AtomicInteger seqAmizade = new AtomicInteger(0);
	private final AtomicInteger seqUsuarioFake = new AtomicInteger(0);

	private ISegurancaServico segurancaServico;
	private IAberturaRepositorio aberturaRepositorio;
	private IAberturaPreferidaRepositorio preferidaRepositorio;
	private IAmizadeRepositorio amizadeRepositorio;
	private AberturaPreferidaServico servico;
	private IAberturaPreferidaPrivacidadeProxy proxy;

	private String usuarioAtual;
	private List<AberturaParaEscolhaVisao> ultimaListagem;
	private RankingAberturasVisao ultimoRanking;
	private AberturaPreferidaPerfilVisao ultimaVisaoPerfil;
	private AberturaPreferidaDetalhesVisao ultimosDetalhes;
	private Throwable ultimaExcecao;

	@Before
	public void configurarCenario() {
		bancoAberturas.clear();
		bancoPreferidas.clear();
		bancoAmizades.clear();
		seqAbertura.set(0);
		seqPreferida.set(0);
		seqAmizade.set(0);
		seqUsuarioFake.set(0);
		usuarioAtual = "lucas";
		ultimaListagem = List.of();
		ultimoRanking = null;
		ultimaVisaoPerfil = null;
		ultimosDetalhes = null;
		ultimaExcecao = null;

		segurancaServico = mock(ISegurancaServico.class);
		when(segurancaServico.getUsuario()).thenAnswer(inv -> usuarioAtual);
		when(segurancaServico.possuiAutorizacao(Autorizacao.USUARIO.name())).thenReturn(true);
		when(segurancaServico.possuiAutorizacao(Autorizacao.ADMINISTRADOR.name())).thenReturn(false);

		aberturaRepositorio = mock(IAberturaRepositorio.class);
		when(aberturaRepositorio.existePorIdentificador(any(AberturaIdentificador.class)))
				.thenAnswer(inv -> bancoAberturas.containsKey(inv.getArgument(0)));
		when(aberturaRepositorio.buscarPorIdentificador(any(AberturaIdentificador.class))).thenAnswer(inv -> {
			AberturaIdentificador id = inv.getArgument(0);
			var encontrada = bancoAberturas.get(id);
			if (encontrada == null) {
				throw new AgregadoInexistenteExcecao(AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return encontrada;
		});
		when(aberturaRepositorio.existePorNumero(anyInt())).thenAnswer(inv -> {
			int numero = inv.getArgument(0);
			return bancoAberturas.values().stream().anyMatch(a -> a.getNumeroValor() == numero);
		});
		when(aberturaRepositorio.buscarPorNumero(anyInt())).thenAnswer(inv -> {
			int numero = inv.getArgument(0);
			return bancoAberturas.values().stream().filter(a -> a.getNumeroValor() == numero).findFirst()
					.orElseThrow(() -> new AgregadoInexistenteExcecao(
							AberturaServico.NUMERO_INEXISTENTE_MODELO.formatted(numero)));
		});
		when(aberturaRepositorio.listarTodas()).thenAnswer(inv -> bancoAberturas.values().stream()
				.sorted(Comparator.comparingInt(Abertura::getNumeroValor)).toList());
		when(aberturaRepositorio.listarPorStatus(anyBoolean())).thenAnswer(inv -> {
			boolean ativa = inv.getArgument(0);
			return bancoAberturas.values().stream().filter(a -> a.isAtiva() == ativa)
					.sorted(Comparator.comparingInt(Abertura::getNumeroValor)).toList();
		});

		preferidaRepositorio = mock(IAberturaPreferidaRepositorio.class);
		when(preferidaRepositorio.existePorIdentificador(any(AberturaPreferidaIdentificador.class)))
				.thenAnswer(inv -> bancoPreferidas.containsKey(inv.getArgument(0)));
		when(preferidaRepositorio.buscarPorIdentificador(any(AberturaPreferidaIdentificador.class))).thenAnswer(inv -> {
			AberturaPreferidaIdentificador id = inv.getArgument(0);
			var encontrada = bancoPreferidas.get(id);
			if (encontrada == null) {
				throw new AgregadoInexistenteExcecao(
						AberturaPreferidaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return encontrada;
		});
		when(preferidaRepositorio.incluir(any(AberturaPreferida.class))).thenAnswer(inv -> {
			AberturaPreferida entrada = inv.getArgument(0);
			var id = new AberturaPreferidaIdentificador(Integer.toString(seqPreferida.incrementAndGet()));
			var salva = new AberturaPreferida(Optional.of(id), entrada.getUsuario(), entrada.getAbertura(),
					entrada.isVisivelNoPerfil(), entrada.getEscolhidaEm());
			bancoPreferidas.put(id, salva);
			return salva;
		});
		doAnswer(inv -> {
			AberturaPreferida alterada = inv.getArgument(0);
			bancoPreferidas.put(alterada.getIdentificador().orElseThrow(), alterada);
			return null;
		}).when(preferidaRepositorio).alterar(any(AberturaPreferida.class));
		when(preferidaRepositorio.existePorUsuario(any(UsuarioIdentificador.class))).thenAnswer(inv -> {
			UsuarioIdentificador u = inv.getArgument(0);
			return bancoPreferidas.values().stream().anyMatch(p -> p.getUsuario().equals(u));
		});
		when(preferidaRepositorio.buscarPorUsuario(any(UsuarioIdentificador.class))).thenAnswer(inv -> {
			UsuarioIdentificador u = inv.getArgument(0);
			return bancoPreferidas.values().stream().filter(p -> p.getUsuario().equals(u)).findFirst()
					.orElseThrow(() -> new AgregadoInexistenteExcecao(
							AberturaPreferidaServico.USUARIO_SEM_PREFERIDA_MODELO.formatted(u)));
		});
		when(preferidaRepositorio.listarTodas()).thenAnswer(inv -> List.copyOf(bancoPreferidas.values()));
		when(preferidaRepositorio.contarPorAbertura(any(AberturaIdentificador.class))).thenAnswer(inv -> {
			AberturaIdentificador abId = inv.getArgument(0);
			return (int) bancoPreferidas.values().stream().filter(p -> p.getAbertura().equals(abId)).count();
		});
		when(preferidaRepositorio.listarPorAbertura(any(AberturaIdentificador.class))).thenAnswer(inv -> {
			AberturaIdentificador abId = inv.getArgument(0);
			return bancoPreferidas.values().stream().filter(p -> p.getAbertura().equals(abId)).toList();
		});

		amizadeRepositorio = mock(IAmizadeRepositorio.class);
		when(amizadeRepositorio.existeRelacaoEntre(any(UsuarioIdentificador.class), any(UsuarioIdentificador.class)))
				.thenAnswer(inv -> bancoAmizades
						.containsKey(chaveAmizade(inv.getArgument(0), inv.getArgument(1))));
		when(amizadeRepositorio.buscarRelacaoEntre(any(UsuarioIdentificador.class), any(UsuarioIdentificador.class)))
				.thenAnswer(inv -> {
					var chave = chaveAmizade(inv.getArgument(0), inv.getArgument(1));
					var am = bancoAmizades.get(chave);
					if (am == null) {
						throw new AgregadoInexistenteExcecao("Amizade inexistente");
					}
					return am;
				});

		servico = new AberturaPreferidaServico(segurancaServico, preferidaRepositorio, aberturaRepositorio);
		proxy = new AberturaPreferidaPrivacidadeProxy(servico, preferidaRepositorio, aberturaRepositorio,
				amizadeRepositorio);
	}

	private static String chaveAmizade(UsuarioIdentificador u1, UsuarioIdentificador u2) {
		var a = u1.toString();
		var b = u2.toString();
		return a.compareTo(b) <= 0 ? a + "|" + b : b + "|" + a;
	}

	private Abertura registrarAbertura(int numero, String nome, String artista, String episodios, boolean ativa) {
		var existente = bancoAberturas.values().stream().filter(a -> a.getNumeroValor() == numero).findFirst();
		if (existente.isPresent()) {
			return existente.get();
		}
		var id = new AberturaIdentificador(Integer.toString(seqAbertura.incrementAndGet()));
		var abertura = new Abertura(Optional.of(id), numero, nome, artista, episodios,
				"https://api.animethemes.moe/img/" + numero + ".jpg",
				"https://v.animethemes.moe/OnePiece-OP" + numero + ".webm", ativa);
		bancoAberturas.put(id, abertura);
		return abertura;
	}

	private void registrarAmizadeAceita(String u1, String u2) {
		var id1 = new UsuarioIdentificador(u1);
		var id2 = new UsuarioIdentificador(u2);
		var amizade = new Amizade(Optional.of(new AmizadeIdentificador(Integer.toString(seqAmizade.incrementAndGet()))),
				id1, id2, StatusAmizade.ACEITA, Instant.now(), Optional.of(Instant.now()));
		bancoAmizades.put(chaveAmizade(id1, id2), amizade);
	}

	private void registrarPreferidaDireta(String usuario, int numeroAbertura, boolean visivel) {
		var abertura = aberturaRepositorio.buscarPorNumero(numeroAbertura);
		var usuarioId = new UsuarioIdentificador(usuario);
		var existente = bancoPreferidas.values().stream().filter(p -> p.getUsuario().equals(usuarioId)).findFirst();
		if (existente.isPresent()) {
			var atualizada = existente.get().substituirAbertura(abertura.getIdentificador().orElseThrow(), Instant.now())
					.alterarVisibilidade(visivel);
			bancoPreferidas.put(atualizada.getIdentificador().orElseThrow(), atualizada);
		} else {
			var id = new AberturaPreferidaIdentificador(Integer.toString(seqPreferida.incrementAndGet()));
			bancoPreferidas.put(id, new AberturaPreferida(Optional.of(id), usuarioId,
					abertura.getIdentificador().orElseThrow(), visivel, Instant.now()));
		}
	}

	private void executarCapturando(Runnable acao) {
		ultimaExcecao = null;
		try {
			acao.run();
		} catch (Throwable ex) {
			ultimaExcecao = ex;
		}
	}

	@Dado("que o usuário {string} ainda não escolheu uma abertura preferida")
	public void que_o_usuario_ainda_nao_escolheu_uma_abertura_preferida(String usuario) {
		var usuarioId = new UsuarioIdentificador(usuario);
		bancoPreferidas.entrySet().removeIf(e -> e.getValue().getUsuario().equals(usuarioId));
	}

	@Dado("que existem as aberturas ativas número {int} {string} e número {int} {string}")
	public void que_existem_as_aberturas_ativas_numero_e_numero(Integer n1, String nome1, Integer n2, String nome2) {
		registrarAbertura(n1, nome1, "Artista " + n1, "1 a 50", true);
		registrarAbertura(n2, nome2, "Artista " + n2, "51 a 100", true);
	}

	@Dado("que existe a abertura inativa número {int} {string}")
	public void que_existe_a_abertura_inativa_numero(Integer numero, String nome) {
		registrarAbertura(numero, nome, "Artista " + numero, "48 a 115", false);
	}

	@Dado("que o usuário {string} é amigo de {string} e escolheu a abertura número {int}")
	public void que_o_usuario_e_amigo_de_e_escolheu_a_abertura_numero(String amigo, String usuario, Integer numero) {
		registrarAmizadeAceita(amigo, usuario);
		registrarPreferidaDireta(amigo, numero, true);
	}

	@Dado("que existem as aberturas ativas número {int} {string} de {string} e número {int} {string} de {string}")
	public void que_existem_as_aberturas_ativas_numero_de_e_numero_de(Integer n1, String nome1, String artista1,
			Integer n2, String nome2, String artista2) {
		registrarAbertura(n1, nome1, artista1, "1 a 47", true);
		registrarAbertura(n2, nome2, artista2, "207 a 263", true);
	}

	@Dado("que existe a abertura ativa número {int} {string} de {string} exibida nos episódios {string}")
	public void que_existe_a_abertura_ativa_numero_de_exibida_nos_episodios(Integer numero, String nome,
			String artista, String episodios) {
		registrarAbertura(numero, nome, artista, episodios, true);
	}

	@Dado("que o usuário {string} já escolheu a abertura número {int} como sua abertura preferida")
	public void que_o_usuario_ja_escolheu_a_abertura_numero_como_sua_abertura_preferida(String usuario,
			Integer numero) {
		registrarPreferidaDireta(usuario, numero, true);
	}

	@Dado("que existe a abertura ativa número {int} {string}")
	public void que_existe_a_abertura_ativa_numero(Integer numero, String nome) {
		registrarAbertura(numero, nome, "Artista " + numero, "1 a 47", true);
	}

	@Dado("que o usuário {string} escolheu a abertura número {int} como sua abertura preferida")
	public void que_o_usuario_escolheu_a_abertura_numero_como_sua_abertura_preferida(String usuario, Integer numero) {
		registrarPreferidaDireta(usuario, numero, true);
	}

	@Dado("que o usuário {string} escolheu a abertura número {int} com exibição oculta no perfil público")
	public void que_o_usuario_escolheu_a_abertura_numero_com_exibicao_oculta_no_perfil_publico(String usuario,
			Integer numero) {
		registrarPreferidaDireta(usuario, numero, false);
	}

	@Dado("que existem as aberturas ativas número {int} {string}, número {int} {string}, número {int} {string}, número {int} {string}, número {int} {string} e número {int} {string}")
	public void que_existem_as_aberturas_ativas_numero_numero_numero_numero_numero_e_numero(Integer n1, String s1,
			Integer n2, String s2, Integer n3, String s3, Integer n4, String s4, Integer n5, String s5, Integer n6,
			String s6) {
		registrarAbertura(n1, s1, "Artista " + n1, "1 a 47", true);
		registrarAbertura(n2, s2, "Artista " + n2, "48 a 115", true);
		registrarAbertura(n3, s3, "Artista " + n3, "116 a 168", true);
		registrarAbertura(n4, s4, "Artista " + n4, "169 a 206", true);
		registrarAbertura(n5, s5, "Artista " + n5, "207 a 263", true);
		registrarAbertura(n6, s6, "Artista " + n6, "264 a 278", true);
	}

	@Dado("que {int} usuários escolheram a abertura número {int}, sendo {int} deles com exibição oculta no perfil público")
	public void que_usuarios_escolheram_a_abertura_numero_sendo_deles_com_exibicao_oculta_no_perfil_publico(
			Integer total, Integer numero, Integer ocultos) {
		for (int i = 0; i < total; i++) {
			boolean visivel = i >= ocultos;
			registrarPreferidaDireta("usuario_rank_" + seqUsuarioFake.incrementAndGet(), numero, visivel);
		}
	}

	@Dado("que {int} usuários escolheram a abertura número {int}")
	public void que_usuarios_escolheram_a_abertura_numero(Integer total, Integer numero) {
		for (int i = 0; i < total; i++) {
			registrarPreferidaDireta("usuario_rank_" + seqUsuarioFake.incrementAndGet(), numero, true);
		}
	}

	@Dado("que {int} usuário escolheu a abertura número {int}")
	public void que_usuario_escolheu_a_abertura_numero(Integer total, Integer numero) {
		for (int i = 0; i < total; i++) {
			registrarPreferidaDireta("usuario_rank_" + seqUsuarioFake.incrementAndGet(), numero, true);
		}
	}

	@Dado("que {string} e {string} são amigos")
	public void que_e_sao_amigos(String u1, String u2) {
		registrarAmizadeAceita(u1, u2);
	}

	@Dado("que o usuário {string} escolheu a abertura número {int} com exibição pública")
	public void que_o_usuario_escolheu_a_abertura_numero_com_exibicao_publica(String usuario, Integer numero) {
		registrarPreferidaDireta(usuario, numero, true);
	}

	@Dado("que o usuário {string} escolheu a abertura número {int}")
	public void que_o_usuario_escolheu_a_abertura_numero(String usuario, Integer numero) {
		registrarPreferidaDireta(usuario, numero, true);
	}

	@Dado("que {string} é amigo de {string} e {string}, e ambos também escolheram a abertura número {int}")
	public void que_e_amigo_de_e_e_ambos_tambem_escolheram_a_abertura_numero(String usuario, String amigo1,
			String amigo2, Integer numero) {
		registrarAmizadeAceita(usuario, amigo1);
		registrarAmizadeAceita(usuario, amigo2);
		registrarPreferidaDireta(amigo1, numero, true);
		registrarPreferidaDireta(amigo2, numero, true);
	}

	@Dado("que existe a abertura inativa número {int} {string} que já havia sido escolhida por {string}")
	public void que_existe_a_abertura_inativa_numero_que_ja_havia_sido_escolhida_por(Integer numero, String nome,
			String usuario) {
		registrarAbertura(numero, nome, "Artista " + numero, "1 a 47", false);
		registrarPreferidaDireta(usuario, numero, true);
	}

	@Quando("o usuário {string} consulta as aberturas disponíveis para escolha")
	public void o_usuario_consulta_as_aberturas_disponiveis_para_escolha(String usuario) {
		usuarioAtual = usuario;
		executarCapturando(() -> ultimaListagem = proxy.listarAberturasAtivasParaEscolha(
				new UsuarioIdentificador(usuario), Optional.empty()));
	}

	@Quando("o usuário {string} pesquisa aberturas disponíveis pelo termo {string}")
	public void o_usuario_pesquisa_aberturas_disponiveis_pelo_termo(String usuario, String termo) {
		usuarioAtual = usuario;
		executarCapturando(() -> ultimaListagem = proxy.listarAberturasAtivasParaEscolha(
				new UsuarioIdentificador(usuario), Optional.of(termo)));
	}

	@Quando("o usuário {string} escolhe a abertura número {int} como sua abertura preferida")
	public void o_usuario_escolhe_a_abertura_numero_como_sua_abertura_preferida(String usuario, Integer numero) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> proxy.escolherPorNumero(u, u, numero));
	}

	@Quando("o usuário {string} tenta escolher a abertura número {int} como sua abertura preferida")
	public void o_usuario_tenta_escolher_a_abertura_numero_como_sua_abertura_preferida(String usuario, Integer numero) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> proxy.escolherPorNumero(u, u, numero));
	}

	@Quando("o usuário {string} escolhe a abertura número {int} como sua nova abertura preferida")
	public void o_usuario_escolhe_a_abertura_numero_como_sua_nova_abertura_preferida(String usuario, Integer numero) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> proxy.escolherPorNumero(u, u, numero));
	}

	@Quando("o usuário {string} oculta a abertura preferida do seu perfil público")
	public void o_usuario_oculta_a_abertura_preferida_do_seu_perfil_publico(String usuario) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> proxy.alterarVisibilidade(u, u, false));
	}

	@Quando("o usuário {string} reexibe a abertura preferida no seu perfil público")
	public void o_usuario_reexibe_a_abertura_preferida_no_seu_perfil_publico(String usuario) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> proxy.alterarVisibilidade(u, u, true));
	}

	@Quando("o usuário {string} consulta o ranking da comunidade de aberturas preferidas")
	public void o_usuario_consulta_o_ranking_da_comunidade_de_aberturas_preferidas(String usuario) {
		usuarioAtual = usuario;
		executarCapturando(() -> ultimoRanking = servico.consultarRankingTop5());
	}

	@Quando("o usuário {string} consulta o perfil do amigo {string}")
	public void o_usuario_consulta_o_perfil_do_amigo(String usuario, String amigo) {
		usuarioAtual = usuario;
		executarCapturando(() -> ultimaVisaoPerfil = proxy.consultarAberturaNoPerfil(
				new UsuarioIdentificador(usuario), new UsuarioIdentificador(amigo)));
	}

	@Quando("o usuário {string} consulta os detalhes da abertura número {int}")
	public void o_usuario_consulta_os_detalhes_da_abertura_numero(String usuario, Integer numero) {
		usuarioAtual = usuario;
		executarCapturando(() -> {
			var abertura = aberturaRepositorio.buscarPorNumero(numero);
			ultimosDetalhes = proxy.consultarDetalhes(new UsuarioIdentificador(usuario),
					abertura.getIdentificador().orElseThrow());
		});
	}

	@Quando("a abertura número {int} é inativada administrativamente")
	public void a_abertura_numero_e_inativada_administrativamente(Integer numero) {
		var existente = aberturaRepositorio.buscarPorNumero(numero);
		bancoAberturas.put(existente.getIdentificador().orElseThrow(), existente.inativar());
	}

	@Entao("a listagem de escolha exibe as aberturas número {int} com {int} amigo e número {int} com {int} amigos")
	public void a_listagem_de_escolha_exibe_as_aberturas_numero_com_amigo_e_numero_com_amigos(Integer n1,
			Integer amigos1, Integer n2, Integer amigos2) {
		assertNull(ultimaExcecao);
		var item1 = ultimaListagem.stream().filter(i -> i.getAbertura().getNumeroValor() == n1).findFirst()
				.orElseThrow();
		var item2 = ultimaListagem.stream().filter(i -> i.getAbertura().getNumeroValor() == n2).findFirst()
				.orElseThrow();
		assertEquals(amigos1, item1.getQuantidadeAmigos());
		assertEquals(amigos2, item2.getQuantidadeAmigos());
	}

	@Entao("a abertura inativa número {int} não aparece na listagem para novas escolhas")
	public void a_abertura_inativa_numero_nao_aparece_na_listagem_para_novas_escolhas(Integer numero) {
		assertTrue(ultimaListagem.stream().noneMatch(i -> i.getAbertura().getNumeroValor() == numero));
	}

	@Entao("a pesquisa retorna apenas a abertura número {int} {string}")
	public void a_pesquisa_retorna_apenas_a_abertura_numero(Integer numero, String nome) {
		assertNull(ultimaExcecao);
		assertEquals(1, ultimaListagem.size());
		assertEquals(numero, ultimaListagem.get(0).getAbertura().getNumeroValor());
		assertEquals(nome, ultimaListagem.get(0).getAbertura().getNome());
	}

	@Entao("a abertura preferida do usuário {string} passa a ser a abertura número {int}")
	public void a_abertura_preferida_do_usuario_passa_a_ser_a_abertura_numero(String usuario, Integer numero) {
		assertNull(ultimaExcecao, () -> "Erro inesperado: " + ultimaExcecao);
		var pref = preferidaRepositorio.buscarPorUsuario(new UsuarioIdentificador(usuario));
		var abertura = aberturaRepositorio.buscarPorIdentificador(pref.getAbertura());
		assertEquals(numero, abertura.getNumeroValor());
	}

	@Entao("a abertura número {int} é exibida no perfil público de {string}")
	public void a_abertura_numero_e_exibida_no_perfil_publico_de(Integer numero, String usuario) {
		var visao = proxy.consultarAberturaNoPerfil(new UsuarioIdentificador("visitante_qualquer"),
				new UsuarioIdentificador(usuario));
		assertTrue(visao.possuiPreferidaVisivel());
		assertEquals(numero, visao.getAbertura().orElseThrow().getNumeroValor());
	}

	@Entao("a escolha da abertura preferida é rejeitada porque a abertura número {int} está inativa")
	public void a_escolha_da_abertura_preferida_e_rejeitada_porque_a_abertura_numero_esta_inativa(Integer numero) {
		assertInstanceOf(IllegalStateException.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("a escolha da abertura preferida é rejeitada porque a abertura número {int} não existe")
	public void a_escolha_da_abertura_preferida_e_rejeitada_porque_a_abertura_numero_nao_existe(Integer numero) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("o usuário {string} mantém apenas {int} abertura preferida cadastrada")
	public void o_usuario_mantem_apenas_abertura_preferida_cadastrada(String usuario, Integer quantidade) {
		var u = new UsuarioIdentificador(usuario);
		long total = bancoPreferidas.values().stream().filter(p -> p.getUsuario().equals(u)).count();
		assertEquals(quantidade.longValue(), total);
	}

	@Entao("o visitante {string} não visualiza a abertura preferida no perfil público de {string}")
	public void o_visitante_nao_visualiza_a_abertura_preferida_no_perfil_publico_de(String visitante, String dono) {
		var visao = proxy.consultarAberturaNoPerfil(new UsuarioIdentificador(visitante),
				new UsuarioIdentificador(dono));
		assertFalse(visao.possuiPreferidaVisivel());
		assertTrue(visao.getAbertura().isEmpty());
	}

	@Entao("o usuário {string} mantém a abertura número {int} salva como sua abertura preferida")
	public void o_usuario_mantem_a_abertura_numero_salva_como_sua_abertura_preferida(String usuario, Integer numero) {
		var visaoProprietario = proxy.consultarAberturaNoPerfil(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario));
		assertTrue(visaoProprietario.possuiPreferidaVisivel());
		assertEquals(numero, visaoProprietario.getAbertura().orElseThrow().getNumeroValor());
	}

	@Entao("o visitante {string} passa a visualizar a abertura número {int} no perfil público de {string}")
	public void o_visitante_passa_a_visualizar_a_abertura_numero_no_perfil_publico_de(String visitante, Integer numero,
			String dono) {
		var visao = proxy.consultarAberturaNoPerfil(new UsuarioIdentificador(visitante),
				new UsuarioIdentificador(dono));
		assertTrue(visao.possuiPreferidaVisivel());
		assertEquals(numero, visao.getAbertura().orElseThrow().getNumeroValor());
	}

	@Entao("o ranking exibe exatamente as {int} aberturas mais escolhidas sobre o total de {int} escolhas")
	public void o_ranking_exibe_exatamente_as_aberturas_mais_escolhidas_sobre_o_total_de_escolhas(Integer qtdItens,
			Integer totalEscolhas) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimoRanking);
		assertEquals(qtdItens, ultimoRanking.getItens().size());
		assertEquals(totalEscolhas, ultimoRanking.getTotalEscolhas());
	}

	@Entao("^a 1ª posição do ranking é a abertura número (\\d+) com (\\d+) escolhas e ([0-9.]+)% do total, sem expor o nome do perfil que a ocultou$")
	public void a_1a_posicao_do_ranking_e_a_abertura_numero_com_escolhas_e_do_total_sem_expor_o_nome_do_perfil_que_a_ocultou(
			Integer numero, Integer escolhas, String percentual) {
		var primeiro = ultimoRanking.getItens().get(0);
		assertEquals(1, primeiro.getPosicao());
		assertEquals(numero, primeiro.getNumero());
		assertEquals(escolhas, primeiro.getQuantidadeEscolhas());
		assertEquals(0, new BigDecimal(percentual).compareTo(primeiro.getPercentual()));
	}

	@Entao("^a 2ª posição do ranking é a abertura número (\\d+) com (\\d+) escolhas e ([0-9.]+)% do total$")
	public void a_2a_posicao_do_ranking_e_a_abertura_numero_com_escolhas_e_do_total(Integer numero, Integer escolhas,
			String percentual) {
		var segundo = ultimoRanking.getItens().get(1);
		assertEquals(2, segundo.getPosicao());
		assertEquals(numero, segundo.getNumero());
		assertEquals(escolhas, segundo.getQuantidadeEscolhas());
		assertEquals(0, new BigDecimal(percentual).compareTo(segundo.getPercentual()));
	}

	@Entao("o ranking retorna vazio com total de {int} escolhas")
	public void o_ranking_retorna_vazio_com_total_de_escolhas(Integer total) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimoRanking);
		assertTrue(ultimoRanking.getItens().isEmpty());
		assertEquals(total, ultimoRanking.getTotalEscolhas());
	}

	@Entao("o sistema indica que {string} possui a mesma abertura preferida que {string}")
	public void o_sistema_indica_que_possui_a_mesma_abertura_preferida_que(String amigo, String usuario) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimaVisaoPerfil);
		assertTrue(ultimaVisaoPerfil.isMesmaPreferenciaDoSolicitante());
		assertTrue(proxy.amigosPossuemMesmaPreferencia(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(amigo)));
	}

	@Entao("o sistema indica que {string} não possui a mesma abertura preferida que {string}")
	public void o_sistema_indica_que_nao_possui_a_mesma_abertura_preferida_que(String amigo, String usuario) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimaVisaoPerfil);
		assertFalse(ultimaVisaoPerfil.isMesmaPreferenciaDoSolicitante());
	}

	@Entao("o sistema não exibe a abertura preferida oculta de {string} nem indica correspondência pública")
	public void o_sistema_nao_exibe_a_abertura_preferida_oculta_de_nem_indica_correspondencia_publica(String amigo) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimaVisaoPerfil);
		assertFalse(ultimaVisaoPerfil.possuiPreferidaVisivel());
		assertFalse(ultimaVisaoPerfil.isMesmaPreferenciaDoSolicitante());
	}

	@Entao("os detalhes exibem artista {string}, episódios {string}, posição {int} no ranking e {int} amigos com a mesma abertura preferida")
	public void os_detalhes_exibem_artista_episodios_posicao_no_ranking_e_amigos_com_a_mesma_abertura_preferida(
			String artista, String episodios, Integer posicao, Integer qtdAmigos) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimosDetalhes);
		assertEquals(artista, ultimosDetalhes.getAbertura().getArtista());
		assertEquals(episodios, ultimosDetalhes.getAbertura().getEpisodios());
		assertEquals(Optional.of(posicao), ultimosDetalhes.getPosicaoRanking());
		assertEquals(qtdAmigos, ultimosDetalhes.getAmigosComMesmaPreferencia().size());
	}

	@Entao("o usuário {string} mantém a abertura número {int} como sua abertura preferida no perfil")
	public void o_usuario_mantem_a_abertura_numero_como_sua_abertura_preferida_no_perfil(String usuario,
			Integer numero) {
		var visao = proxy.consultarAberturaNoPerfil(new UsuarioIdentificador(usuario),
				new UsuarioIdentificador(usuario));
		assertTrue(visao.possuiPreferidaVisivel());
		assertEquals(numero, visao.getAbertura().orElseThrow().getNumeroValor());
		assertFalse(visao.getAbertura().orElseThrow().isAtiva());
	}

	@Entao("a abertura número {int} deixa de aparecer na listagem de novas escolhas para {string}")
	public void a_abertura_numero_deixa_de_aparecer_na_listagem_de_novas_escolhas_para(Integer numero, String usuario) {
		var disponiveis = proxy.listarAberturasAtivasParaEscolha(new UsuarioIdentificador(usuario), Optional.empty());
		assertTrue(disponiveis.stream().noneMatch(i -> i.getAbertura().getNumeroValor() == numero));
	}

	@Entao("a escolha da abertura preferida para {string} é rejeitada porque a abertura número {int} está inativa")
	public void a_escolha_da_abertura_preferida_para_e_rejeitada_porque_a_abertura_numero_esta_inativa(String usuario,
			Integer numero) {
		assertInstanceOf(IllegalStateException.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}
}
