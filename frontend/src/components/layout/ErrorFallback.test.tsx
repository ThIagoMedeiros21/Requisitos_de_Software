import { render, screen } from '@testing-library/react';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { ErroHttp } from '../../lib/api';
import { ErrorFallback } from './ErrorFallback';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(
    [
      {
        path: '/',
        ErrorBoundary: ErrorFallback,
        children: [
          {
            path: 'comunidade/perfil/:id',
            loader: () => {
              throw new ErroHttp(404, { mensagem: 'Perfil comunitário não encontrado.' });
            },
            Component: () => null,
          },
        ],
      },
    ],
    { initialEntries: [caminho] },
  );
  render(<RouterProvider router={roteador} />);
}

describe('ErrorFallback', () => {
  it('exibe a mensagem do erro lançado pelo loader', async () => {
    renderizar('/comunidade/perfil/999');

    expect(await screen.findByText('Perfil comunitário não encontrado.')).toBeInTheDocument();
  });

  it('exibe que a página não foi encontrada para um caminho sem rota', async () => {
    renderizar('/inexistente');

    expect(await screen.findByText('Página não encontrada.')).toBeInTheDocument();
  });
});
