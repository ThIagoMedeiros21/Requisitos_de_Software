import { chamarApi } from '../../../../lib/api';
import type {
  ComposicaoDeckDto,
  DeckCatalogoDto,
  ListaDecksFavoritosDto,
} from '../types/decks.types';

const BASE = '/backend/comunidade/deck-favorito';

export function listarMeusDecksFavoritos() {
  return chamarApi<ListaDecksFavoritosDto>(`${BASE}/pesquisa`);
}

export function listarDecksFavoritosDoPerfil(perfilId: string) {
  return chamarApi<ListaDecksFavoritosDto>(`${BASE}/perfil/${encodeURIComponent(perfilId)}`);
}

export function pesquisarCatalogoDecks(termo: string, cor: string) {
  const params = new URLSearchParams();
  if (termo.trim()) {
    params.set('termo', termo.trim());
  }
  if (cor.trim()) {
    params.set('cor', cor.trim());
  }
  const qs = params.toString();
  return chamarApi<DeckCatalogoDto[]>(`${BASE}/catalogo${qs ? `?${qs}` : ''}`);
}

export function obterComposicaoDeck(deckId: string) {
  return chamarApi<ComposicaoDeckDto>(`${BASE}/${encodeURIComponent(deckId)}/composicao`);
}

export function incluirDeckFavorito(deckId: string) {
  return chamarApi<ListaDecksFavoritosDto>(`${BASE}/incluir`, {
    method: 'POST',
    corpo: { deckId },
  });
}

export function excluirDeckFavorito(deckId: string) {
  return chamarApi<void>(`${BASE}/${encodeURIComponent(deckId)}/excluir`, {
    method: 'POST',
    corpo: {},
  });
}

export function ocultarDecksFavoritos() {
  return chamarApi<void>(`${BASE}/ocultar`, {
    method: 'POST',
    corpo: {},
  });
}

export function reexibirDecksFavoritos() {
  return chamarApi<void>(`${BASE}/reexibir`, {
    method: 'POST',
    corpo: {},
  });
}
