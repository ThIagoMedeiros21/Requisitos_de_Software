import { useSuspenseQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { useParams } from 'react-router';
import { BarraNavegacaoComunidade } from './BarraNavegacaoComunidade';
import { CartasFavoritasGrid } from './CartasFavoritasGrid';
import { PerfilCabecalhoCard } from './PerfilCabecalhoCard';
import { PerfilEdicaoModal } from './PerfilEdicaoModal';
import { SelecaoCartaFavoritaModal } from './SelecaoCartaFavoritaModal';
import { consultaPerfilVisao } from './perfilConsultas';
import { usePerfilStore } from './perfilStore';

export function PerfilVisualizacao() {
  const { id } = useParams();
  const modalEdicaoAberto = usePerfilStore((estado) => estado.modalEdicaoAberto);
  const definirAbaAtiva = usePerfilStore((estado) => estado.definirAbaAtiva);

  useEffect(() => {
    definirAbaAtiva('meu-perfil');
  }, [definirAbaAtiva]);

  const { data: perfil } = useSuspenseQuery(consultaPerfilVisao(id));

  return (
    <div className="mx-auto max-w-6xl space-y-6 px-4 py-6">
      <BarraNavegacaoComunidade abaAtual="meu-perfil" />

      <PerfilCabecalhoCard perfil={perfil} />

      <CartasFavoritasGrid perfil={perfil} />

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        {(perfil.ehProprietario || perfil.decksFavoritosVisiveis) && (
          <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-xs">
            <div className="flex items-center justify-between gap-2">
              <h2 className="m-0 text-base font-bold text-slate-900">Decks Favoritos</h2>
              {perfil.ehProprietario && !perfil.decksFavoritosVisiveis && (
                <span className="rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-medium text-slate-600">
                  Oculto no perfil público
                </span>
              )}
            </div>
            <p className="m-0 mt-2 text-sm text-slate-500">
              {perfil.decksFavoritosVisiveis
                ? 'Vitrine pública de decks favoritos habilitada para a comunidade.'
                : 'Seus decks favoritos estão ocultos para visitantes.'}
            </p>
          </section>
        )}

        <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-xs">
          <h2 className="m-0 text-base font-bold text-slate-900">Preferências Comunitárias</h2>
          <p className="m-0 mt-2 text-sm text-slate-500">
            {perfil.preferenciasPublicas?.trim()
              ? perfil.preferenciasPublicas
              : 'Nenhuma preferência pública informada.'}
          </p>
        </section>
      </div>

      {perfil.ehProprietario && (
        <>
          <PerfilEdicaoModal perfil={perfil} aberto={modalEdicaoAberto} />
          <SelecaoCartaFavoritaModal perfil={perfil} />
        </>
      )}
    </div>
  );
}
