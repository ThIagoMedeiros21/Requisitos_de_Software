import { Icon } from '../../../../components/ui/Icon';
import { useToastStore } from '../../../../stores/useToastStore';
import type { PerfilComunitarioVisaoDto } from '../types/profile.types';

export function ProfileWishlistPreview({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const publicar = useToastStore((estado) => estado.publicar);
  const linkToken = `tcghub.com/w/${perfil.usuario}`;

  function copiarToken() {
    if (navigator.clipboard?.writeText) {
      void navigator.clipboard.writeText(`https://${linkToken}`).catch(() => undefined);
    }
    publicar('sucesso', 'Link da Wishlist pública copiado!');
  }

  return (
    <section className="flex flex-col justify-between rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
      <div className="space-y-space-sm">
        <div className="flex items-center justify-between gap-2">
          <div className="flex items-center gap-2">
            <Icon nome="bookmark" className="text-[22px] text-ocean-blue" />
            <h2 className="font-headline-md text-headline-md text-deep-navy">Wishlist Pública</h2>
          </div>
          <span className="rounded-full bg-status-positive-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-positive-text">
            Token Seguro Ativo
          </span>
        </div>

        <p className="font-body-sm text-body-sm text-text-muted">
          {perfil.preferenciasPublicas?.trim()
            ? `Preferências: ${perfil.preferenciasPublicas}`
            : 'Permite que membros da comunidade consultem prioridades de cartas e proponham trocas.'}
        </p>
      </div>

      <div className="mt-space-md flex flex-wrap items-center justify-between gap-2 rounded-lg bg-surface-container-low p-2.5">
        <code className="truncate font-mono text-xs text-deep-navy">{linkToken}</code>
        <button
          type="button"
          onClick={copiarToken}
          className="inline-flex items-center gap-1 rounded-md bg-pure-white px-2.5 py-1 font-label-sm text-label-sm font-semibold text-ocean-blue shadow-sm transition-colors hover:bg-sky-tint"
        >
          <Icon nome="content_copy" className="text-[15px]" />
          <span>Copiar</span>
        </button>
      </div>
    </section>
  );
}
