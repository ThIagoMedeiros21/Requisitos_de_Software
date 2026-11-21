import { Icon } from '../../../../components/ui/Icon';
import { useFixarCartaFavorita } from '../api/profileQueries';
import { useProfileUiStore } from '../stores/useProfileUiStore';
import type { PerfilComunitarioVisaoDto } from '../types/profile.types';

function formatarPreco(valor: number): string {
  return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

export function SelectFavoriteCardModal({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const modalAberto = useProfileUiStore((estado) => estado.modalSlotAberto);
  const filtro = useProfileUiStore((estado) => estado.filtroColecaoSlot);
  const definirFiltro = useProfileUiStore((estado) => estado.definirFiltroColecaoSlot);
  const fecharModal = useProfileUiStore((estado) => estado.fecharModalSlot);

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
      // Erro publicado pelo hook `useFixarCartaFavorita`.
    }
  }

  return (
    <div
      id="fav-manage-modal"
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-escolher-carta-favorita"
      className="fixed inset-0 z-50 flex items-center justify-center bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      <div className="flex max-h-[85vh] w-full max-w-2xl flex-col overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
        <div className="flex items-center justify-between border-b border-ice-border px-space-lg py-space-md">
          <div className="flex items-center gap-space-xs">
            <Icon nome="stars" className="text-[22px] text-ocean-blue" />
            <h2
              id="titulo-escolher-carta-favorita"
              className="font-headline-md text-headline-md text-deep-navy"
            >
              Escolher Carta Favorita
            </h2>
          </div>
          <button
            type="button"
            aria-label="Fechar seleção"
            onClick={fecharModal}
            className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
          >
            <Icon nome="close" className="text-[20px]" />
          </button>
        </div>

        <div className="space-y-space-md overflow-y-auto p-space-lg">
          <div className="flex items-start gap-space-sm rounded-xl bg-sky-tint p-3 font-body-sm text-body-sm text-deep-navy">
            <Icon nome="info" className="text-[20px] text-ocean-blue" />
            <div>
              <strong>Regra de Negócio (4 Slots Estritos):</strong> Selecione uma carta da sua
              coleção para alocar no slot disponível da vitrine comunitária.
            </div>
          </div>

          <div className="relative flex items-center">
            <Icon
              nome="search"
              className="pointer-events-none absolute left-3 text-[20px] text-secondary"
            />
            <label htmlFor="filtro-colecao-slot" className="sr-only">
              Filtrar cartas da coleção
            </label>
            <input
              id="filtro-colecao-slot"
              type="search"
              value={filtro}
              onChange={(evento) => definirFiltro(evento.target.value)}
              placeholder="Filtrar cartas da coleção por nome, código ou cor..."
              className="w-full rounded-lg bg-surface-container-low py-2.5 pr-3 pl-10 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border transition-all outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>

          <div className="space-y-space-xs">
            <h3 className="font-label-sm text-label-sm font-bold tracking-wider text-secondary uppercase">
              Cartas Disponíveis da Coleção
            </h3>

            {cartasFiltradas.length === 0 || !haDisponivel ? (
              <div className="rounded-xl border border-dashed border-steel-border bg-surface-container-low p-space-lg text-center font-body-sm text-body-sm text-text-muted">
                Nenhuma nova carta disponível na sua coleção para fixação.
              </div>
            ) : (
              <ul className="m-0 list-none space-y-2 p-0">
                {cartasFiltradas.map((carta) => (
                  <li
                    key={carta.cartaId}
                    className="flex items-center justify-between gap-space-sm rounded-xl border border-ice-border bg-surface-container-lowest p-3 transition-colors hover:bg-surface-container-low/50"
                  >
                    <div className="flex items-center gap-space-sm">
                      <div className="flex h-12 w-10 flex-shrink-0 items-center justify-center overflow-hidden rounded-lg bg-deep-navy font-label-sm text-[10px] font-bold text-pure-white">
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
                        <p className="font-headline-sm text-headline-sm text-deep-navy">
                          {carta.nome}
                        </p>
                        <p className="font-body-sm text-body-sm text-text-muted">
                          {carta.codigo} • {carta.raridade} • {carta.cor} •{' '}
                          <span className="font-semibold text-deep-navy">
                            {formatarPreco(carta.precoMercado)}
                          </span>
                        </p>
                      </div>
                    </div>

                    <button
                      type="button"
                      disabled={carta.jaDestacada || fixarCarta.isPending}
                      onClick={() => aoFixar(carta.cartaId)}
                      className="flex-shrink-0 rounded-lg bg-ocean-blue px-3.5 py-2 font-label-sm text-label-sm font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:bg-surface-container disabled:text-secondary"
                    >
                      {carta.jaDestacada ? 'Já fixada' : 'Fixar Slot'}
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>

        <div className="flex justify-end border-t border-ice-border bg-surface-container-low/40 px-space-lg py-space-sm">
          <button
            type="button"
            onClick={fecharModal}
            className="rounded-lg bg-surface-container px-4 py-2 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container-high hover:text-deep-navy"
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
