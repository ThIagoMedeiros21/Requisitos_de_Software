import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../../lib/queryClient';
import { rotas } from '../../../../routes';
import { useReviewsUiStore } from '../../../../stores/reviewsUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import type { CartaCatalogoAvaliacaoDto } from '../types/reviews.types';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

const CATALOGO_BASE: CartaCatalogoAvaliacaoDto[] = [
  {
    produtoId: 'OP01-001',
    card_set_id: 'OP01-001',
    card_name: 'Roronoa Zoro',
    set_id: 'OP-01',
    set_name: 'Romance Dawn',
    rarity: 'L',
    card_color: 'Red',
    card_type: 'Leader',
    card_cost: null,
    card_power: '5000',
    market_price: 25.5,
    inventory_price: 20.0,
    card_image_id: 'OP01-001',
    card_image: null,
    media: 4.5,
    quantidadeValida: 2,
    distribuicao: { '5': 1, '4': 1, '3': 0, '2': 0, '1': 0 },
    podeAvaliar: true,
    motivoInelegibilidade: null,
    minhaAvaliacao: {
      id: '10',
      usuario: 'lucas',
      apelido: 'LucasTC',
      produtoId: 'OP01-001',
      nota: 5,
      comentario: 'Excelente líder agressivo!',
      compraVerificada: true,
      selo: 'compra verificada',
      valida: true,
      avaliadoEm: '2026-11-23T12:00:00Z',
    },
  },
  {
    produtoId: 'OP05-119',
    card_set_id: 'OP05-119',
    card_name: 'Monkey.D.Luffy',
    set_id: 'OP-05',
    set_name: 'Awakening of the New Era',
    rarity: 'SEC',
    card_color: 'Purple',
    card_type: 'Character',
    card_cost: '10',
    card_power: '12000',
    market_price: 180.0,
    inventory_price: 165.0,
    card_image_id: 'OP05-119',
    card_image: null,
    media: 0.0,
    quantidadeValida: 0,
    distribuicao: { '5': 0, '4': 0, '3': 0, '2': 0, '1': 0 },
    podeAvaliar: false,
    motivoInelegibilidade:
      'Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta.',
    minhaAvaliacao: null,
  },
];

describe('CardReviewsCatalogPage (F003)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
    useReviewsUiStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('lista cartas do catálogo com média, estado 0.0 sem avaliações, selo Compra Verificada e filtros', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({
            usuario: 'lucas',
            avaliacaoProduto: {
              incluir: true,
              buscar: true,
              alterar: true,
              excluir: false,
              avaliar: true,
            },
          });
        }
        if (url.startsWith('/backend/comunidade/avaliacao-produto/catalogo')) {
          return Response.json(CATALOGO_BASE);
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/avaliacoes');

    expect(
      await screen.findByRole('heading', { name: 'Avaliações de Cartas' }),
    ).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Roronoa Zoro' })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: 'Monkey.D.Luffy' })).toBeInTheDocument();
    expect(screen.getByText('4.5')).toBeInTheDocument();
    expect(screen.getByText('0.0')).toBeInTheDocument();
    expect(screen.getByText('(Sem avaliações)')).toBeInTheDocument();
    expect(screen.getByText('✓ Compra Verificada')).toBeInTheDocument();
    expect(screen.getByText('Sua: 5.0 ★')).toBeInTheDocument();

    // Filtro: Elegíveis para Avaliar (esconde Monkey.D.Luffy inelegível)
    await user.click(screen.getByRole('button', { name: 'Elegíveis para Avaliar' }));
    expect(screen.getByRole('heading', { name: 'Roronoa Zoro' })).toBeInTheDocument();
    expect(screen.queryByRole('heading', { name: 'Monkey.D.Luffy' })).not.toBeInTheDocument();

    // Volta para Todas e filtra por texto
    await user.click(screen.getByRole('button', { name: 'Todas as Cartas' }));
    await user.type(screen.getByPlaceholderText(/Buscar carta por nome ou código/i), 'OP05-119');
    expect(screen.getByRole('heading', { name: 'Monkey.D.Luffy' })).toBeInTheDocument();
    expect(screen.queryByRole('heading', { name: 'Roronoa Zoro' })).not.toBeInTheDocument();
  });
});
