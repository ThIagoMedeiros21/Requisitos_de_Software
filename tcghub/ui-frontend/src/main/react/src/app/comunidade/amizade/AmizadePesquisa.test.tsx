import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../d5';
import { rotas } from '../../rotas';
import { usePerfilStore } from '../perfil/perfilStore';
import type { RedeAmizadesDto } from './amizadeConsultas';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

describe('AmizadePesquisa e AmizadeInclusao (F001 — P19 e P20)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    usePerfilStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('alterna abas da rede de amigos, aceita solicitação e confirma remoção bilateral', async () => {
    let redeAtual: RedeAmizadesDto = {
      amigos: [
        {
          amizadeId: '10',
          perfilId: '2',
          usuario: 'ricardo',
          apelido: 'ZoroTC',
          cidade: 'Olinda',
          foto: null,
          status: 'ACEITA',
          criadoEm: '2026-11-21T10:00:00Z',
          atualizadoEm: '2026-11-21T10:05:00Z',
        },
      ],
      recebidas: [
        {
          amizadeId: '11',
          perfilId: '3',
          usuario: 'thiago',
          apelido: 'SanjiOP',
          cidade: 'Recife',
          foto: null,
          status: 'PENDENTE',
          criadoEm: '2026-11-21T11:00:00Z',
          atualizadoEm: null,
        },
      ],
      enviadas: [],
    };

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url === '/backend/comunidade/amizade/pesquisa') {
          return Response.json(redeAtual);
        }
        if (url === '/backend/comunidade/amizade/11/aceitar' && init?.method === 'POST') {
          redeAtual = {
            ...redeAtual,
            amigos: [...redeAtual.amigos, { ...redeAtual.recebidas[0]!, status: 'ACEITA' }],
            recebidas: [],
          };
          return new Response(null, { status: 204 });
        }
        if (url === '/backend/comunidade/amizade/10/remover' && init?.method === 'POST') {
          redeAtual = {
            ...redeAtual,
            amigos: redeAtual.amigos.filter((item) => item.amizadeId !== '10'),
          };
          return new Response(null, { status: 204 });
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/amizade/pesquisa');

    expect(await screen.findByText('ZoroTC')).toBeInTheDocument();

    await user.click(screen.getByRole('tab', { name: 'Recebidas (1)' }));
    expect(screen.getByText('SanjiOP')).toBeInTheDocument();
    await user.click(screen.getByRole('button', { name: 'Aceitar' }));
    expect(await screen.findByText('Nenhuma solicitação de amizade recebida.')).toBeInTheDocument();

    await user.click(screen.getByRole('tab', { name: 'Meus Amigos (2)' }));
    await user.click(screen.getAllByRole('button', { name: 'Remover Amigo' })[0]!);

    const dialogo = await screen.findByRole('dialog');
    expect(within(dialogo).getByText(/bilateral/i)).toBeInTheDocument();
    await user.click(within(dialogo).getByRole('button', { name: 'Confirmar Remoção' }));

    expect(await screen.findByText('Amizade removida bilateralmente.')).toBeInTheDocument();
  });

  it('pesquisa colecionadores e envia solicitação de amizade (P19)', async () => {
    let enviado = false;
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url.startsWith('/backend/comunidade/amizade/inclusao')) {
          if (url.includes('termo=vazio')) {
            return Response.json([]);
          }
          return Response.json([
            {
              perfilId: '2',
              usuario: 'ricardo',
              apelido: 'ZoroTC',
              cidade: 'Olinda',
              foto: null,
              statusRelacao: enviado ? 'PENDENTE_ENVIADA' : 'NENHUMA',
              amizadeId: enviado ? '10' : null,
            },
          ]);
        }
        if (url === '/backend/comunidade/amizade/incluir' && init?.method === 'POST') {
          enviado = true;
          return Response.json({ amizadeId: '10' });
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/amizade/inclusao');

    expect(await screen.findByText('ZoroTC')).toBeInTheDocument();
    await user.click(screen.getByRole('button', { name: 'Adicionar Amigo' }));

    expect(await screen.findByText('Solicitação Enviada')).toBeInTheDocument();

    await user.type(screen.getByPlaceholderText('Buscar colecionador por apelido...'), 'vazio');
    await user.click(screen.getByRole('button', { name: 'Buscar' }));
    expect(
      await screen.findByText('Nenhum colecionador encontrado para o termo pesquisado.'),
    ).toBeInTheDocument();
  });
});
