import { queryOptions, useMutation } from '@tanstack/react-query';
import { mensagemErro } from '../../../../lib/api';
import { clienteConsultas } from '../../../../lib/queryClient';
import { useToastStore } from '../../../../stores/useToastStore';
import { chavePerfilComunitario } from '../../profile/api/profileQueries';
import {
  excluirDeckFavorito,
  incluirDeckFavorito,
  listarDecksFavoritosDoPerfil,
  listarMeusDecksFavoritos,
  obterComposicaoDeck,
  ocultarDecksFavoritos,
  pesquisarCatalogoDecks,
  reexibirDecksFavoritos,
} from './decksApi';

export const chaveDeckFavorito = ['comunidade', 'deck-favorito'] as const;

export const consultaMeusDecksFavoritos = queryOptions({
  queryKey: [...chaveDeckFavorito, 'pesquisa'] as const,
  queryFn: listarMeusDecksFavoritos,
});

export function consultaDecksDoPerfil(perfilId: string) {
  return queryOptions({
    queryKey: [...chaveDeckFavorito, 'perfil', perfilId] as const,
    queryFn: () => listarDecksFavoritosDoPerfil(perfilId),
  });
}

export function consultaCatalogoDecks(termo: string, cor: string) {
  return queryOptions({
    queryKey: [...chaveDeckFavorito, 'catalogo', termo.trim(), cor.trim()] as const,
    queryFn: () => pesquisarCatalogoDecks(termo, cor),
  });
}

export function consultaComposicaoDeck(deckId: string) {
  return queryOptions({
    queryKey: [...chaveDeckFavorito, 'composicao', deckId] as const,
    queryFn: () => obterComposicaoDeck(deckId),
  });
}

export async function carregarDecksFavoritos() {
  return clienteConsultas.ensureQueryData(consultaMeusDecksFavoritos);
}

async function invalidarDecksEPerfil() {
  await clienteConsultas.invalidateQueries({ queryKey: chaveDeckFavorito });
  await clienteConsultas.invalidateQueries({ queryKey: chavePerfilComunitario });
}

export function useAdicionarDeckFavorito() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (deckId: string) => incluirDeckFavorito(deckId),
    onSuccess: async () => {
      await invalidarDecksEPerfil();
      publicar('sucesso', 'Deck adicionado aos seus favoritos.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useRemoverDeckFavorito() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (deckId: string) => excluirDeckFavorito(deckId),
    onSuccess: async () => {
      await invalidarDecksEPerfil();
      publicar('sucesso', 'Deck removido dos favoritos.');
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}

export function useAlternarVisibilidadeDecks() {
  const publicar = useToastStore((estado) => estado.publicar);

  return useMutation({
    mutationFn: (exibir: boolean) => (exibir ? reexibirDecksFavoritos() : ocultarDecksFavoritos()),
    onSuccess: async (_, exibir) => {
      await invalidarDecksEPerfil();
      publicar(
        'sucesso',
        exibir
          ? 'Decks favoritos reexibidos no perfil público.'
          : 'Decks favoritos ocultados no perfil público.',
      );
    },
    onError: (erro) => {
      publicar('erro', mensagemErro(erro));
    },
  });
}
