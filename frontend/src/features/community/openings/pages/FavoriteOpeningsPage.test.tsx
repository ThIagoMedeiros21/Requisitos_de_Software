import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../../lib/queryClient';
import { rotas } from '../../../../routes';
import { useOpeningsUiStore } from '../../../../stores/openingsUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import type { PesquisaAberturasComunidadeDto } from '../types/openings.types';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

function dadosBase(
  parcial: Partial<PesquisaAberturasComunidadeDto> = {},
): PesquisaAberturasComunidadeDto {
  return {
    minhaPreferida: {
      id: '100',
      aberturaId: '1',
      numero: 1,
      nome: 'We Are!',
      artista: 'Hiroshi Kitadani',
      episodios: '1 a 47',
      imagem: '',
      video: 'https://v.animethemes.moe/OnePiece-OP1.webm',
      ativa: true,
      visivelNoPerfil: true,
      escolhidaEm: '2026-11-21T19:00:00Z',
    },
    aberturasAtivas: [
      {
        id: '1',
        numero: 1,
        nome: 'We Are!',
        artista: 'Hiroshi Kitadani',
        episodios: '1 a 47',
        imagem: '',
        video: 'https://v.animethemes.moe/OnePiece-OP1.webm',
        ativa: true,
        quantidadeAmigos: 2,
        ehMinhaPreferida: true,
        posicaoRanking: 1,
      },
      {
        id: '2',
        numero: 20,
        nome: 'Hope',
        artista: 'Namie Amuro',
        episodios: '807 a 855',
        imagem: '',
        video: 'https://v.animethemes.moe/OnePiece-OP20.webm',
        ativa: true,
        quantidadeAmigos: 1,
        ehMinhaPreferida: false,
        posicaoRanking: 2,
      },
    ],
    ranking: {
      totalEscolhas: 5,
      itens: [
        {
          posicao: 1,
          aberturaId: '1',
          numero: 1,
          nome: 'We Are!',
          artista: 'Hiroshi Kitadani',
          imagem: '',
          quantidadeEscolhas: 3,
          percentual: 60.0,
        },
        {
          posicao: 2,
          aberturaId: '2',
          numero: 20,
          nome: 'Hope',
          artista: 'Namie Amuro',
          imagem: '',
          quantidadeEscolhas: 2,
          percentual: 40.0,
        },
      ],
    },
    ...parcial,
  };
}

describe('FavoriteOpeningsPage e OpeningDetailsModal (F004)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
    useOpeningsUiStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('exibe ranking Top 5, alterna visibilidade pública, troca abertura preferida e abre detalhes', async () => {
    let estado = dadosBase();

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({
            usuario: 'lucas',
            aberturaPreferida: { incluir: true, buscar: true, alterar: true, excluir: false },
          });
        }
        if (url.startsWith('/backend/comunidade/abertura-preferida/pesquisa')) {
          return Response.json(estado);
        }
        if (
          url === '/backend/comunidade/abertura-preferida/ocultar' &&
          init?.method === 'POST'
        ) {
          estado = {
            ...estado,
            minhaPreferida: { ...estado.minhaPreferida!, visivelNoPerfil: false },
          };
          return new Response(null, { status: 204 });
        }
        if (
          url === '/backend/comunidade/abertura-preferida/escolher' &&
          init?.method === 'POST'
        ) {
          estado = {
            ...estado,
            minhaPreferida: {
              ...estado.minhaPreferida!,
              aberturaId: '2',
              numero: 20,
              nome: 'Hope',
              artista: 'Namie Amuro',
              episodios: '807 a 855',
            },
            aberturasAtivas: estado.aberturasAtivas.map((a) => ({
              ...a,
              ehMinhaPreferida: a.id === '2',
            })),
          };
          return Response.json(estado.minhaPreferida);
        }
        if (url === '/backend/comunidade/abertura-preferida/2/visualizacao') {
          return Response.json({
            id: '2',
            numero: 20,
            nome: 'Hope',
            artista: 'Namie Amuro',
            episodios: '807 a 855',
            imagem: '',
            video: 'https://v.animethemes.moe/OnePiece-OP20.webm',
            ativa: true,
            ehMinhaPreferida: true,
            visivelNoPerfil: false,
            posicaoRanking: 2,
            totalEscolhasComunidade: 5,
            quantidadeAmigos: 1,
            amigosComMesmaPreferencia: [{ usuario: 'ricardo', apelido: 'ZoroTC', perfilId: '2' }],
          });
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/aberturas');

    expect(await screen.findByRole('heading', { name: /Op\. 01 — We Are!/i })).toBeInTheDocument();
    expect(screen.getByText('Top 5 Escolhas Oficiais')).toBeInTheDocument();
    expect(screen.getByText('60.0%')).toBeInTheDocument();
    expect(screen.getByText('2 amigos escolheram')).toBeInTheDocument();

    const toggle = screen.getByRole('switch', { name: 'Exibir no Perfil Público' });
    expect(toggle).toHaveAttribute('aria-checked', 'true');
    await user.click(toggle);
    expect(await screen.findByText('Oculta no perfil público')).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /Escolher como Preferida/i }));
    expect(await screen.findByRole('heading', { name: /Op\. 20 — Hope/i })).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: 'Ver Detalhes' }));
    const dialogo = await screen.findByRole('dialog');
    expect(await within(dialogo).findByText('@ZoroTC')).toBeInTheDocument();
    expect(within(dialogo).getByText('#2 no Ranking')).toBeInTheDocument();
  });

  it('exibe estado vazio do ranking quando ainda não há escolhas registradas (RN004)', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url.startsWith('/backend/comunidade/abertura-preferida/pesquisa')) {
          return Response.json(
            dadosBase({
              minhaPreferida: null,
              ranking: { totalEscolhas: 0, itens: [] },
            }),
          );
        }
        return new Response(null, { status: 404 });
      }),
    );

    renderizar('/comunidade/aberturas');

    expect(
      await screen.findByText('Nenhuma escolha registrada na comunidade ainda.'),
    ).toBeInTheDocument();
  });
});
