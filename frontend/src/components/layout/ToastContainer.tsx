import { useToastStore } from '../../stores/useToastStore';
import { ToastItem } from './ToastItem';

export function ToastContainer() {
  const toasts = useToastStore((estado) => estado.toasts);
  const remover = useToastStore((estado) => estado.remover);

  return (
    <div
      id="tcg-toast"
      aria-live="polite"
      className="pointer-events-none fixed bottom-6 left-1/2 z-[60] flex w-[min(92vw,28rem)] -translate-x-1/2 flex-col gap-space-sm"
    >
      {toasts.map((toast) => (
        <div key={toast.id} className="pointer-events-auto">
          <ToastItem toast={toast} aoRemover={remover} />
        </div>
      ))}
    </div>
  );
}
