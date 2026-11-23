import { queryOptions, useMutation } from '@tanstack/react-query';
import type { LoaderFunctionArgs } from 'react-router';
import { mensagemErro } from '../../../../lib/api';
import { clienteConsultas } from '../../../../lib/queryClient';
import { useToastStore } from '../../../../stores/useToastStore';
import type {
  RegistrarAvaliacaoEntrada,
  RegistrarCompraEntrada,
} from '../types/reviews.types';
import {
  listarCatalogoAvaliacoes,
  obterDetalhesAvaliacoesCarta,
  reembolsarCompraVerificada,
  registrarCompraVerificada,
  registrarOuAtualizarAvaliacao,
} from './reviewsApi';

export const chaveAvaliacaoProduto = ['comunidade', 'avaliacao-produto'] as const;

export function consultaCatalogoAvaliacoes(termo = '') {
  return queryOptions({
    queryKey: [...chaveAvaliacaoProduto, 'catalogo', termo.trim()] as const,
    queryFn: () => listarCatalogoAvaliacoes(termo),
  });
}

export function consultaDetalhesAvaliacoesCarta(produtoId: string) {
  return queryOptions({
    queryKey: [...chaveAvaliacaoProduto, 'pesquisa', produtoId] as const,
    queryFn: () => obterDetalhesAvaliacoesCarta(produtoId),
  });
}

export async function carregarCatalogoAvaliacoes() {
  return clienteConsultas.ensureQueryData(consultaCatalogoAvaliacoes());
}

export async function carregarDetalhesAvaliacoesCarta({ params }: LoaderFunctionArgs) {
  const produtoId = params['produtoId'];
  if (!produtoId) {
    throw new Response('Identificador da carta não informado.', { status: 400 });
  }
  return clienteConsultas.ensureQueryData(consultaDetalhesAvaliacoesCarta(produtoId));
}

export function useSalvarAvaliacao(produtoId: string) {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (entrada: RegistrarAvaliacaoEntrada) =>
      registrarOuAtualizarAvaliacao(produtoId, entrada),
    onSuccess: async () => {
      await clienteConsultas.invalidateQueries({ queryKey: chaveAvaliacaoProduto });
      publicar('sucesso', 'Avaliação salva com selo Compra Verificada!');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useRegistrarCompraVerificada() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (entrada: RegistrarCompraEntrada) => registrarCompraVerificada(entrada),
    onSuccess: async (_, entrada) => {
      await clienteConsultas.invalidateQueries({ queryKey: chaveAvaliacaoProduto });
      publicar(
        'sucesso',
        entrada.paga
          ? `Compra paga ${entrada.pedidoId} registrada. Elegibilidade habilitada!`
          : `Pedido pendente ${entrada.pedidoId} registrado (aguardando pagamento).`,
      );
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useReembolsarCompraVerificada() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: ({ pedidoId, produtoId }: { pedidoId: string; produtoId: string }) =>
      reembolsarCompraVerificada(pedidoId, produtoId),
    onSuccess: async () => {
      await clienteConsultas.invalidateQueries({ queryKey: chaveAvaliacaoProduto });
      publicar(
        'info',
        'Reembolso processado. Elegibilidade e média da carta recalculadas automaticamente.',
      );
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}
