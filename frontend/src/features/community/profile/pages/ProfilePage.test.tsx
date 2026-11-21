import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../../lib/queryClient';
import { rotas } from '../../../../routes';
import { useToastStore } from '../../../../stores/useToastStore';
import { useProfileUiStore } from '../stores/useProfileUiStore';
import type { PerfilComunitarioVisaoDto } from '../types/profile.types';

function perfilBase(parcial: Partial<PerfilComunitarioVisaoDto> = {}): PerfilComunitarioVisaoDto {
  return {
    id: '1',
    usuario: 'lucas',
    apelido: 'LuffyBR',
    cidade: 'Recife',
    descricao: 'Colecionador de One Piece',
    foto: null,
    preferenciasPublicas: 'Trocas presenciais',
    decksFavoritosVisiveis: true,
    ehProprietario: true,
    statusRelacao: 'PROPRIO',
    amizadeId: null,
    cartasFavoritas: [
      {
        cartaId: 'OP01-001',
        codigo: 'OP01-001',
        nome: 'Monkey D. Luffy',
        raridade: 'LÍDER / SEC',
        edicaoCondicao: 'Near Mint',
        cor: 'Vermelho',
        precoMercado: 1850,
        imagem: null,
      },
    ],
    cartasColecaoDisponiveis: [
      {
        cartaId: 'OP01-001',
        codigo: 'OP01-001',
        nome: 'Monkey D. Luffy',
        raridade: 'LÍDER / SEC',
        edicaoCondicao: 'Near Mint',
        cor: 'Vermelho',
        precoMercado: 1850,
        imagem: null,
        jaDestacada: true,
      },
      {
        cartaId: 'OP01-005',
        codigo: 'OP01-005',
        nome: 'Roronoa Zoro',
        raridade: 'SR',
        edicaoCondicao: 'Holo Foil',
        cor: 'Verde',
        precoMercado: 120,
        imagem: null,
        jaDestacada: false,
      },
    ],
    ...parcial,
  };
}

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

describe('ProfilePage e EditProfileModal (F001 — P01 a P05)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
    useProfileUiStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('exibe perfil do proprietário, slots disponíveis e permite fixar e remover carta favorita', async () => {
    let perfilAtual = perfilBase();
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({
            usuario: 'lucas',
            perfilComunitario: { incluir: true, buscar: true, alterar: true, excluir: false },
            amizade: { incluir: true, buscar: true, alterar: true, excluir: true },
          });
        }
        if (url === '/backend/comunidade/perfil-comunitario/visualizacao') {
          return Response.json(perfilAtual);
        }
        if (
          url === '/backend/comunidade/perfil-comunitario/1/fixar-destaque' &&
          init?.method === 'POST'
        ) {
          perfilAtual = perfilBase({
            cartasFavoritas: [
              ...perfilAtual.cartasFavoritas,
              {
                cartaId: 'OP01-005',
                codigo: 'OP01-005',
                nome: 'Roronoa Zoro',
                raridade: 'SR',
                edicaoCondicao: 'Holo Foil',
                cor: 'Verde',
                precoMercado: 120,
                imagem: null,
              },
            ],
          });
          return Response.json(perfilAtual);
        }
        if (
          url === '/backend/comunidade/perfil-comunitario/1/remover-destaque' &&
          init?.method === 'POST'
        ) {
          perfilAtual = perfilBase({ cartasFavoritas: [] });
          return Response.json(perfilAtual);
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/perfil');

    expect(await screen.findByRole('heading', { name: 'LuffyBR' })).toBeInTheDocument();
    expect(screen.getByText('1 de 4 slots preenchidos')).toBeInTheDocument();
    expect(screen.getAllByText('Slot Disponível')).toHaveLength(3);

    await user.click(screen.getAllByRole('button', { name: '+ Adicionar Carta' })[0]!);
    const dialogo = await screen.findByRole('dialog');
    expect(within(dialogo).getByText('Escolher Carta Favorita')).toBeInTheDocument();

    await user.click(within(dialogo).getByRole('button', { name: 'Fixar Slot' }));
    expect(await screen.findByText('2 de 4 slots preenchidos')).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: 'Remover Monkey D. Luffy dos destaques' }));
    expect(await screen.findByText('0 de 4 slots preenchidos')).toBeInTheDocument();
  });

  it('valida edição do perfil (RN001/RN002): cancelar preserva estado, apelido vazio bloqueia e erro 409 notifica', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url === '/backend/comunidade/perfil-comunitario/visualizacao') {
          return Response.json(perfilBase());
        }
        if (url === '/backend/comunidade/perfil-comunitario/1/alterar' && init?.method === 'POST') {
          return Response.json({ mensagem: 'O apelido "ZoroTC" já está em uso.' }, { status: 409 });
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/perfil');

    await user.click(await screen.findByRole('button', { name: /Editar Perfil/i }));
    const dialogo = await screen.findByRole('dialog', { name: 'Editar Perfil' });
    const campoApelido = within(dialogo).getByLabelText('Apelido Comunitário *');

    await user.clear(campoApelido);
    await user.click(within(dialogo).getByRole('button', { name: 'Salvar Alterações' }));
    expect(await within(dialogo).findByText('O apelido é obrigatório.')).toBeInTheDocument();

    await user.click(within(dialogo).getByRole('button', { name: 'Cancelar' }));
    expect(screen.queryByRole('dialog', { name: 'Editar Perfil' })).not.toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'LuffyBR' })).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /Editar Perfil/i }));
    const dialogoReaberto = await screen.findByRole('dialog', { name: 'Editar Perfil' });
    const campoApelido2 = within(dialogoReaberto).getByLabelText('Apelido Comunitário *');
    await user.clear(campoApelido2);
    await user.type(campoApelido2, 'ZoroTC');
    await user.click(within(dialogoReaberto).getByRole('button', { name: 'Salvar Alterações' }));

    expect(await screen.findByText('O apelido "ZoroTC" já está em uso.')).toBeInTheDocument();
  });

  it('oculta controles privados para visitante e exibe ErrorFallback para perfil 404', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url === '/backend/comunidade/perfil-comunitario/2/visualizacao') {
          return Response.json(
            perfilBase({
              id: '2',
              usuario: 'ricardo',
              apelido: 'ZoroTC',
              ehProprietario: false,
              decksFavoritosVisiveis: false,
              statusRelacao: 'NENHUMA',
              cartasFavoritas: [],
              cartasColecaoDisponiveis: [],
            }),
          );
        }
        return Response.json({ mensagem: 'Perfil comunitário não encontrado.' }, { status: 404 });
      }),
    );

    renderizar('/comunidade/perfil/2');

    expect(await screen.findByRole('heading', { name: 'ZoroTC' })).toBeInTheDocument();
    expect(screen.queryByRole('button', { name: /Editar Perfil/i })).not.toBeInTheDocument();
    expect(screen.queryByText('Decks Favoritos')).not.toBeInTheDocument();
    expect(
      screen.getByText('Este colecionador ainda não destacou cartas favoritas'),
    ).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Adicionar Amigo/i })).toBeInTheDocument();

    clienteConsultas.clear();
    renderizar('/comunidade/perfil/999');
    expect(await screen.findByText('Perfil comunitário não encontrado.')).toBeInTheDocument();
  });
});
