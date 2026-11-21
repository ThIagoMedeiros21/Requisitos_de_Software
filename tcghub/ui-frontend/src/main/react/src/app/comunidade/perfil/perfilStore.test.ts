import { usePerfilStore } from './perfilStore';

describe('usePerfilStore', () => {
  beforeEach(() => {
    usePerfilStore.getState().resetarEstadoUi();
  });

  it('inicia com estado padrão de UI e alterna abas', () => {
    const estado = usePerfilStore.getState();
    expect(estado.abaAtiva).toBe('meu-perfil');
    expect(estado.subAbaAmizade).toBe('meus-amigos');
    expect(estado.modalEdicaoAberto).toBe(false);
    expect(estado.modalSlotAberto).toBe(false);

    estado.definirAbaAtiva('rede-amigos');
    estado.definirSubAbaAmizade('recebidas');

    expect(usePerfilStore.getState().abaAtiva).toBe('rede-amigos');
    expect(usePerfilStore.getState().subAbaAmizade).toBe('recebidas');
  });

  it('controla abertura e fechamento do modal de edição e do seletor de slot', () => {
    usePerfilStore.getState().abrirModalEdicao();
    expect(usePerfilStore.getState().modalEdicaoAberto).toBe(true);
    usePerfilStore.getState().fecharModalEdicao();
    expect(usePerfilStore.getState().modalEdicaoAberto).toBe(false);

    usePerfilStore.getState().definirFiltroColecaoSlot('Luffy');
    usePerfilStore.getState().abrirModalSlot(2);
    expect(usePerfilStore.getState().modalSlotAberto).toBe(true);
    expect(usePerfilStore.getState().slotSelecionado).toBe(2);
    expect(usePerfilStore.getState().filtroColecaoSlot).toBe('');

    usePerfilStore.getState().fecharModalSlot();
    expect(usePerfilStore.getState().modalSlotAberto).toBe(false);
    expect(usePerfilStore.getState().slotSelecionado).toBeNull();
  });

  it('controla confirmação de remoção bilateral, filtros e reset completo', () => {
    usePerfilStore.getState().abrirConfirmacaoRemocao({ id: '10', apelido: 'ZoroTC' });
    usePerfilStore.getState().definirFiltroColecionadores('Olinda');

    expect(usePerfilStore.getState().alvoRemocaoAmizade).toEqual({ id: '10', apelido: 'ZoroTC' });
    expect(usePerfilStore.getState().filtroColecionadores).toBe('Olinda');

    usePerfilStore.getState().fecharConfirmacaoRemocao();
    expect(usePerfilStore.getState().alvoRemocaoAmizade).toBeNull();

    usePerfilStore.getState().resetarEstadoUi();
    expect(usePerfilStore.getState().filtroColecionadores).toBe('');
    expect(usePerfilStore.getState().abaAtiva).toBe('meu-perfil');
  });
});
