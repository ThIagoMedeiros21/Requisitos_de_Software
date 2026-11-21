import { Icon } from '../../../../components/ui/Icon';
import { useProfileUiStore } from '../../profile/stores/useProfileUiStore';
import { useRemoverAmizade } from '../api/friendsQueries';

export function RemoveFriendModal() {
  const alvoRemocao = useProfileUiStore((estado) => estado.alvoRemocaoAmizade);
  const fecharConfirmacao = useProfileUiStore((estado) => estado.fecharConfirmacaoRemocao);
  const remover = useRemoverAmizade();

  if (!alvoRemocao) {
    return null;
  }

  async function confirmarRemocao() {
    if (!alvoRemocao) {
      return;
    }
    try {
      await remover.mutateAsync(alvoRemocao.id);
      fecharConfirmacao();
    } catch {
      // Erro publicado pelo hook `useRemoverAmizade`.
    }
  }

  return (
    <div
      id="unfriend-confirm-modal"
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-confirmar-remocao"
      className="fixed inset-0 z-50 flex items-center justify-center bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      <div className="w-full max-w-md space-y-4 overflow-hidden rounded-2xl bg-pure-white p-6 shadow-level-4">
        <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-error-container/40 text-error">
          <Icon nome="person_remove" className="text-[28px]" />
        </div>

        <div className="space-y-2">
          <h2
            id="titulo-confirmar-remocao"
            className="font-headline-md text-headline-md text-deep-navy"
          >
            Encerrar Amizade Bilateral?
          </h2>
          <p className="font-body-sm text-body-sm leading-relaxed text-text-muted">
            Você está prestes a remover o colecionador{' '}
            <strong className="text-deep-navy">{alvoRemocao.apelido}</strong> da sua rede. Em
            conformidade com a <strong>RN006</strong>, a remoção é <strong>bilateral</strong>: o
            vínculo será desfeito imediatamente para ambos os lados.
          </p>
        </div>

        <div className="flex items-start gap-2 rounded-xl bg-surface-container-low p-3 text-[12px] text-text-muted">
          <Icon nome="info" className="text-[18px] text-ocean-blue" />
          <span>
            Ambos os colecionadores deixarão de constar na lista de amigos um do outro
            simultaneamente.
          </span>
        </div>

        <div className="flex items-center justify-end gap-2.5 pt-2">
          <button
            type="button"
            onClick={fecharConfirmacao}
            className="rounded-xl bg-surface-container-low px-4 py-2 font-label-sm text-label-sm font-semibold text-secondary transition-colors hover:bg-surface-container"
          >
            Cancelar
          </button>
          <button
            type="button"
            disabled={remover.isPending}
            onClick={confirmarRemocao}
            className="flex items-center gap-1.5 rounded-xl bg-error px-4 py-2 font-label-sm text-label-sm font-semibold text-on-error shadow-sm transition-colors hover:opacity-90 disabled:opacity-50"
          >
            <Icon nome="person_remove" className="text-[16px]" />
            <span>Confirmar Remoção</span>
          </button>
        </div>
      </div>
    </div>
  );
}
