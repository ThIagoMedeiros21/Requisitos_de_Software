import { queryOptions, useQuery } from '@tanstack/react-query';
import { redirect, type LoaderFunctionArgs } from 'react-router';
import { chamarApi, ErroHttp, mensagemErro } from '../../../lib/api';
import { chaveAutenticacao, clienteConsultas } from '../../../lib/queryClient';

export interface IAgregadoAutorizacoes {
  incluir: boolean;
  buscar: boolean;
  alterar: boolean;
  excluir: boolean;
}

/**
 * Espelha o JSON devolvido pela subclasse de AutenticacaoControladorAbstrato do backend: o nome do
 * usuário, as authorities (opcional) e, por agregado exposto na UI, um objeto de autorizações
 * (consulte-o com `autorizacoesDe`).
 */
export interface Usuario {
  usuario: string;
  autorizacoes?: string[];
  [agregado: string]: unknown;
}

export interface CadastrarContaEntrada {
  usuario: string;
  apelido: string;
  email: string;
  senha: string;
  cidade?: string | null;
  descricao?: string | null;
}

export interface CadastroContaResposta {
  usuario: string;
  apelido: string;
  email: string;
  perfilId: string;
}

function cabecalhoBasic(usuario: string, senha: string): string {
  // btoa só aceita Latin-1; passar por UTF-8 evita falhar com acentos na senha.
  const bytes = new TextEncoder().encode(`${usuario}:${senha}`);
  return `Basic ${btoa(String.fromCharCode(...bytes))}`;
}

export function mensagemErroAutenticacao(erro: unknown): string {
  if (erro instanceof ErroHttp) {
    const corpo = erro.corpo;
    if (typeof corpo === 'object' && corpo !== null && 'mensagem' in corpo) {
      if (typeof corpo.mensagem === 'string' && corpo.mensagem.trim()) {
        return corpo.mensagem;
      }
    }
    if (erro.status === 401) {
      return 'Usuário ou senha inválidos.';
    }
  }
  return mensagemErro(erro) !== 'Não foi possível concluir a operação. Tente novamente.'
    ? mensagemErro(erro)
    : 'Não foi possível entrar. Tente novamente.';
}

/**
 * Confirma com o backend se a sessão (cookie) ainda é válida. Qualquer falha significa anônimo.
 * Sem `staleTime` finito, a sessão só é consultada uma vez por carregamento da página; depois, o
 * estado é mantido por `entrar`, `sair` e pela camada HTTP.
 */
export const consultaAutenticacao = queryOptions({
  queryKey: chaveAutenticacao,
  queryFn: async (): Promise<Usuario | null> => {
    try {
      return await chamarApi<Usuario>('/backend/autenticacao');
    } catch {
      return null;
    }
  },
  staleTime: Infinity,
});

/**
 * Autentica sem alterar o estado da aplicação. Quem precisa decidir antes de aceitar o usuário
 * (como o modal de reautenticação) usa esta função; os demais, `entrar`.
 */
export function autenticar(usuario: string, senha: string): Promise<Usuario> {
  return chamarApi<Usuario>('/backend/autenticacao', {
    headers: { Authorization: cabecalhoBasic(usuario, senha) },
  });
}

export async function entrar(usuario: string, senha: string): Promise<Usuario> {
  const autenticado = await autenticar(usuario, senha);
  clienteConsultas.setQueryData(chaveAutenticacao, autenticado);
  return autenticado;
}

/**
 * Cadastra uma nova conta no banco relacional (`POST /backend/autenticacao/cadastro`), criando
 * automaticamente o perfil comunitário e autenticando a sessão imediatamente.
 */
export async function cadastrarConta(entrada: CadastrarContaEntrada): Promise<Usuario> {
  const criado = await chamarApi<CadastroContaResposta>('/backend/autenticacao/cadastro', {
    method: 'POST',
    corpo: entrada,
  });
  return entrar(criado.usuario || entrada.usuario, entrada.senha);
}

/** Encerra a sessão no backend, sem mexer no estado da aplicação. Falhas são ignoradas. */
export async function encerrarSessao(): Promise<void> {
  await chamarApi('/backend/sair', { method: 'POST' }).catch(() => undefined);
}

export async function sair(): Promise<void> {
  await encerrarSessao();
  clienteConsultas.setQueryData(chaveAutenticacao, null);
}

export function usuarioAtual(): Usuario | null {
  return clienteConsultas.getQueryData<Usuario | null>(chaveAutenticacao) ?? null;
}

export function useAutenticacao() {
  const { data } = useQuery(consultaAutenticacao);
  const usuario = data ?? null;

  return {
    autenticado: usuario !== null,
    usuario: usuario?.usuario ?? null,
    autorizacoes: usuario?.autorizacoes ?? [],
    /** Autorizações do agregado (ex.: `autorizacoesDe<IAgregadoAutorizacoes>('agregado')`). */
    autorizacoesDe: <T extends IAgregadoAutorizacoes = IAgregadoAutorizacoes>(agregado: string) =>
      usuario?.[agregado] as T | undefined,
  };
}

/** Loader das rotas protegidas: sem sessão válida, vai para o login e volta depois. */
export async function exigirAutenticacao({ request }: LoaderFunctionArgs) {
  const usuario = await clienteConsultas.ensureQueryData(consultaAutenticacao);
  if (!usuario) {
    const { pathname, search } = new URL(request.url);
    throw redirect(`/login?retorno=${encodeURIComponent(pathname + search)}`);
  }
  return null;
}
