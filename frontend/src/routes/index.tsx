import { createBrowserRouter, Navigate, useParams } from 'react-router';
import { AppShell } from '../components/layout/AppShell';
import { ErrorFallback } from '../components/layout/ErrorFallback';
import { RouteLoading } from '../components/layout/RouteLoading';
import { exigirAutenticacao } from '../features/auth/api/authApi';
import { ProtectedRoute } from '../features/auth/components/ProtectedRoute';

function RedirecionarPerfilPorId() {
  const { id } = useParams();
  return <Navigate to={`/comunidade/perfil/${encodeURIComponent(id ?? '')}`} replace />;
}

export const rotas = [
  {
    path: '/',
    Component: AppShell,
    ErrorBoundary: ErrorFallback,
    HydrateFallback: RouteLoading,
    children: [
      {
        path: 'login',
        lazy: async () => ({
          Component: (await import('../features/auth/pages/LoginPage')).LoginPage,
        }),
      },
      {
        Component: ProtectedRoute,
        ErrorBoundary: ErrorFallback,
        loader: exigirAutenticacao,
        children: [
          { index: true, element: <Navigate to="/comunidade/perfil" replace /> },
          { path: 'inicio', element: <Navigate to="/comunidade/perfil" replace /> },
          {
            path: 'comunidade/perfil',
            lazy: async () => {
              const [{ ProfilePage }, { carregarProprioPerfil }] = await Promise.all([
                import('../features/community/profile/pages/ProfilePage'),
                import('../features/community/profile/api/profileQueries'),
              ]);
              return { Component: ProfilePage, loader: carregarProprioPerfil };
            },
          },
          {
            path: 'comunidade/perfil/:id',
            lazy: async () => {
              const [{ ProfilePage }, { carregarPerfilPorId }] = await Promise.all([
                import('../features/community/profile/pages/ProfilePage'),
                import('../features/community/profile/api/profileQueries'),
              ]);
              return { Component: ProfilePage, loader: carregarPerfilPorId };
            },
          },
          {
            path: 'comunidade/amigos',
            lazy: async () => {
              const [{ FriendsNetworkPage }, { carregarRedeAmizades }] = await Promise.all([
                import('../features/community/friends/pages/FriendsNetworkPage'),
                import('../features/community/friends/api/friendsQueries'),
              ]);
              return { Component: FriendsNetworkPage, loader: carregarRedeAmizades };
            },
          },
          {
            path: 'comunidade/colecionadores',
            lazy: async () => {
              const [{ DiscoverCollectorsPage }, { carregarColecionadores }] = await Promise.all([
                import('../features/community/friends/pages/DiscoverCollectorsPage'),
                import('../features/community/friends/api/friendsQueries'),
              ]);
              return { Component: DiscoverCollectorsPage, loader: carregarColecionadores };
            },
          },
          // Compatibilidade com caminhos legados D5
          {
            path: 'comunidade/perfil-comunitario/visualizacao',
            element: <Navigate to="/comunidade/perfil" replace />,
          },
          {
            path: 'comunidade/perfil-comunitario/:id/visualizacao',
            Component: RedirecionarPerfilPorId,
          },
          {
            path: 'comunidade/perfil-comunitario/:id/alteracao',
            lazy: async () => {
              const [{ ProfilePage }, { carregarPerfilPorId }] = await Promise.all([
                import('../features/community/profile/pages/ProfilePage'),
                import('../features/community/profile/api/profileQueries'),
              ]);
              return { Component: ProfilePage, loader: carregarPerfilPorId };
            },
          },
          {
            path: 'comunidade/amizade/pesquisa',
            element: <Navigate to="/comunidade/amigos" replace />,
          },
          {
            path: 'comunidade/amizade/inclusao',
            element: <Navigate to="/comunidade/colecionadores" replace />,
          },
          {
            path: 'comunidade/amizade/:id/visualizacao',
            element: <Navigate to="/comunidade/amigos" replace />,
          },
        ],
      },
    ],
  },
];

export const roteador = createBrowserRouter(rotas);
