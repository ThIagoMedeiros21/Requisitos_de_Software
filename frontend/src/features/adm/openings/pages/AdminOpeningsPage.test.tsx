import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../../../../lib/queryClient';
import { rotas } from '../../../../routes';
import { useOpeningsUiStore } from '../../../../stores/openingsUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import type { ListaAberturasAdminDto } from '../types/adminOpenings.types';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

describe('AdminOpeningsPage e AdminOpeningFormModal (adm / F001)', () => {
  beforeEach(() => {
    clienteConsultas.clear();
    useToastStore.getState().limpar();
    useOpeningsUiStore.getState().resetarEstadoUi();
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('bloqueia acesso de usuário comum sem papel ADMINISTRADOR (RN002)', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL) => {
        if (String(input) === '/backend/autenticacao') {
          return Response.json({
            usuario: 'lucas',
            abertura: { incluir: false, buscar: true, alterar: false, excluir: false },
          });
        }
        return new Response(null, { status: 403 });
      }),
    );

    renderizar('/adm/aberturas');

    expect(
      await screen.findByRole('heading', { name: 'Acesso restrito a administradores' }),
    ).toBeInTheDocument();
  });

  it('permite cadastrar abertura, inativar e exibe feedback amigável para bloqueio RN006 na exclusão', async () => {
    let catalogo: ListaAberturasAdminDto = {
      total: 1,
      ativas: 1,
      inativas: 0,
      itens: [
        {
          id: '1',
          numero: 1,
          nome: 'We Are!',
          artista: 'Hiroshi Kitadani',
          episodios: '1 a 47',
          imagem: 'https://example.com/op1.jpg',
          video: 'https://v.animethemes.moe/OnePiece-OP1.webm',
          ativa: true,
          quantidadePreferencias: 3,
        },
      ],
    };

    vi.stubGlobal(
      'fetch',
      vi.fn(async (input: RequestInfo | URL, init?: RequestInit) => {
        const url = String(input);
        if (url === '/backend/autenticacao') {
          return Response.json({
            usuario: 'a',
            abertura: { incluir: true, buscar: true, alterar: true, excluir: true },
          });
        }
        if (url.startsWith('/backend/adm/abertura/pesquisa')) {
          return Response.json(catalogo);
        }
        if (url === '/backend/adm/abertura/incluir' && init?.method === 'POST') {
          const nova = {
            id: '2',
            numero: 26,
            nome: 'UUUUUS!',
            artista: 'Hiroshi Kitadani',
            episodios: '1089 a 1122',
            imagem: 'https://example.com/op26.jpg',
            video: 'https://v.animethemes.moe/OnePiece-OP26.webm',
            ativa: true,
            quantidadePreferencias: 0,
          };
          catalogo = {
            total: 2,
            ativas: 2,
            inativas: 0,
            itens: [...catalogo.itens, nova],
          };
          return Response.json(nova);
        }
        if (url === '/backend/adm/abertura/1/inativar' && init?.method === 'POST') {
          catalogo = {
            ...catalogo,
            ativas: catalogo.ativas - 1,
            inativas: catalogo.inativas + 1,
            itens: catalogo.itens.map((i) => (i.id === '1' ? { ...i, ativa: false } : i)),
          };
          return new Response(null, { status: 204 });
        }
        if (url === '/backend/adm/abertura/1/excluir' && init?.method === 'POST') {
          return Response.json(
            {
              mensagem:
                'A abertura já foi escolhida por usuários e só pode ser inativada.',
            },
            { status: 409 },
          );
        }
        return new Response(null, { status: 404 });
      }),
    );

    const user = userEvent.setup();
    renderizar('/adm/aberturas');

    expect(await screen.findByRole('heading', { name: 'Gestão de Aberturas' })).toBeInTheDocument();
    expect(await screen.findByText('We Are!')).toBeInTheDocument();

    // Cadastrar nova abertura
    await user.click(screen.getByRole('button', { name: /\+ Cadastrar abertura/i }));
    const dialogo = await screen.findByRole('dialog');
    await user.type(within(dialogo).getByLabelText('Número da Abertura *'), '26');
    await user.type(within(dialogo).getByLabelText('Nome da Abertura *'), 'UUUUUS!');
    await user.type(within(dialogo).getByLabelText('Artista / Intérprete *'), 'Hiroshi Kitadani');
    await user.type(within(dialogo).getByLabelText('Episódios *'), '1089 a 1122');
    await user.type(
      within(dialogo).getByLabelText('URL da Imagem de Capa (Aspecto 16:9) *'),
      'https://example.com/op26.jpg',
    );
    await user.type(
      within(dialogo).getByLabelText('Referência de Vídeo Público (AnimeThemes / URL) *'),
      'https://v.animethemes.moe/OnePiece-OP26.webm',
    );
    await user.click(within(dialogo).getByRole('button', { name: 'Cadastrar abertura' }));

    expect(await screen.findByText('UUUUUS!')).toBeInTheDocument();

    // Inativar
    await user.click(screen.getAllByRole('button', { name: 'Inativar' })[0]!);
    expect(await screen.findByText('• Inativa')).toBeInTheDocument();

    // Bloqueio RN006 na exclusão de abertura já escolhida
    await user.click(screen.getAllByRole('button', { name: 'Excluir' })[0]!);
    const dialogoExcluir = await screen.findByRole('dialog');
    expect(within(dialogoExcluir).getByText(/Atenção \(RN006\)/i)).toBeInTheDocument();
    await user.click(within(dialogoExcluir).getByRole('button', { name: 'Confirmar Exclusão' }));

    expect(
      await screen.findByText('A abertura já foi escolhida por usuários e só pode ser inativada.'),
    ).toBeInTheDocument();
  });
});
