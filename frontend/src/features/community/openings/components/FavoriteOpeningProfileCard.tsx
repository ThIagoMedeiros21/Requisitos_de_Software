import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router';
import { Icon } from '../../../../components/ui/Icon';
import { useOpeningsUiStore } from '../../../../stores/openingsUiStore';
import type { PerfilComunitarioVisaoDto } from '../../profile/types/profile.types';
import {
  consultaAberturaDoPerfil,
  useAlternarVisibilidadeAbertura,
} from '../api/openingsQueries';
import { OpeningCover } from './OpeningCover';
import { OpeningDetailsModal } from './OpeningDetailsModal';

export function FavoriteOpeningProfileCard({ perfil }: { perfil: PerfilComunitarioVisaoDto }) {
  const abrirDetalhes = useOpeningsUiStore((estado) => estado.abrirDetalhesAbertura);
  const alternarVisibilidade = useAlternarVisibilidadeAbertura();

  const consulta = useQuery(consultaAberturaDoPerfil(perfil.id));
  const dados = consulta.data;

  // Se visitante e a abertura estiver ausente ou oculta via AberturaPreferidaPrivacidadeProxy, omitir bloco
  if (!perfil.ehProprietario && (!dados || !dados.possuiPreferidaVisivel || !dados.abertura)) {
    return null;
  }

  const abertura = dados?.abertura ?? null;
  const visivelNoPerfil = dados?.visivelNoPerfil ?? true;

  return (
    <>
      <section className="rounded-xl border border-ice-border/60 bg-pure-white p-space-lg shadow-sm">
        <div className="flex flex-col justify-between gap-space-md md:flex-row md:items-center">
          <div className="flex items-start gap-space-md sm:items-center">
            {abertura ? (
              <OpeningCover
                numero={abertura.numero}
                nome={abertura.nome}
                imagem={abertura.imagem}
                className="aspect-video w-32 flex-shrink-0 rounded-xl shadow-sm"
              />
            ) : (
              <div className="flex h-16 w-16 flex-shrink-0 items-center justify-center rounded-xl bg-sky-tint text-ocean-blue">
                <Icon nome="library_music" className="text-[28px]" />
              </div>
            )}

            <div className="space-y-1">
              <div className="flex flex-wrap items-center gap-2">
                <span className="font-label-sm text-label-sm font-bold tracking-wider text-ocean-blue uppercase">
                  Abertura Preferida de One Piece
                </span>
                {dados?.mesmaPreferenciaDoVisitante && !perfil.ehProprietario && (
                  <span className="inline-flex items-center gap-1 rounded-full bg-status-positive-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-positive-text">
                    <Icon nome="favorite" preenchido className="text-[14px]" />
                    <span>Mesma abertura que você</span>
                  </span>
                )}
                {perfil.ehProprietario && abertura && !visivelNoPerfil && (
                  <span className="rounded-full bg-surface-container px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-secondary">
                    Oculta no perfil público
                  </span>
                )}
                {abertura && !abertura.ativa && (
                  <span className="rounded-full border border-steel-border bg-status-warning-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-warning-text">
                    Inativa no Catálogo
                  </span>
                )}
              </div>

              {abertura ? (
                <>
                  <h2 className="font-headline-md text-headline-md text-deep-navy">
                    Op. {String(abertura.numero).padStart(2, '0')} — {abertura.nome}
                  </h2>
                  <p className="font-body-sm text-body-sm text-text-muted">
                    {abertura.artista} • Episódios {abertura.episodios}
                  </p>
                </>
              ) : (
                <p className="font-body-sm text-body-sm text-text-muted">
                  Você ainda não definiu sua abertura preferida de One Piece.
                </p>
              )}
            </div>
          </div>

          <div className="flex flex-wrap items-center gap-space-sm">
            {perfil.ehProprietario && abertura && (
              <div className="flex items-center gap-2 rounded-lg bg-surface-container-low px-3 py-1.5">
                <span className="font-label-sm text-label-sm text-deep-navy">
                  Exibir no Perfil Público
                </span>
                <button
                  type="button"
                  role="switch"
                  aria-checked={visivelNoPerfil}
                  aria-label="Exibir abertura preferida no perfil público"
                  disabled={alternarVisibilidade.isPending}
                  onClick={() => alternarVisibilidade.mutate(!visivelNoPerfil)}
                  className={`relative inline-flex h-5 w-10 items-center rounded-full transition-colors ${
                    visivelNoPerfil ? 'bg-ocean-blue' : 'bg-steel-border'
                  }`}
                >
                  <span
                    className={`inline-block h-3.5 w-3.5 transform rounded-full bg-pure-white transition-transform ${
                      visivelNoPerfil ? 'translate-x-5' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>
            )}

            {abertura && (
              <button
                type="button"
                onClick={() => abrirDetalhes(abertura.aberturaId)}
                className="inline-flex items-center gap-1 rounded-lg bg-surface-container-low px-3.5 py-2 font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint"
              >
                <Icon nome="play_circle" className="text-[18px]" />
                <span>Ver Detalhes</span>
              </button>
            )}

            {perfil.ehProprietario && (
              <Link
                to="/comunidade/aberturas"
                className="inline-flex items-center gap-1 rounded-lg bg-ocean-blue px-3.5 py-2 font-label-sm text-label-sm font-semibold text-pure-white no-underline shadow-sm transition-colors hover:bg-deep-navy"
              >
                <Icon nome="library_music" className="text-[16px]" />
                <span>{abertura ? 'Trocar Abertura' : 'Escolher Abertura'}</span>
              </Link>
            )}
          </div>
        </div>
      </section>

      <OpeningDetailsModal />
    </>
  );
}
