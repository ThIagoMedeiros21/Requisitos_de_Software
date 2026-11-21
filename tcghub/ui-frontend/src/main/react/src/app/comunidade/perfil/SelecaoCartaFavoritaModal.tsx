import { type PerfilComunitarioVisaoDto, useFixarCartaFavorita } from './perfilConsultas';
import { usePerfilStore } from './perfilStore';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function SelecaoCartaFavoritaModal({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const modalAberto = usePerfilStore((estado) => estado.modalSlotAberto);
  const filtro = usePerfilStore((estado) => estado.filtroColecaoSlot);
  const definirFiltro = usePerfilStore((estado) => estado.definirFiltroColecaoSlot);
  const fecharModal = usePerfilStore((estado) => estado.fecharModalSlot);

  const fixarCarta = useFixarCartaFavorita(perfil.id);

  if (!modalAberto) {
    return null;
  }

  const termo = filtro.trim().toLowerCase();
  const cartasFiltradas = perfil.cartasColecaoDisponiveis.filter((carta) => {
    if (!termo) {
      return true;
    }
    return (
      carta.nome.toLowerCase().includes(termo) ||
      carta.codigo.toLowerCase().includes(termo) ||
      carta.cor.toLowerCase().includes(termo)
    );
  });

  const haDisponivel = cartasFiltradas.some((carta) => !carta.jaDestacada);

  async function aoFixar(cartaId: string) {
    try {
      await fixarCarta.mutateAsync(cartaId);
      fecharModal();
    } catch {
      // Erro já publicado por `useFixarCartaFavorita` via `useNotificacoes`.
    }
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-escolher-carta-favorita"
      className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/60 p-4"
    >
      <div className="flex max-h-[85vh] w-full max-w-2xl flex-col overflow-hidden rounded-2xl bg-white shadow-xl">
        <div className="flex items-center justify-between border-b border-slate-200 px-6 py-4">
          <h2 id="titulo-escolher-carta-favorita" className="m-0 text-lg font-bold text-slate-900">
            Escolher Carta Favorita
          </h2>
          <button
            type="button"
            aria-label="Fechar seleção"
            onClick={fecharModal}
            className="rounded-lg px-2 py-1 text-slate-500 hover:bg-slate-100 hover:text-slate-800"
          >
            ×
          </button>
        </div>

        <div className="space-y-4 overflow-y-auto p-6">
          <div className="rounded-xl border border-sky-200 bg-sky-50 p-3 text-xs text-sky-900">
            <strong>Regra de Negócio (4 Slots Estritos):</strong> Selecione uma carta da sua
            coleção para alocar no slot disponível da vitrine comunitária.
          </div>

          <div>
            <label htmlFor="filtro-colecao-slot" className="sr-only">
              Filtrar cartas da coleção
            </label>
            <input
              id="filtro-colecao-slot"
              type="search"
              value={filtro}
              onChange={(evento) => definirFiltro(evento.target.value)}
              placeholder="Filtrar cartas da coleção por nome, código ou cor..."
              className="w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
            />
          </div>

          <div className="space-y-2">
            <h3 className="m-0 text-xs font-bold tracking-wide text-slate-500 uppercase">
              Cartas disponíveis da coleção
            </h3>

            {cartasFiltradas.length === 0 || !haDisponivel ? (
              <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
                Nenhuma nova carta disponível na sua coleção para fixação.
              </div>
            ) : (
              <ul className="m-0 list-none space-y-2 p-0">
                {cartasFiltradas.map((carta) => (
                  <li
                    key={carta.cartaId}
                    className="flex items-center justify-between gap-3 rounded-xl border border-slate-200 p-3"
                  >
                    <div className="flex items-center gap-3">
                      <div className="flex h-12 w-10 shrink-0 items-center justify-center overflow-hidden rounded bg-slate-100 text-[10px] font-semibold text-slate-500">
                        {carta.imagem ? (
                          <img
                            src={carta.imagem}
                            alt={carta.nome}
                            className="h-full w-full object-cover"
                          />
                        ) : (
                          carta.codigo
                        )}
                      </div>
                      <div>
                        <p className="m-0 text-sm font-semibold text-slate-900">{carta.nome}</p>
                        <p className="m-0 text-xs text-slate-500">
                          {carta.codigo} • {carta.raridade} • {carta.cor} •{' '}
                          {formatarPreco(carta.precoMercado)}
                        </p>
                      </div>
                    </div>

                    <button
                      type="button"
                      disabled={carta.jaDestacada || fixarCarta.isPending}
                      onClick={() => aoFixar(carta.cartaId)}
                      className="shrink-0 rounded-lg bg-indigo-600 px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-indigo-500 disabled:bg-slate-200 disabled:text-slate-500"
                    >
                      {carta.jaDestacada ? 'Já fixada' : 'Fixar Slot'}
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        <div className="flex justify-end border-t border-slate-200 px-6 py-3">
          <button
            type="button"
            onClick={fecharModal}
            className="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
