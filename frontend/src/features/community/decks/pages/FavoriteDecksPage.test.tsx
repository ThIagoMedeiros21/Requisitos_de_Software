import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../../lib/queryClient';
import { rotas } from '../../../../routes';
import { useDecksUiStore } from '../../../../stores/decksUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import type {
  ComposicaoDeckDto,
  DeckCatalogoDto,
  ListaDecksFavoritosDto,
} from '../types/decks.types';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

function listaBase(parcial: Partial<ListaDecksFavoritosDto> = {}): ListaDecksFavoritosDto {
  return {
    total: 2,
    limite: 4,
    decksFavoritosVisiveis: true,
    ehProprietario: true,
    decks: [
      {
        id: '1',
        deckId: 'ST01',
        ordem: 1,
        adicionadoEm: '2026-11-21T18:00:00Z',
        nomeDeck: 'Straw Hat Crew',
        lider: 'Monkey D. Luffy (ST01-001)',
        liderCodigo: 'ST01-001',
        liderImagem: null,
        cor: 'Vermelho',
        ativo: true,
        totalCartasMainDeck: 50,
        custoEstimadoMercado: 890,
      },
      {
        id: '2',
        deckId: 'ST09-INATIVO',
        ordem: 2,
        adicionadoEm: '2026-11-21T18:05:00Z',
        nomeDeck: 'Yamato Legacy',
        lider: 'Yamato (ST09-001)',
        liderCodigo: 'ST09-001',
        liderImagem: null,
        cor: 'Amarelo',
        ativo: false,
        totalCartasMainDeck: 50,
        custoEstimadoMercado: 450,
      },
    ],
    ...parcial,
  };
}

function composicaoST01(): ComposicaoDeckDto {
  return {
    deckId: 'ST01',
    nomeDeck: 'Straw Hat Crew',
    lider: 'Monkey D. Luffy (ST01-001)',
    liderCodigo: 'ST01-001',
    liderImagem: null,
    cor: 'Vermelho',
    ativo: true,
    totalCartasMainDeck: 50,
    totalCartasComLider: 51,
    conformidadeBandai: true,
    custoEstimadoMercado: 890,
    totaisPorTipo: { Leader: 1, Character: 36, Event: 10, Stage: 4 },
    decklistBandai: '1xST01-001\n4xOP01-016',
    itens: [
      {
        card_set_id: 'ST01-001',
        card_name: 'Monkey D. Luffy',
        set_id: 'ST-01',
        set_name: 'Straw Hat Crew',
        rarity: 'L',
        card_color: 'Vermelho',
        card_type: 'Leader',
        card_cost: null,
        card_power: '5000',
        sub_types: 'Straw Hat Crew',
        counter_amount: null,
        attribute: 'Strike',
        market_price: 45,
        inventory_price: 42,
        card_image_id: 'ST01-001',
        card_image: null,
        quantidade: 1,
        subtotal: 45,
      },
      {
        card_set_id: 'OP01-016',
        card_name: 'Nami',
        set_id: 'OP-01',
        set_name: 'Romance Dawn',
        rarity: 'R',
        card_color: 'Vermelho',
        card_type: 'Character',
        card_cost: '1',
        card_power: '2000',
        sub_types: 'Straw Hat Crew',
        counter_amount: '1000',
        attribute: 'Special',
        market_price: 25,
        inventory_price: 22,
        card_image_id: 'OP01-016',
        card_image: null,
        quantidade: 4,
        subtotal: 100,
      },
    ],
  };
}

