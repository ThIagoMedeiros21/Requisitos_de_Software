import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../../lib/queryClient';
import { rotas } from '../../../../routes';
import { useReviewsUiStore } from '../../../../stores/reviewsUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import type { DetalheAvaliacoesCartaDto } from '../types/reviews.types';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

function detalheBase(
  parcial: Partial<DetalheAvaliacoesCartaDto> = {},
): DetalheAvaliacoesCartaDto {
  return {
    carta: {
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
    },
    resumo: {
      produtoId: 'OP01-001',
      media: 0.0,
      quantidadeValida: 0,
      distribuicao: { '5': 0, '4': 0, '3': 0, '2': 0, '1': 0 },
    },
    podeAvaliar: true,
    motivoInelegibilidade: null,
    minhaAvaliacao: null,
    comprasDoUsuario: [
      {
        id: '1',
        pedidoId: 'PED-1001',
        produtoId: 'OP01-001',
        paga: true,
        reembolsada: false,
        elegivel: true,
        atualizadoEm: '2026-11-23T10:00:00Z',
      },
    ],
    avaliacoes: [],
    ...parcial,
  };
}

describe('CardReviewDetailPage e ReviewFormModal (F003)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
    useReviewsUiStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('valida nota 1..5, preserva estado ao cancelar, salva avaliação com Compra Verificada e recalcula média no reembolso', async () => {
    let estado = detalheBase();
    let chamadasAvaliar = 0;

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
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
        if (url === '/backend/comunidade/avaliacao-produto/OP01-001/pesquisa') {
          return Response.json(estado);
        }
        if (
          url === '/backend/comunidade/avaliacao-produto/OP01-001/avaliar' &&
          init?.method === 'POST'
        ) {
          chamadasAvaliar += 1;
          const corpo = JSON.parse(String(init.body)) as { nota: number; comentario: string | null };
          const novaAvaliacao = {
            id: '55',
            usuario: 'lucas',
            apelido: 'LucasTC',
            produtoId: 'OP01-001',
            nota: corpo.nota,
            comentario: corpo.comentario,
            compraVerificada: true,
            selo: 'compra verificada',
            valida: true,
            avaliadoEm: '2026-11-23T15:00:00Z',
          };
          estado = {
            ...estado,
            minhaAvaliacao: novaAvaliacao,
            avaliacoes: [novaAvaliacao],
            resumo: {
              produtoId: 'OP01-001',
              media: corpo.nota,
              quantidadeValida: 1,
              distribuicao: {
                '5': corpo.nota === 5 ? 1 : 0,
                '4': corpo.nota === 4 ? 1 : 0,
                '3': corpo.nota === 3 ? 1 : 0,
                '2': corpo.nota === 2 ? 1 : 0,
                '1': corpo.nota === 1 ? 1 : 0,
              },
            },
          };
          return Response.json(novaAvaliacao);
        }
        if (
          url === '/backend/comunidade/avaliacao-produto/compras/PED-1001/reembolsar' &&
          init?.method === 'POST'
        ) {
          estado = {
            ...estado,
            podeAvaliar: false,
            motivoInelegibilidade:
              'Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta.',
            minhaAvaliacao: null,
            avaliacoes: [],
            comprasDoUsuario: [
              {
                ...estado.comprasDoUsuario[0]!,
                reembolsada: true,
                elegivel: false,
              },
            ],
            resumo: {
              produtoId: 'OP01-001',
              media: 0.0,
              quantidadeValida: 0,
              distribuicao: { '5': 0, '4': 0, '3': 0, '2': 0, '1': 0 },
            },
          };
          return Response.json(estado.resumo);
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/avaliacoes/OP01-001');

    // Estado inicial sem avaliações (média 0.0 — RN004)
    expect(await screen.findByRole('heading', { name: 'Roronoa Zoro' })).toBeInTheDocument();
    expect(screen.getByText('0.0')).toBeInTheDocument();
    expect(
      screen.getByText('Esta carta ainda não possui avaliações da comunidade.'),
    ).toBeInTheDocument();

    // Abre modal e tenta salvar sem selecionar nota (RN003: 1..5)
    await user.click(screen.getByRole('button', { name: /Avaliar Carta/i }));
    const dialogo = await screen.findByRole('dialog');
    await user.click(within(dialogo).getByRole('button', { name: 'Salvar Avaliação' }));
    expect(
      within(dialogo).getByText('Selecione uma nota entre 1 e 5 estrelas.'),
    ).toBeInTheDocument();
    expect(chamadasAvaliar).toBe(0);

    // Seleciona 4 estrelas, digita rascunho e clica em Cancelar (RN002: descarta sem salvar)
    await user.click(within(dialogo).getByRole('button', { name: '4 estrelas' }));
    await user.type(within(dialogo).getByLabelText(/Seu Comentário/i), 'Rascunho descartado');
    await user.click(within(dialogo).getByRole('button', { name: 'Cancelar' }));
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    expect(chamadasAvaliar).toBe(0);
    expect(screen.getByText('0.0')).toBeInTheDocument();

    // Reabre modal, seleciona 5 estrelas e salva com sucesso (RN006: Compra Verificada)
    await user.click(screen.getByRole('button', { name: /Avaliar Carta/i }));
    const dialogo2 = await screen.findByRole('dialog');
    await user.click(within(dialogo2).getByRole('button', { name: '5 estrelas' }));
    await user.type(within(dialogo2).getByLabelText(/Seu Comentário/i), 'Excelente para torneio!');
    await user.click(within(dialogo2).getByRole('button', { name: 'Salvar Avaliação' }));

    expect(await screen.findByText('Excelente para torneio!')).toBeInTheDocument();
    expect(screen.getByText('✓ Compra Verificada')).toBeInTheDocument();
    expect(screen.getByText('5.0')).toBeInTheDocument();
    expect(chamadasAvaliar).toBe(1);

    // Expande painel de pedidos e simula reembolso (RN005: invalidação síncrona + recálculo 0.0)
    await user.click(screen.getByRole('button', { name: /Meus Pedidos desta Carta/i }));
    expect(screen.getByText('PED-1001')).toBeInTheDocument();
    await user.click(screen.getByRole('button', { name: 'Simular Reembolso' }));

    expect(
      await screen.findByText('Esta carta ainda não possui avaliações da comunidade.'),
    ).toBeInTheDocument();
    expect(screen.getByText('0.0')).toBeInTheDocument();
    expect(screen.getByText('Reembolsado')).toBeInTheDocument();
  });

  it('bloqueia envio quando usuário não é elegível para avaliar (RN001)', async () => {
    const estadoInelegivel = detalheBase({
      podeAvaliar: false,
      motivoInelegibilidade:
        'Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta.',
      comprasDoUsuario: [],
    });

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
        if (url === '/backend/comunidade/avaliacao-produto/OP01-001/pesquisa') {
          return Response.json(estadoInelegivel);
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/comunidade/avaliacoes/OP01-001');

    expect(
      await screen.findByText(
        'Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta.',
      ),
    ).toBeInTheDocument();

    await user.click(screen.getByRole('button', { name: /Avaliar Carta/i }));
    const dialogo = await screen.findByRole('dialog');
    expect(within(dialogo).getByRole('alert')).toHaveTextContent(
      'Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta.',
    );
    expect(within(dialogo).getByRole('button', { name: 'Salvar Avaliação' })).toBeDisabled();
  });
});
