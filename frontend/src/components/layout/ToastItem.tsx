import { useEffect } from 'react';
import type { Toast } from '../../stores/useToastStore';
import { Icon } from '../ui/Icon';

const DURACAO_AUTOMATICA_MS = 8000;

const ICONE_POR_TIPO = {
  erro: 'error',
  sucesso: 'check_circle',
  info: 'info',
} as const;

export function ToastItem({ toast, aoRemover }: { toast: Toast; aoRemover: (id: number) => void }) {
  const { id, tipo, mensagem } = toast;

  useEffect(() => {
    // Erros ficam até serem dispensados; os demais somem sozinhos.
    if (tipo === 'erro') {
      return;
    }
    const temporizador = setTimeout(() => aoRemover(id), DURACAO_AUTOMATICA_MS);
    return () => clearTimeout(temporizador);
  }, [id, tipo, aoRemover]);

  return (
    <div
      role={tipo === 'erro' ? 'alert' : 'status'}
      className="flex items-center gap-space-sm rounded-xl bg-deep-navy px-space-md py-3 font-body-md text-body-md text-pure-white shadow-level-4"
    >
      <Icon
        nome={ICONE_POR_TIPO[tipo]}
        className={`text-[20px] ${tipo === 'erro' ? 'text-error-container' : 'text-sky-tint'}`}
      />
      <span className="flex-1">{mensagem}</span>
      <button
        type="button"
        aria-label="Dispensar notificação"
        onClick={() => aoRemover(id)}
        className="rounded-lg p-1 text-sky-tint transition-colors hover:bg-ocean-blue"
      >
        <Icon nome="close" className="text-[18px]" />
      </button>
    </div>
  );
}
