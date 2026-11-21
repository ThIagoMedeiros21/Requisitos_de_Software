import { create } from 'zustand';

export type FiltroTipoComposicao = 'TODOS' | 'Leader' | 'Character' | 'Event' | 'Stage';
export type OrdenacaoComposicao = 'TIPO' | 'CUSTO';

export interface EstadoDecksUi {
  modalAdicionarAberto: boolean;
  deckIdComposicaoAberta: string | null;
  termoBuscaCatalogo: string;
  corFiltroCatalogo: string;
  filtroTipoComposicao: FiltroTipoComposicao;
  ordenacaoComposicao: OrdenacaoComposicao;
  abrirModalAdicionar: () => void;
  fecharModalAdicionar: () => void;
  abrirComposicao: (deckId: string) => void;
  fecharComposicao: () => void;
  definirTermoBuscaCatalogo: (termo: string) => void;
  definirCorFiltroCatalogo: (cor: string) => void;
  definirFiltroTipoComposicao: (tipo: FiltroTipoComposicao) => void;
  definirOrdenacaoComposicao: (ordenacao: OrdenacaoComposicao) => void;
  resetarEstadoUi: () => void;
}

const estadoInicial = {
  modalAdicionarAberto: false,
  deckIdComposicaoAberta: null as string | null,
  termoBuscaCatalogo: '',
  corFiltroCatalogo: '',
  filtroTipoComposicao: 'TODOS' as FiltroTipoComposicao,
  ordenacaoComposicao: 'TIPO' as OrdenacaoComposicao,
};

/**
 * Store Zustand estritamente para estado efêmero de UI da F002 (modais e filtros).
 * Dados remotos de decks e composição permanecem 100% no TanStack Query.
 */
export const useDecksUiStore = create<EstadoDecksUi>((set) => ({
  ...estadoInicial,
  abrirModalAdicionar: () =>
    set({ modalAdicionarAberto: true, termoBuscaCatalogo: '', corFiltroCatalogo: '' }),
  fecharModalAdicionar: () =>
    set({ modalAdicionarAberto: false, termoBuscaCatalogo: '', corFiltroCatalogo: '' }),
  abrirComposicao: (deckId) =>
    set({
      deckIdComposicaoAberta: deckId,
      filtroTipoComposicao: 'TODOS',
      ordenacaoComposicao: 'TIPO',
    }),
  fecharComposicao: () => set({ deckIdComposicaoAberta: null }),
  definirTermoBuscaCatalogo: (termoBuscaCatalogo) => set({ termoBuscaCatalogo }),
  definirCorFiltroCatalogo: (corFiltroCatalogo) => set({ corFiltroCatalogo }),
  definirFiltroTipoComposicao: (filtroTipoComposicao) => set({ filtroTipoComposicao }),
  definirOrdenacaoComposicao: (ordenacaoComposicao) => set({ ordenacaoComposicao }),
  resetarEstadoUi: () => set(estadoInicial),
}));
