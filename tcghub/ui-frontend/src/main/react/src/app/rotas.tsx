import { createBrowserRouter, Navigate } from 'react-router';
import { exigirAutenticacao, RotaProtegida } from '../d5';
import { ErroRota } from '../d5/bootstrap/ErroRota';
import { App } from './App';

export const rotas = [
  {
    path: '/',
    Component: App,
    // Caminhos que não correspondem a nenhuma rota
    ErrorBoundary: ErroRota,
    children: [
      {
        path: 'login',
        lazy: async () => ({ Component: (await import('./login/Login')).Login }),
      },
      {
        // Adicione aqui as rotas de cada agregado, dentro desta rota protegida.
        Component: RotaProtegida,
        // Erros dos loaders das rotas filhas, exibidos dentro do layout, com o menu
        ErrorBoundary: ErroRota,
        loader: exigirAutenticacao,
        children: [
          { index: true, element: <Navigate to="/inicio" replace /> },
          {
            path: 'inicio',
            lazy: async () => ({ Component: (await import('./inicio/Inicio')).Inicio }),
          },
          {
            path: 'comunidade/perfil-comunitario/visualizacao',
            lazy: async () => {
              const [{ PerfilVisualizacao }, { carregarProprioPerfil }] = await Promise.all([
                import('./comunidade/perfil/PerfilVisualizacao'),
                import('./comunidade/perfil/perfilConsultas'),
              ]);
              return { Component: PerfilVisualizacao, loader: carregarProprioPerfil };
            },
          },
          {
            path: 'comunidade/perfil-comunitario/:id/visualizacao',
            lazy: async () => {
              const [{ PerfilVisualizacao }, { carregarPerfilPorId }] = await Promise.all([
                import('./comunidade/perfil/PerfilVisualizacao'),
                import('./comunidade/perfil/perfilConsultas'),
              ]);
              return { Component: PerfilVisualizacao, loader: carregarPerfilPorId };
            },
          },
          {
            path: 'comunidade/perfil-comunitario/:id/alteracao',
            lazy: async () => {
              const [{ PerfilEdicao }, { carregarPerfilAlteracao }] = await Promise.all([
                import('./comunidade/perfil/PerfilEdicao'),
                import('./comunidade/perfil/perfilConsultas'),
              ]);
              return { Component: PerfilEdicao, loader: carregarPerfilAlteracao };
            },
          },
          {
            path: 'comunidade/amizade/pesquisa',
            lazy: async () => {
              const [{ AmizadePesquisa }, { carregarRedeAmizades }] = await Promise.all([
                import('./comunidade/amizade/AmizadePesquisa'),
                import('./comunidade/amizade/amizadeConsultas'),
              ]);
              return { Component: AmizadePesquisa, loader: carregarRedeAmizades };
            },
          },
          {
            path: 'comunidade/amizade/inclusao',
            lazy: async () => {
              const [{ AmizadeInclusao }, { carregarColecionadores }] = await Promise.all([
                import('./comunidade/amizade/AmizadeInclusao'),
                import('./comunidade/amizade/amizadeConsultas'),
              ]);
              return { Component: AmizadeInclusao, loader: carregarColecionadores };
            },
          },
          {
            path: 'comunidade/amizade/:id/visualizacao',
            lazy: async () => {
              const [{ AmizadeVisualizacao }, { carregarAmizadePorId }] = await Promise.all([
                import('./comunidade/amizade/AmizadeVisualizacao'),
                import('./comunidade/amizade/amizadeConsultas'),
              ]);
              return { Component: AmizadeVisualizacao, loader: carregarAmizadePorId };
            },
          },
        ],
      },
    ],
  },
];

export const roteador = createBrowserRouter(rotas);
