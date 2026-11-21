import { type PerfilComunitarioVisaoDto, useRemoverCartaFavorita } from './perfilConsultas';
import { usePerfilStore } from './perfilStore';

const TOTAL_SLOTS = 4;

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function CartasFavoritasGrid({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const abrirModalSlot = usePerfilStore((estado) => estado.abrirModalSlot);
  const removerCarta = useRemoverCartaFavorita(perfil.id);

  const preenchidos = perfil.cartasFavoritas.length;
  const slots = Array.from({ length: TOTAL_SLOTS }, (_, indice) => {
    return perfil.cartasFavoritas[indice] ?? null;
  });

  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-xs">
      <div className="mb-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <div className="flex flex-wrap items-center gap-2">
            <h2 className="m-0 text-lg font-bold text-slate-900">Cartas Favoritas</h2>
            <span className="rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-semibold text-slate-700">
              {preenchidos} de {TOTAL_SLOTS} slots preenchidos
            </span>
          </div>
          <p className="m-0 mt-1 text-xs text-slate-500">
            Destaques da coleção pessoal expostos na comunidade (Máximo de 4 slots estritos)
          </p>
        </div>

        {perfil.ehProprietario && preenchidos < TOTAL_SLOTS && (
          <button
            type="button"
            onClick={() => abrirModalSlot(preenchidos)}
            className="rounded-lg border border-slate-300 bg-white px-3.5 py-2 text-xs font-semibold text-slate-700 transition hover:bg-slate-50"
          >
            Gerenciar Slots
          </button>
        )}
      </div>

      {!perfil.ehProprietario && preenchidos === 0 ? (
        <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
          Este colecionador ainda não destacou cartas favoritas
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {slots.map((carta, indice) => {
            if (!carta) {
              if (!perfil.ehProprietario) {
                return null;
              }
              return (
                <div
                  key={`slot-vazio-${indice}`}
                  className="flex min-h-64 flex-col items-center justify-between rounded-xl border-2 border-dashed border-slate-300 bg-slate-50 p-4 text-center"
                >
                  <div className="mt-6 flex flex-col items-center gap-2">
                    <span className="flex h-10 w-10 items-center justify-center rounded-full bg-white text-lg font-bold text-slate-500 shadow-xs">
                      +
                    </span>
                    <p className="m-0 text-sm font-semibold text-slate-700">Slot Disponível</p>
                    <p className="m-0 text-xs text-slate-500">Toque para adicionar da coleção</p>
                  </div>
                  <button
                    type="button"
                    onClick={() => abrirModalSlot(indice)}
                    className="mt-4 w-full rounded-lg bg-indigo-600 px-3 py-2 text-xs font-semibold text-white transition hover:bg-indigo-500"
                  >
                    + Adicionar Carta
                  </button>
                </div>
              );
            }

            return (
              <article
                key={carta.cartaId}
                className="relative flex flex-col justify-between rounded-xl border border-slate-200 bg-slate-50 p-4 transition hover:border-slate-300"
              >
                {perfil.ehProprietario && (
                  <button
                    type="button"
                    aria-label={`Remover ${carta.nome} dos destaques`}
                    disabled={removerCarta.isPending}
                    onClick={() => removerCarta.mutate(carta.cartaId)}
                    className="absolute top-2.5 right-2.5 flex h-7 w-7 items-center justify-center rounded-full border border-slate-200 bg-white text-sm font-bold text-slate-600 shadow-xs transition hover:bg-rose-50 hover:text-rose-600 disabled:opacity-50"
                  >
                    ×
                  </button>
                )}

                <div className="space-y-3">
                  <div className="flex flex-wrap gap-1.5 pr-7">
                    <span className="rounded bg-amber-100 px-2 py-0.5 text-[11px] font-bold text-amber-800">
                      {carta.raridade}
                    </span>
                    <span className="rounded bg-slate-200 px-2 py-0.5 text-[11px] font-medium text-slate-700">
                      {carta.edicaoCondicao}
                    </span>
                  </div>

                  <div className="flex h-32 items-center justify-center overflow-hidden rounded-lg bg-white p-2">
                    {carta.imagem ? (
                      <img
                        src={carta.imagem}
                        alt={carta.nome}
                        className="h-full w-auto object-contain"
                      />
                    ) : (
                      <span className="text-xs font-semibold text-slate-400">{carta.codigo}</span>
                    )}
                  </div>

                  <div>
                    <span className="inline-block rounded-full bg-indigo-50 px-2 py-0.5 text-[11px] font-medium text-indigo-700">
                      {carta.cor}
                    </span>
                    <h3 className="m-0 mt-1 text-sm font-bold text-slate-900">{carta.nome}</h3>
                    <p className="m-0 text-xs text-slate-500">{carta.codigo}</p>
                  </div>
                </div>

                <p className="m-0 mt-3 border-t border-slate-200 pt-2 text-xs font-semibold text-slate-700">
                  {formatarPreco(carta.precoMercado)} • Mkt Base
                </p>
              </article>
            );
          })}
        </div>
      )}
    </section>
  );
}
