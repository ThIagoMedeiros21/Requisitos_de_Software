import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import {
  type AlterarPerfilEntrada,
  type PerfilComunitarioAlteracaoDto,
  useAlterarPerfil,
} from './perfilConsultas';
import { usePerfilStore } from './perfilStore';

interface FormularioPerfil {
  apelido: string;
  cidade: string;
  descricao: string;
  foto: string;
  preferenciasPublicas: string;
  decksFavoritosVisiveis: boolean;
}

function textoOuVazio(valor: string | null | undefined): string {
  return valor ?? '';
}

function paraValoresFormulario(perfil: PerfilComunitarioAlteracaoDto): FormularioPerfil {
  return {
    apelido: perfil.apelido,
    cidade: textoOuVazio(perfil.cidade),
    descricao: textoOuVazio(perfil.descricao),
    foto: textoOuVazio(perfil.foto),
    preferenciasPublicas: textoOuVazio(perfil.preferenciasPublicas),
    decksFavoritosVisiveis: perfil.decksFavoritosVisiveis,
  };
}

export function PerfilEdicaoModal({
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
  const fecharModalEdicao = usePerfilStore((estado) => estado.fecharModalEdicao);
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
      // Erro publicado pelo hook `useAlterarPerfil` via `useNotificacoes`.
    }
  }

  const conteudo = (
    <div className="w-full max-w-xl overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-xl">
      <div className="flex items-center justify-between border-b border-slate-200 px-6 py-4">
        <h2 className="m-0 text-lg font-bold text-slate-900">Editar Perfil</h2>
        <button
          type="button"
          aria-label="Fechar edição"
          onClick={cancelar}
          className="rounded-lg px-2 py-1 text-slate-500 hover:bg-slate-100 hover:text-slate-800"
        >
          ×
        </button>
      </div>

      <form onSubmit={handleSubmit(aoSubmeter)} noValidate className="space-y-4 p-6">
        <div className="flex flex-col gap-3 rounded-xl bg-slate-50 p-4 sm:flex-row sm:items-center">
          <div className="flex h-16 w-16 shrink-0 items-center justify-center overflow-hidden rounded-full bg-slate-900 text-sm font-bold text-white">
            {fotoAtual.trim() ? (
              <img src={fotoAtual} alt="Prévia da foto" className="h-full w-full object-cover" />
            ) : (
              <span>{(perfil.apelido || perfil.usuario).slice(0, 2).toUpperCase()}</span>
            )}
          </div>
          <div className="flex-1 space-y-2">
            <label htmlFor="perfil-foto" className="block text-xs font-semibold text-slate-700">
              Foto de Perfil (URL ou arquivo)
            </label>
            <div className="flex gap-2">
              <input
                id="perfil-foto"
                type="text"
                {...register('foto')}
                placeholder="Ex.: avatar-luffy.png"
                className="flex-1 rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
              />
              <button
                type="button"
                onClick={() => setValue('foto', '', { shouldDirty: true })}
                className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-xs font-medium text-slate-700 hover:bg-slate-100"
              >
                Remover
              </button>
            </div>
            <p className="m-0 text-[11px] text-slate-500">
              PNG, JPG ou GIF recomendados até 5MB
            </p>
          </div>
        </div>

        <div>
          <label htmlFor="perfil-apelido" className="block text-sm font-semibold text-slate-800">
            Apelido Comunitário *
          </label>
          <input
            id="perfil-apelido"
            type="text"
            {...register('apelido', {
              validate: (valor) => valor.trim().length > 0 || 'O apelido é obrigatório.',
            })}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
          />
          {errors.apelido ? (
            <p role="alert" className="m-0 mt-1 text-xs font-medium text-rose-600">
              {errors.apelido.message}
            </p>
          ) : (
            <p className="m-0 mt-1 text-xs text-slate-500">
              Seu identificador único para amigos, trocas e cotações.
            </p>
          )}
        </div>

        <div>
          <label htmlFor="perfil-cidade" className="block text-sm font-semibold text-slate-800">
            Cidade e Estado
          </label>
          <input
            id="perfil-cidade"
            type="text"
            {...register('cidade')}
            placeholder="Ex.: São Paulo, SP"
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
          />
        </div>

        <div>
          <div className="flex items-center justify-between">
            <label htmlFor="perfil-descricao" className="block text-sm font-semibold text-slate-800">
              Bio / Descrição de Perfil
            </label>
            <span className="text-xs text-slate-500">{descricaoAtual.length} / 200</span>
          </div>
          <textarea
            id="perfil-descricao"
            rows={3}
            maxLength={200}
            {...register('descricao')}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
          />
        </div>

        <div>
          <label
            htmlFor="perfil-preferencias"
            className="block text-sm font-semibold text-slate-800"
          >
            Preferências Públicas
          </label>
          <input
            id="perfil-preferencias"
            type="text"
            {...register('preferenciasPublicas')}
            placeholder="Ex.: Trocas presenciais, torneios locais"
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-900 focus:border-indigo-500 focus:outline-none"
          />
        </div>

        <label className="flex items-start gap-2.5 rounded-xl border border-slate-200 p-3">
          <input
            type="checkbox"
            {...register('decksFavoritosVisiveis')}
            className="mt-1 h-4 w-4 rounded border-slate-300 text-indigo-600"
          />
          <span>
            <span className="block text-sm font-semibold text-slate-800">
              Exibir Decks Favoritos no perfil
            </span>
            <span className="block text-xs text-slate-500">
              Mostra suas listas ativas no formato oficial Bandai para outros colecionadores.
            </span>
          </span>
        </label>

        <div className="flex justify-end gap-2 border-t border-slate-200 pt-4">
          <button
            type="button"
            onClick={cancelar}
            className="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={alterarPerfil.isPending}
            className="rounded-lg bg-slate-900 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:opacity-50"
          >
            Salvar Alterações
          </button>
        </div>
      </form>
    </div>
  );

  if (emPagina) {
    return <div className="mx-auto max-w-2xl px-4 py-6">{conteudo}</div>;
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-label="Editar Perfil"
      className="fixed inset-0 z-50 flex items-center justify-center overflow-y-auto bg-slate-950/60 p-4"
    >
      {conteudo}
    </div>
  );
}
