import { queryOptions, useMutation } from '@tanstack/react-query';
import { mensagemErro } from '../../../../lib/api';
import { clienteConsultas } from '../../../../lib/queryClient';
import { useToastStore } from '../../../../stores/useToastStore';
import { chavePerfilComunitario } from '../../profile/api/profileQueries';
import {
  escolherAberturaPreferida,
  obterAberturaDoPerfil,
  obterDetalhesAberturaComunidade,
  obterRankingAberturas,
  ocultarAberturaPreferida,
  pesquisarAberturasComunidade,
  reexibirAberturaPreferida,
} from './openingsApi';

export const chaveAberturaPreferida = ['comunidade', 'abertura-preferida'] as const;

export function consultaAberturasComunidade(termo = '', filtro = 'todas') {
  return queryOptions({
    queryKey: [...chaveAberturaPreferida, 'pesquisa', termo.trim(), filtro] as const,
    queryFn: () => pesquisarAberturasComunidade(termo, filtro),
  });
}

export const consultaRankingAberturas = queryOptions({
  queryKey: [...chaveAberturaPreferida, 'ranking'] as const,
  queryFn: obterRankingAberturas,
});

export function consultaAberturaDoPerfil(perfilId: string) {
  return queryOptions({
    queryKey: [...chaveAberturaPreferida, 'perfil', perfilId] as const,
    queryFn: () => obterAberturaDoPerfil(perfilId),
  });
}

export function consultaDetalhesAbertura(aberturaId: string) {
  return queryOptions({
    queryKey: [...chaveAberturaPreferida, 'visualizacao', aberturaId] as const,
    queryFn: () => obterDetalhesAberturaComunidade(aberturaId),
  });
}

export async function carregarAberturasComunidade() {
  return clienteConsultas.ensureQueryData(consultaAberturasComunidade());
}

async function invalidarAberturasEPerfil() {
  await clienteConsultas.invalidateQueries({ queryKey: chaveAberturaPreferida });
  await clienteConsultas.invalidateQueries({ queryKey: chavePerfilComunitario });
  await clienteConsultas.invalidateQueries({ queryKey: ['adm', 'abertura'] });
}

export function useEscolherAberturaPreferida() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (aberturaId: string) => escolherAberturaPreferida(aberturaId),
    onSuccess: async (escolhida) => {
      await invalidarAberturasEPerfil();
      publicar('sucesso', `Abertura Op. ${String(escolhida.numero).padStart(2, '0')} — ${escolhida.nome} definida como preferida!`);
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useAlternarVisibilidadeAbertura() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (exibir: boolean) =>
      exibir ? reexibirAberturaPreferida() : ocultarAberturaPreferida(),
    onSuccess: async (_, exibir) => {
      await invalidarAberturasEPerfil();
      publicar(
        'sucesso',
        exibir
          ? 'Abertura preferida reexibida no perfil público.'
          : 'Abertura preferida ocultada do perfil público.',
      );
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}
