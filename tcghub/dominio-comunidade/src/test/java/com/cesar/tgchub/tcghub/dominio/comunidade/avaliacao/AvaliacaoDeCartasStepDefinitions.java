package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacao;

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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoElegibilidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.IAvaliacaoElegibilidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.NotaAvaliacao;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.ResumoAvaliacoesProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.spi.IAvaliacaoProdutoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi.ICompraVerificadaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AvaliacaoDeCartasStepDefinitions {
	private final Map<CompraVerificadaIdentificador, CompraVerificada> bancoCompras = new LinkedHashMap<>();
	private final Map<AvaliacaoProdutoIdentificador, AvaliacaoProduto> bancoAvaliacoes = new LinkedHashMap<>();
	private final AtomicInteger seqCompra = new AtomicInteger(0);
	private final AtomicInteger seqAvaliacao = new AtomicInteger(0);

	private ISegurancaServico segurancaServico;
	private ICompraVerificadaRepositorio compraRepositorio;
	private IAvaliacaoProdutoRepositorio avaliacaoRepositorio;
	private CompraVerificadaServico compraServico;
	private AvaliacaoProdutoServico avaliacaoServico;
	private IAvaliacaoElegibilidadeProxy proxy;

	private String usuarioAtual;
	private ProdutoIdentificador produtoContexto;
	private AvaliacaoProduto ultimaAvaliacao;
	private ResumoAvaliacoesProduto ultimoResumo;
	private List<AvaliacaoProduto> ultimasAvaliacoesValidas;
	private Throwable ultimaExcecao;

	@Before
	public void configurarCenario() {
		bancoCompras.clear();
		bancoAvaliacoes.clear();
		seqCompra.set(0);
		seqAvaliacao.set(0);
		usuarioAtual = "lucas";
		produtoContexto = new ProdutoIdentificador("OP05-119");
		ultimaAvaliacao = null;
		ultimoResumo = null;
		ultimasAvaliacoesValidas = List.of();
		ultimaExcecao = null;

		segurancaServico = mock(ISegurancaServico.class);
		when(segurancaServico.getUsuario()).thenAnswer(inv -> usuarioAtual);
		when(segurancaServico.possuiAutorizacao(Autorizacao.USUARIO.name())).thenReturn(true);
		when(segurancaServico.possuiAutorizacao(Autorizacao.ADMINISTRADOR.name())).thenReturn(false);

		compraRepositorio = mock(ICompraVerificadaRepositorio.class);
		when(compraRepositorio.existePorIdentificador(any(CompraVerificadaIdentificador.class)))
				.thenAnswer(inv -> bancoCompras.containsKey(inv.getArgument(0)));
		when(compraRepositorio.buscarPorIdentificador(any(CompraVerificadaIdentificador.class))).thenAnswer(inv -> {
			CompraVerificadaIdentificador id = inv.getArgument(0);
			var c = bancoCompras.get(id);
			if (c == null) {
				throw new AgregadoInexistenteExcecao(
						CompraVerificadaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return c;
		});
		when(compraRepositorio.incluir(any(CompraVerificada.class))).thenAnswer(inv -> {
			CompraVerificada entrada = inv.getArgument(0);
			var id = new CompraVerificadaIdentificador(Integer.toString(seqCompra.incrementAndGet()));
			var salva = new CompraVerificada(Optional.of(id), entrada.getUsuario(), entrada.getProduto(),
					entrada.getPedido(), entrada.isPaga(), entrada.isReembolsada(), entrada.getAtualizadoEm());
			bancoCompras.put(id, salva);
			return salva;
		});
		doAnswer(inv -> {
			CompraVerificada alterada = inv.getArgument(0);
			bancoCompras.put(alterada.getIdentificador().orElseThrow(), alterada);
			return null;
		}).when(compraRepositorio).alterar(any(CompraVerificada.class));
		when(compraRepositorio.existeCompraElegivel(any(UsuarioIdentificador.class), any(ProdutoIdentificador.class)))
				.thenAnswer(inv -> {
					UsuarioIdentificador u = inv.getArgument(0);
					ProdutoIdentificador p = inv.getArgument(1);
					return bancoCompras.values().stream()
							.anyMatch(c -> c.getUsuario().equals(u) && c.getProduto().equals(p) && c.isElegivel());
				});
		when(compraRepositorio.existePorUsuarioEPedidoEProduto(any(UsuarioIdentificador.class),
				any(PedidoIdentificador.class), any(ProdutoIdentificador.class))).thenAnswer(inv -> {
					UsuarioIdentificador u = inv.getArgument(0);
					PedidoIdentificador ped = inv.getArgument(1);
					ProdutoIdentificador prod = inv.getArgument(2);
					return bancoCompras.values().stream().anyMatch(c -> c.getUsuario().equals(u)
							&& c.getPedido().equals(ped) && c.getProduto().equals(prod));
				});
		when(compraRepositorio.buscarPorUsuarioEPedidoEProduto(any(UsuarioIdentificador.class),
				any(PedidoIdentificador.class), any(ProdutoIdentificador.class))).thenAnswer(inv -> {
					UsuarioIdentificador u = inv.getArgument(0);
					PedidoIdentificador ped = inv.getArgument(1);
					ProdutoIdentificador prod = inv.getArgument(2);
					return bancoCompras.values().stream()
							.filter(c -> c.getUsuario().equals(u) && c.getPedido().equals(ped)
									&& c.getProduto().equals(prod))
							.findFirst().orElseThrow(() -> new AgregadoInexistenteExcecao(
									CompraVerificadaServico.COMPRA_INEXISTENTE_MODELO.formatted(u, ped, prod)));
				});
		when(compraRepositorio.listarPorUsuarioEProduto(any(UsuarioIdentificador.class),
				any(ProdutoIdentificador.class))).thenAnswer(inv -> {
					UsuarioIdentificador u = inv.getArgument(0);
					ProdutoIdentificador p = inv.getArgument(1);
					return bancoCompras.values().stream()
							.filter(c -> c.getUsuario().equals(u) && c.getProduto().equals(p)).toList();
				});
		when(compraRepositorio.listarPorUsuario(any(UsuarioIdentificador.class))).thenAnswer(inv -> {
			UsuarioIdentificador u = inv.getArgument(0);
			return bancoCompras.values().stream().filter(c -> c.getUsuario().equals(u)).toList();
		});

		avaliacaoRepositorio = mock(IAvaliacaoProdutoRepositorio.class);
		when(avaliacaoRepositorio.existePorIdentificador(any(AvaliacaoProdutoIdentificador.class)))
				.thenAnswer(inv -> bancoAvaliacoes.containsKey(inv.getArgument(0)));
		when(avaliacaoRepositorio.buscarPorIdentificador(any(AvaliacaoProdutoIdentificador.class))).thenAnswer(inv -> {
			AvaliacaoProdutoIdentificador id = inv.getArgument(0);
			var av = bancoAvaliacoes.get(id);
			if (av == null) {
				throw new AgregadoInexistenteExcecao(
						AvaliacaoProdutoServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(id));
			}
			return av;
		});
		when(avaliacaoRepositorio.incluir(any(AvaliacaoProduto.class))).thenAnswer(inv -> {
			AvaliacaoProduto entrada = inv.getArgument(0);
			var id = new AvaliacaoProdutoIdentificador(Integer.toString(seqAvaliacao.incrementAndGet()));
			var salva = new AvaliacaoProduto(Optional.of(id), entrada.getUsuario(), entrada.getProduto(),
					entrada.getNota(), entrada.getComentario(), entrada.isCompraVerificada(), entrada.isValida(),
					entrada.getAvaliadoEm());
			bancoAvaliacoes.put(id, salva);
			return salva;
		});
		doAnswer(inv -> {
			AvaliacaoProduto alterada = inv.getArgument(0);
			bancoAvaliacoes.put(alterada.getIdentificador().orElseThrow(), alterada);
			return null;
		}).when(avaliacaoRepositorio).alterar(any(AvaliacaoProduto.class));
		when(avaliacaoRepositorio.existePorUsuarioEProduto(any(UsuarioIdentificador.class),
				any(ProdutoIdentificador.class))).thenAnswer(inv -> {
					UsuarioIdentificador u = inv.getArgument(0);
					ProdutoIdentificador p = inv.getArgument(1);
					return bancoAvaliacoes.values().stream()
							.anyMatch(a -> a.getUsuario().equals(u) && a.getProduto().equals(p));
				});
		when(avaliacaoRepositorio.buscarPorUsuarioEProduto(any(UsuarioIdentificador.class),
				any(ProdutoIdentificador.class))).thenAnswer(inv -> {
					UsuarioIdentificador u = inv.getArgument(0);
					ProdutoIdentificador p = inv.getArgument(1);
					return bancoAvaliacoes.values().stream()
							.filter(a -> a.getUsuario().equals(u) && a.getProduto().equals(p)).findFirst()
							.orElseThrow(() -> new AgregadoInexistenteExcecao("Avaliação não encontrada"));
				});
		when(avaliacaoRepositorio.listarValidasPorProduto(any(ProdutoIdentificador.class))).thenAnswer(inv -> {
			ProdutoIdentificador p = inv.getArgument(0);
			return bancoAvaliacoes.values().stream().filter(a -> a.getProduto().equals(p) && a.isValida()).toList();
		});

		compraServico = new CompraVerificadaServico(segurancaServico, compraRepositorio);
		avaliacaoServico = new AvaliacaoProdutoServico(segurancaServico, avaliacaoRepositorio, compraRepositorio,
				compraServico);
		proxy = new AvaliacaoElegibilidadeProxy(avaliacaoServico, compraRepositorio);
	}

	private void executarCapturando(Runnable acao) {
		ultimaExcecao = null;
		try {
			acao.run();
		} catch (Throwable ex) {
			ultimaExcecao = ex;
		}
	}

	private void registrarCompra(String usuario, String pedido, String produto, boolean paga, boolean reembolsada) {
		var id = new CompraVerificadaIdentificador(Integer.toString(seqCompra.incrementAndGet()));
		bancoCompras.put(id, new CompraVerificada(Optional.of(id), new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto), new PedidoIdentificador(pedido), paga, reembolsada, Instant.now()));
	}

	@Dado("que existe o produto de carta {string} de nome {string}")
	public void que_existe_o_produto_de_carta_de_nome(String produtoId, String nome) {
		produtoContexto = new ProdutoIdentificador(produtoId);
	}

	@Dado("que não existe nenhuma avaliação cadastrada para a carta {string}")
	public void que_nao_existe_nenhuma_avaliacao_cadastrada_para_a_carta(String produtoId) {
		var prod = new ProdutoIdentificador(produtoId);
		bancoAvaliacoes.entrySet().removeIf(e -> e.getValue().getProduto().equals(prod));
	}

	@Dado("que o usuário {string} possui o pedido {string} pago e não reembolsado contendo a carta {string}")
	public void que_o_usuario_possui_o_pedido_pago_e_nao_reembolsado_contendo_a_carta(String usuario, String pedido,
			String produto) {
		registrarCompra(usuario, pedido, produto, true, false);
	}

	@Dado("que o usuário {string} não possui nenhum pedido contendo a carta {string}")
	public void que_o_usuario_nao_possui_nenhum_pedido_contendo_a_carta(String usuario, String produto) {
		var u = new UsuarioIdentificador(usuario);
		var p = new ProdutoIdentificador(produto);
		bancoCompras.entrySet().removeIf(e -> e.getValue().getUsuario().equals(u) && e.getValue().getProduto().equals(p));
	}

	@Dado("que o usuário {string} possui apenas o pedido {string} com pagamento pendente contendo a carta {string}")
	public void que_o_usuario_possui_apenas_o_pedido_com_pagamento_pendente_contendo_a_carta(String usuario,
			String pedido, String produto) {
		que_o_usuario_nao_possui_nenhum_pedido_contendo_a_carta(usuario, produto);
		registrarCompra(usuario, pedido, produto, false, false);
	}

	@Dado("que o usuário {string} possui apenas o pedido {string} reembolsado contendo a carta {string}")
	public void que_o_usuario_possui_apenas_o_pedido_reembolsado_contendo_a_carta(String usuario, String pedido,
			String produto) {
		que_o_usuario_nao_possui_nenhum_pedido_contendo_a_carta(usuario, produto);
		registrarCompra(usuario, pedido, produto, true, true);
	}

	@Dado("que o usuário {string} possui o pedido {string} reembolsado contendo a carta {string}")
	public void que_o_usuario_possui_o_pedido_reembolsado_contendo_a_carta(String usuario, String pedido,
			String produto) {
		registrarCompra(usuario, pedido, produto, true, true);
	}

	@Dado("que o usuário {string} também possui o pedido {string} pago e não reembolsado contendo a carta {string}")
	public void que_o_usuario_tambem_possui_o_pedido_pago_e_nao_reembolsado_contendo_a_carta(String usuario,
			String pedido, String produto) {
		registrarCompra(usuario, pedido, produto, true, false);
	}

	@Dado("que o usuário {string} já avaliou a carta {string} com nota {int} e comentário {string}")
	public void que_o_usuario_ja_avaliou_a_carta_com_nota_e_comentario(String usuario, String produto, Integer nota,
			String comentario) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		ultimaAvaliacao = proxy.registrarOuAtualizarAvaliacao(u, u, new ProdutoIdentificador(produto),
				new NotaAvaliacao(nota), Optional.of(comentario));
	}

	@Dado("que o usuário {string} avaliou a carta {string} com nota {int} e comentário {string}")
	public void que_o_usuario_avaliou_a_carta_com_nota_e_comentario(String usuario, String produto, Integer nota,
			String comentario) {
		que_o_usuario_ja_avaliou_a_carta_com_nota_e_comentario(usuario, produto, nota, comentario);
	}

	@Quando("o usuário {string} avalia a carta {string} com nota {int} e comentário {string}")
	public void o_usuario_avalia_a_carta_com_nota_e_comentario(String usuario, String produto, Integer nota,
			String comentario) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> ultimaAvaliacao = proxy.registrarOuAtualizarAvaliacao(u, u,
				new ProdutoIdentificador(produto), new NotaAvaliacao(nota), Optional.of(comentario)));
	}

	@Quando("o usuário {string} avalia a carta {string} com nota {int} e sem comentário")
	public void o_usuario_avalia_a_carta_com_nota_e_sem_comentario(String usuario, String produto, Integer nota) {
		usuarioAtual = usuario;
		var u = new UsuarioIdentificador(usuario);
		executarCapturando(() -> ultimaAvaliacao = proxy.registrarOuAtualizarAvaliacao(u, u,
				new ProdutoIdentificador(produto), new NotaAvaliacao(nota), Optional.empty()));
	}

	@Quando("o usuário {string} tenta avaliar a carta {string} com nota {int} e comentário {string}")
	public void o_usuario_tenta_avaliar_a_carta_com_nota_e_comentario(String usuario, String produto, Integer nota,
			String comentario) {
		o_usuario_avalia_a_carta_com_nota_e_comentario(usuario, produto, nota, comentario);
	}

	@Quando("o usuário {string} reavalia a carta {string} com nota {int} e comentário {string}")
	public void o_usuario_reavalia_a_carta_com_nota_e_comentario(String usuario, String produto, Integer nota,
			String comentario) {
		o_usuario_avalia_a_carta_com_nota_e_comentario(usuario, produto, nota, comentario);
	}

	@Quando("o usuário {string} inicia a edição da avaliação para nota {int} e cancela a alteração")
	public void o_usuario_inicia_a_edicao_da_avaliacao_para_nota_e_cancela_a_alteracao(String usuario, Integer nota) {
		// Cancelar edição não invoca persistência — rascunho é descartado preservando estado anterior
		new NotaAvaliacao(nota);
		ultimaExcecao = null;
	}

	@Quando("um visitante consulta as avaliações da carta {string} no catálogo")
	public void um_visitante_consulta_as_avaliacoes_da_carta_no_catalogo(String produto) {
		var p = new ProdutoIdentificador(produto);
		executarCapturando(() -> {
			ultimoResumo = avaliacaoServico.consultarResumo(p);
			ultimasAvaliacoesValidas = avaliacaoServico.listarAvaliacoesValidas(p);
		});
	}

	@Quando("o pedido {string} do usuário {string} é reembolsado")
	public void o_pedido_do_usuario_e_reembolsado(String pedido, String usuario) {
		usuarioAtual = usuario;
		executarCapturando(() -> ultimoResumo = avaliacaoServico.invalidarPorReembolso(
				new UsuarioIdentificador(usuario), new PedidoIdentificador(pedido), produtoContexto));
	}

	@Quando("o usuário {string} tenta alterar diretamente a avaliação do usuário {string} para a carta {string}")
	public void o_usuario_tenta_alterar_diretamente_a_avaliacao_do_usuario_para_a_carta(String solicitante,
			String autor, String produto) {
		usuarioAtual = solicitante;
		executarCapturando(() -> proxy.registrarOuAtualizarAvaliacao(new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(autor), new ProdutoIdentificador(produto), new NotaAvaliacao(1),
				Optional.of("Alteração indevida")));
	}

	@Entao("a avaliação da carta {string} pelo usuário {string} é registrada com sucesso")
	public void a_avaliacao_da_carta_pelo_usuario_e_registrada_com_sucesso(String produto, String usuario) {
		assertNull(ultimaExcecao, () -> "Erro inesperado: " + ultimaExcecao);
		assertTrue(avaliacaoRepositorio.existePorUsuarioEProduto(new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto)));
		ultimaAvaliacao = avaliacaoRepositorio.buscarPorUsuarioEProduto(new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto));
		assertTrue(ultimaAvaliacao.isValida());
	}

	@Entao("a avaliação exibe o selo {string}")
	public void a_avaliacao_exibe_o_selo(String seloEsperado) {
		assertNotNull(ultimaAvaliacao);
		assertTrue(ultimaAvaliacao.isCompraVerificada());
		assertEquals(seloEsperado, ultimaAvaliacao.getSelo());
	}

	@Entao("^o resumo da carta \"([^\"]*)\" passa a exibir média ([0-9.]+) e (\\d+) avaliação válida$")
	public void o_resumo_da_carta_passa_a_exibir_media_e_avaliacao_valida(String produto, String mediaEsperada,
			Integer quantidade) {
		var resumo = avaliacaoServico.consultarResumo(new ProdutoIdentificador(produto));
		assertEquals(0, new BigDecimal(mediaEsperada).compareTo(resumo.getMedia()));
		assertEquals(quantidade, resumo.getQuantidadeValida());
	}

	@Entao("a avaliação é rejeitada porque o usuário {string} não possui compra paga e não reembolsada da carta {string}")
	public void a_avaliacao_e_rejeitada_porque_o_usuario_nao_possui_compra_paga_e_nao_reembolsada_da_carta(
			String usuario, String produto) {
		assertInstanceOf(IllegalStateException.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(usuario));
		assertTrue(ultimaExcecao.getMessage().contains(produto));
	}

	@Entao("a avaliação é rejeitada porque a nota deve estar entre {int} e {int}")
	public void a_avaliacao_e_rejeitada_porque_a_nota_deve_estar_entre_e(Integer min, Integer max) {
		assertInstanceOf(IllegalArgumentException.class, ultimaExcecao);
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(min)));
		assertTrue(ultimaExcecao.getMessage().contains(Integer.toString(max)));
	}

	@Entao("a avaliação da carta {string} pelo usuário {string} é atualizada com sucesso para nota {int} e comentário {string}")
	public void a_avaliacao_da_carta_pelo_usuario_e_atualizada_com_sucesso_para_nota_e_comentario(String produto,
			String usuario, Integer nota, String comentario) {
		assertNull(ultimaExcecao);
		var atual = avaliacaoRepositorio.buscarPorUsuarioEProduto(new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto));
		assertEquals(nota, atual.getNotaValor());
		assertEquals(Optional.of(comentario), atual.getComentario());
	}

	@Entao("a avaliação da carta {string} pelo usuário {string} permanece com nota {int} e comentário {string}")
	public void a_avaliacao_da_carta_pelo_usuario_permanece_com_nota_e_comentario(String produto, String usuario,
			Integer nota, String comentario) {
		var atual = avaliacaoRepositorio.buscarPorUsuarioEProduto(new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto));
		assertEquals(nota, atual.getNotaValor());
		assertEquals(Optional.of(comentario), atual.getComentario());
	}

	@Entao("^o resumo exibido apresenta média ([0-9.]+) e (\\d+) avaliações válidas$")
	public void o_resumo_exibido_apresenta_media_e_avaliacoes_validas(String mediaEsperada, Integer quantidade) {
		assertNull(ultimaExcecao);
		assertNotNull(ultimoResumo);
		assertEquals(0, new BigDecimal(mediaEsperada).compareTo(ultimoResumo.getMedia()));
		assertEquals(quantidade, ultimoResumo.getQuantidadeValida());
	}

	@Entao("todas as avaliações exibidas possuem o selo {string}")
	public void todas_as_avaliacoes_exibidas_possuem_o_selo(String seloEsperado) {
		assertFalse(ultimasAvaliacoesValidas.isEmpty());
		assertTrue(ultimasAvaliacoesValidas.stream().allMatch(a -> seloEsperado.equals(a.getSelo())));
	}

	@Entao("a avaliação do usuário {string} para a carta {string} é invalidada")
	public void a_avaliacao_do_usuario_para_a_carta_e_invalidada(String usuario, String produto) {
		assertNull(ultimaExcecao);
		var av = avaliacaoRepositorio.buscarPorUsuarioEProduto(new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto));
		assertFalse(av.isValida());
	}

	@Entao("a avaliação do usuário {string} para a carta {string} continua válida com o selo {string}")
	public void a_avaliacao_do_usuario_para_a_carta_continua_valida_com_o_selo(String usuario, String produto,
			String seloEsperado) {
		assertNull(ultimaExcecao);
		var av = avaliacaoRepositorio.buscarPorUsuarioEProduto(new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto));
		assertTrue(av.isValida());
		assertEquals(seloEsperado, av.getSelo());
	}

	@Entao("^o resumo da carta \"([^\"]*)\" permanece com média ([0-9.]+) e (\\d+) avaliação válida$")
	public void o_resumo_da_carta_permanece_com_media_e_avaliacao_valida(String produto, String mediaEsperada,
			Integer quantidade) {
		var resumo = avaliacaoServico.consultarResumo(new ProdutoIdentificador(produto));
		assertEquals(0, new BigDecimal(mediaEsperada).compareTo(resumo.getMedia()));
		assertEquals(quantidade, resumo.getQuantidadeValida());
	}

	@Entao("a alteração da avaliação é rejeitada por falta de autorização")
	public void a_alteracao_da_avaliacao_e_rejeitada_por_falta_de_autorizacao() {
		assertInstanceOf(NaoAutorizadoExcecao.class, ultimaExcecao);
	}
}
