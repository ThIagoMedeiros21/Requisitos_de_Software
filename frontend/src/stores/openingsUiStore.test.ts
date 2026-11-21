import { useOpeningsUiStore } from './openingsUiStore';

describe('useOpeningsUiStore', () => {
  beforeEach(() => {
    useOpeningsUiStore.getState().resetarEstadoUi();
  });

  it('controla busca, filtro e modal de detalhes da comunidade (F004)', () => {
    useOpeningsUiStore.getState().definirTermoBusca('We Are');
    useOpeningsUiStore.getState().definirFiltroComunidade('amigos');
    useOpeningsUiStore.getState().abrirDetalhesAbertura('1');

    const estado = useOpeningsUiStore.getState();
    expect(estado.termoBusca).toBe('We Are');
    expect(estado.filtroComunidade).toBe('amigos');
    expect(estado.aberturaDetalheId).toBe('1');
    expect(estado.modalDetalhesAberto).toBe(true);

    estado.fecharDetalhesAbertura();
    expect(useOpeningsUiStore.getState().aberturaDetalheId).toBeNull();
    expect(useOpeningsUiStore.getState().modalDetalhesAberto).toBe(false);
  });

  it('controla filtros e modais de cadastro/edição/exclusão administrativos (adm/F001)', () => {
    const amostra = {
      id: '10',
      numero: 20,
      nome: 'Hope',
      artista: 'Namie Amuro',
      episodios: '807 a 855',
      imagem: 'https://example.com/op20.jpg',
      video: 'https://v.animethemes.moe/OnePiece-OP20.webm',
      ativa: true,
      quantidadePreferencias: 3,
    };

    useOpeningsUiStore.getState().definirFiltroStatusAdmin('ATIVAS');
    useOpeningsUiStore.getState().definirTermoBuscaAdmin('Hope');
    useOpeningsUiStore.getState().abrirModalEditarAbertura(amostra);
    useOpeningsUiStore.getState().abrirConfirmacaoExclusaoAdmin(amostra);

    const estado = useOpeningsUiStore.getState();
    expect(estado.filtroStatusAdmin).toBe('ATIVAS');
    expect(estado.termoBuscaAdmin).toBe('Hope');
    expect(estado.modalAdminAberto).toBe(true);
    expect(estado.aberturaEmEdicao?.nome).toBe('Hope');
    expect(estado.alvoExclusaoAdmin?.id).toBe('10');

    estado.fecharModalAdmin();
    estado.fecharConfirmacaoExclusaoAdmin();
    expect(useOpeningsUiStore.getState().modalAdminAberto).toBe(false);
    expect(useOpeningsUiStore.getState().aberturaEmEdicao).toBeNull();
    expect(useOpeningsUiStore.getState().alvoExclusaoAdmin).toBeNull();
  });
});
