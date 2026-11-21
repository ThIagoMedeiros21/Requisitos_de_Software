import { useDecksUiStore } from './decksUiStore';

describe('useDecksUiStore', () => {
  beforeEach(() => {
    useDecksUiStore.getState().resetarEstadoUi();
  });

  it('controla modal de adicionar deck e filtros do catálogo', () => {
    useDecksUiStore.getState().definirTermoBuscaCatalogo('Luffy');
    useDecksUiStore.getState().definirCorFiltroCatalogo('Vermelho');
    expect(useDecksUiStore.getState().termoBuscaCatalogo).toBe('Luffy');
    expect(useDecksUiStore.getState().corFiltroCatalogo).toBe('Vermelho');

    useDecksUiStore.getState().abrirModalAdicionar();
    expect(useDecksUiStore.getState().modalAdicionarAberto).toBe(true);
    expect(useDecksUiStore.getState().termoBuscaCatalogo).toBe('');
    expect(useDecksUiStore.getState().corFiltroCatalogo).toBe('');

    useDecksUiStore.getState().fecharModalAdicionar();
    expect(useDecksUiStore.getState().modalAdicionarAberto).toBe(false);
  });

  it('controla modal de composição expandida, filtro de tipo e ordenação', () => {
    useDecksUiStore.getState().abrirComposicao('ST01');
    expect(useDecksUiStore.getState().deckIdComposicaoAberta).toBe('ST01');
    expect(useDecksUiStore.getState().filtroTipoComposicao).toBe('TODOS');
    expect(useDecksUiStore.getState().ordenacaoComposicao).toBe('TIPO');

    useDecksUiStore.getState().definirFiltroTipoComposicao('Character');
    useDecksUiStore.getState().definirOrdenacaoComposicao('CUSTO');
    expect(useDecksUiStore.getState().filtroTipoComposicao).toBe('Character');
    expect(useDecksUiStore.getState().ordenacaoComposicao).toBe('CUSTO');

    useDecksUiStore.getState().fecharComposicao();
    expect(useDecksUiStore.getState().deckIdComposicaoAberta).toBeNull();
  });
});
