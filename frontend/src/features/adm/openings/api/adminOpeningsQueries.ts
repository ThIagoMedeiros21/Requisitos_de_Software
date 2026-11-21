import { queryOptions, useMutation } from '@tanstack/react-query';
import { mensagemErro } from '../../../../lib/api';
import { clienteConsultas } from '../../../../lib/queryClient';
import type { FiltroStatusAdminAbertura } from '../../../../stores/openingsUiStore';
import { useToastStore } from '../../../../stores/useToastStore';
import { chaveAberturaPreferida } from '../../../community/openings/api/openingsQueries';
import type { SalvarAberturaEntrada } from '../types/adminOpenings.types';
import {
  alterarAberturaAdmin,
  excluirAberturaAdmin,
  inativarAberturaAdmin,
  incluirAberturaAdmin,
  pesquisarAberturasAdmin,
  reativarAberturaAdmin,
} from './adminOpeningsApi';

export const chaveAberturaAdmin = ['adm', 'abertura'] as const;

export function consultaAberturasAdmin(
  termo = '',
  status: FiltroStatusAdminAbertura = 'TODAS',
) {
  return queryOptions({
    queryKey: [...chaveAberturaAdmin, 'pesquisa', termo.trim(), status] as const,
    queryFn: () => pesquisarAberturasAdmin(termo, status),
  });
}

async function invalidarAberturas() {
  await clienteConsultas.invalidateQueries({ queryKey: chaveAberturaAdmin });
  await clienteConsultas.invalidateQueries({ queryKey: chaveAberturaPreferida });
}

export function useIncluirAberturaAdmin() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (entrada: SalvarAberturaEntrada) => incluirAberturaAdmin(entrada),
    onSuccess: async () => {
      await invalidarAberturas();
      publicar('sucesso', 'Abertura cadastrada com sucesso no catálogo oficial.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useAlterarAberturaAdmin() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: ({ id, entrada }: { id: string; entrada: SalvarAberturaEntrada }) =>
      alterarAberturaAdmin(id, entrada),
    onSuccess: async () => {
      await invalidarAberturas();
      publicar('sucesso', 'Abertura atualizada com sucesso.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useAlternarStatusAberturaAdmin() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: ({ id, ativar }: { id: string; ativar: boolean }) =>
      ativar ? reativarAberturaAdmin(id) : inativarAberturaAdmin(id),
    onSuccess: async (_, { ativar }) => {
      await invalidarAberturas();
      publicar(
        'sucesso',
        ativar
          ? 'Abertura reativada para novas escolhas na comunidade.'
          : 'Abertura inativada. Escolhas existentes permanecem preservadas.',
      );
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useExcluirAberturaAdmin() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (id: string) => excluirAberturaAdmin(id),
    onSuccess: async () => {
      await invalidarAberturas();
      publicar('sucesso', 'Abertura excluída do catálogo administrativo.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}
