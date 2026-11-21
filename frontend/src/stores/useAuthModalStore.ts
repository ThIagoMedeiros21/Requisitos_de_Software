import { create } from 'zustand';

interface Pedido {
  promessa: Promise<void>;
  resolver: () => void;
  rejeitar: (motivo: Error) => void;
}

interface EstadoAuthModal {
  aberto: boolean;
  /**
   * Pede a reautenticação: resolve quando o usuário se autentica de novo e rejeita quando ele
   * desiste. Chamadas concorrentes compartilham o mesmo pedido (um único modal por vez).
   */
  solicitar: () => Promise<void>;
  concluir: () => void;
  cancelar: () => void;
}

let pedido: Pedido | null = null;

export const useAuthModalStore = create<EstadoAuthModal>((set) => ({
  aberto: false,
  solicitar: () => {
    if (!pedido) {
      let resolver!: () => void;
      let rejeitar!: (motivo: Error) => void;
      const promessa = new Promise<void>((resolve, reject) => {
        resolver = resolve;
        rejeitar = reject;
      });
      pedido = { promessa, resolver, rejeitar };
      set({ aberto: true });
    }
    return pedido.promessa;
  },
  concluir: () => {
    const atual = pedido;
    pedido = null;
    set({ aberto: false });
    atual?.resolver();
  },
  cancelar: () => {
    const atual = pedido;
    pedido = null;
    set({ aberto: false });
    atual?.rejeitar(new Error('Reautenticação cancelada.'));
  },
}));
