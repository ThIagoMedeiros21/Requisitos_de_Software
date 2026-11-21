package com.cesar.tgchub.tcghub.dominio.adm.abertura;

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

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoExistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AberturaStepDefinitions {
	private final Map<AberturaIdentificador, Abertura> bancoAberturas = new LinkedHashMap<>();
	private final Set<AberturaIdentificador> aberturasEscolhidas = new HashSet<>();
	private final AtomicInteger sequenciaId = new AtomicInteger(0);

	private ISegurancaServico segurancaServico;
	private IAberturaRepositorio repositorio;
	private AberturaServico servico;

	private boolean usuarioEhAdmin;
	private Abertura ultimaAbertura;
	private List<Abertura> ultimaListagem;
	private Throwable ultimaExcecao;

	@Before
	public void configurarCenario() {
		bancoAberturas.clear();
		aberturasEscolhidas.clear();
		sequenciaId.set(0);
		usuarioEhAdmin = true;
		ultimaAbertura = null;
		ultimaListagem = List.of();
		ultimaExcecao = null;

		segurancaServico = mock(ISegurancaServico.class);
		when(segurancaServico.getUsuario()).thenAnswer(inv -> usuarioEhAdmin ? "admin" : "lucas");
		when(segurancaServico.possuiAutorizacao(Autorizacao.ADMINISTRADOR.name()))
				.thenAnswer(inv -> usuarioEhAdmin);
		when(segurancaServico.possuiAutorizacao(Autorizacao.USUARIO.name())).thenReturn(true);

		repositorio = mock(IAberturaRepositorio.class);
		when(repositorio.existePorIdentificador(any(AberturaIdentificador.class)))
				.thenAnswer(inv -> bancoAberturas.containsKey(inv.getArgument(0)));
		when(repositorio.buscarPorIdentificador(any(AberturaIdentificador.class))).thenAnswer(inv -> {
			AberturaIdentificador id = inv.getArgument(0);
			var encontrada = bancoAberturas.get(id);
			if (encontrada == null) {
				throw new AgregadoInexistenteExcecao(AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return encontrada;
		});
		when(repositorio.incluir(any(Abertura.class))).thenAnswer(inv -> {
			Abertura entrada = inv.getArgument(0);
			var id = new AberturaIdentificador(Integer.toString(sequenciaId.incrementAndGet()));
			var salva = new Abertura(Optional.of(id), entrada.getNumero(), entrada.getNome(), entrada.getArtista(),
					entrada.getEpisodios(), entrada.getImagem(), entrada.getVideo(), entrada.getStatus());
			bancoAberturas.put(id, salva);
			return salva;
		});
		doAnswer(inv -> {
			Abertura alterada = inv.getArgument(0);
			var id = alterada.getIdentificador().orElseThrow();
			bancoAberturas.put(id, alterada);
			return null;
		}).when(repositorio).alterar(any(Abertura.class));
		doAnswer(inv -> {
			AberturaIdentificador id = inv.getArgument(0);
			bancoAberturas.remove(id);
			return null;
		}).when(repositorio).excluir(any(AberturaIdentificador.class));
		when(repositorio.existePorNumero(anyInt(), any())).thenAnswer(inv -> {
			int numero = inv.getArgument(0);
			@SuppressWarnings("unchecked")
			Optional<AberturaIdentificador> ignorado = inv.getArgument(1);
			return bancoAberturas.values().stream()
					.anyMatch(a -> a.getNumeroValor() == numero
							&& (ignorado.isEmpty() || !a.getIdentificador().equals(ignorado)));
		});
		when(repositorio.existePorNumero(anyInt())).thenAnswer(inv -> {
			int numero = inv.getArgument(0);
			return bancoAberturas.values().stream().anyMatch(a -> a.getNumeroValor() == numero);
		});
		when(repositorio.buscarPorNumero(anyInt())).thenAnswer(inv -> {
			int numero = inv.getArgument(0);
			return bancoAberturas.values().stream().filter(a -> a.getNumeroValor() == numero).findFirst()
					.orElseThrow(() -> new AgregadoInexistenteExcecao(
							AberturaServico.NUMERO_INEXISTENTE_MODELO.formatted(numero)));
		});
		when(repositorio.possuiEscolhaPorUsuario(any(AberturaIdentificador.class)))
				.thenAnswer(inv -> aberturasEscolhidas.contains(inv.getArgument(0)));
		when(repositorio.listarTodas()).thenAnswer(inv -> bancoAberturas.values().stream()
				.sorted(Comparator.comparingInt(Abertura::getNumeroValor)).toList());
		when(repositorio.listarPorStatus(anyBoolean())).thenAnswer(inv -> {
			boolean ativa = inv.getArgument(0);
			return bancoAberturas.values().stream().filter(a -> a.isAtiva() == ativa)
					.sorted(Comparator.comparingInt(Abertura::getNumeroValor)).toList();
		});

		servico = new AberturaServico(segurancaServico, repositorio);
	}

	private void executarCapturando(Runnable acao) {
		ultimaExcecao = null;
		try {
			acao.run();
		} catch (Throwable ex) {
			ultimaExcecao = ex;
		}
	}

	private void cadastrarExistente(int numero, String nome, String artista, String episodios, boolean ativa) {
		var id = new AberturaIdentificador(Integer.toString(sequenciaId.incrementAndGet()));
		var abertura = new Abertura(Optional.of(id), numero, nome, artista, episodios,
				"https://api.animethemes.moe/img/" + numero + ".jpg",
				"https://v.animethemes.moe/OnePiece-OP" + numero + ".webm", ativa);
		bancoAberturas.put(id, abertura);
	}

	@Dado("que não existe nenhuma abertura cadastrada")
	public void que_nao_existe_nenhuma_abertura_cadastrada() {
		bancoAberturas.clear();
		aberturasEscolhidas.clear();
	}

	@Dado("que sou um usuário administrador")
	public void que_sou_um_usuario_administrador() {
		usuarioEhAdmin = true;
	}

	@Dado("que sou um usuário sem autorização de administrador")
	public void que_sou_um_usuario_sem_autorizacao_de_administrador() {
		usuarioEhAdmin = false;
	}

	@Dado("que existe a abertura número {int}, nome {string}, artista {string}, episódios {string} e status ativa")
	public void que_existe_a_abertura_numero_nome_artista_episodios_e_status_ativa(Integer numero, String nome,
			String artista, String episodios) {
		cadastrarExistente(numero, nome, artista, episodios, true);
	}

	@Dado("que existe a abertura número {int}, nome {string}, artista {string}, episódios {string} e status inativa")
	public void que_existe_a_abertura_numero_nome_artista_episodios_e_status_inativa(Integer numero, String nome,
			String artista, String episodios) {
		cadastrarExistente(numero, nome, artista, episodios, false);
	}

	@Dado("que a abertura número {int} já foi escolhida como preferida por usuários")
	public void que_a_abertura_numero_ja_foi_escolhida_como_preferida_por_usuarios(Integer numero) {
		var abertura = repositorio.buscarPorNumero(numero);
		aberturasEscolhidas.add(abertura.getIdentificador().orElseThrow());
	}

	@Dado("que a abertura número {int} nunca foi escolhida como preferida por nenhum usuário")
	public void que_a_abertura_numero_nunca_foi_escolhida_como_preferida_por_nenhum_usuario(Integer numero) {
		var abertura = repositorio.buscarPorNumero(numero);
		aberturasEscolhidas.remove(abertura.getIdentificador().orElseThrow());
	}

	@Quando("eu cadastro a abertura número {int}, nome {string}, artista {string}, episódios {string}, imagem {string}, vídeo {string} e status ativa")
	public void eu_cadastro_a_abertura_numero_nome_artista_episodios_imagem_video_e_status_ativa(Integer numero,
			String nome, String artista, String episodios, String imagem, String video) {
		executarCapturando(() -> ultimaAbertura = servico
				.incluir(new Abertura(Optional.empty(), numero, nome, artista, episodios, imagem, video, true)));
	}

	@Quando("eu cadastro a abertura número {int}, nome {string}, artista {string}, episódios {string}, imagem {string}, vídeo {string} e status inativa")
	public void eu_cadastro_a_abertura_numero_nome_artista_episodios_imagem_video_e_status_inativa(Integer numero,
			String nome, String artista, String episodios, String imagem, String video) {
		executarCapturando(() -> ultimaAbertura = servico
				.incluir(new Abertura(Optional.empty(), numero, nome, artista, episodios, imagem, video, false)));
	}

	@Quando("eu tento cadastrar a abertura número {int}, nome {string}, artista {string}, episódios {string}, imagem {string}, vídeo {string} e status ativa")
	public void eu_tento_cadastrar_a_abertura_numero_nome_artista_episodios_imagem_video_e_status_ativa(Integer numero,
			String nome, String artista, String episodios, String imagem, String video) {
		executarCapturando(() -> ultimaAbertura = servico
				.incluir(new Abertura(Optional.empty(), numero, nome, artista, episodios, imagem, video, true)));
	}

	@Quando("eu consulto a abertura número {int}")
	public void eu_consulto_a_abertura_numero(Integer numero) {
		executarCapturando(() -> ultimaAbertura = servico.buscarPorNumero(numero));
	}

	@Quando("eu tento consultar a abertura número {int}")
	public void eu_tento_consultar_a_abertura_numero(Integer numero) {
		executarCapturando(() -> ultimaAbertura = servico.buscarPorNumero(numero));
	}

	@Quando("eu filtro as aberturas administrativas pelo status inativa")
	public void eu_filtro_as_aberturas_administrativas_pelo_status_inativa() {
		executarCapturando(() -> ultimaListagem = servico.listarPorStatus(Optional.of(false)));
	}

	@Quando("eu edito a abertura número {int} para nome {string}, artista {string}, episódios {string} e status ativa")
	public void eu_edito_a_abertura_numero_para_nome_artista_episodios_e_status_ativa(Integer numero, String nome,
			String artista, String episodios) {
		executarCapturando(() -> {
			var existente = servico.buscarPorNumero(numero);
			var editada = existente.editar(existente.getNumero(), nome, artista, episodios, existente.getImagem(),
					existente.getVideo(), StatusAbertura.ATIVA);
			servico.alterar(editada);
			ultimaAbertura = editada;
		});
	}

	@Quando("eu tento editar a abertura número {int} para nome {string}, artista {string}, episódios {string} e status ativa")
	public void eu_tento_editar_a_abertura_numero_para_nome_artista_episodios_e_status_ativa(Integer numero,
			String nome, String artista, String episodios) {
		executarCapturando(() -> {
			var existente = servico.buscarPorNumero(numero);
			var editada = existente.editar(existente.getNumero(), nome, artista, episodios, existente.getImagem(),
					existente.getVideo(), StatusAbertura.ATIVA);
			servico.alterar(editada);
			ultimaAbertura = editada;
		});
	}

	@Quando("eu tento alterar o número da abertura {int} para o número {int}")
	public void eu_tento_alterar_o_numero_da_abertura_para_o_numero(Integer numeroAtual, Integer novoNumero) {
		executarCapturando(() -> {
			var existente = servico.buscarPorNumero(numeroAtual);
			var editada = existente.editar(new NumeroAbertura(novoNumero), existente.getNome(), existente.getArtista(),
					existente.getEpisodios(), existente.getImagem(), existente.getVideo(), existente.getStatus());
			servico.alterar(editada);
			ultimaAbertura = editada;
		});
	}

	@Quando("eu inativo a abertura número {int}")
	public void eu_inativo_a_abertura_numero(Integer numero) {
		executarCapturando(() -> ultimaAbertura = servico.inativarPorNumero(numero));
	}

	@Quando("eu tento inativar a abertura número {int}")
	public void eu_tento_inativar_a_abertura_numero(Integer numero) {
		executarCapturando(() -> ultimaAbertura = servico.inativarPorNumero(numero));
	}

	@Quando("eu reativo a abertura número {int}")
	public void eu_reativo_a_abertura_numero(Integer numero) {
		executarCapturando(() -> ultimaAbertura = servico.reativarPorNumero(numero));
	}

	@Quando("eu excluo a abertura número {int}")
	public void eu_excluo_a_abertura_numero(Integer numero) {
		executarCapturando(() -> servico.excluirPorNumero(numero));
	}

	@Quando("eu tento excluir a abertura número {int}")
	public void eu_tento_excluir_a_abertura_numero(Integer numero) {
		executarCapturando(() -> servico.excluirPorNumero(numero));
	}

	@Entao("a abertura número {int} é cadastrada com sucesso")
	public void a_abertura_numero_e_cadastrada_com_sucesso(Integer numero) {
		assertNull(ultimaExcecao, () -> "Erro inesperado: " + ultimaExcecao);
		assertNotNull(ultimaAbertura);
		assertEquals(numero, ultimaAbertura.getNumeroValor());
	}

	@Entao("a abertura número {int} passa a existir com status ativa")
	public void a_abertura_numero_passa_a_existir_com_status_ativa(Integer numero) {
		assertTrue(repositorio.existePorNumero(numero));
		assertTrue(repositorio.buscarPorNumero(numero).isAtiva());
	}

	@Entao("a abertura número {int} passa a existir com status inativa")
	public void a_abertura_numero_passa_a_existir_com_status_inativa(Integer numero) {
		assertTrue(repositorio.existePorNumero(numero));
		assertFalse(repositorio.buscarPorNumero(numero).isAtiva());
	}

	@Entao("o cadastro da abertura é rejeitado por falta de autorização")
	public void o_cadastro_da_abertura_e_rejeitado_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, ultimaExcecao);
	}

	@Entao("o cadastro da abertura é rejeitado porque já existe uma abertura com o número {int}")
	public void o_cadastro_da_abertura_e_rejeitado_porque_ja_existe_uma_abertura_com_o_numero(Integer numero) {
		assertInstanceOf(AgregadoExistenteExcecao.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("o cadastro da abertura é rejeitado por dados obrigatórios inválidos")
	public void o_cadastro_da_abertura_e_rejeitado_por_dados_obrigatorios_invalidos() {
		assertInstanceOf(IllegalArgumentException.class, ultimaExcecao);
	}

	@Entao("a abertura encontrada tem nome {string}, artista {string}, episódios {string} e status ativa")
	public void a_abertura_encontrada_tem_nome_artista_episodios_e_status_ativa(String nome, String artista,
			String episodios) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimaAbertura);
		assertEquals(nome, ultimaAbertura.getNome());
		assertEquals(artista, ultimaAbertura.getArtista());
		assertEquals(episodios, ultimaAbertura.getEpisodios());
		assertTrue(ultimaAbertura.isAtiva());
	}

	@Entao("a consulta da abertura é rejeitada porque não existe abertura com o número {int}")
	public void a_consulta_da_abertura_e_rejeitada_porque_nao_existe_abertura_com_o_numero(Integer numero) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("a listagem administrativa retorna apenas a abertura número {int}")
	public void a_listagem_administrativa_retorna_apenas_a_abertura_numero(Integer numero) {
		assertNull(ultimaExcecao);
		assertEquals(1, ultimaListagem.size());
		assertEquals(numero, ultimaListagem.get(0).getNumeroValor());
	}

	@Entao("a edição da abertura é concluída com sucesso")
	public void a_edicao_da_abertura_e_concluida_com_sucesso() {
		assertNull(ultimaExcecao);
	}

	@Entao("a abertura número {int} passa a ter nome {string} e episódios {string}")
	public void a_abertura_numero_passa_a_ter_nome_e_episodios(Integer numero, String nome, String episodios) {
		var atual = repositorio.buscarPorNumero(numero);
		assertEquals(nome, atual.getNome());
		assertEquals(episodios, atual.getEpisodios());
	}

	@Entao("a edição da abertura é rejeitada por falta de autorização")
	public void a_edicao_da_abertura_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, ultimaExcecao);
	}

	@Entao("a edição da abertura é rejeitada porque não existe abertura com o número {int}")
	public void a_edicao_da_abertura_e_rejeitada_porque_nao_existe_abertura_com_o_numero(Integer numero) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("a edição da abertura é rejeitada porque já existe uma abertura com o número {int}")
	public void a_edicao_da_abertura_e_rejeitada_porque_ja_existe_uma_abertura_com_o_numero(Integer numero) {
		assertInstanceOf(AgregadoExistenteExcecao.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("a abertura número {int} é inativada com sucesso")
	public void a_abertura_numero_e_inativada_com_sucesso(Integer numero) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimaAbertura);
		assertEquals(numero, ultimaAbertura.getNumeroValor());
	}

	@Entao("a abertura número {int} passa a ter status inativa")
	public void a_abertura_numero_passa_a_ter_status_inativa(Integer numero) {
		assertFalse(repositorio.buscarPorNumero(numero).isAtiva());
	}

	@Entao("a abertura número {int} é reativada com sucesso")
	public void a_abertura_numero_e_reativada_com_sucesso(Integer numero) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimaAbertura);
		assertEquals(numero, ultimaAbertura.getNumeroValor());
	}

	@Entao("a abertura número {int} passa a ter status ativa")
	public void a_abertura_numero_passa_a_ter_status_ativa(Integer numero) {
		assertTrue(repositorio.buscarPorNumero(numero).isAtiva());
	}

	@Entao("a inativação da abertura é rejeitada por falta de autorização")
	public void a_inativacao_da_abertura_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, ultimaExcecao);
	}

	@Entao("a inativação da abertura é rejeitada porque não existe abertura com o número {int}")
	public void a_inativacao_da_abertura_e_rejeitada_porque_nao_existe_abertura_com_o_numero(Integer numero) {
		assertInstanceOf(AgregadoInexistenteExcecao.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
	}

	@Entao("a abertura número {int} é excluída com sucesso")
	public void a_abertura_numero_e_excluida_com_sucesso(Integer numero) {
		assertNull(ultimaExcecao);
		assertFalse(repositorio.existePorNumero(numero));
	}

	@Entao("a abertura número {int} deixa de existir")
	public void a_abertura_numero_deixa_de_existir(Integer numero) {
		assertFalse(repositorio.existePorNumero(numero));
	}

	@Entao("a exclusão da abertura é rejeitada porque a abertura número {int} já foi escolhida por usuários e só pode ser inativada")
	public void a_exclusao_da_abertura_e_rejeitada_porque_a_abertura_numero_ja_foi_escolhida_por_usuarios_e_so_pode_ser_inativada(
			Integer numero) {
		assertInstanceOf(IllegalStateException.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(numero)));
		assertTrue(ultimaExcecao.getMessage().toLowerCase().contains("inativar"));
	}

	@Entao("a abertura número {int} continua a existir")
	public void a_abertura_numero_continua_a_existir(Integer numero) {
		assertTrue(repositorio.existePorNumero(numero));
	}

	@Entao("a exclusão da abertura é rejeitada por falta de autorização")
	public void a_exclusao_da_abertura_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, ultimaExcecao);
	}
}
