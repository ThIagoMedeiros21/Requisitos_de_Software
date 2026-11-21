import { Link } from 'react-router';
import { Avatar } from '../../../../components/ui/Avatar';
import { Icon } from '../../../../components/ui/Icon';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import type { ItemRedeAmizadeDto } from '../types/friends.types';

export function FriendCard({ amigo }: { amigo: ItemRedeAmizadeDto }) {
  const abrirConfirmacao = useProfileUiStore((estado) => estado.abrirConfirmacaoRemocao);

  return (
    <article className="friend-card flex flex-col justify-between rounded-2xl border border-ice-border/60 bg-pure-white p-5 shadow-sm transition-shadow hover:shadow-md">
      <div className="space-y-4">
        <div className="flex items-start justify-between gap-2">
          <div className="flex min-w-0 items-center gap-3.5">
            <Avatar apelido={amigo.apelido} foto={amigo.foto} className="h-14 w-14 rounded-2xl" />
            <div className="min-w-0">
              <div className="flex items-center gap-1.5">
                <h3 className="truncate font-headline-sm text-headline-sm text-deep-navy">
                  {amigo.apelido}
                </h3>
                <Icon
                  nome="verified"
                  preenchido
                  className="text-[18px] text-ocean-blue"
                />
              </div>
              <p className="pt-0.5 font-body-sm text-body-sm text-text-muted">
                @{amigo.usuario}
                {amigo.cidade ? ` • ${amigo.cidade}` : ''}
              </p>
            </div>
          </div>

          <button
            type="button"
            title="Desfazer amizade bilateral"
            aria-label="Remover Amigo"
            onClick={() => abrirConfirmacao({ id: amigo.amizadeId, apelido: amigo.apelido })}
            className="inline-flex items-center gap-1 rounded-lg px-2 py-1.5 font-label-sm text-label-sm text-outline transition-colors hover:bg-error-container/30 hover:text-error"
          >
            <Icon nome="person_remove" className="text-[20px]" />
            <span>Remover Amigo</span>
          </button>
        </div>
      </div>

      <div className="mt-4 pt-3">
        <Link
          to={`/comunidade/perfil/${encodeURIComponent(amigo.perfilId)}`}
          className="flex w-full items-center justify-center gap-2 rounded-xl bg-ocean-blue px-4 py-2.5 font-label-sm text-label-sm font-semibold text-pure-white no-underline shadow-sm transition-colors hover:bg-deep-navy"
        >
          <Icon nome="person" className="text-[18px]" />
          <span>Ver Perfil</span>
        </Link>
      </div>
    </article>
  );
}
