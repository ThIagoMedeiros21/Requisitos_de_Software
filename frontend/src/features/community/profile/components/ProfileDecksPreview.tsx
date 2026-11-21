import { Icon } from '../../../../components/ui/Icon';
import { useToastStore } from '../../../../stores/useToastStore';
import type { PerfilComunitarioVisaoDto } from '../types/profile.types';

export function ProfileDecksPreview({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const publicar = useToastStore((estado) => estado.publicar);

  if (!perfil.ehProprietario && !perfil.decksFavoritosVisiveis) {
    return null;
  }

  return (
    <section className="flex flex-col justify-between rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
      <div className="space-y-space-sm">
        <div className="flex items-center justify-between gap-2">
          <div className="flex items-center gap-2">
            <Icon nome="view_carousel" className="text-[22px] text-ocean-blue" />
            <h2 className="font-headline-md text-headline-md text-deep-navy">Decks Favoritos</h2>
          </div>

          {perfil.ehProprietario && !perfil.decksFavoritosVisiveis ? (
            <span className="rounded-full bg-surface-container px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-secondary">
              Oculto no perfil público
            </span>
          ) : (
            <span className="rounded-full bg-sky-tint px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-ocean-blue">
              Formato Oficial Bandai (1 Líder + 50 Cartas)
            </span>
          )}
        </div>

        <p className="font-body-sm text-body-sm text-text-muted">
          {perfil.decksFavoritosVisiveis
            ? 'Vitrine pública de decks favoritos habilitada para consulta de listas competitivas na comunidade.'
            : 'Seus decks favoritos estão configurados como privados e não são exibidos para visitantes.'}
        </p>
      </div>

      {perfil.ehProprietario && (
        <div className="pt-space-md">
          <button
            type="button"
            onClick={() => publicar('info', 'Gestão de Decks Favoritos (F002) na próxima etapa.')}
            className="inline-flex items-center gap-1.5 rounded-lg bg-surface-container-low px-4 py-2 font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint"
          >
            <Icon nome="add" className="text-[18px]" />
            <span>+ Adicionar deck favorito</span>
          </button>
        </div>
      )}
    </section>
  );
}
