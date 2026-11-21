import { Avatar } from '../../../../components/ui/Avatar';
import { Icon } from '../../../../components/ui/Icon';
import { useToastStore } from '../../../../stores/useToastStore';
import {
  useAceitarSolicitacaoAmizade,
  useCancelarSolicitacaoAmizade,
  useEnviarSolicitacaoAmizade,
  useRecusarSolicitacaoAmizade,
  useRemoverAmizade,
} from '../../friends/api/friendsQueries';
import { useProfileUiStore } from '../stores/useProfileUiStore';
import type { PerfilComunitarioVisaoDto } from '../types/profile.types';

export function ProfileHeaderCard({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const abrirModalEdicao = useProfileUiStore((estado) => estado.abrirModalEdicao);
  const publicar = useToastStore((estado) => estado.publicar);

  const enviarSolicitacao = useEnviarSolicitacaoAmizade();
  const aceitarSolicitacao = useAceitarSolicitacaoAmizade();
  const recusarSolicitacao = useRecusarSolicitacaoAmizade();
  const cancelarSolicitacao = useCancelarSolicitacaoAmizade();
  const removerAmizade = useRemoverAmizade();

  const pendente =
    enviarSolicitacao.isPending ||
    aceitarSolicitacao.isPending ||
    recusarSolicitacao.isPending ||
    cancelarSolicitacao.isPending ||
    removerAmizade.isPending;

  function compartilharPerfil() {
    if (navigator.clipboard?.writeText) {
      void navigator.clipboard.writeText(window.location.href).catch(() => undefined);
    }
    publicar('sucesso', 'Link do perfil copiado para compartilhamento!');
  }

  return (
    <div className="rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
      <div className="flex flex-col justify-between gap-space-lg lg:flex-row lg:items-center">
        <div className="flex min-w-0 flex-col items-start gap-space-md sm:flex-row sm:items-center">
          <div className="relative flex-shrink-0">
            <div className="h-20 w-20 rounded-full bg-surface-container-low p-1 shadow-sm">
              <Avatar apelido={perfil.apelido} foto={perfil.foto} className="h-full w-full" />
            </div>
            <span
              title="Status: Online"
              className="absolute right-1 bottom-1 h-4 w-4 rounded-full bg-cobalt-accent ring-2 ring-pure-white"
            />
          </div>

          <div className="min-w-0 space-y-1.5">
            <div className="flex flex-wrap items-center gap-space-xs">
              <h1 className="truncate font-headline-lg text-headline-lg tracking-tight text-deep-navy">
                {perfil.apelido}
              </h1>
              <span className="inline-flex items-center gap-1 rounded-full bg-sky-tint px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-ocean-blue">
                <Icon nome="verified" preenchido className="text-[15px]" />
                <span>Colecionador Verificado</span>
              </span>
              {!perfil.ehProprietario && perfil.statusRelacao === 'AMIGO' && (
                <span className="rounded-full bg-status-positive-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-positive-text">
                  Amigo Mútuo
                </span>
              )}
            </div>

            <p className="max-w-2xl font-body-md text-body-md text-text-muted">
              {perfil.descricao?.trim() ? (
                perfil.descricao
              ) : (
                <span className="italic text-outline">Nenhuma descrição adicionada</span>
              )}
            </p>

            <div className="flex flex-wrap items-center gap-space-md pt-0.5">
              <div className="flex items-center gap-1 font-label-sm text-label-sm text-secondary">
                <Icon nome="alternate_email" className="text-[16px] text-ocean-blue" />
                <span>@{perfil.usuario}</span>
              </div>
              {perfil.cidade?.trim() && (
                <div className="flex items-center gap-1 font-label-sm text-label-sm text-secondary">
                  <Icon nome="location_on" className="text-[16px] text-ocean-blue" />
                  <span>{perfil.cidade}</span>
                </div>
              )}
              {perfil.preferenciasPublicas?.trim() && (
                <div className="flex items-center gap-1 font-label-sm text-label-sm font-semibold text-deep-navy">
                  <Icon nome="grade" preenchido className="text-[16px] text-ocean-blue" />
                  <span>{perfil.preferenciasPublicas}</span>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="flex items-start justify-between gap-space-sm pt-space-xs sm:flex-row sm:items-center lg:flex-col lg:items-end lg:pt-0">
          <div className="flex flex-wrap items-center gap-space-sm">
            {perfil.ehProprietario ? (
              <>
                <button
                  type="button"
                  onClick={abrirModalEdicao}
                  className="inline-flex items-center gap-1.5 rounded-lg bg-surface-container-low px-4 py-2 font-label-md text-label-md font-medium text-ocean-blue shadow-sm transition-colors hover:bg-sky-tint"
                >
                  <Icon nome="edit" className="text-[18px]" />
                  <span>Editar Perfil</span>
                </button>
                <button
                  type="button"
                  title="Compartilhar Perfil"
                  aria-label="Compartilhar Perfil"
                  onClick={compartilharPerfil}
                  className="rounded-lg bg-surface-container-low p-2 text-deep-navy transition-colors hover:bg-sky-tint"
                >
                  <Icon nome="share" className="text-[20px]" />
                </button>
              </>
            ) : (
              <>
                {perfil.statusRelacao === 'NENHUMA' && (
                  <button
                    type="button"
                    disabled={pendente}
                    onClick={() => enviarSolicitacao.mutate(perfil.usuario)}
                    className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-4 py-2 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
                  >
                    <Icon nome="person_add" className="text-[18px]" />
                    <span>Adicionar Amigo</span>
                  </button>
                )}

                {perfil.statusRelacao === 'PENDENTE_ENVIADA' && perfil.amizadeId && (
                  <button
                    type="button"
                    disabled={pendente}
                    onClick={() => cancelarSolicitacao.mutate(perfil.amizadeId!)}
                    className="inline-flex items-center gap-1.5 rounded-lg border border-steel-border bg-surface-container-low px-4 py-2 font-label-md text-label-md font-medium text-secondary transition-colors hover:bg-surface-container disabled:opacity-50"
                  >
                    <Icon nome="schedule" className="text-[18px]" />
                    <span>Cancelar Solicitação</span>
                  </button>
                )}

                {perfil.statusRelacao === 'PENDENTE_RECEBIDA' && perfil.amizadeId && (
                  <>
                    <button
                      type="button"
                      disabled={pendente}
                      onClick={() => aceitarSolicitacao.mutate(perfil.amizadeId!)}
                      className="inline-flex items-center gap-1 rounded-lg bg-ocean-blue px-4 py-2 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
                    >
                      <Icon nome="check" className="text-[18px]" />
                      <span>Aceitar</span>
                    </button>
                    <button
                      type="button"
                      disabled={pendente}
                      onClick={() => recusarSolicitacao.mutate(perfil.amizadeId!)}
                      className="inline-flex items-center gap-1 rounded-lg bg-error-container px-4 py-2 font-label-md text-label-md font-semibold text-on-error-container transition-colors hover:opacity-90 disabled:opacity-50"
                    >
                      <Icon nome="close" className="text-[18px]" />
                      <span>Recusar</span>
                    </button>
                  </>
                )}

                {perfil.statusRelacao === 'AMIGO' && perfil.amizadeId && (
                  <button
                    type="button"
                    disabled={pendente}
                    onClick={() => removerAmizade.mutate(perfil.amizadeId!)}
                    className="inline-flex items-center gap-1.5 rounded-lg bg-error-container/50 px-4 py-2 font-label-md text-label-md font-semibold text-error transition-colors hover:bg-error-container disabled:opacity-50"
                  >
                    <Icon nome="person_remove" className="text-[18px]" />
                    <span>Remover Amigo</span>
                  </button>
                )}
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
