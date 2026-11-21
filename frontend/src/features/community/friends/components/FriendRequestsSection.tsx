import { Link } from 'react-router';
import { Avatar } from '../../../../components/ui/Avatar';
import { Icon } from '../../../../components/ui/Icon';
import {
  useAceitarSolicitacaoAmizade,
  useCancelarSolicitacaoAmizade,
  useRecusarSolicitacaoAmizade,
} from '../api/friendsQueries';
import type { ItemRedeAmizadeDto } from '../types/friends.types';

export function FriendRequestsSection({
  tipo,
  itens,
}: {
  tipo: 'recebidas' | 'enviadas';
  itens: ItemRedeAmizadeDto[];
}) {
  const aceitar = useAceitarSolicitacaoAmizade();
  const recusar = useRecusarSolicitacaoAmizade();
  const cancelar = useCancelarSolicitacaoAmizade();

  if (itens.length === 0) {
    return (
      <div className="rounded-xl border border-dashed border-steel-border bg-pure-white p-space-lg text-center font-body-md text-body-md text-text-muted">
        {tipo === 'recebidas'
          ? 'Nenhuma solicitação de amizade recebida.'
          : 'Nenhuma solicitação de amizade enviada.'}
      </div>
    );
  }

  return (
    <ul className="m-0 list-none space-y-3 p-0">
      {itens.map((item) => (
        <li
          key={item.amizadeId}
          className="flex flex-col justify-between gap-3 rounded-xl border border-ice-border bg-pure-white p-space-md shadow-sm sm:flex-row sm:items-center"
        >
          <div className="flex items-center gap-3">
            <Avatar apelido={item.apelido} foto={item.foto} className="h-12 w-12" />
            <div>
              <p className="font-headline-sm text-headline-sm text-deep-navy">{item.apelido}</p>
              <p className="font-body-sm text-body-sm text-text-muted">
                @{item.usuario}
                {item.cidade ? ` • ${item.cidade}` : ''}
              </p>
            </div>
          </div>

          <div className="flex flex-wrap items-center gap-2">
            <Link
              to={`/comunidade/perfil/${encodeURIComponent(item.perfilId)}`}
              className="rounded-lg bg-surface-container-low px-3.5 py-2 font-label-sm text-label-sm font-semibold text-ocean-blue no-underline transition-colors hover:bg-sky-tint"
            >
              Ver Perfil
            </Link>

            {tipo === 'recebidas' ? (
              <>
                <button
                  type="button"
                  disabled={aceitar.isPending}
                  onClick={() => aceitar.mutate(item.amizadeId)}
                  className="inline-flex items-center gap-1 rounded-lg bg-ocean-blue px-3.5 py-2 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
                >
                  <Icon nome="check" className="text-[16px]" />
                  <span>Aceitar</span>
                </button>
                <button
                  type="button"
                  disabled={recusar.isPending}
                  onClick={() => recusar.mutate(item.amizadeId)}
                  className="inline-flex items-center gap-1 rounded-lg bg-error-container px-3.5 py-2 font-label-sm text-label-sm font-semibold text-on-error-container transition-colors hover:opacity-90 disabled:opacity-50"
                >
                  <Icon nome="close" className="text-[16px]" />
                  <span>Recusar</span>
                </button>
              </>
            ) : (
              <button
                type="button"
                disabled={cancelar.isPending}
                onClick={() => cancelar.mutate(item.amizadeId)}
                className="inline-flex items-center gap-1 rounded-lg border border-steel-border bg-surface-container-low px-3.5 py-2 font-label-sm text-label-sm font-semibold text-secondary transition-colors hover:bg-surface-container hover:text-deep-navy disabled:opacity-50"
              >
                <Icon nome="cancel" className="text-[16px]" />
                <span>Cancelar Solicitação</span>
              </button>
            )}
          </div>
        </li>
      ))}
    </ul>
  );
}
