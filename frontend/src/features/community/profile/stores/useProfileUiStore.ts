import { create } from 'zustand';

export type AbaComunidade =
  | 'meu-perfil'
  | 'decks-favoritos'
  | 'avaliacoes'
  | 'rede-amigos'
  | 'buscar-colecionadores'
  | 'abertura-preferida';
export type SubAbaAmizade = 'meus-amigos' | 'recebidas' | 'enviadas';

export interface AlvoRemocaoAmizade {
  id: string;
  apelido: string;
}

export interface EstadoPerfilUi {
  abaAtiva: AbaComunidade;
  subAbaAmizade: SubAbaAmizade;
  modalEdicaoAberto: boolean;
  modalSlotAberto: boolean;
  slotSelecionado: number | null;
  alvoRemocaoAmizade: AlvoRemocaoAmizade | null;
  filtroColecionadores: string;
  filtroColecaoSlot: string;
  filtroAmigosRede: string;
  definirAbaAtiva: (aba: AbaComunidade) => void;
  definirSubAbaAmizade: (subAba: SubAbaAmizade) => void;
  abrirModalEdicao: () => void;
  fecharModalEdicao: () => void;
  abrirModalSlot: (indiceSlot?: number | null) => void;
  fecharModalSlot: () => void;
  abrirConfirmacaoRemocao: (alvo: AlvoRemocaoAmizade) => void;
  fecharConfirmacaoRemocao: () => void;
  definirFiltroColecionadores: (termo: string) => void;
  definirFiltroColecaoSlot: (termo: string) => void;
  definirFiltroAmigosRede: (termo: string) => void;
  resetarEstadoUi: () => void;
}

const estadoInicial = {
  abaAtiva: 'meu-perfil' as AbaComunidade,
  subAbaAmizade: 'meus-amigos' as SubAbaAmizade,
  modalEdicaoAberto: false,
  modalSlotAberto: false,
  slotSelecionado: null as number | null,
  alvoRemocaoAmizade: null as AlvoRemocaoAmizade | null,
  filtroColecionadores: '',
  filtroColecaoSlot: '',
  filtroAmigosRede: '',
};

/**
 * Store Zustand estritamente para estado efêmero de interface de F001 (abas, modais e filtros).
 * Todo estado remoto permanece no TanStack Query (`clienteConsultas`).
 */
export const useProfileUiStore = create<EstadoPerfilUi>((set) => ({
  ...estadoInicial,
  definirAbaAtiva: (abaAtiva) => set({ abaAtiva }),
  definirSubAbaAmizade: (subAbaAmizade) => set({ subAbaAmizade }),
  abrirModalEdicao: () => set({ modalEdicaoAberto: true }),
  fecharModalEdicao: () => set({ modalEdicaoAberto: false }),
  abrirModalSlot: (indiceSlot = null) =>
    set({ modalSlotAberto: true, slotSelecionado: indiceSlot, filtroColecaoSlot: '' }),
  fecharModalSlot: () => set({ modalSlotAberto: false, slotSelecionado: null, filtroColecaoSlot: '' }),
  abrirConfirmacaoRemocao: (alvoRemocaoAmizade) => set({ alvoRemocaoAmizade }),
  fecharConfirmacaoRemocao: () => set({ alvoRemocaoAmizade: null }),
  definirFiltroColecionadores: (filtroColecionadores) => set({ filtroColecionadores }),
  definirFiltroColecaoSlot: (filtroColecaoSlot) => set({ filtroColecaoSlot }),
  definirFiltroAmigosRede: (filtroAmigosRede) => set({ filtroAmigosRede }),
  resetarEstadoUi: () => set(estadoInicial),
}));
