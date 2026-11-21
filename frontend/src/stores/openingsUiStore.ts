import { create } from 'zustand';
import type { AberturaAdminDto } from '../features/adm/openings/types/adminOpenings.types';

export type FiltroComunidadeAbertura = 'todas' | 'amigos' | 'ranking';
export type FiltroStatusAdminAbertura = 'TODAS' | 'ATIVAS' | 'INATIVAS';

export interface EstadoOpeningsUi {
  // Comunidade (F004)
  termoBusca: string;
  filtroComunidade: FiltroComunidadeAbertura;
  aberturaDetalheId: string | null;
  modalDetalhesAberto: boolean;
  // Administrativo (adm/F001)
  termoBuscaAdmin: string;
  filtroStatusAdmin: FiltroStatusAdminAbertura;
  modalAdminAberto: boolean;
  aberturaEmEdicao: AberturaAdminDto | null;
  alvoExclusaoAdmin: AberturaAdminDto | null;
  // Ações Comunidade
  definirTermoBusca: (termo: string) => void;
  definirFiltroComunidade: (filtro: FiltroComunidadeAbertura) => void;
  abrirDetalhesAbertura: (aberturaId: string) => void;
  fecharDetalhesAbertura: () => void;
  // Ações Admin
  definirTermoBuscaAdmin: (termo: string) => void;
  definirFiltroStatusAdmin: (status: FiltroStatusAdminAbertura) => void;
  abrirModalNovaAbertura: () => void;
  abrirModalEditarAbertura: (abertura: AberturaAdminDto) => void;
  fecharModalAdmin: () => void;
  abrirConfirmacaoExclusaoAdmin: (abertura: AberturaAdminDto) => void;
  fecharConfirmacaoExclusaoAdmin: () => void;
  resetarEstadoUi: () => void;
}

const estadoInicial = {
  termoBusca: '',
  filtroComunidade: 'todas' as FiltroComunidadeAbertura,
  aberturaDetalheId: null as string | null,
  modalDetalhesAberto: false,
  termoBuscaAdmin: '',
  filtroStatusAdmin: 'TODAS' as FiltroStatusAdminAbertura,
  modalAdminAberto: false,
  aberturaEmEdicao: null as AberturaAdminDto | null,
  alvoExclusaoAdmin: null as AberturaAdminDto | null,
};

/**
 * Store Zustand estritamente para estado efêmero de UI da Etapa 3.3 (comunidade + admin).
 * Todos os dados remotos de aberturas e ranking permanecem no TanStack Query.
 */
export const useOpeningsUiStore = create<EstadoOpeningsUi>((set) => ({
  ...estadoInicial,
  definirTermoBusca: (termoBusca) => set({ termoBusca }),
  definirFiltroComunidade: (filtroComunidade) => set({ filtroComunidade }),
  abrirDetalhesAbertura: (aberturaDetalheId) =>
    set({ aberturaDetalheId, modalDetalhesAberto: true }),
  fecharDetalhesAbertura: () => set({ aberturaDetalheId: null, modalDetalhesAberto: false }),
  definirTermoBuscaAdmin: (termoBuscaAdmin) => set({ termoBuscaAdmin }),
  definirFiltroStatusAdmin: (filtroStatusAdmin) => set({ filtroStatusAdmin }),
  abrirModalNovaAbertura: () => set({ modalAdminAberto: true, aberturaEmEdicao: null }),
  abrirModalEditarAbertura: (aberturaEmEdicao) => set({ modalAdminAberto: true, aberturaEmEdicao }),
  fecharModalAdmin: () => set({ modalAdminAberto: false, aberturaEmEdicao: null }),
  abrirConfirmacaoExclusaoAdmin: (alvoExclusaoAdmin) => set({ alvoExclusaoAdmin }),
  fecharConfirmacaoExclusaoAdmin: () => set({ alvoExclusaoAdmin: null }),
  resetarEstadoUi: () => set(estadoInicial),
}));
