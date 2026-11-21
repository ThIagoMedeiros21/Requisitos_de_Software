/** Indicador de carregamento inicial das rotas (HydrateFallback do roteador). */
export function RouteLoading() {
  return (
    <div role="status" aria-label="Carregando" className="flex min-h-[40vh] items-center justify-center">
      <span className="h-8 w-8 animate-spin rounded-full border-2 border-ice-border border-t-ocean-blue" />
    </div>
  );
}
