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
        path: 'cadastro',
        lazy: async () => ({
          Component: (await import('../features/auth/pages/RegisterPage')).RegisterPage,
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
            path: 'comunidade/decks-favoritos',
            lazy: async () => {
              const [{ FavoriteDecksPage }, { carregarDecksFavoritos }] = await Promise.all([
                import('../features/community/decks/pages/FavoriteDecksPage'),
                import('../features/community/decks/api/decksQueries'),
              ]);
              return { Component: FavoriteDecksPage, loader: carregarDecksFavoritos };
            },
          },
          {
            path: 'comunidade/decks',
            element: <Navigate to="/comunidade/decks-favoritos" replace />,
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
          {
            path: 'comunidade/avaliacoes',
            lazy: async () => {
              const [{ CardReviewsCatalogPage }, { carregarCatalogoAvaliacoes }] = await Promise.all([
                import('../features/community/reviews/pages/CardReviewsCatalogPage'),
                import('../features/community/reviews/api/reviewsQueries'),
              ]);
              return { Component: CardReviewsCatalogPage, loader: carregarCatalogoAvaliacoes };
            },
          },
          {
            path: 'comunidade/avaliacoes/:produtoId',
            lazy: async () => {
              const [{ CardReviewDetailPage }, { carregarDetalhesAvaliacoesCarta }] =
                await Promise.all([
                  import('../features/community/reviews/pages/CardReviewDetailPage'),
                  import('../features/community/reviews/api/reviewsQueries'),
                ]);
              return { Component: CardReviewDetailPage, loader: carregarDetalhesAvaliacoesCarta };
            },
          },
          {
            path: 'comunidade/aberturas',
            lazy: async () => {
              const [{ FavoriteOpeningsPage }, { carregarAberturasComunidade }] = await Promise.all([
                import('../features/community/openings/pages/FavoriteOpeningsPage'),
                import('../features/community/openings/api/openingsQueries'),
              ]);
              return { Component: FavoriteOpeningsPage, loader: carregarAberturasComunidade };
            },
          },
          {
            path: 'adm/aberturas',
            lazy: async () => ({
              Component: (await import('../features/adm/openings/pages/AdminOpeningsPage'))
                .AdminOpeningsPage,
            }),
          },
          // Compatibilidade com caminhos legados D5
          {
            path: 'comunidade/avaliacao-produto/pesquisa',
            element: <Navigate to="/comunidade/avaliacoes" replace />,
          },
          {
            path: 'comunidade/abertura-preferida',
            element: <Navigate to="/comunidade/aberturas" replace />,
          },
          {
            path: 'comunidade/abertura-preferida/pesquisa',
            element: <Navigate to="/comunidade/aberturas" replace />,
          },
          {
            path: 'comunidade/abertura-preferida/ranking',
            element: <Navigate to="/comunidade/aberturas" replace />,
          },
          {
            path: 'adm/abertura/pesquisa',
            element: <Navigate to="/adm/aberturas" replace />,
          },
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
            path: 'comunidade/deck-favorito/pesquisa',
            element: <Navigate to="/comunidade/decks-favoritos" replace />,
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
