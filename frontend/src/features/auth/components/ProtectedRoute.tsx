import { Navigate, Outlet, useLocation } from 'react-router';
import { useAutenticacao } from '../api/authApi';

/**
 * O loader `exigirAutenticacao` barra quem chega sem sessão; este componente cobre a sessão que
 * termina com a página aberta (por exemplo, quando o usuário desiste de reautenticar).
 */
export function ProtectedRoute() {
  const { autenticado } = useAutenticacao();
  const { pathname, search } = useLocation();

  if (!autenticado) {
    return <Navigate to={`/login?retorno=${encodeURIComponent(pathname + search)}`} replace />;
  }
  return <Outlet />;
}
