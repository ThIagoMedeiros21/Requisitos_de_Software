import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Icon } from '../../../../components/ui/Icon';
import { useOpeningsUiStore } from '../../../../stores/openingsUiStore';
import {
  useAlterarAberturaAdmin,
  useIncluirAberturaAdmin,
} from '../api/adminOpeningsQueries';
import type { AberturaAdminDto, SalvarAberturaEntrada } from '../types/adminOpenings.types';

interface FormularioAbertura {
  numero: string;
  nome: string;
  artista: string;
  episodios: string;
  imagem: string;
  video: string;
  ativa: boolean;
}

function paraFormulario(abertura: AberturaAdminDto | null): FormularioAbertura {
  if (!abertura) {
    return {
      numero: '',
      nome: '',
      artista: '',
      episodios: '',
      imagem: '',
      video: '',
      ativa: true,
    };
  }
  return {
    numero: String(abertura.numero),
    nome: abertura.nome,
    artista: abertura.artista,
    episodios: abertura.episodios,
    imagem: abertura.imagem,
    video: abertura.video,
    ativa: abertura.ativa,
  };
}

export function AdminOpeningFormModal() {
  const aberto = useOpeningsUiStore((estado) => estado.modalAdminAberto);
  const emEdicao = useOpeningsUiStore((estado) => estado.aberturaEmEdicao);
  const fechar = useOpeningsUiStore((estado) => estado.fecharModalAdmin);

  const incluir = useIncluirAberturaAdmin();
  const alterar = useAlterarAberturaAdmin();

  const [urlPrevia, definirUrlPrevia] = useState<string | null>(null);
  const [erroPrevia, definirErroPrevia] = useState(false);

  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<FormularioAbertura>({
    defaultValues: paraFormulario(emEdicao),
  });

  useEffect(() => {
    if (aberto) {
      reset(paraFormulario(emEdicao));
      definirUrlPrevia(emEdicao?.imagem ?? null);
      definirErroPrevia(false);
    }
  }, [aberto, emEdicao, reset]);

  if (!aberto) {
    return null;
  }

  const imagemDigitada = watch('imagem') ?? '';
  const pendente = incluir.isPending || alterar.isPending;

  function testarPrevia() {
    definirErroPrevia(false);
    definirUrlPrevia(imagemDigitada.trim() || null);
  }

  async function aoSubmeter(dados: FormularioAbertura) {
    const entrada: SalvarAberturaEntrada = {
      numero: Number.parseInt(dados.numero, 10),
      nome: dados.nome.trim(),
      artista: dados.artista.trim(),
      episodios: dados.episodios.trim(),
      imagem: dados.imagem.trim(),
      video: dados.video.trim(),
      ativa: dados.ativa,
    };

    try {
      if (emEdicao) {
        await alterar.mutateAsync({ id: emEdicao.id, entrada });
      } else {
        await incluir.mutateAsync(entrada);
      }
      fechar();
    } catch {
      // Erro já publicado pelos hooks via toast.
    }
  }

  const classeCampo =
    'mt-1 w-full rounded-lg bg-surface-container-low px-3 py-2 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue';

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="titulo-form-abertura-admin"
      className="fixed inset-0 z-50 flex items-center justify-center overflow-y-auto bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      <div className="my-auto w-full max-w-2xl overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
        <div className="flex items-start justify-between gap-2 border-b border-ice-border px-space-lg py-space-md">
          <div>
            <h2
              id="titulo-form-abertura-admin"
              className="font-headline-md text-headline-md text-deep-navy"
            >
              {emEdicao ? 'Editar abertura' : 'Cadastrar abertura'}
            </h2>
            <p className="mt-0.5 font-body-sm text-body-sm text-text-muted">
              {emEdicao
                ? 'Atualize os metadados oficiais AnimeThemes da abertura selecionada'
                : 'Adicione uma nova música tema à base canônica de One Piece'}
            </p>
          </div>
          <button
            type="button"
            aria-label="Fechar formulário de abertura"
            onClick={fechar}
            className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
          >
            <Icon nome="close" className="text-[20px]" />
          </button>
        </div>

        <form onSubmit={handleSubmit(aoSubmeter)} noValidate className="space-y-space-md p-space-lg">
          <div className="grid grid-cols-1 gap-space-md sm:grid-cols-3">
            <div>
              <label
                htmlFor="abertura-numero"
                className="block font-label-md text-label-md font-semibold text-deep-navy"
              >
                Número da Abertura *
              </label>
              <input
                id="abertura-numero"
                type="number"
                min={1}
                placeholder="Ex: 26"
                {...register('numero', {
                  validate: (valor) => {
                    const n = Number.parseInt(valor, 10);
                    return (!Number.isNaN(n) && n > 0) || 'Informe um número inteiro maior que zero.';
                  },
                })}
                className={classeCampo}
              />
              {errors.numero && (
                <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
                  {errors.numero.message}
                </p>
              )}
            </div>

            <div className="sm:col-span-2">
              <label
                htmlFor="abertura-nome"
                className="block font-label-md text-label-md font-semibold text-deep-navy"
              >
                Nome da Abertura *
              </label>
              <input
                id="abertura-nome"
                type="text"
                placeholder="Ex: UUUUUS!"
                {...register('nome', {
                  validate: (valor) => valor.trim().length > 0 || 'O nome da abertura é obrigatório.',
                })}
                className={classeCampo}
              />
              {errors.nome && (
                <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
                  {errors.nome.message}
                </p>
              )}
            </div>
          </div>

          <div className="grid grid-cols-1 gap-space-md sm:grid-cols-2">
            <div>
              <label
                htmlFor="abertura-artista"
                className="block font-label-md text-label-md font-semibold text-deep-navy"
              >
                Artista / Intérprete *
              </label>
              <input
                id="abertura-artista"
                type="text"
                placeholder="Ex: Hiroshi Kitadani"
                {...register('artista', {
                  validate: (valor) => valor.trim().length > 0 || 'O artista é obrigatório.',
                })}
                className={classeCampo}
              />
              {errors.artista && (
                <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
                  {errors.artista.message}
                </p>
              )}
            </div>

            <div>
              <label
                htmlFor="abertura-episodios"
                className="block font-label-md text-label-md font-semibold text-deep-navy"
              >
                Episódios *
              </label>
              <input
                id="abertura-episodios"
                type="text"
                placeholder="Ex: 1 a 47"
                {...register('episodios', {
                  validate: (valor) => valor.trim().length > 0 || 'A faixa de episódios é obrigatória.',
                })}
                className={classeCampo}
              />
              {errors.episodios && (
                <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
                  {errors.episodios.message}
                </p>
              )}
            </div>
          </div>

          <div>
            <label
              htmlFor="abertura-imagem"
              className="block font-label-md text-label-md font-semibold text-deep-navy"
            >
              URL da Imagem de Capa (Aspecto 16:9) *
            </label>
            <div className="mt-1 flex gap-2">
              <input
                id="abertura-imagem"
                type="text"
                placeholder="https://pub-92474f7785774e91a790e086dfa6b2ef.r2.dev/..."
                {...register('imagem', {
                  validate: (valor) => valor.trim().length > 0 || 'A imagem de capa é obrigatória.',
                })}
                className="flex-1 rounded-lg bg-surface-container-low px-3 py-2 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
              />
              <button
                type="button"
                onClick={testarPrevia}
                className="rounded-lg bg-surface-container px-3.5 py-2 font-label-sm text-label-sm font-semibold text-ocean-blue transition-colors hover:bg-sky-tint"
              >
                Testar Prévia
              </button>
            </div>
            {errors.imagem && (
              <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
                {errors.imagem.message}
              </p>
            )}

            <div className="mt-2 flex h-28 items-center justify-center overflow-hidden rounded-xl border border-dashed border-steel-border bg-surface-container-low">
              {urlPrevia && !erroPrevia ? (
                <img
                  src={urlPrevia}
                  alt="Prévia da capa"
                  onError={() => definirErroPrevia(true)}
                  className="h-full w-full object-cover"
                />
              ) : erroPrevia ? (
                <span className="font-body-sm text-body-sm text-error">
                  Não foi possível carregar a imagem informada.
                </span>
              ) : (
                <span className="font-body-sm text-body-sm text-text-muted">
                  Prévia da imagem aparecerá aqui
                </span>
              )}
            </div>
          </div>

          <div>
            <label
              htmlFor="abertura-video"
              className="block font-label-md text-label-md font-semibold text-deep-navy"
            >
              Referência de Vídeo Público (AnimeThemes / URL) *
            </label>
            <input
              id="abertura-video"
              type="text"
              placeholder="https://v.animethemes.moe/OnePiece-OP1.webm"
              {...register('video', {
                validate: (valor) => valor.trim().length > 0 || 'A URL de vídeo é obrigatória.',
              })}
              className={classeCampo}
            />
            {errors.video && (
              <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
                {errors.video.message}
              </p>
            )}
          </div>

          <label className="flex cursor-pointer items-start gap-3 rounded-xl border border-ice-border bg-surface-container-low/40 p-3">
            <input
              type="checkbox"
              {...register('ativa')}
              className="mt-1 h-4 w-4 rounded border-steel-border text-ocean-blue"
            />
            <span>
              <span className="block font-label-md text-label-md font-semibold text-deep-navy">
                Ativa para novas escolhas
              </span>
              <span className="block font-body-sm text-body-sm text-text-muted">
                Usuários do TCGHub poderão selecionar este tema musical em seu perfil imediatamente.
              </span>
            </span>
          </label>

          <div className="flex justify-end gap-space-sm border-t border-ice-border pt-space-md">
            <button
              type="button"
              onClick={fechar}
              className="rounded-lg bg-surface-container-low px-4 py-2.5 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={pendente}
              className="rounded-lg bg-ocean-blue px-5 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
            >
              {emEdicao ? 'Salvar alterações' : 'Cadastrar abertura'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
