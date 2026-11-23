import { useReviewsUiStore } from './reviewsUiStore';

describe('useReviewsUiStore', () => {
  beforeEach(() => {
    useReviewsUiStore.getState().resetarEstadoUi();
  });

  it('controla busca, filtro de elegibilidade e modal de avaliação', () => {
    useReviewsUiStore.getState().definirTermoBusca('Luffy');
    useReviewsUiStore.getState().definirFiltroElegibilidade('elegiveis');
    useReviewsUiStore.getState().abrirModalAvaliacao('OP05-119');
    useReviewsUiStore.getState().definirNotaHover(4);

    const estado = useReviewsUiStore.getState();
    expect(estado.termoBusca).toBe('Luffy');
    expect(estado.filtroElegibilidade).toBe('elegiveis');
    expect(estado.produtoModalAberto).toBe('OP05-119');
    expect(estado.modalAvaliacaoAberto).toBe(true);
    expect(estado.notaHover).toBe(4);

    estado.fecharModalAvaliacao();
    expect(useReviewsUiStore.getState().modalAvaliacaoAberto).toBe(false);
    expect(useReviewsUiStore.getState().produtoModalAberto).toBeNull();
    expect(useReviewsUiStore.getState().notaHover).toBeNull();
  });

  it('alterna painel demonstrativo de pedidos e reseta estado completo', () => {
    expect(useReviewsUiStore.getState().painelPedidosAberto).toBe(false);
    useReviewsUiStore.getState().alternarPainelPedidos();
    expect(useReviewsUiStore.getState().painelPedidosAberto).toBe(true);

    useReviewsUiStore.getState().resetarEstadoUi();
    expect(useReviewsUiStore.getState().painelPedidosAberto).toBe(false);
    expect(useReviewsUiStore.getState().filtroElegibilidade).toBe('todas');
  });
});
