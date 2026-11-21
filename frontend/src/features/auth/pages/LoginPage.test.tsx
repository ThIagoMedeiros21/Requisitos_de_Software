import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../lib/queryClient';
import { rotas } from '../../../routes';
import { useToastStore } from '../../../stores/useToastStore';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

describe('LoginPage e Roteamento Protegido', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('envia o usuário anônimo para /login com retorno', async () => {
    vi.stubGlobal('fetch', vi.fn(async () => new Response(null, { status: 401 })));

    const roteador = renderizar('/comunidade/amigos');

    expect(await screen.findByRole('heading', { name: 'Entrar' })).toBeInTheDocument();
    expect(roteador.state.location.pathname).toBe('/login');
    expect(roteador.state.location.search).toBe('?retorno=%2Fcomunidade%2Famigos');
    expect(document.querySelector('.navbar-brand')).toHaveTextContent('TCGHub');
  });

  it('permite login via acesso rápido de demonstração e exibe menu conforme autorizações', async () => {
    let autenticado = false;
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        const headers = new Headers(init?.headers);
        if (url === '/backend/autenticacao') {
          if (headers.has('Authorization')) {
            autenticado = true;
          }
          if (!autenticado) {
            return new Response(null, { status: 401 });
          }
          return Response.json({
            usuario: 'lucas',
            perfilComunitario: { incluir: true, buscar: true, alterar: true, excluir: false },
            amizade: { incluir: true, buscar: true, alterar: true, excluir: true },
          });
        }
        if (url === '/backend/comunidade/perfil-comunitario/visualizacao') {
          return Response.json({
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
            cartasFavoritas: [],
            cartasColecaoDisponiveis: [],
          });
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/login');

    expect(await screen.findByRole('heading', { name: 'Entrar' })).toBeInTheDocument();
    await user.click(screen.getByRole('button', { name: /Lucas/i }));

    expect(await screen.findByRole('heading', { name: 'LuffyBR' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Sair' })).toBeInTheDocument();
    expect(screen.getByRole('link', { name: 'Amigos' })).toBeInTheDocument();
  });
});
