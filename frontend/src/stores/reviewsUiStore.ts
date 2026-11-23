import { create } from 'zustand';

export type FiltroElegibilidadeAvaliacao = 'todas' | 'elegiveis' | 'avaliadas';

export interface EstadoReviewsUi {
  termoBusca: string;
  filtroElegibilidade: FiltroElegibilidadeAvaliacao;
  produtoModalAberto: string | null;
  modalAvaliacaoAberto: boolean;
  notaHover: number | null;
  painelPedidosAberto: boolean;
  definirTermoBusca: (termo: string) => void;
  definirFiltroElegibilidade: (filtro: FiltroElegibilidadeAvaliacao) => void;
  abrirModalAvaliacao: (produtoId: string) => void;
  fecharModalAvaliacao: () => void;
  definirNotaHover: (nota: number | null) => void;
  alternarPainelPedidos: () => void;
  resetarEstadoUi: () => void;
}

const estadoInicial = {
  termoBusca: '',
  filtroElegibilidade: 'todas' as FiltroElegibilidadeAvaliacao,
  produtoModalAberto: null as string | null,
  modalAvaliacaoAberto: false,
  notaHover: null as number | null,
  painelPedidosAberto: false,
};

/**
 * Store Zustand estritamente para estado efêmero de UI da F003 (filtros, modal de avaliação,
 * hover de estrelas e painel colapsável de pedidos demonstrativos).
 */
export const useReviewsUiStore = create<EstadoReviewsUi>((set) => ({
  ...estadoInicial,
  definirTermoBusca: (termoBusca) => set({ termoBusca }),
  definirFiltroElegibilidade: (filtroElegibilidade) => set({ filtroElegibilidade }),
  abrirModalAvaliacao: (produtoModalAberto) =>
    set({ produtoModalAberto, modalAvaliacaoAberto: true, notaHover: null }),
  fecharModalAvaliacao: () =>
    set({ produtoModalAberto: null, modalAvaliacaoAberto: false, notaHover: null }),
  definirNotaHover: (notaHover) => set({ notaHover }),
  alternarPainelPedidos: () =>
    set((estado) => ({ painelPedidosAberto: !estado.painelPedidosAberto })),
  resetarEstadoUi: () => set(estadoInicial),
}));
