import { queryOptions, useMutation } from '@tanstack/react-query';
import type { LoaderFunctionArgs } from 'react-router';
import { chamarApi, mensagemErro } from '../../../../lib/api';
import { clienteConsultas } from '../../../../lib/queryClient';
import { useToastStore } from '../../../../stores/useToastStore';
import { chavePerfilComunitario } from '../../profile/api/profileQueries';
import type {
  AmizadeDetalheDto,
  ColecionadorBuscaDto,
  RedeAmizadesDto,
} from '../types/friends.types';

export const chaveAmizade = ['comunidade', 'amizade'] as const;

export const consultaRedeAmizades = queryOptions({
  queryKey: [...chaveAmizade, 'pesquisa'] as const,
  queryFn: () => chamarApi<RedeAmizadesDto>('/backend/comunidade/amizade/pesquisa'),
});

export function consultaColecionadores(termo: string) {
  const termoNormalizado = termo.trim();
  return queryOptions({
    queryKey: [...chaveAmizade, 'inclusao', termoNormalizado] as const,
    queryFn: () => {
      const consulta = termoNormalizado ? `?termo=${encodeURIComponent(termoNormalizado)}` : '';
      return chamarApi<ColecionadorBuscaDto[]>(`/backend/comunidade/amizade/inclusao${consulta}`);
    },
  });
}

export function consultaAmizadePorId(id: string) {
  return queryOptions({
    queryKey: [...chaveAmizade, 'visualizacao', id] as const,
    queryFn: () =>
      chamarApi<AmizadeDetalheDto>(
        `/backend/comunidade/amizade/${encodeURIComponent(id)}/visualizacao`,
      ),
  });
}

export async function carregarRedeAmizades() {
  return clienteConsultas.ensureQueryData(consultaRedeAmizades);
}

export async function carregarColecionadores({ request }: LoaderFunctionArgs) {
  const termo = new URL(request.url).searchParams.get('termo') ?? '';
  return clienteConsultas.ensureQueryData(consultaColecionadores(termo));
}

export async function carregarAmizadePorId({ params }: LoaderFunctionArgs) {
  const id = params['id'];
  if (!id) {
    throw new Response('Identificador da amizade não informado.', { status: 400 });
  }
  return clienteConsultas.ensureQueryData(consultaAmizadePorId(id));
}

async function invalidarComunidade() {
  await clienteConsultas.invalidateQueries({ queryKey: chaveAmizade });
  await clienteConsultas.invalidateQueries({ queryKey: chavePerfilComunitario });
}

export function useEnviarSolicitacaoAmizade() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (destinatario: string) =>
      chamarApi<AmizadeDetalheDto>('/backend/comunidade/amizade/incluir', {
        method: 'POST',
        corpo: { destinatario },
      }),
    onSuccess: async () => {
      await invalidarComunidade();
      publicar('sucesso', 'Solicitação de amizade enviada.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useAceitarSolicitacaoAmizade() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (id: string) =>
      chamarApi<void>(`/backend/comunidade/amizade/${encodeURIComponent(id)}/aceitar`, {
        method: 'POST',
        corpo: {},
      }),
    onSuccess: async () => {
      await invalidarComunidade();
      publicar('sucesso', 'Solicitação de amizade aceita.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useRecusarSolicitacaoAmizade() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (id: string) =>
      chamarApi<void>(`/backend/comunidade/amizade/${encodeURIComponent(id)}/recusar`, {
        method: 'POST',
        corpo: {},
      }),
    onSuccess: async () => {
      await invalidarComunidade();
      publicar('sucesso', 'Solicitação de amizade recusada.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useCancelarSolicitacaoAmizade() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (id: string) =>
      chamarApi<void>(`/backend/comunidade/amizade/${encodeURIComponent(id)}/cancelar`, {
        method: 'POST',
        corpo: {},
      }),
    onSuccess: async () => {
      await invalidarComunidade();
      publicar('sucesso', 'Solicitação de amizade cancelada.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useRemoverAmizade() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (id: string) =>
      chamarApi<void>(`/backend/comunidade/amizade/${encodeURIComponent(id)}/remover`, {
        method: 'POST',
        corpo: {},
      }),
    onSuccess: async () => {
      await invalidarComunidade();
      publicar('sucesso', 'Amizade removida bilateralmente.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}
