import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Avatar } from '../../../../components/ui/Avatar';
import { Icon } from '../../../../components/ui/Icon';
import { useAlterarPerfil } from '../api/profileQueries';
import { useProfileUiStore } from '../stores/useProfileUiStore';
import type {
  AlterarPerfilEntrada,
  PerfilComunitarioAlteracaoDto,
} from '../types/profile.types';

interface FormularioPerfil {
  apelido: string;
  cidade: string;
  descricao: string;
  foto: string;
  preferenciasPublicas: string;
  decksFavoritosVisiveis: boolean;
}

function paraValoresFormulario(perfil: PerfilComunitarioAlteracaoDto): FormularioPerfil {
  return {
    apelido: perfil.apelido,
    cidade: perfil.cidade ?? '',
    descricao: perfil.descricao ?? '',
    foto: perfil.foto ?? '',
    preferenciasPublicas: perfil.preferenciasPublicas ?? '',
    decksFavoritosVisiveis: perfil.decksFavoritosVisiveis,
  };
}

export function EditProfileModal({
  perfil,
  aberto,
  emPagina = false,
  aoFechar,
}: {
  perfil: PerfilComunitarioAlteracaoDto;
  aberto: boolean;
  emPagina?: boolean;
  aoFechar?: () => void;
}) {
  const fecharModalEdicao = useProfileUiStore((estado) => estado.fecharModalEdicao);
  const alterarPerfil = useAlterarPerfil(perfil.id);

  const {
    register,
    handleSubmit,
    reset,
    watch,
    setValue,
    formState: { errors },
  } = useForm<FormularioPerfil>({
    defaultValues: paraValoresFormulario(perfil),
  });

  useEffect(() => {
    if (aberto || emPagina) {
      reset(paraValoresFormulario(perfil));
    }
  }, [aberto, emPagina, perfil, reset]);

  if (!aberto && !emPagina) {
    return null;
  }

  const descricaoAtual = watch('descricao') ?? '';
  const fotoAtual = watch('foto') ?? '';

  function cancelar() {
    reset(paraValoresFormulario(perfil));
    if (aoFechar) {
      aoFechar();
    } else {
      fecharModalEdicao();
    }
  }

  async function aoSubmeter(dados: FormularioPerfil) {
    const entrada: AlterarPerfilEntrada = {
      apelido: dados.apelido.trim(),
      cidade: dados.cidade.trim() || null,
      descricao: dados.descricao.trim() || null,
      foto: dados.foto.trim() || null,
      preferenciasPublicas: dados.preferenciasPublicas.trim() || null,
      decksFavoritosVisiveis: dados.decksFavoritosVisiveis,
    };

    try {
      await alterarPerfil.mutateAsync(entrada);
      if (aoFechar) {
        aoFechar();
      } else {
        fecharModalEdicao();
      }
    } catch {
      // Erro publicado pelo hook `useAlterarPerfil` via `useToastStore`.
    }
  }

  const conteudo = (
    <div className="w-full max-w-xl overflow-hidden rounded-2xl border border-ice-border bg-pure-white shadow-level-4">
      <div className="flex items-center justify-between border-b border-ice-border px-space-lg py-space-md">
        <div className="flex items-center gap-space-xs">
          <Icon nome="edit_square" className="text-[22px] text-ocean-blue" />
          <h2 className="font-headline-md text-headline-md text-deep-navy">Editar Perfil</h2>
        </div>
        <button
          type="button"
          aria-label="Fechar edição"
          onClick={cancelar}
          className="rounded-lg p-1.5 text-secondary transition-colors hover:bg-surface-container-low hover:text-deep-navy"
        >
          <Icon nome="close" className="text-[20px]" />
        </button>
      </div>

      <form onSubmit={handleSubmit(aoSubmeter)} noValidate className="space-y-space-md p-space-lg">
        <div className="flex flex-col gap-space-md rounded-xl border border-ice-border bg-surface-container-low/60 p-space-md sm:flex-row sm:items-center">
          <Avatar
            apelido={perfil.apelido}
            foto={fotoAtual.trim() || null}
            className="h-16 w-16"
          />
          <div className="flex-1 space-y-1.5">
            <label htmlFor="perfil-foto" className="block font-label-sm text-label-sm text-deep-navy">
              Foto de Perfil (URL ou arquivo)
            </label>
            <div className="flex gap-2">
              <input
                id="perfil-foto"
                type="text"
                {...register('foto')}
                placeholder="Ex.: avatar-luffy.png"
                className="flex-1 rounded-lg bg-pure-white px-3 py-1.5 font-body-sm text-body-sm text-deep-navy ring-1 ring-steel-border outline-none focus:ring-2 focus:ring-ocean-blue"
              />
              <button
                type="button"
                onClick={() => setValue('foto', '', { shouldDirty: true })}
                className="rounded-lg border border-steel-border bg-pure-white px-3 py-1.5 font-label-sm text-label-sm text-secondary transition-colors hover:bg-surface-container"
              >
                Remover
              </button>
            </div>
            <p className="font-body-sm text-[11px] text-text-muted">
              PNG, JPG ou GIF recomendados até 5MB
            </p>
          </div>
        </div>

        <div>
          <label
            htmlFor="perfil-apelido"
            className="block font-label-md text-label-md font-semibold text-deep-navy"
          >
            Apelido Comunitário *
          </label>
          <div className="relative mt-1 flex items-center">
            <span className="pointer-events-none absolute left-3 font-mono text-sm font-bold text-ocean-blue">
              @
            </span>
            <input
              id="perfil-apelido"
              type="text"
              {...register('apelido', {
                validate: (valor) => valor.trim().length > 0 || 'O apelido é obrigatório.',
              })}
              className="w-full rounded-lg bg-surface-container-low py-2.5 pr-3 pl-8 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>
          {errors.apelido ? (
            <p role="alert" className="mt-1 font-body-sm text-body-sm font-semibold text-error">
              {errors.apelido.message}
            </p>
          ) : (
            <p className="mt-1 font-body-sm text-body-sm text-text-muted">
              Seu identificador único para amigos, trocas e cotações.
            </p>
          )}
        </div>

        <div>
          <label
            htmlFor="perfil-cidade"
            className="block font-label-md text-label-md font-semibold text-deep-navy"
          >
            Cidade e Estado
          </label>
          <div className="relative mt-1 flex items-center">
            <Icon
              nome="location_on"
              className="pointer-events-none absolute left-3 text-[18px] text-secondary"
            />
            <input
              id="perfil-cidade"
              type="text"
              {...register('cidade')}
              placeholder="Ex.: São Paulo, SP"
              className="w-full rounded-lg bg-surface-container-low py-2.5 pr-3 pl-9 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
            />
          </div>
        </div>

        <div>
          <div className="flex items-center justify-between">
            <label
              htmlFor="perfil-descricao"
              className="block font-label-md text-label-md font-semibold text-deep-navy"
            >
              Bio / Descrição de Perfil
            </label>
            <span className="font-label-sm text-label-sm text-secondary">
              {descricaoAtual.length} / 200
            </span>
          </div>
          <textarea
            id="perfil-descricao"
            rows={3}
            maxLength={200}
            {...register('descricao')}
            className="mt-1 w-full rounded-lg bg-surface-container-low px-3 py-2 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
          />
        </div>

        <div>
          <label
            htmlFor="perfil-preferencias"
            className="block font-label-md text-label-md font-semibold text-deep-navy"
          >
            Preferências Públicas
          </label>
          <input
            id="perfil-preferencias"
            type="text"
            {...register('preferenciasPublicas')}
            placeholder="Ex.: Trocas presenciais, torneios locais"
            className="mt-1 w-full rounded-lg bg-surface-container-low px-3 py-2.5 font-body-md text-body-md text-deep-navy ring-1 ring-steel-border outline-none focus:bg-pure-white focus:ring-2 focus:ring-ocean-blue"
          />
        </div>

        <label className="flex cursor-pointer items-start gap-3 rounded-xl border border-ice-border bg-surface-container-low/40 p-3.5">
          <input
            type="checkbox"
            {...register('decksFavoritosVisiveis')}
            className="mt-1 h-4 w-4 rounded border-steel-border text-ocean-blue"
          />
          <span>
            <span className="block font-label-md text-label-md font-semibold text-deep-navy">
              Exibir Decks Favoritos no perfil
            </span>
            <span className="block font-body-sm text-body-sm text-text-muted">
              Mostra suas listas ativas no formato oficial Bandai para outros colecionadores.
            </span>
          </span>
        </label>

        <div className="flex justify-end gap-space-sm border-t border-ice-border pt-space-md">
          <button
            type="button"
            onClick={cancelar}
            className="rounded-lg bg-surface-container-low px-4 py-2.5 font-label-md text-label-md font-semibold text-secondary transition-colors hover:bg-surface-container hover:text-deep-navy"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={alterarPerfil.isPending}
            className="rounded-lg bg-ocean-blue px-5 py-2.5 font-label-md text-label-md font-semibold text-pure-white shadow-sm transition-colors hover:bg-deep-navy disabled:opacity-50"
          >
            Salvar Alterações
          </button>
        </div>
      </form>
    </div>
  );

  if (emPagina) {
    return <div className="mx-auto max-w-2xl py-space-md">{conteudo}</div>;
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-label="Editar Perfil"
      className="fixed inset-0 z-50 flex items-center justify-center overflow-y-auto bg-deep-navy/60 p-4 backdrop-blur-sm"
    >
      {conteudo}
    </div>
  );
}