describe('FavoriteDecksPage, AddFavoriteDeckModal e DeckCompositionModal (F002)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
    useDecksUiStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('exibe decks favoritos, badge de inativo, alterna visibilidade pública e permite adicionar/remover', async () => {
    let estadoDecks = listaBase();
    const catalogo: DeckCatalogoDto[] = [
      {
        deckId: 'ST01',
        nomeDeck: 'Straw Hat Crew',
        lider: 'Monkey D. Luffy (ST01-001)',
        liderCodigo: 'ST01-001',
        liderImagem: null,
        cor: 'Vermelho',
        ativo: true,
        totalCartasMainDeck: 50,
        custoEstimadoMercado: 890,
        jaFavoritado: true,
      },
      {
        deckId: 'ST10',
        nomeDeck: 'The Three Captains',
        lider: 'Trafalgar Law (ST10-001)',
        liderCodigo: 'ST10-001',
        liderImagem: null,
        cor: 'Roxo',
        ativo: true,
        totalCartasMainDeck: 50,
        custoEstimadoMercado: 1120,
        jaFavoritado: false,
      },
    ];

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({
            usuario: 'lucas',
            deckFavorito: { incluir: true, buscar: true, alterar: true, excluir: true },
          });
        }
        if (url === '/backend/comunidade/deck-favorito/pesquisa') {
          return Response.json(estadoDecks);
        }
        if (url.startsWith('/backend/comunidade/deck-favorito/catalogo')) {
          return Response.json(catalogo);
        }
        if (url === '/backend/comunidade/deck-favorito/ocultar' && init?.method === 'POST') {
          estadoDecks = { ...estadoDecks, decksFavoritosVisiveis: false };
          return new Response(null, { status: 204 });
        }
        if (url === '/backend/comunidade/deck-favorito/incluir' && init?.method === 'POST') {
          estadoDecks = {
            ...estadoDecks,
            total: estadoDecks.total + 1,
            decks: [
              ...estadoDecks.decks,
              {
                id: '3',
                deckId: 'ST10',
                ordem: 3,
                adicionadoEm: '2026-11-21T18:10:00Z',
                nomeDeck: 'The Three Captains',
                lider: 'Trafalgar Law (ST10-001)',
                liderCodigo: 'ST10-001',
                liderImagem: null,
                cor: 'Roxo',
                ativo: true,
                totalCartasMainDeck: 50,
                custoEstimadoMercado: 1120,
              },
            ],
          };
          catalogo[1]!.jaFavoritado = true;
          return Response.json(estadoDecks);
        }
        if (
          url === '/backend/comunidade/deck-favorito/ST09-INATIVO/excluir' &&
          init?.method === 'POST'
        ) {
          estadoDecks = {
            ...estadoDecks,
            total: estadoDecks.total - 1,
            decks: estadoDecks.decks.filter((d) => d.deckId !== 'ST09-INATIVO'),
          };
          return new Response(null, { status: 204 });
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/decks-favoritos');

    expect(await screen.findByRole('heading', { name: 'Decks Favoritos' })).toBeInTheDocument();
    expect(screen.getByText('2/4 decks favoritos')).toBeInTheDocument();
    expect(screen.getByText('Descontinuado / Inativo no Catálogo')).toBeInTheDocument();

    const toggle = screen.getByRole('switch', { name: 'Exibir no Perfil Público' });
    expect(toggle).toHaveAttribute('aria-checked', 'true');
    await user.click(toggle);
    expect(
      await screen.findByText('Oculto no perfil público — somente você vê'),
    ).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /\+ Adicionar deck favorito/i }));
    const dialogo = await screen.findByRole('dialog');
    expect(within(dialogo).getByText('Adicionar Deck Favorito')).toBeInTheDocument();
    await user.click(within(dialogo).getByRole('button', { name: /Favoritar/i }));
    expect(await screen.findByText('3/4 decks favoritos')).toBeInTheDocument();

    await user.click(within(dialogo).getByRole('button', { name: 'Cancelar' }));
    const botoesRemover = screen.getAllByRole('button', { name: /Remover/i });
    await user.click(botoesRemover[1]!);
    expect(await screen.findByText('2/4 decks favoritos')).toBeInTheDocument();
  });

  it('desabilita botão de inclusão ao atingir limite de 4 decks favoritos (RN001)', async () => {
    const quatroDecks = listaBase({
      total: 4,
      decks: [1, 2, 3, 4].map((n) => ({
        id: String(n),
        deckId: `ST0${n}`,
        ordem: n,
        adicionadoEm: '2026-11-21T18:00:00Z',
        nomeDeck: `Deck ${n}`,
        lider: `Líder ${n}`,
        liderCodigo: `ST0${n}-001`,
        liderImagem: null,
        cor: 'Vermelho',
        ativo: true,
        totalCartasMainDeck: 50,
        custoEstimadoMercado: 500,
      })),
    });

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url === '/backend/comunidade/deck-favorito/pesquisa') {
          return Response.json(quatroDecks);
        }
        return new Response(null, { status: 404 });
      }),
    );

    renderizar('/comunidade/decks-favoritos');

    expect(await screen.findByText('4/4 decks favoritos')).toBeInTheDocument();
    expect(
      screen.getByRole('button', { name: /\+ Adicionar deck favorito/i }),
    ).toBeDisabled();
  });

  it('abre composição do deck, copia no formato Bandai e trata indisponibilidade 503 do Proxy (RN003)', async () => {
    let simularIndisponivel = false;

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({ usuario: 'lucas' });
        }
        if (url === '/backend/comunidade/deck-favorito/pesquisa') {
          return Response.json(listaBase());
        }
        if (url === '/backend/comunidade/deck-favorito/ST01/composicao') {
          if (simularIndisponivel) {
            return Response.json(
              { mensagem: 'O serviço de Catálogo está temporariamente indisponível.' },
              { status: 503 },
            );
          }
          return Response.json(composicaoST01());
        }
        if (url === '/backend/comunidade/deck-favorito/ST09-INATIVO/composicao') {
          return Response.json(
            { mensagem: 'O serviço de Catálogo está temporariamente indisponível.' },
            { status: 503 },
          );
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    const writeText = vi.spyOn(navigator.clipboard, 'writeText').mockResolvedValue(undefined);
    renderizar('/comunidade/decks-favoritos');

    const botoesVer = await screen.findAllByRole('button', { name: /Visualizar deck/i });
    await user.click(botoesVer[0]!);

    const dialogo = await screen.findByRole('dialog');
    expect(await within(dialogo).findByText('Nami')).toBeInTheDocument();
    expect(
      within(dialogo).getByText(/Conformidade Estrita \(1 Líder \+ 50 Cartas/i),
    ).toBeInTheDocument();

    await user.click(
      within(dialogo).getByRole('button', { name: /Copiar Decklist \(Formato Bandai\)/i }),
    );
    expect(writeText).toHaveBeenCalledWith('1xST01-001\n4xOP01-016');
    expect(await screen.findByText('Decklist copiada no formato Bandai!')).toBeInTheDocument();

    await user.click(within(dialogo).getByRole('button', { name: 'Fechar' }));

    simularIndisponivel = true;
    await user.click(botoesVer[1]!);
    const dialogoErro = await screen.findByRole('dialog');
    expect(
      await within(dialogoErro).findByText(
        'Não foi possível carregar a composição porque o serviço de Catálogo está indisponível.',
      ),
    ).toBeInTheDocument();
    expect(
      within(dialogoErro).getByRole('button', { name: /Tentar novamente/i }),
    ).toBeInTheDocument();
  });
});
