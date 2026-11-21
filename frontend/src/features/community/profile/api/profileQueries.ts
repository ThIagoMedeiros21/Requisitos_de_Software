import { queryOptions, useMutation } from '@tanstack/react-query';
import type { LoaderFunctionArgs } from 'react-router';
import { chamarApi, mensagemErro } from '../../../../lib/api';
import { clienteConsultas } from '../../../../lib/queryClient';
import { useToastStore } from '../../../../stores/useToastStore';
import type {
  AlterarPerfilEntrada,
  PerfilComunitarioAlteracaoDto,
  PerfilComunitarioVisaoDto,
} from '../types/profile.types';

export const chavePerfilComunitario = ['comunidade', 'perfil-comunitario'] as const;

export function consultaPerfilVisao(id?: string) {
  return queryOptions({
    queryKey: [...chavePerfilComunitario, 'visualizacao', id ?? 'proprio'] as const,
    queryFn: () =>
      chamarApi<PerfilComunitarioVisaoDto>(
        id
          ? `/backend/comunidade/perfil-comunitario/${encodeURIComponent(id)}/visualizacao`
          : '/backend/comunidade/perfil-comunitario/visualizacao',
      ),
  });
}

export const consultaProprioPerfil = consultaPerfilVisao();

export function consultaPerfilPorId(id: string) {
  return consultaPerfilVisao(id);
}

export function consultaPerfilAlteracao(id: string) {
  return queryOptions({
    queryKey: [...chavePerfilComunitario, 'alteracao', id] as const,
    queryFn: () =>
      chamarApi<PerfilComunitarioAlteracaoDto>(
        `/backend/comunidade/perfil-comunitario/${encodeURIComponent(id)}/alteracao`,
      ),
  });
}

export async function carregarProprioPerfil() {
  return clienteConsultas.ensureQueryData(consultaProprioPerfil);
}

export async function carregarPerfilPorId({ params }: LoaderFunctionArgs) {
  const id = params['id'];
  if (!id) {
    throw new Response('Identificador do perfil não informado.', { status: 400 });
  }
  return clienteConsultas.ensureQueryData(consultaPerfilPorId(id));
}

export async function carregarPerfilAlteracao({ params }: LoaderFunctionArgs) {
  const id = params['id'];
  if (!id) {
    throw new Response('Identificador do perfil não informado.', { status: 400 });
  }
  return clienteConsultas.ensureQueryData(consultaPerfilAlteracao(id));
}

export function useAlterarPerfil(id: string) {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (corpo: AlterarPerfilEntrada) =>
      chamarApi<PerfilComunitarioVisaoDto>(
        `/backend/comunidade/perfil-comunitario/${encodeURIComponent(id)}/alterar`,
        { method: 'POST', corpo },
      ),
    onSuccess: async () => {
      await clienteConsultas.invalidateQueries({ queryKey: chavePerfilComunitario });
      await clienteConsultas.invalidateQueries({ queryKey: ['comunidade', 'amizade'] });
      publicar('sucesso', 'Perfil comunitário atualizado com sucesso.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useFixarCartaFavorita(id: string) {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (cartaId: string) =>
      chamarApi<PerfilComunitarioVisaoDto>(
        `/backend/comunidade/perfil-comunitario/${encodeURIComponent(id)}/fixar-destaque`,
        { method: 'POST', corpo: { cartaId } },
      ),
    onSuccess: async () => {
      await clienteConsultas.invalidateQueries({ queryKey: chavePerfilComunitario });
      publicar('sucesso', 'Carta fixada nos destaques do perfil.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useRemoverCartaFavorita(id: string) {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (cartaId: string) =>
      chamarApi<PerfilComunitarioVisaoDto>(
        `/backend/comunidade/perfil-comunitario/${encodeURIComponent(id)}/remover-destaque`,
        { method: 'POST', corpo: { cartaId } },
      ),
    onSuccess: async () => {
      await clienteConsultas.invalidateQueries({ queryKey: chavePerfilComunitario });
      publicar('sucesso', 'Carta removida dos destaques do perfil.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}
