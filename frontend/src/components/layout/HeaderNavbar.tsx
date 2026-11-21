import { useState } from 'react';
import { Link, useNavigate } from 'react-router';
import { sair, useAutenticacao } from '../../features/auth/api/authApi';
import { useToastStore } from '../../stores/useToastStore';
import { Icon } from '../ui/Icon';

export function HeaderNavbar() {
  const { autenticado, usuario, autorizacoesDe } = useAutenticacao();
  const publicar = useToastStore((estado) => estado.publicar);
  const navegar = useNavigate();
  const [gavetaAberta, definirGavetaAberta] = useState(false);

  const podeBuscarPerfil = Boolean(autorizacoesDe('perfilComunitario')?.buscar);
  const podeBuscarAmizade = Boolean(autorizacoesDe('amizade')?.buscar);

  async function aoSair() {
    definirGavetaAberta(false);
    await sair();
    navegar('/login');
  }

  return (
    <>
      <header className="fixed top-0 z-50 w-full bg-pure-white/95 shadow-[0_1px_8px_rgba(10,37,64,0.06)] backdrop-blur-xl">
        <div className="mx-auto flex h-16 max-w-7xl items-center justify-between gap-space-md px-margin md:px-margin-lg">
          <div className="flex flex-shrink-0 items-center gap-space-lg">
            {autenticado && (
              <button
                type="button"
                aria-label="Abrir Menu de Navegação"
                onClick={() => definirGavetaAberta(true)}
                className="mr-1 flex items-center justify-center rounded-lg p-2 text-on-surface-variant transition-colors hover:bg-surface-container-low hover:text-on-surface lg:hidden"
              >
                <Icon nome="menu" className="text-[24px]" />
              </button>
            )}

            <Link
              to="/"
              className="navbar-brand flex items-center gap-space-xs font-headline-md text-headline-md tracking-tight text-primary no-underline"
            >
              <Icon nome="style" className="text-[26px] text-ocean-blue" />
              <span>
                TCG<span className="font-extrabold text-ocean-blue">Hub</span>
              </span>
            </Link>

            {autenticado && (
              <nav aria-label="Navegação principal" className="hidden items-center gap-space-sm lg:flex">
                <button
                  type="button"
                  onClick={() => publicar('info', 'Catálogo disponível em breve.')}
                  className="rounded-lg px-3 py-1.5 font-label-md text-label-md text-on-surface-variant transition-colors hover:bg-surface-container-low hover:text-on-surface"
                >
                  Catálogo
                </button>
                <button
                  type="button"
                  onClick={() => publicar('info', 'Decks disponíveis na próxima etapa.')}
                  className="rounded-lg px-3 py-1.5 font-label-md text-label-md text-on-surface-variant transition-colors hover:bg-surface-container-low hover:text-on-surface"
                >
                  Decks
                </button>
                <Link
                  to="/comunidade/perfil"
                  aria-current="page"
                  className="relative rounded-lg bg-primary px-3 py-1.5 font-label-md text-label-md font-bold text-pure-white no-underline shadow-sm transition-colors"
                >
                  Comunidade
                  <span className="absolute right-2 bottom-0 left-2 h-0.5 rounded-full bg-cobalt-accent" />
                </Link>
                {podeBuscarPerfil && (
                  <Link
                    to="/comunidade/perfil"
                    className="rounded-lg px-3 py-1.5 font-label-md text-label-md text-on-surface-variant no-underline transition-colors hover:bg-surface-container-low hover:text-on-surface"
                  >
                    Meu Perfil
                  </Link>
                )}
                {podeBuscarAmizade && (
                  <Link
                    to="/comunidade/amigos"
                    className="rounded-lg px-3 py-1.5 font-label-md text-label-md text-on-surface-variant no-underline transition-colors hover:bg-surface-container-low hover:text-on-surface"
                  >
                    Amigos
                  </Link>
                )}
                <button
                  type="button"
                  onClick={() => publicar('info', 'Wishlist disponível em breve.')}
                  className="rounded-lg px-3 py-1.5 font-label-md text-label-md text-on-surface-variant transition-colors hover:bg-surface-container-low hover:text-on-surface"
                >
                  Wishlist
                </button>
              </nav>
            )}
          </div>

          {autenticado && (
            <>
              <div className="mx-space-sm hidden max-w-md flex-1 sm:block">
                <div className="relative flex items-center">
                  <Icon
                    nome="search"
                    className="pointer-events-none absolute left-3 text-[20px] text-outline"
                  />
                  <input
                    type="text"
                    aria-label="Buscar carta por código"
                    placeholder="Buscar por código (ex: OP05-060, ST10-001)..."
                    className="w-full rounded-lg bg-surface-container-lowest py-2 pr-12 pl-10 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border transition-all outline-none placeholder:text-outline focus:ring-2 focus:ring-ocean-blue"
                  />
                  <kbd className=" absolute right-3 rounded border border-steel-border bg-surface-container px-1.5 py-0.5 font-mono text-[10px] text-secondary">
                    ⌘K
                  </kbd>
                </div>
              </div>

              <div className="flex flex-shrink-0 items-center gap-space-sm">
                <button
                  type="button"
                  aria-label="Carrinho de Compras"
                  onClick={() => publicar('info', 'Carrinho do marketplace.')}
                  className="relative rounded-lg p-2 text-on-surface-variant transition-colors hover:bg-surface-container-low hover:text-on-surface"
                >
                  <Icon nome="shopping_bag" className="text-[24px]" />
                  <span className="absolute top-1 right-1 flex h-4 w-4 items-center justify-center rounded-full bg-ocean-blue font-label-sm text-[10px] leading-tight font-bold text-pure-white">
                    3
                  </span>
                </button>

                <Link
                  to="/comunidade/perfil"
                  title="Ver Meu Perfil"
                  className="flex items-center gap-1.5 rounded-full bg-surface-container-low py-1 pr-3 pl-1 text-deep-navy no-underline transition-colors hover:bg-sky-tint"
                >
                  <span className="flex h-7 w-7 items-center justify-center rounded-full bg-primary text-on-primary">
                    <Icon nome="person" className="text-[16px]" />
                  </span>
                  <span className="font-label-sm text-label-sm font-semibold">{usuario}</span>
                </Link>

                <button
                  type="button"
                  onClick={aoSair}
                  className="rounded-lg bg-surface-container-low px-3 py-1.5 font-label-sm text-label-sm font-semibold text-secondary transition-colors hover:bg-surface-container hover:text-deep-navy"
                >
                  Sair
                </button>
              </div>
            </>
          )}
        </div>
      </header>

      {autenticado && gavetaAberta && (
        <div className="fixed inset-0 z-50 lg:hidden">
          <div
            aria-hidden="true"
            onClick={() => definirGavetaAberta(false)}
            className="fixed inset-0 bg-deep-navy/60 backdrop-blur-sm"
          />
          <aside className="fixed top-0 right-0 z-50 flex h-full w-72 max-w-[85vw] flex-col justify-between border-l border-ice-border bg-pure-white shadow-level-4">
            <div className="flex items-center justify-between border-b border-ice-border bg-surface-container-low/40 p-space-md">
              <div className="flex items-center gap-space-xs font-headline-md text-headline-md tracking-tight text-primary">
                <Icon nome="style" className="text-[24px] text-ocean-blue" />
                <span>
                  TCG<span className="font-extrabold text-ocean-blue">Hub</span>
                </span>
              </div>
              <button
                type="button"
                aria-label="Fechar Menu"
                onClick={() => definirGavetaAberta(false)}
                className="rounded-lg p-2 text-secondary transition-colors hover:bg-surface-container hover:text-deep-navy"
              >
                <Icon nome="close" className="text-[22px]" />
              </button>
            </div>

            <div className="flex-1 space-y-2 overflow-y-auto p-space-md">
              <Link
                to="/comunidade/perfil"
                onClick={() => definirGavetaAberta(false)}
                className="flex items-center gap-2 rounded-xl bg-primary px-3.5 py-2.5 font-label-md text-label-md font-semibold text-pure-white no-underline"
              >
                <Icon nome="badge" className="text-[18px]" />
                Meu Perfil
              </Link>
              <Link
                to="/comunidade/amigos"
                onClick={() => definirGavetaAberta(false)}
                className="flex items-center gap-2 rounded-xl px-3.5 py-2.5 font-label-md text-label-md font-semibold text-deep-navy no-underline hover:bg-surface-container-low"
              >
                <Icon nome="group" className="text-[18px]" />
                Rede de Amigos
              </Link>
              <Link
                to="/comunidade/colecionadores"
                onClick={() => definirGavetaAberta(false)}
                className="flex items-center gap-2 rounded-xl px-3.5 py-2.5 font-label-md text-label-md font-semibold text-deep-navy no-underline hover:bg-surface-container-low"
              >
                <Icon nome="person_search" className="text-[18px]" />
                Buscar Colecionadores
              </Link>
            </div>
          </aside>
        </div>
      )}
    </>
  );
}
