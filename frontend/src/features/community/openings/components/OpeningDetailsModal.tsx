import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { Link } from 'react-router';
import { Icon } from '../../../../components/ui/Icon';
import { mensagemErro } from '../../../../lib/api';
import { useOpeningsUiStore } from '../../../../stores/openingsUiStore';
import {
  consultaDetalhesAbertura,
  useAlternarVisibilidadeAbertura,
  useEscolherAberturaPreferida,
} from '../api/openingsQueries';
import { OpeningCover } from './OpeningCover';

function ehVideoDireto(url: string): boolean {
  return /\.(webm|mp4|ogg)(\?.*)?$/i.test(url.trim());
}

export function OpeningDetailsModal() {
  const aberturaId = useOpeningsUiStore((estado) => estado.aberturaDetalheId);
  const aberto = useOpeningsUiStore((estado) => estado.modalDetalhesAberto);
  const fechar = useOpeningsUiStore((estado) => estado.fecharDetalhesAbertura);
  const [reproduzindo, definirReproduzindo] = useState(false);

  const escolher = useEscolherAberturaPreferida();
  const alternarVisibilidade = useAlternarVisibilidadeAbertura();

  const consulta = useQuery({
    ...consultaDetalhesAbertura(aberturaId ?? ''),
    enabled: aberto && Boolean(aberturaId),
  });

  if (!aberto || !aberturaId) {
    return null;
  }

  const detalhe = consulta.data;

  function fecharModal() {
    definirReproduzindo(false);
    fechar();
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-detalhes-abertura"
      className="fixed inset-0 z-50 flex items-center justify-center bg-deep-navy/65 p-4 backdrop-blur-sm"
    >
      <div className="flex max-h-[90vh] w-full max-w-3xl flex-col overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
        <div className="flex items-center justify-between border-b border-ice-border px-space-lg py-space-md">
          <div className="flex items-center gap-2">
            <Icon nome="library_music" className="text-[22px] text-ocean-blue" />
            <h2
              id="titulo-detalhes-abertura"
              className="font-headline-md text-headline-md text-deep-navy"
            >
              {detalhe
                ? `Op. ${String(detalhe.numero).padStart(2, '0')} — ${detalhe.nome}`
                : 'Detalhes da Abertura'}
            </h2>
          </div>
          <button
            type="button"
            aria-label="Fechar detalhes da abertura"
            onClick={fecharModal}
            className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
          >
            <Icon nome="close" className="text-[20px]" />
          </button>
        </div>

        <div className="flex-1 space-y-space-md overflow-y-auto p-space-lg">
          {consulta.isLoading ? (
            <div className="space-y-3" role="status" aria-label="Carregando detalhes da abertura">
              <div className="aspect-video w-full animate-pulse rounded-xl bg-surface-container-low" />
              <div className="h-20 animate-pulse rounded-xl bg-surface-container-low" />
            </div>
          ) : consulta.isError || !detalhe ? (
            <div
              role="alert"
              className="rounded-xl bg-error-container p-4 font-body-md text-body-md text-on-error-container"
            >
              {mensagemErro(consulta.error)}
            </div>
          ) : (
            <>
              <div className="relative overflow-hidden rounded-xl border border-ice-border bg-deep-navy">
                {reproduzindo && detalhe.video && ehVideoDireto(detalhe.video) ? (
                  <video
                    controls
                    autoPlay
                    src={detalhe.video}
                    className="aspect-video w-full bg-deep-navy"
                  >
                    Seu navegador não suporta reprodução direta de vídeo.
                  </video>
                ) : (
                  <div className="relative">
                    <OpeningCover
                      numero={detalhe.numero}
                      nome={detalhe.nome}
                      imagem={detalhe.imagem}
                      className="aspect-video w-full"
                    />
                    <div className="absolute inset-0 flex flex-col justify-end bg-gradient-to-t from-deep-navy/90 via-deep-navy/35 to-transparent p-space-lg text-pure-white">
                      <div className="flex flex-wrap items-center gap-2">
                        <span className="rounded bg-cobalt-accent px-2.5 py-0.5 font-label-sm text-label-sm font-bold uppercase">
                          OP. {String(detalhe.numero).padStart(2, '0')}
                        </span>
                        <span className="rounded bg-pure-white/20 px-2.5 py-0.5 font-label-sm text-label-sm backdrop-blur-sm">
                          Episódios {detalhe.episodios}
                        </span>
                        {!detalhe.ativa && (
                          <span className="rounded bg-status-warning-bg px-2.5 py-0.5 font-label-sm text-label-sm font-semibold text-status-warning-text">
                            Inativa no Catálogo
                          </span>
                        )}
                      </div>
                      <h3 className="mt-2 font-headline-lg text-headline-lg text-pure-white">
                        {detalhe.nome}
                      </h3>
                      <p className="font-body-md text-body-md text-sky-tint">
                        {detalhe.artista} • Episódios {detalhe.episodios}
                      </p>

                      <div className="mt-4 flex flex-wrap items-center gap-2">
                        {detalhe.video && ehVideoDireto(detalhe.video) && (
                          <button
                            type="button"
                            onClick={() => definirReproduzindo(true)}
                            className="inline-flex items-center gap-1.5 rounded-lg bg-pure-white px-4 py-2 font-label-sm text-label-sm font-bold text-deep-navy shadow-sm transition-colors hover:bg-sky-tint"
                          >
                            <Icon nome="play_arrow" preenchido className="text-[18px]" />
                            <span>Assistir Abertura</span>
                          </button>
                        )}
                        {detalhe.video && (
                          <a
                            href={detalhe.video}
                            target="_blank"
                            rel="noreferrer"
                            className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue/90 px-4 py-2 font-label-sm text-label-sm font-semibold text-pure-white no-underline transition-colors hover:bg-cobalt-accent"
                          >
                            <Icon nome="open_in_new" className="text-[16px]" />
                            <span>Abrir no AnimeThemes</span>
                          </a>
                        )}
                        {detalhe.ehMinhaPreferida ? (
                          <span className="inline-flex items-center gap-1 rounded-lg bg-sky-tint px-3.5 py-2 font-label-sm text-label-sm font-bold text-ocean-blue">
                            <Icon nome="star" preenchido className="text-[16px]" />
                            <span>Sua Preferida (Ativa)</span>
                          </span>
                        ) : (
                          detalhe.ativa && (
                            <button
                              type="button"
                              disabled={escolher.isPending}
                              onClick={() => escolher.mutate(detalhe.id)}
                              className="inline-flex items-center gap-1.5 rounded-lg bg-ocean-blue px-4 py-2 font-label-sm text-label-sm font-semibold text-pure-white transition-colors hover:bg-cobalt-accent disabled:opacity-50"
                            >
                              <Icon nome="star" className="text-[16px]" />
                              <span>Escolher como Preferida</span>
                            </button>
                          )
                        )}
                      </div>
                    </div>
                  </div>
                )}
              </div>

              {detalhe.ehMinhaPreferida && (
                <div className="flex items-center justify-between gap-3 rounded-xl border border-ice-border bg-surface-container-low/60 px-4 py-3">
                  <div>
                    <span className="block font-label-md text-label-md font-semibold text-deep-navy">
                      Exibir no Perfil Público
                    </span>
                    <span className="block font-body-sm text-body-sm text-text-muted">
                      {detalhe.visivelNoPerfil
                        ? 'Visível para visitantes e correspondência entre amigos'
                        : 'Oculta no perfil público — contabilizada anonimamente no Top 5'}
                    </span>
                  </div>
                  <button
                    type="button"
                    role="switch"
                    aria-checked={detalhe.visivelNoPerfil}
                    aria-label="Exibir no Perfil Público"
                    disabled={alternarVisibilidade.isPending}
                    onClick={() => alternarVisibilidade.mutate(!detalhe.visivelNoPerfil)}
                    className={`relative inline-flex h-6 w-11 flex-shrink-0 items-center rounded-full transition-colors ${
                      detalhe.visivelNoPerfil ? 'bg-ocean-blue' : 'bg-steel-border'
                    }`}
                  >
                    <span
                      className={`inline-block h-4 w-4 transform rounded-full bg-pure-white shadow-sm transition-transform ${
                        detalhe.visivelNoPerfil ? 'translate-x-6' : 'translate-x-1'
                      }`}
                    />
                  </button>
                </div>
              )}

              <div className="grid grid-cols-1 gap-space-md md:grid-cols-3">
                <div className="rounded-xl border border-ice-border bg-surface-container-low/50 p-4">
                  <span className="font-label-sm text-label-sm font-bold tracking-wider text-secondary uppercase">
                    Amigos que Escolheram
                  </span>
                  <p className="mt-1 font-headline-md text-headline-md text-deep-navy">
                    {detalhe.quantidadeAmigos}{' '}
                    {detalhe.quantidadeAmigos === 1 ? 'Amigo' : 'Amigos'}
                  </p>
                  {detalhe.amigosComMesmaPreferencia.length === 0 ? (
                    <p className="mt-1 font-body-sm text-body-sm text-text-muted">
                      Nenhum amigo com esta abertura pública.
                    </p>
                  ) : (
                    <div className="mt-2 flex flex-wrap gap-1.5">
                      {detalhe.amigosComMesmaPreferencia.map((amigo) => (
                        <Link
                          key={amigo.usuario}
                          to={`/comunidade/perfil/${encodeURIComponent(amigo.perfilId)}`}
                          onClick={fecharModal}
                          className="rounded-full bg-sky-tint px-2.5 py-0.5 font-label-sm text-[11px] font-semibold text-ocean-blue no-underline hover:bg-ocean-blue hover:text-pure-white"
                        >
                          @{amigo.apelido}
                        </Link>
                      ))}
                    </div>
                  )}
                </div>

                <div className="rounded-xl border border-ice-border bg-surface-container-low/50 p-4">
                  <span className="font-label-sm text-label-sm font-bold tracking-wider text-secondary uppercase">
                    Posição Global
                  </span>
                  <p className="mt-1 font-headline-md text-headline-md text-deep-navy">
                    {detalhe.posicaoRanking ? `#${detalhe.posicaoRanking} no Ranking` : 'Fora do Top 5'}
                  </p>
                  <p className="mt-1 font-body-sm text-body-sm text-text-muted">
                    {detalhe.totalEscolhasComunidade} escolhas totais na comunidade
                  </p>
                </div>

                <div className="rounded-xl border border-ice-border bg-surface-container-low/50 p-4">
                  <span className="font-label-sm text-label-sm font-bold tracking-wider text-secondary uppercase">
                    Cronologia do Anime
                  </span>
                  <p className="mt-1 font-headline-md text-headline-md text-deep-navy">
                    Ep. {detalhe.episodios}
                  </p>
                  <p className="mt-1 font-body-sm text-body-sm text-text-muted">
                    Intérprete: {detalhe.artista}
                  </p>
                </div>
              </div>
            </>
          )}
        </div>

        <div className="flex justify-end border-t border-ice-border bg-surface-container-low/40 px-space-lg py-space-sm">
          <button
            type="button"
            onClick={fecharModal}
            className="rounded-lg bg-surface-container px-4 py-2 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container-high hover:text-deep-navy"
          >
            Fechar
          </button>
        </div>
      </div>
    </div>
  );
}
