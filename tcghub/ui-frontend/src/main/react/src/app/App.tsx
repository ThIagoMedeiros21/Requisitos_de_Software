import { Link, Outlet, useNavigate } from 'react-router';
import { NotificacoesProvider, sair, useAutenticacao } from '../d5';
import { Notificacoes } from '../d5/bootstrap/Notificacoes';
import { ReautenticacaoProvider } from '../d5/bootstrap/ReautenticacaoProvider';

export function App() {
  const { autenticado, autorizacoesDe } = useAutenticacao();
  const navegar = useNavigate();
  const podeBuscarPerfil = Boolean(autorizacoesDe('perfilComunitario')?.buscar);
  const podeBuscarAmizade = Boolean(autorizacoesDe('amizade')?.buscar);

  async function aoSair() {
    await sair();
    navegar('/login');
  }

  return (
    <NotificacoesProvider>
      <ReautenticacaoProvider>
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
          <div className="container">
            <Link className="navbar-brand d-flex align-items-center gap-2" to="/">
              <strong>Tcghub</strong>
            </Link>
            {autenticado && (
              <div className="navbar-collapse show">
                <ul className="navbar-nav">
                  {podeBuscarPerfil && (
                    <li className="nav-item">
                      <Link className="nav-link" to="/comunidade/perfil-comunitario/visualizacao">
                        Meu Perfil
                      </Link>
                    </li>
                  )}
                  {podeBuscarAmizade && (
                    <li className="nav-item">
                      <Link className="nav-link" to="/comunidade/amizade/pesquisa">
                        Amigos
                      </Link>
                    </li>
                  )}
                </ul>
                <ul className="navbar-nav ms-auto">
                  <li className="nav-item">
                    <button className="btn btn-link nav-link" type="button" onClick={aoSair}>
                      Sair
                    </button>
                  </li>
                </ul>
              </div>
            )}
          </div>
        </nav>
        <Notificacoes />
        <Outlet />
      </ReautenticacaoProvider>
    </NotificacoesProvider>
  );
}
