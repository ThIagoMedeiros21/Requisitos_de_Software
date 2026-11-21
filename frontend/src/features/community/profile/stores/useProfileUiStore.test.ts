import { useProfileUiStore } from './useProfileUiStore';

describe('useProfileUiStore', () => {
  beforeEach(() => {
    useProfileUiStore.getState().resetarEstadoUi();
  });

  it('inicia com estado padrão de UI e alterna abas', () => {
    const estado = useProfileUiStore.getState();
    expect(estado.abaAtiva).toBe('meu-perfil');
    expect(estado.subAbaAmizade).toBe('meus-amigos');
    expect(estado.modalEdicaoAberto).toBe(false);
    expect(estado.modalSlotAberto).toBe(false);

    estado.definirAbaAtiva('rede-amigos');
    estado.definirSubAbaAmizade('recebidas');

    expect(useProfileUiStore.getState().abaAtiva).toBe('rede-amigos');
    expect(useProfileUiStore.getState().subAbaAmizade).toBe('recebidas');
  });

  it('controla abertura e fechamento do modal de edição e do seletor de slot', () => {
    useProfileUiStore.getState().abrirModalEdicao();
    expect(useProfileUiStore.getState().modalEdicaoAberto).toBe(true);
    useProfileUiStore.getState().fecharModalEdicao();
    expect(useProfileUiStore.getState().modalEdicaoAberto).toBe(false);

    useProfileUiStore.getState().definirFiltroColecaoSlot('Luffy');
    useProfileUiStore.getState().abrirModalSlot(2);
    expect(useProfileUiStore.getState().modalSlotAberto).toBe(true);
    expect(useProfileUiStore.getState().slotSelecionado).toBe(2);
    expect(useProfileUiStore.getState().filtroColecaoSlot).toBe('');

    useProfileUiStore.getState().fecharModalSlot();
    expect(useProfileUiStore.getState().modalSlotAberto).toBe(false);
    expect(useProfileUiStore.getState().slotSelecionado).toBeNull();
  });

  it('controla confirmação de remoção bilateral, filtros e reset completo', () => {
    useProfileUiStore.getState().abrirConfirmacaoRemocao({ id: '10', apelido: 'ZoroTC' });
    useProfileUiStore.getState().definirFiltroColecionadores('Olinda');
    useProfileUiStore.getState().definirFiltroAmigosRede('zoro');

    expect(useProfileUiStore.getState().alvoRemocaoAmizade).toEqual({
      id: '10',
      apelido: 'ZoroTC',
    });
    expect(useProfileUiStore.getState().filtroColecionadores).toBe('Olinda');
    expect(useProfileUiStore.getState().filtroAmigosRede).toBe('zoro');

    useProfileUiStore.getState().fecharConfirmacaoRemocao();
    expect(useProfileUiStore.getState().alvoRemocaoAmizade).toBeNull();

    useProfileUiStore.getState().resetarEstadoUi();
    expect(useProfileUiStore.getState().filtroColecionadores).toBe('');
    expect(useProfileUiStore.getState().filtroAmigosRede).toBe('');
    expect(useProfileUiStore.getState().abaAtiva).toBe('meu-perfil');
  });
});
