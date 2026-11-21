import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { Link } from 'react-router';
import { CommunitySubNav } from '../../../../components/layout/CommunitySubNav';
import { Icon } from '../../../../components/ui/Icon';
import {
  type SubAbaAmizade,
  useProfileUiStore,
} from '../../profile/stores/useProfileUiStore';
import { consultaRedeAmizades } from '../api/friendsQueries';
import { FriendCard } from '../components/FriendCard';
import { FriendRequestsSection } from '../components/FriendRequestsSection';
import { RemoveFriendModal } from '../components/RemoveFriendModal';

export function FriendsNetworkPage() {
  const { data: rede } = useSuspenseQuery(consultaRedeAmizades);
  const subAba = useProfileUiStore((estado) => estado.subAbaAmizade);
  const definirSubAba = useProfileUiStore((estado) => estado.definirSubAbaAmizade);
  const filtro = useProfileUiStore((estado) => estado.filtroAmigosRede);
  const definirFiltro = useProfileUiStore((estado) => estado.definirFiltroAmigosRede);
  const definirAbaAtiva = useProfileUiStore((estado) => estado.definirAbaAtiva);

  useEffect(() => {
    definirAbaAtiva('rede-amigos');
  }, [definirAbaAtiva]);

  const abas: { chave: SubAbaAmizade; rotulo: string; total: number }[] = [
    { chave: 'meus-amigos', rotulo: 'Meus Amigos', total: rede.amigos.length },
    { chave: 'recebidas', rotulo: 'Recebidas', total: rede.recebidas.length },
    { chave: 'enviadas', rotulo: 'Enviadas', total: rede.enviadas.length },
  ];

  const termo = filtro.trim().replace(/^@/, '').toLowerCase();
  const amigosFiltrados = rede.amigos.filter((amigo) => {
    if (!termo) {
      return true;
    }
    return (
      amigo.apelido.toLowerCase().includes(termo) ||
      amigo.usuario.toLowerCase().includes(termo) ||
      (amigo.cidade?.toLowerCase().includes(termo) ?? false)
    );
  });

  return (
    <div className="mx-auto max-w-7xl space-y-space-lg px-margin py-space-lg md:px-margin-lg">
      <CommunitySubNav abaAtiva="rede-amigos" totalAmigos={rede.amigos.length} />

      <section className="flex flex-col items-center justify-between gap-4 rounded-xl border border-ice-border/60 bg-pure-white p-4 shadow-sm lg:flex-row">
        <div
          role="tablist"
          aria-label="Filtros da rede de amigos"
          className="flex w-full items-center gap-2 overflow-x-auto pb-1 lg:w-auto lg:pb-0"
        >
          {abas.map((aba) => {
            const ativa = aba.chave === subAba;
            return (
              <button
                key={aba.chave}
                type="button"
                role="tab"
                aria-selected={ativa}
                onClick={() => definirSubAba(aba.chave)}
                className={`rounded-lg px-3.5 py-1.5 font-label-sm text-label-sm transition-all ${
                  ativa
                    ? 'bg-deep-navy font-semibold text-pure-white shadow-sm'
                    : 'bg-surface-container-low font-medium text-secondary hover:bg-surface-container hover:text-deep-navy'
                }`}
              >
                {aba.rotulo} ({aba.total})
              </button>
            );
          })}
        </div>

        <div className="flex w-full items-center gap-3 lg:w-auto">
          <div className="relative flex-1 sm:w-64">
            <Icon
              nome="search"
              className="pointer-events-none absolute top-2.5 left-3 text-[18px] text-secondary"
            />
            <label htmlFor="friend-search-filter" className="sr-only">
              Filtrar por apelido
            </label>
            <input
              id="friend-search-filter"
              type="text"
              value={filtro}
              onChange={(evento) => definirFiltro(evento.target.value)}
              placeholder="Filtrar por @apelido..."
              className="w-full rounded-lg bg-surface-container-low py-1.5 pr-3 pl-9 font-body-sm text-body-sm text-deep-navy transition-all outline-none placeholder:text-outline focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>

          <Link
            to="/comunidade/colecionadores"
            className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-3.5 py-1.5 font-label-sm text-label-sm font-semibold text-pure-white no-underline shadow-sm transition-colors hover:bg-deep-navy"
          >
            <Icon nome="person_search" className="text-[16px]" />
            <span>Buscar Colecionadores</span>
          </Link>
        </div>
      </section>

      {subAba === 'meus-amigos' && (
        <section>
          {amigosFiltrados.length === 0 ? (
            <div className="rounded-xl border border-dashed border-steel-border bg-pure-white p-space-lg text-center font-body-md text-body-md text-text-muted">
              Você ainda não possui amigos adicionados.
            </div>
          ) : (
            <div id="friends-grid" className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
              {amigosFiltrados.map((amigo) => (
                <FriendCard key={amigo.amizadeId} amigo={amigo} />
              ))}
            </div>
          )}
        </section>
      )}

      {subAba === 'recebidas' && <FriendRequestsSection tipo="recebidas" itens={rede.recebidas} />}

      {subAba === 'enviadas' && <FriendRequestsSection tipo="enviadas" itens={rede.enviadas} />}

      <RemoveFriendModal />
    </div>
  );
}
