import { create } from 'zustand';

export type TipoToast = 'erro' | 'sucesso' | 'info';

export interface Toast {
  id: number;
  tipo: TipoToast;
  mensagem: string;
}

interface EstadoToast {
  toasts: Toast[];
  publicar: (tipo: TipoToast, mensagem: string) => void;
  remover: (id: number) => void;
  limpar: () => void;
}

let proximoId = 0;

/** Notificações flutuantes (`#tcg-toast`). Estado de interface apenas — nunca dados do servidor. */
export const useToastStore = create<EstadoToast>((set) => ({
  toasts: [],
  publicar: (tipo, mensagem) =>
    set((estado) => ({ toasts: [...estado.toasts, { id: proximoId++, tipo, mensagem }] })),
  remover: (id) => set((estado) => ({ toasts: estado.toasts.filter((toast) => toast.id !== id) })),
  limpar: () => set({ toasts: [] }),
}));
